package me.bot.commands.admin;

import discord4j.core.object.entity.*;
import discord4j.core.object.entity.channel.TextChannel;
import discord4j.rest.util.Permission;

import me.bot.base.*;
import me.main.Prefixes;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("unused")
public class SuperAdminHelp implements ICommand {
	@Override
	public CommandType getType() {
		return CommandType.ADMIN;
	}

	@Override
	public String getHelp() {
		return "Lists the help page";
	}

	@Override
	public String[] getNames() {
		
		return new String[]{"help","h"};
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
	public void run(final Bot bot, final Member author, final TextChannel channel, final Guild guild, final Message message, final String commandName, final String[] args, final String content) {
		ArrayList<String> out = new ArrayList<>();
		final String serverPrefix = Prefixes.getSuperAdminPrefix();
		bot.getCommands().stream().filter(iCommand -> iCommand.getType().equals(CommandType.ADMIN)).toList()
		   .forEach(iCommand -> out.add("`" + serverPrefix + iCommand.getNames()[0] + "` - " + iCommand.getHelp()));

		MessageBuilder builder = new MessageBuilder();

		builder.withChannel(channel);
		builder.appendContent("Super admin Commands:\n");
		out.forEach(msg -> builder.appendContent(msg + "\n"));
		builder.send().subscribe();

	}

	@Override
	public void onLoad(final Bot bot) {

	}
}
