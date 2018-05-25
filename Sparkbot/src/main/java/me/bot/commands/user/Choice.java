package me.bot.commands.user;

import discord4j.core.object.entity.Guild;
import discord4j.core.object.entity.Message;
import discord4j.core.object.entity.TextChannel;
import discord4j.core.object.entity.User;
import discord4j.core.object.util.Permission;
import discord4j.core.spec.MessageCreateSpec;
import me.bot.base.Bot;
import me.bot.base.CommandType;
import me.bot.base.ICommand;
import me.main.Prefixes;
import reactor.core.publisher.Mono;

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

	private static Permission[] PERMISSIONS = new Permission[]{};

	@Override
	public Permission[] getRequiredPermissions() {
		return PERMISSIONS;
	}
	@Override
	public List<Permission> requiredBotPermissions() {
		return null;
	}

	@Override
	public void run(Bot bot, User author, TextChannel channel, Guild guild, Message message, String commandname, String[] args, String content) {
		Random rnd = new Random();
		String rndchoice = args[rnd.nextInt(args.length)];
		channel.createMessage(new MessageCreateSpec().setContent("I choose " + rndchoice)).subscribe();
	}

	@Override
	public void onLoad() {

	}
}
