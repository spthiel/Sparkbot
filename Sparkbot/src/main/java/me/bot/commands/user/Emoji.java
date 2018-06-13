package me.bot.commands.user;

import discord4j.core.object.entity.Guild;
import discord4j.core.object.entity.Member;
import discord4j.core.object.entity.Message;
import discord4j.core.object.entity.TextChannel;
import discord4j.core.object.util.Permission;
import discord4j.core.spec.EmbedCreateSpec;
import discord4j.core.spec.MessageCreateSpec;
import me.bot.base.Bot;
import me.bot.base.CommandType;
import me.bot.base.ICommand;
import me.main.Prefixes;

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

	@Override
	public List<Permission> getRequiredPermissions() {
		return null;
	}

	@Override
	public List<Permission> requiredBotPermissions() {
		return null;
	}

	//https://cdn.discordapp.com/emojis/<id>.png?v=1

	@Override
	public void run(final Bot bot, final Member author, final TextChannel channel, final Guild guild, final Message message, final String command, final String[] args, final String content) {
		if(args.length >= 1) {
			String emoji = args[0];
			if(emoji.matches("<:.+?:\\d+>")) {
				emoji = emoji.replaceAll("<:.+?:(\\d+)>","$1");
				channel.createMessage(new MessageCreateSpec().setEmbed(new EmbedCreateSpec().setImage("https://cdn.discordapp.com/emojis/" + emoji + ".png?v=1"))).subscribe();
			} else if(emoji.matches("<a:.+?:\\d+>")) {
				emoji = emoji.replaceAll("<a:.+?:(\\d+)>","$1");
				channel.createMessage(new MessageCreateSpec().setEmbed(new EmbedCreateSpec().setImage("https://cdn.discordapp.com/emojis/" + emoji + ".gif?v=1"))).subscribe();
			}
		}
	}

	@Override
	public void onLoad(final Bot bot) {

	}
}
