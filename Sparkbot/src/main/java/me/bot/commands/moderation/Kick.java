package me.bot.commands.moderation;

import discord4j.core.object.entity.Guild;
import discord4j.core.object.entity.Member;
import discord4j.core.object.entity.Message;
import discord4j.core.object.entity.TextChannel;
import discord4j.core.object.util.Permission;
import discord4j.core.object.util.Snowflake;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.List;

import me.bot.base.Bot;
import me.bot.base.CommandType;
import me.bot.base.ICommand;
import me.bot.base.MessageAPI;
import me.bot.base.polls.Bool;
import me.bot.base.polls.Confirmation;
import me.bot.base.polls.Poll;
import me.main.Prefixes;
import me.main.utils.DiscordUtils;

public class Kick implements ICommand {
    
    @Override
    public CommandType getType() {
        
        return CommandType.MOD;
    }
    
    @Override
    public String getHelp() {
        
        return "Kick a user";
    }
    
    @Override
    public String[] getNames() {
        
        return new String[]{"kick"};
    }
    
    @Override
    public String[] getPrefixes(Guild guild) {
        
        return Prefixes.getAdminPrefixesFor(guild);
    }
    
    @Override
    public List<Permission> getRequiredPermissions() {
        
        return PERMISSIONS;
    }
    
    private static List<Permission> PERMISSIONS = Arrays.asList(Permission.KICK_MEMBERS);
    
    @Override
    public List<Permission> requiredBotPermissions() {
        
        return PERMISSIONS;
    }
    
    @Override
    public void run(Bot bot, Member author, TextChannel channel, Guild guild, Message message, String commandname, String[] args, String content) {
        
        message.delete().subscribe((ignored) -> {
        }, (ignored) -> {
        });
        
        if (args.length < 1) {
            channel.createMessage("Please mention the user to kick as first argument.").subscribe();
            return;
        }
        
        StringBuilder reason = new StringBuilder();
        for (int i = 1 ; i < args.length ; i++) {
            String s = args[i];
            reason.append(s).append(" ");
        }
        
        if (!args[0].matches("(?:<@!?)?\\d+>?")) {
            channel.createMessage("Please mention the user to kick as first argument.").subscribe();
        } else {
            String id = args[0].replaceAll("(?:<@!?)?(\\d+)>?", "$1");
            System.out.println(id);
            guild.getMemberById(Snowflake.of(id)).subscribe(
                    member -> {
                        Confirmation poll = new Confirmation(
                                bot,
                                author,
                                channel,
                                "Do you really want to kick " + member.getUsername() + "#" + member.getDiscriminator() + "?",
                                "Type confirm or cancel",
                                false,
                                30 * 1000
                        );
                        bot.addPoll(poll);
                        poll.subscribe((value, type) -> {
                            switch (type) {
                                case INACTIVE:
                                    MessageAPI.sendAndDeleteMessageLater(
                                            channel,
                                            "Confirmation timed out, canceling kick",
                                            10000
                                                                        );
                                    return;
                                case SUCCESS:
                                    if (value) {
                                        break;
                                    }
                                case EXIT:
                                default:
                                    MessageAPI.sendAndDeleteMessageLater(channel, "Canceling kick", 10000);
                                    return;
                            }
                            member.getPrivateChannel().subscribe(dm -> {
                                String text = "You have been kicked from " + guild.getName();
                                text += reason.length() > 0 ? " for " + reason : " without reason";
                                dm.createMessage(text)
                                  .subscribe(
                                          (ignored) -> {
                                              kickMember(member, reason.toString(), channel, member.getUsername());
                                          },
                                          (ignored) -> {
                                              kickMember(member, reason.toString(), channel, member.getUsername());
                                          }
                                            );
                            });
                        });
                    },
                    (ignored) -> {
                    }
                                                           );
        }
        
    }
    
    private void kickMember(Member member, String reason, TextChannel kickchannel, String username) {
        
        member.kick(reason).subscribe(
                (ignored) -> {},
                (ignored) -> {},
                () -> {
                    String r = reason.length() > 0 ? " for ```\n" + reason + "\n```" : "";
                    kickchannel.createMessage(username + " (" + member.getId().asString() + ") has successfully been kicked" + r)
                            .subscribe();
                });
    }
    
    @Override
    public void onLoad(Bot bot) {
    
    }
}
