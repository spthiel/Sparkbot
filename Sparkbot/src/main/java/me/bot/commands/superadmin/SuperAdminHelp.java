package me.bot.commands.superadmin;

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

public class SuperAdminHelp implements ICommand {
	@Override
	public CommandType getType() {
		return CommandType.ADMIN;
	}

	@Override
	public String getHelp() {
		return "Lists the Helppage";
	}

	@Override
	public String[] getNames() {
		String[] names = {"help","h"};
		return names;
	}

	@Override
	public String[] getPrefixes(Guild guild) {
		String[] prefixes = {Prefixes.getSuperAdminPrefix()};
		return prefixes;
	}

	@Override
	public boolean hasPermissions( User user, Guild guild) {
		return true;
	}

	@Override
	public List<Permission> requiredBotPermissions() {
		return null;
	}

	@Override
	public void run(Bot bot, User author, MessageChannel channel, Guild guild, String content, Message message, String[] args) {
		ArrayList<String> out = new ArrayList<>();
		final String serverprefix = Prefixes.getSuperAdminPrefix();
		bot.getCommands().stream().filter(iCommand -> iCommand.getType().equals(CommandType.ADMIN)).collect(Collectors.toList()).forEach(iCommand -> {
			out.add("`" + serverprefix + "" + iCommand.getNames()[0] + "` - " + iCommand.getHelp());
		});

		MessageBuilder builder = new MessageBuilder(bot.getClient());

		builder.withChannel(channel);
		builder.appendContent("Superadmin Commands:\n");
		out.forEach(msg -> builder.appendContent(msg + "\n"));
		builder.send();

	}

	@Override
	public void onLoad() {

	}
}
