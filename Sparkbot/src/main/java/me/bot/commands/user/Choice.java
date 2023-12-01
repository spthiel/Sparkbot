package me.bot.commands.user;

import discord4j.core.object.entity.Guild;
import discord4j.core.object.entity.Member;
import discord4j.core.object.entity.Message;
import discord4j.core.object.entity.channel.TextChannel;
import discord4j.rest.util.Permission;

import me.bot.base.Bot;
import me.bot.base.CommandType;
import me.bot.base.ICommand;
import me.bot.base.IDisabledCommand;
import me.main.Prefixes;

import java.util.List;
import java.util.Random;

@SuppressWarnings("unused")
public class Choice implements ICommand, IDisabledCommand {
	@Override
	public CommandType getType() {
		return CommandType.PUBLIC;
	}

	@Override
	public String getHelp() {
		return "Makes a choice for you";
	}

	@Override
	public String[] getNames() {
		return new String[]{"choice","choose"};
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

	private static final Random RANDOM = new Random();

	@Override
	public void run(final Bot bot, final Member author, final TextChannel channel, final Guild guild, final Message message, final String commandName, final String[] args, final String content) {
		if(args.length > 0) {
			String choice = args[RANDOM.nextInt(args.length)];
			channel.createMessage(spec -> spec.setContent("I choose " + choice)).subscribe();
		}
	}

	@Override
	public void onLoad(final Bot bot) {

	}
}
