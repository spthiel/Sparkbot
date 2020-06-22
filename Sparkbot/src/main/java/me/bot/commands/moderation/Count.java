package me.bot.commands.moderation;

import discord4j.common.util.Snowflake;
import discord4j.core.object.entity.Guild;
import discord4j.core.object.entity.Member;
import discord4j.core.object.entity.Message;
import discord4j.core.object.entity.channel.TextChannel;
import discord4j.rest.util.Permission;

import java.util.Arrays;
import java.util.List;

import me.bot.base.Bot;
import me.bot.base.CommandType;
import me.bot.base.ICommand;
import me.bot.base.MessageAPI;
import me.main.Prefixes;

public class Count implements ICommand {
    
    @Override
    public CommandType getType() {
        
        return CommandType.MOD;
    }
    
    @Override
    public String getHelp() {
        
        return "Count messages to a after a message";
    }
    
    private static final String[] names = {"count"};
    
    @Override
    public String[] getNames() {
        
        return names;
    }
    
    @Override
    public String[] getPrefixes(Guild guild) {
        
        return Prefixes.getAdminPrefixesFor(guild);
    }
    
    private static List<Permission> PERMISSIONS = Arrays.asList(Permission.MANAGE_MESSAGES, Permission.MANAGE_GUILD, Permission.MANAGE_ROLES);
    
    @Override
    public List<Permission> getRequiredPermissions() {
        
        return PERMISSIONS;
    }
    
    @Override
    public List<Permission> requiredBotPermissions() {
        
        return null;
    }
    
    @Override
    public void run(Bot bot, Member author, TextChannel channel, Guild guild, Message message, String commandname, String[] args, String content) {
        if(args.length == 0 || !args[0].matches("^\\d+$")) {
            MessageAPI.sendAndDeleteMessageLater(channel, "s!count <message id>", 10000);
            return;
        }
        try {
            long id = Long.parseLong(args[0]);
            channel.getMessagesAfter(Snowflake.of(id))
                   .count().subscribe(c -> {
                MessageAPI.sendAndDeleteMessageLater(
                        channel,
                        "There's " + (c + 2) + " messages until that message",
                        10000
                                                    );
            });
        } catch(NumberFormatException e) {
            MessageAPI.sendAndDeleteMessageLater(channel, "s!count <message id>", 10000);
        }
        
    }
    
    @Override
    public void onLoad(Bot bot) {
    
    }
}
