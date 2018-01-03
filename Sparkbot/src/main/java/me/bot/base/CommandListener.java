package me.bot.base;

import sx.blah.discord.api.events.EventSubscriber;
import sx.blah.discord.handle.impl.events.ReadyEvent;
import sx.blah.discord.handle.impl.events.guild.GuildCreateEvent;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.handle.obj.IGuild;
import sx.blah.discord.handle.obj.Permissions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.List;

public class CommandListener {
    
    public ArrayList<ICommand> commands = new ArrayList<>();
    private Bot bot;

    public CommandListener(Bot bot){
        this.bot = bot;
    }

    public List<ICommand> getCommands() {
        return commands;
    }
    
    public void addCommands(ICommand... c) {
        Arrays.stream(c).forEach(command -> {
            command.onLoad();
            commands.add(command);
        });
    }
    
    @EventSubscriber
    public void onReadyEvent(ReadyEvent event) {
        
        String name = bot.getName();

        if(!bot.getClient().getOurUser().getName().equalsIgnoreCase(name))
            bot.getClient().changeUsername(name);

        int servers = bot.getClient().getGuilds().size();

        if(!bot.isStreaming())
            bot.getClient().changePlayingText(servers + " Server" + (servers > 1 ? "s" : ""));
        else
            bot.getClient().streaming(servers + " Server" + (servers > 1 ? "s" : ""),bot.getUrl());

    }

    @EventSubscriber
    public void onJoinServer(GuildCreateEvent event) {
        int servers = bot.getClient().getGuilds().size();
        if(!bot.isStreaming())
            bot.getClient().changePlayingText(servers + " Server" + (servers > 1 ? "s" : ""));
        else
            bot.getClient().streaming(servers + " Server" + (servers > 1 ? "s" : ""),bot.getUrl());


    }
    
    @EventSubscriber
    public void onMessageReceivedEvent(MessageReceivedEvent event) {
        
        if(event.getAuthor().isBot()){
            return;
        }
        
        String message = event.getMessage().getContent();
        
        for (ICommand command : commands) {
            
            for(String prefix : command.getPrefixes(event.getMessage().getGuild())) {

                if (message.startsWith(prefix)) {

                    String messageWithoutPrefix = message.substring(prefix.length(), message.length());
                    String[] args = messageWithoutPrefix.split(" ");

                    if (args.length == 0)
                        continue;

                    for (String name : command.getNames()) {

                        if (args[0].equalsIgnoreCase(name) && command.hasPermissions(event.getGuild(), event.getAuthor())) {

                            if(command.requiredBotPermissions() != null) {
                                List<Permissions> required = requiredPermissions(event.getGuild(),command.requiredBotPermissions());
                                if(required != null && required.size() != 0){
                                    MessageAPI.sendAndDeleteMessageLater(event.getChannel(),"<:red_cross:398120014974287873> **| I need `" + permsToString(required) + "` permissions to perfom that command.**",5000L);
                                    return;
                                }
                            }

                            bot.LOGGER.info(event.getAuthor().getName() + " issued " + event.getMessage().getContent());
                            command.run(bot, event.getAuthor(), event.getMessage(), args);
                            return;
                        } else if(args[0].equalsIgnoreCase(name)) {
                            bot.LOGGER.info(event.getAuthor().getName() + " failed to issue " + event.getMessage().getContent());
                            MessageAPI.sendAndDeleteMessageLater(event.getChannel(),"<:no:364443305582526474> **| <@" + event.getAuthor().getLongID() + "> You don't have enough permissions to perform that command",5000L);
                            break;
                        }
                    }
                }
            }
        }
    }

    private List<Permissions> requiredPermissions(IGuild guild, List<Permissions> perms){
        EnumSet<Permissions> set = bot.getClient().getOurUser().getPermissionsForGuild(guild);
        if(set.contains(Permissions.ADMINISTRATOR))
            return null;
        else {
            List<Permissions> out = null;
            for (Permissions perm : perms) {
                if(!set.contains(perm))
                    if(out == null) {
                        out = new ArrayList<>();
                        out.add(perm);
                    } else
                        out.add(perm);

            }
            return out;
        }
    }

    private String permsToString(List<Permissions> perms) {
        StringBuilder out = new StringBuilder();
        for(int i = 0; i < perms.size(); i++) {
            Permissions p = perms.get(i);
            if(perms.size() == 1) {
                out.append("`" + p.toString() + "`");
            } else if(i != perms.size()-1) {
                out.append("`" + p.toString() + "`, ");
            } else {
                out.append("and `" + p.toString() + "`");
            }
        }
        return out.toString();
    }
}
