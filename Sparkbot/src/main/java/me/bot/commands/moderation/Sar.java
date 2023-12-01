package me.bot.commands.moderation;

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
import me.bot.base.IDisabledCommand;
import me.main.Prefixes;

@SuppressWarnings("unused")
public class Sar implements ICommand, IDisabledCommand {
    
    @Override
    public CommandType getType() {
        
        return CommandType.MOD;
    }
    
    @Override
    public String getHelp() {
        
        return "Self assignable roles";
    }
    
    @SuppressWarnings("SpellCheckingInspection")
    private static final String[] names = {"selfroles","sar"};
    
    @Override
    public String[] getNames() {
        
        return names;
    }
    
    @Override
    public String[] getPrefixes(Guild guild) {
        
        return Prefixes.getAdminPrefixesFor(guild);
    }
    
    private static final List<Permission> PERMISSIONS = Arrays.asList(Permission.MANAGE_MESSAGES, Permission.MANAGE_GUILD, Permission.MANAGE_ROLES);
    
    @Override
    public List<Permission> getRequiredPermissions() {
        
        return PERMISSIONS;
    }
    
    @Override
    public List<Permission> requiredBotPermissions() {
        
        return null;
    }
    
    @Override
    public void run(Bot bot, Member author, TextChannel channel, Guild guild, Message message, String commandName, String[] args, String content) {
    
    }
    
    @Override
    public void onLoad(Bot bot) {
    
    }
}
