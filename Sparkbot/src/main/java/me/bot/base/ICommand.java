package me.bot.base;

import discord4j.core.object.entity.*;
import discord4j.core.object.util.Permission;
import reactor.core.publisher.Mono;

import java.util.EnumSet;
import java.util.List;

public interface ICommand {

	CommandType getType();
	String getHelp();
	String[] getNames();
	String[] getPrefixes(final Guild guild);
	Permission[] getRequiredPermissions();
	List<Permission> requiredBotPermissions();
	void run(final Bot bot, final User author, final TextChannel channel, final Guild guild, final Message message, final String commandname, final String[] args, final String content);
	void onLoad();

}
