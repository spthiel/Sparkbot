package me.bot.commands.admin;

import discord4j.core.object.entity.Guild;
import discord4j.core.object.entity.Member;
import discord4j.core.object.entity.Message;
import discord4j.core.object.entity.TextChannel;
import discord4j.core.object.util.Permission;

import java.util.Collections;
import java.util.List;

import me.bot.base.Bot;
import me.bot.base.CommandType;
import me.bot.base.ICommand;
import me.main.Prefixes;

public class TisNo implements ICommand {
    
    @Override
    public CommandType getType() {
        
        return CommandType.ADMIN;
    }
    
    @Override
    public String getHelp() {
        
        return "Tis no bot ocmmands though :c";
    }
    
    @Override
    public String[] getNames() {
        
        return new String[]{"tisno"};
    }
    
    @Override
    public String[] getPrefixes(Guild guild) {
        
        return new String[]{Prefixes.getSuperAdminPrefix()};
    }
    
    private static List<Permission> PERMISSIONS = Collections.singletonList(Permission.MANAGE_MESSAGES);
    
    @Override
    public List<Permission> getRequiredPermissions() {
        
        return null;
    }
    
    @Override
    public List<Permission> requiredBotPermissions() {
        
        return PERMISSIONS;
    }
    
    @Override
    public void run(Bot bot, Member author, TextChannel channel, Guild guild, Message message, String commandname, String[] args, String content) {
        message.delete().subscribe();
        channel.createMessage("Tis no bot ocmmands though :c").subscribe();
    }
    
    @Override
    public void onLoad(Bot bot) {
    
    }
}
