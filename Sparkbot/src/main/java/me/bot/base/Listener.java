package me.bot.base;

import discord4j.common.util.Snowflake;
import discord4j.core.event.domain.guild.GuildCreateEvent;
import discord4j.core.event.domain.lifecycle.ReadyEvent;
import discord4j.core.event.domain.lifecycle.ReconnectStartEvent;
import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.core.object.entity.*;
import discord4j.core.object.entity.channel.Channel;
import discord4j.core.object.entity.channel.MessageChannel;
import discord4j.core.object.entity.channel.TextChannel;
import discord4j.core.object.presence.ClientActivity;
import discord4j.core.object.presence.ClientPresence;
import discord4j.rest.util.Permission;
import reactor.core.publisher.Flux;

import me.bot.base.polls.Poll;

import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.*;
import java.util.function.Predicate;

public class Listener {
    
    public final  ArrayList<ICommand> commands = new ArrayList<>();
    private final List<Poll<?>>       polls    = new ArrayList<>();
    private final Bot                 bot;
    
    public Listener(Bot bot) {
        
        this.bot = bot;
        Flux.interval(Duration.ofSeconds(1))
            .subscribe(
                    l -> {
                        for (int i = 0 ; i < polls.size() ; i++) {
                            Poll<?> poll = polls.get(i);
                            if (poll.getTimeUntilInactive() > -1 && poll.getTimeUntilInactive() < System.currentTimeMillis() - poll
                                    .getLastInteraction()) {
                                polls.remove(i).onInactive();
                            } else if (poll.ended()) {
                                //noinspection SuspiciousListRemoveInLoop
                                polls.remove(i);
                            }
                        }
                    }
                      );
        
    }
    
    public List<ICommand> getCommands() {
        
        return commands;
    }
    
    public void addCommands(Bot bot, ICommand... c) {
        
        Arrays.stream(c).forEach(command -> {
            command.onLoad(bot);
            commands.add(command);
            System.out.println(command.getNames()[0] + " has been enabled.");
        });
    }
    
    public void addPoll(Poll<?> poll) {
        
        polls.add(poll);
        poll.sendMessage();
    }
    
    public void onReconnectStartEvent(@SuppressWarnings("unused") ReconnectStartEvent event) {
    
        System.out.println("Reconnect Start");
        updatePresence();
    }
    
    public void onReadyEvent(@SuppressWarnings("unused") ReadyEvent event) {
        
        System.out.println("Ready");
        updatePresence();
        
    }
    
    public void onJoinServer(GuildCreateEvent event) {
        
        System.out.println("Joined server " + event.getGuild().getName() + " (" + event.getGuild().getId().asString() + ")");
        //		updatePresence();
    }
    
    private void updatePresence() {
        
        String message = "s!help";
        if (!bot.isStreaming()) {
            bot.getGateway().updatePresence(ClientPresence.online(ClientActivity.playing(message)))
                .doOnSuccess((ignored) -> System.out.println("Changed playing presence"))
                .doOnError(Throwable :: printStackTrace)
               .subscribe();
        } else {
            bot.getGateway().updatePresence(ClientPresence.online(ClientActivity.streaming(message, bot.getUrl())))
               .doOnSuccess((ignored) -> System.out.println("Changed streaming presence"))
               .doOnError(Throwable :: printStackTrace)
               .subscribe();
        }
        
    }
    
    public void onMessageReceivedEvent(final MessageCreateEvent event) {
        
        Snowflake guildSnowflake = event.getGuildId().orElse(null);
        if (guildSnowflake == null) {
            return;
        }
        long guildId = guildSnowflake.asLong();
        
        event.getMessage().getChannel()
             .filter(messageChannel -> messageChannel instanceof TextChannel)
             .filter(messageChannel -> event.getMember().isPresent() && !event.getMember().get().isBot())
             .zipWith(event.getMessage().getGuild())
             .subscribe(
                     objects -> {
                         //noinspection OptionalGetWithoutIsPresent
                         Member      member  = event.getMember().get();
                         TextChannel channel = (TextChannel) objects.getT1();
                         Guild       guild   = objects.getT2();
                    
                         if (!processPoll(member, channel, event.getMessage())) {
                             processCommand(member, guild, channel, event, isOnWhitelist(bot, guildId, channel));
                         }
                     }
                       );
    }
    
    private boolean processPoll(final Member member, final TextChannel channel, final Message message) {
        
        final Poll<?> poll = getPollOfPlayer(member.getId().asLong());
        if (poll != null) {
            final String messageContent = message.getContent();
            
            if (messageContent.startsWith("skip")) {
                
                if (!poll.isSkippable()) {
                    sendNotAValidPollRespond(channel);
                } else {
                    poll.onSkip();
                }
                
            } else if (messageContent.startsWith("exit")) {
                poll.onExit();
            } else {
                if (!poll.onTrigger(message)) {
                    sendNotAValidPollRespond(channel);
                }
            }
            return true;
        } else {
            return false;
        }
    }
    
