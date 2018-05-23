package me.bot.commands.user;

import discord4j.core.object.entity.Guild;
import discord4j.core.object.entity.Message;
import discord4j.core.object.entity.MessageChannel;
import discord4j.core.object.entity.User;
import discord4j.core.object.util.Permission;
import me.bot.base.*;
import me.main.Prefixes;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class UserHelp implements ICommand {
	@Override
	public CommandType getType() {
		return CommandType.PUBLIC;
	}

	@Override
	public String getHelp() {
		return "Lists the Helppage";
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
	public boolean hasPermissions(User user, Guild guild) {
		return true;
	}

	@Override
	public List<Permission> requiredBotPermissions() {
		return null;
	}

	@Override
	public void run(Bot bot, User author, MessageChannel channel, Guild guild, Message message, String command, String[] args, String content) {
		ArrayList<String> out = new ArrayList<>();
		final String serverprefix = Prefixes.getNormalPrefixFor(guild);
		bot.getCommands().stream().filter(iCommand -> iCommand.getType().equals(CommandType.PUBLIC)).collect(Collectors.toList()).forEach(iCommand -> {
			out.add("`" + serverprefix + "" + iCommand.getNames()[0] + "` ");
		});

		System.out.println(out);
		MessageBuilder builder = new MessageBuilder(bot.getClient());

		builder.withChannel(channel);
		builder.appendContent("Public Commands:\n");
		out.forEach(msg -> builder.appendContent(msg + "\n"));
		builder.send().subscribe();

	}

	@Override
	public void onLoad() {

	}
}
