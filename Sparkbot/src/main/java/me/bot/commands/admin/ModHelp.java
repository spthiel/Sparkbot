package me.bot.commands.admin;

import me.bot.base.Bot;
import me.bot.base.CommandType;
import me.bot.base.ICommand;
import me.bot.base.MessageAPI;
import me.main.Prefixes;
import sx.blah.discord.handle.obj.IGuild;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.handle.obj.IUser;
import sx.blah.discord.handle.obj.Permissions;
import sx.blah.discord.util.MessageBuilder;

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
		String[] names = {"help","h"};
		return names;
	}

	@Override
	public String[] getPrefixes(IGuild guild) {
		return Prefixes.getAdminPrefixesFor(guild);
	}

	@Override
	public boolean hasPermissions(IGuild guild, IUser user) {
		return true;
	}

	@Override
	public List<Permissions> requiredBotPermissions() {
		return null;
	}

	@Override
	public void run(Bot bot, IUser author, IMessage message, String[] args) {
		ArrayList<String> out = new ArrayList<>();
		final String serverprefix = Prefixes.getAdminPrefixFor(message.getGuild());
		bot.getCommands().stream().filter(iCommand -> iCommand.getType().equals(CommandType.MOD)).collect(Collectors.toList()).forEach(iCommand -> {
			out.add("`" + serverprefix + "" + iCommand.getNames()[0] + "` - " + iCommand.getHelp());
		});

		MessageBuilder builder = new MessageBuilder(bot.getClient());

		builder.withChannel(message.getChannel());
		builder.appendContent("Moderator Commands:\n");
		out.forEach(msg -> builder.appendContent(msg + "\n"));
		MessageAPI.sendMessage(builder);

	}

	@Override
	public void onLoad() {

	}
}
