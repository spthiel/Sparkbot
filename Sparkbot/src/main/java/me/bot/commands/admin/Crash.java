package me.bot.commands.admin;

import discord4j.core.object.entity.Guild;
import discord4j.core.object.entity.Member;
import discord4j.core.object.entity.Message;
import discord4j.core.object.entity.channel.TextChannel;
import discord4j.rest.util.Permission;

import java.util.List;

import me.bot.base.Bot;
import me.bot.base.CommandType;
import me.bot.base.ICommand;
import me.main.Prefixes;

public class Crash implements ICommand {
	
	@Override
	public CommandType getType() {
		
		return CommandType.ADMIN;
	}
	
	@Override
	public String getHelp() {
		
		return "CRAASH!!!111";
	}
	
	@Override
	public String[] getNames() {
		
		return new String[]{"crash","go_die"};
	}
	
	@Override
	public String[] getPrefixes(Guild guild) {
		
		return new String[]{Prefixes.getSuperAdminPrefix()};
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
	public void run(Bot bot, Member author, TextChannel channel, Guild guild, Message message, String commandname, String[] args, String content) {
		channel.createMessage(spec -> spec.setContent("Oh noe")).subscribe();
		throw new RuntimeException("Manual Requested Crash");
	}
	
	@Override
	public void onLoad(Bot bot) {
	
	}
}
