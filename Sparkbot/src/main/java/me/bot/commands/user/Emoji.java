package me.bot.commands.user;

import discord4j.core.object.entity.Guild;
import discord4j.core.object.entity.Message;
import discord4j.core.object.entity.MessageChannel;
import discord4j.core.object.entity.User;
import discord4j.core.object.util.Permission;
import discord4j.core.spec.EmbedCreateSpec;
import discord4j.core.spec.MessageCreateSpec;
import me.bot.base.Bot;
import me.bot.base.CommandType;
import me.bot.base.ICommand;
import me.main.Prefixes;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
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
	public boolean hasPermissions(User user, Guild guild) {
		return true;
	}

	@Override
	public List<Permission> requiredBotPermissions() {
		return null;
	}

	//https://cdn.discordapp.com/emojis/<id>.png?v=1

	@Override
	public void run(Bot bot, User author, MessageChannel channel, Guild guild, Message message, String command, String[] args, String content) {
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
