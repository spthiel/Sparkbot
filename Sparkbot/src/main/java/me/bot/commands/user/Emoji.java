package me.bot.commands.user;

import discord4j.core.object.entity.*;
import discord4j.core.object.util.Permission;
import discord4j.core.spec.EmbedCreateSpec;
import discord4j.core.spec.MessageCreateSpec;
import me.bot.base.Bot;
import me.bot.base.CommandType;
import me.bot.base.ICommand;
import me.main.Prefixes;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.List;

public class Emoji implements ICommand {
	@Override
	public CommandType getType() {
		return CommandType.PUBLIC;
	}

	@Override
	public String getHelp() {
		return "Enlarges an emoji";
	}

	@Override
	public String[] getNames() {
		return new String[]{"emoji","enlarge"};
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

	//https://cdn.discordapp.com/emojis/<id>.png?v=1

	@Override
	public void run(Bot bot, User author, TextChannel channel, Guild guild, Message message, String command, String[] args, String content) {
		System.out.println(Arrays.asList(args));
		if(args.length >= 1) {
			String emoji = args[0];
			if(emoji.matches("<:.+?:\\d+>")) {
				emoji = emoji.replaceAll("<:.+?:(\\d+)>","$1");
				channel.createMessage(new MessageCreateSpec().setEmbed(new EmbedCreateSpec().setImage("https://cdn.discordapp.com/emojis/" + emoji + ".png?v=1"))).block();
			}
		}
	}

	@Override
	public void onLoad() {

	}
}
