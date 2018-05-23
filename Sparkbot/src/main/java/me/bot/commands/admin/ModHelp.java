package me.bot.commands.admin;

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

public class ModHelp implements ICommand {
	@Override
	public CommandType getType() {
		return CommandType.MOD;
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
		return Prefixes.getAdminPrefixesFor(guild);
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
		final String serverprefix = Prefixes.getAdminPrefixFor(guild);
		bot.getCommands().stream().filter(iCommand -> iCommand.getType().equals(CommandType.MOD)).collect(Collectors.toList()).forEach(iCommand -> {
			out.add("`" + serverprefix + "" + iCommand.getNames()[0] + "` - " + iCommand.getHelp());
		});

		MessageBuilder builder = new MessageBuilder(bot.getClient());

		builder.withChannel(channel);
		builder.appendContent("Moderator Commands:\n");
		out.forEach(msg -> builder.appendContent(msg + "\n"));
		builder.send().subscribe();

	}

	@Override
	public void onLoad() {

	}
}