    private void processCommand(final Member member, final Guild guild, final TextChannel channel, final MessageCreateEvent event, final boolean isOnWhitelist) {
        
        String message = event.getMessage().getContent();
        
        if (channel == null || message.isEmpty()) {
            return;
        }
        
        commands
            .stream()
            .filter(command -> !(!isOnWhitelist && command.getType().equals(CommandType.PUBLIC)))
            .forEach(command -> Arrays.stream(command.getPrefixes(guild))
                  .filter(message :: startsWith)
                  .map(prefix -> message.substring(prefix.length()))
                  .map(messageWithoutPrefix -> messageWithoutPrefix.split(" "))
                  .map(this::removeEmpty)
                  .filter(args -> args.length > 0)
                  .forEach(
                          args ->
                                  Arrays.stream(command.getNames())
                                        .filter(name -> args[0].equalsIgnoreCase(name))
                                        .findFirst()
                                        .ifPresent(ignored -> finalProcessing(event, command, guild, member, args, message, channel))
                          ));
        
    }
    
    private String[] removeEmpty(String[] strings) {
        int empty = 0;
        for (int i = 1 ; i < strings.length ; i++) {
            String s = strings[i];
            if (s.isEmpty()) {
                empty++;
            }
        }
        String[] out = new String[strings.length-empty];
        int idx = 0;
        for (int i = 0 ; i < strings.length ; i++) {
            String string = strings[i];
            if (i == 0 || !string.isEmpty()) {
                out[idx++] = string;
            }
        }
        return out;
    }
    
    private void finalProcessing(final MessageCreateEvent event, final ICommand command, final Guild guild, final Member member, final String[] args, final String message, final TextChannel channel) {
        command.hasPermission(bot, guild, member)
               .subscribe(bool -> {
                   if(!bool) {
                       System.out.println(member.getUsername() + " failed to issue " + message);
                       MessageAPI.sendAndDeleteMessageLater(channel, "<:red_cross:398120014974287873> **| " + member.getUsername() + "** You don't have enough permissions to perform that command.", 5000L);
                   } else {
                       Mono.just(true)
                           .flatMap(ignored -> {
                               if (command.requiredBotPermissions() == null) {
                                   return Mono.just(Collections.<Permission>emptyList());
                               }
                               return requiredPermissions(channel, command.requiredBotPermissions());
                           }).subscribe(list -> {
                               if(!list.isEmpty()) {
                                   System.out.println(member.getUsername() + " failed to issue " + message);
                                   MessageAPI.sendAndDeleteMessageLater(channel, "<:red_cross:398120014974287873> **| I need `" + permsToString(list) + "` permissions to perform that command.**", 5000L);
                               } else {
                                   System.out.println(member.getUsername() + " issued " + message);
                                   command.run(bot, member, channel, guild, event.getMessage(), args[0], Arrays.copyOfRange(args, 1, args.length), message);
                               }
                           });
                   }
               });
    }
    
    private boolean isOnWhitelist(Bot bot, long guildId, Channel channel) {
        
        List<String> whitelist = getWhitelist(bot, guildId);
        return whitelist == null || whitelist.isEmpty() || whitelist.contains(channel.getId().asString());
    }
    
    private List<String> getWhitelist(Bot bot, long guildId) {
        
        Map<String, Object> map = bot.getResourceManager().getConfig("configs/" + guildId, "whitelist.json");
        
        if (!map.containsKey("channels")) {
            return null;
        }
        
        Object channels = map.get("channels");
        
        if (!(channels instanceof List)) {
            return null;
        }
        
        if (((List<?>) channels).isEmpty()) {
            return null;
        }
        
        if (!(((List<?>) channels).get(0) instanceof String)) {
            return null;
        }
    
        //noinspection unchecked
        return (List<String>) map.get("channels");
    }
    
    private void sendNotAValidPollRespond(MessageChannel channel) {
        MessageAPI.sendAndDeleteMessageLater(channel, "<:red_cross:398120014974287873> **| That's not a valid response to the poll.", 5000L);
    }
    
    private Poll<?> getPollOfPlayer(long userid) {
        
        long now = System.currentTimeMillis();
        for (int i = 0 ; i < polls.size() ; i++) {
            Poll<?> poll = polls.get(i);
            if (poll.ended()) {
                //noinspection SuspiciousListRemoveInLoop
                polls.remove(i);
            } else if (now - poll.getLastInteraction() > poll.getTimeUntilInactive() && poll.getTimeUntilInactive() >= 0) {
                polls.remove(i).onInactive();
            } else if (poll.getUserID() == userid) {
                return poll;
            }
        }
        return null;
        
    }
    
    //TODO: Add permission check
    private Mono<List<Permission>> requiredPermissions(TextChannel channel, List<Permission> perms) {
        
        Flux<Permission> permissions = channel
                .getEffectivePermissions(bot.getGateway().getSelfId())
                .flatMapMany(Flux :: fromIterable);
        Mono<Boolean>    isAdmin     = permissions.filter(perm -> perm.equals(Permission.ADMINISTRATOR)).hasElements();
        
        return isAdmin.filter(Predicate.isEqual(Boolean.TRUE))
                      .flatMap((ignored) -> Mono.just(Collections.<Permission> emptyList()))
                      .switchIfEmpty(Mono.just(perms))
                      .filterWhen(perm -> permissions.any(perm :: contains).map(bool -> !bool));
    }
    
    private String permsToString(List<Permission> perms) {
        
        StringBuilder out = new StringBuilder();
        for (int i = 0 ; i < perms.size() ; i++) {
            Permission p = perms.get(i);
            if (perms.size() == 1) {
                out
                        .append("`")
                        .append(p.toString())
                        .append("`");
            } else if (i != perms.size() - 1) {
                out
                        .append("`")
                        .append(p.toString())
                        .append("`, ");
            } else {
                out
                        .append("and `")
                        .append(p.toString())
                        .append("`");
            }
        }
        return out.toString();
    }
}
