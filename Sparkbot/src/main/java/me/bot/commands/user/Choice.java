package me.bot.commands.user;

import discord4j.core.object.entity.Guild;
import discord4j.core.object.entity.Member;
import discord4j.core.object.entity.Message;
import discord4j.core.object.entity.TextChannel;
import discord4j.core.object.util.Permission;
import discord4j.core.spec.MessageCreateSpec;
import me.bot.base.Bot;
import me.bot.base.CommandType;
import me.bot.base.ICommand;
import me.main.Prefixes;

import java.util.List;
import java.util.Random;

public class Choice implements ICommand {
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

	private static Random RANDOM = new Random();

	@Override
	public void run(final Bot bot, final Member author, final TextChannel channel, final Guild guild, final Message message, final String command, final String[] args, final String content) {
		if(args.length > 0) {
			String rndchoice = args[RANDOM.nextInt(args.length)];
			channel.createMessage(spec -> spec.setContent("I choose " + rndchoice)).subscribe();
		}
	}

	@Override
	public void onLoad(final Bot bot) {

	}
}
