package me.bot.commands.user;

import discord4j.core.object.entity.Guild;
import discord4j.core.object.entity.Message;
import discord4j.core.object.entity.TextChannel;
import discord4j.core.object.entity.User;
import discord4j.core.object.util.Permission;
import me.bot.base.Bot;
import me.bot.base.CommandType;
import me.bot.base.ICommand;
import me.main.Prefixes;

import java.util.List;

public class Stats implements ICommand {

	@Override
	public CommandType getType() {
		return CommandType.PUBLIC;
	}

	@Override
	public String getHelp() {
		return "Displays Stats";
	}

	@Override
	public String[] getNames() {
		return new String[]{"stats","info"};
	}

	@Override
	public String[] getPrefixes(Guild guild) {
		return Prefixes.getNormalPrefixesFor(guild);
	}

	private static Permission[] PERMISSIONS = new Permission[]{};

	@Override
	public Permission[] getRequiredPermissions() {
		return PERMISSIONS;
	}

	@Override
	public List<Permission> requiredBotPermissions() {
		return null;
	}

	@Override
	public void run(Bot bot, User author, TextChannel channel, Guild guild, Message message, String commandname, String[] args, String content) {

	}

	@Override
	public void onLoad() {

	}
}
