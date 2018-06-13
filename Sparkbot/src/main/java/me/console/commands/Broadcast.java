package me.console.commands;


import discord4j.core.object.entity.Channel;
import discord4j.core.object.entity.Guild;
import discord4j.core.object.entity.MessageChannel;
import discord4j.core.object.entity.TextChannel;
import me.bot.base.Bot;
import me.bot.base.MessageAPI;
import me.bot.base.MessageBuilder;
import me.console.ConsoleCommand;
import me.main.Main;

public class Broadcast implements ConsoleCommand {

	@Override
	public String getHelp() {
		return "Broadcast a message (currently) to 1 channel.";
	}

	@Override
	public void run(String... args) {

		if(args.length <= 1 || args[1].trim().equalsIgnoreCase("")) {
			System.err.println("[Broadcast] Please enter at least one argument");
			return;
		}

		StringBuilder stringBuilder = new StringBuilder();


		for (String str : args) {
			stringBuilder.append(str).append(" ");
		}
		String message = stringBuilder.toString().trim();

		for (Guild guild : Bot.getBotByName(args[0]).getClient().getGuilds().toIterable()) {
			MessageChannel broadcastChannel = getBraodcastChannel(guild);
			if(broadcastChannel != null)
				new MessageBuilder().withChannel(broadcastChannel).appendContent(message).send();
		}
	}

	private MessageChannel getBraodcastChannel(Guild guild) {
		for(Channel channel : guild.getChannels().toIterable()) {
			if(channel.getType().equals(Channel.Type.GUILD_TEXT)) {
				if (((TextChannel) channel).getName().equalsIgnoreCase("spark-announcements"))
					return (MessageChannel) channel;
			}
		}
		return null;
	}

	@Override
	public void onLoad() {

	}

	@Override
	public String[] getNames() {
		return new String[]{"broadcast","bc"};
	}

}