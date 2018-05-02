package me.bot.base;

import discord4j.core.object.entity.*;
import discord4j.core.object.util.Permission;

import java.util.List;

public interface ICommand {

	CommandType getType();
	String getHelp();
	String[] getNames();
	String[] getPrefixes(final Guild guild);
	boolean hasPermissions(final User user, final Guild guild);
	List<Permission> requiredBotPermissions();
	void run(final Bot bot, final User author, final MessageChannel channel, final Guild guild, final String content, final Message message, final String[] args);
	void onLoad();

}
