package me.console.commands;

import discord4j.core.object.entity.*;
import discord4j.core.object.util.Snowflake;
import me.bot.base.DiscordUtils;
import me.console.ConsoleCommand;
import me.main.Main;

import java.util.List;

public class Chatlog implements ConsoleCommand {
	@Override
	public String[] getNames() {
		String[] out = {"chatlog"};
		return out;
	}

	@Override
	public String getHelp() {
		return "chatlog <guildid> <channelname> [amount]";
	}

	@Override
	public void run(String... args) {

		if(args.length >= 2)
			try {
				final int amount;
				if(args.length > 2) {
					amount = Integer.parseInt(args[2]);
				} else {
					amount = 100;
				}
				final long guildID = Long.parseLong(args[0]);

				MessageChannel channel = getChannel(guildID,args[1]);

				if(channel == null) {
					System.err.println("Couldn't find that channel");
					return;
				}

				//TODO: Fix stuff

//				List<IMessage> messageList = channel.getLastMessage(amount);

//				System.out.println("Messages of #" + channel.getName() + " in " + channel.getGuild().getName());
//				messageList.forEach(message -> System.out.println("\t" + message.getAuthor().getName() + ": " + message.getContent().replace("\n","\n\t\t")));
			} catch(NumberFormatException e) {
				System.err.println("Please enter valid numbers.");
			}
		else
			System.err.println(getHelp());
	}

	private MessageChannel getChannel(long guildID, String channelName) {
		Guild guild = Main.getBot().getClient().getGuildById(Snowflake.of(guildID)).block();
		for(Channel c : guild.getChannels().toIterable()) {
			if(c.getType().equals(Channel.Type.GUILD_TEXT)) {
				TextChannel channel = DiscordUtils.getTextChannelOfChannel(c);
				if (channel.getName().equalsIgnoreCase(channelName))
					return channel;
			}
		}

		return null;
	}

	private MessageChannel getChannel(long channelid) {
		return DiscordUtils.getMessageChannelOfChannel(Main.getBot().getClient().getChannelById(Snowflake.of(channelid)).block());
	}

	@Override
	public void onLoad() {

	}
}
