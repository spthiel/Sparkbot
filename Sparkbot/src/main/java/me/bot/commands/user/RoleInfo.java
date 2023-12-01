package me.bot.commands.user;

import discord4j.core.object.entity.*;
import discord4j.core.object.entity.channel.TextChannel;
import discord4j.rest.util.Permission;

import java.util.List;
import java.util.Optional;

import me.bot.base.Bot;
import me.bot.base.CommandType;
import me.bot.base.ICommand;
import me.bot.base.IDisabledCommand;
import me.main.Prefixes;
import me.main.utils.DiscordUtils;

@SuppressWarnings("unused")
public class RoleInfo implements ICommand, IDisabledCommand {
    
    @Override
    public CommandType getType() {
        
        return CommandType.PUBLIC;
    }
    
    @Override
    public String getHelp() {
        
        return "Shows id of a role";
    }
    
    @SuppressWarnings("SpellCheckingInspection")
    @Override
    public String[] getNames() {
        
        return new String[]{"role","roleid"};
    }
    
    @Override
    public String[] getPrefixes(Guild guild) {
        
        return Prefixes.getNormalPrefixesFor(guild);
    }
    
    @Override
    public List<Permission> getRequiredPermissions() {
        
        return null;
    }
    
    @Override
    public List<Permission> requiredBotPermissions() {
        
        return null;
    }
    
    @Override
    public void run(Bot bot, Member author, TextChannel channel, Guild guild, Message message, String commandName, String[] args, String content) {
        
        if(args.length == 0) {
            channel.createMessage("<:red_cross:398120014974287873> **| Please specify a role by id, ping or name.**").subscribe();
        } else if(args[0].matches("\\d+")) {
            DiscordUtils.getRoleById(bot, guild, args[0]).subscribe(role -> printRole(channel, role));
        } else if(args[0].matches("<@&\\d+>")) {
            message.getRoleMentions().single().subscribe(role -> printRole(channel, Optional.of(role)));
        } else {
            DiscordUtils.getRoleByName(bot, guild, args[0]).subscribe(role -> printRole(channel, role));
        }
    }
    
    private void printRole(TextChannel channel, Optional<Role> role) {
        if(role.isEmpty()) {
            channel.createMessage("Couldn't find a role matching that identifier").subscribe();
            return;
        }
        channel.createMessage(spec -> spec.setContent("Role " + role.get().getName() + " has the id " + role.get().getId().asString())).subscribe();
    }
    
    @Override
    public void onLoad(Bot bot) {
    
    }
}
