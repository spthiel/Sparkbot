package me.bot.commands.user;

import discord4j.core.object.entity.*;
import me.bot.base.*;
import me.main.Prefixes;

import discord4j.core.object.entity.channel.TextChannel;
import discord4j.rest.util.Permission;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("unused")
public class UserHelp implements ICommand {
	@Override
	public CommandType getType() {
		return CommandType.PUBLIC;
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
	public void run(final Bot bot, final Member author, final TextChannel channel, final Guild guild, final Message message, final String commandName, final String[] args, final String content) {
		ArrayList<String> out = new ArrayList<>();
		final String serverPrefix = Prefixes.getNormalPrefixFor(guild);
		bot.getCommands().stream().filter(iCommand -> iCommand.getType().equals(CommandType.PUBLIC)).toList()
		   .forEach(iCommand -> out.add("`" + serverPrefix + iCommand.getNames()[0] + "` "));

		System.out.println(out);
		MessageBuilder builder = new MessageBuilder();

		builder.withChannel(channel);
		builder.appendContent("Public Commands:\n");
		out.forEach(msg -> builder.appendContent(msg + "\n"));
		builder.send().subscribe();

	}

	@Override
	public void onLoad(final Bot bot) {

	}
}
