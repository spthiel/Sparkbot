package me.bot.commands.admin;

import discord4j.core.object.entity.Guild;
import discord4j.core.object.entity.Member;
import discord4j.core.object.entity.Message;
import discord4j.core.object.entity.channel.TextChannel;
import discord4j.rest.util.Permission;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Collections;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import me.bot.base.Bot;
import me.bot.base.CommandType;
import me.bot.base.ICommand;
import me.main.Prefixes;

@SuppressWarnings("unused")
public class Moan implements ICommand {
    
    private static final Logger logger = LoggerFactory.getLogger(Moan.class);
    
    @Override
    public CommandType getType() {
        
        return CommandType.ADMIN;
    }
    
    @Override
    public String getHelp() {
        
        return "Moans";
    }
    
    @Override
    public String[] getNames() {
        
        return new String[]{"moan","touch"};
    }
    
    @Override
    public String[] getPrefixes(Guild guild) {
        
        return new String[]{Prefixes.getSuperAdminPrefix()};
    }
    
    @Override
    public List<Permission> getRequiredPermissions() {
        
        return null;
    }
    
    private static final List<Permission> PERMISSIONS = Collections.singletonList(Permission.MANAGE_MESSAGES);
    
    @Override
    public List<Permission> requiredBotPermissions() {
        
        return PERMISSIONS;
    }
    
    @Override
    public void run(Bot bot, Member author, TextChannel channel, Guild guild, Message message, String commandName, String[] args, String content) {
        message.delete().subscribe();
        channel.createMessage(spec -> {
            try {
                spec.addFile("moan.wav",new FileInputStream(new File(bot.getResourceManager().getBaseFolder(),"moan.wav")));
            } catch (FileNotFoundException e) {
                logger.error("Couldn't load file", e);
            }
        }).subscribe();
    }
    
    @Override
    public void onLoad(Bot bot) {
    
    }
}
