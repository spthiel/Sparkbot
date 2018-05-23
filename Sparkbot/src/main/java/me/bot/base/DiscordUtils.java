package me.bot.base;

import discord4j.core.object.entity.Channel;
import discord4j.core.object.entity.MessageChannel;
import discord4j.core.object.entity.TextChannel;

public class DiscordUtils {

	private Bot bot;

	public DiscordUtils(Bot bot) {
		this.bot = bot;
	}

	public static MessageChannel getMessageChannelOfChannel(Channel channel) {
		if(channel instanceof MessageChannel)
			return (MessageChannel)channel;
		return null;
	}

	public static TextChannel getTextChannelOfChannel(Channel channel) {
		if(channel instanceof TextChannel)
			return (TextChannel)channel;
		return null;
	}

}
