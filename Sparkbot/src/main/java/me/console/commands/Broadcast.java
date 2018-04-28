package me.console.commands;


import me.bot.base.MessageAPI;
import me.console.ConsoleCommand;
import me.main.Main;
import sx.blah.discord.handle.obj.*;
import sx.blah.discord.util.MessageBuilder;

public class Broadcast implements ConsoleCommand {

	@Override
	public String getHelp() {
		return "Broadcast a message (currently) to 1 channel.";
	}

	@Override
	public void run(String... args) {

		if(args.length == 0 || args[0].trim().equalsIgnoreCase("")) {
			System.err.println("[Broadcast] Please enter at least one argument");
			return;
		}

		StringBuilder stringBuilder = new StringBuilder();


		for (String str : args) {
			stringBuilder.append(str).append(" ");
		}
		String message = stringBuilder.toString().trim();

		for (IGuild guild : Main.getBot().getClient().getGuilds()) {
			IChannel broadcastChannel = getBraodcastChannel(guild);
			if(broadcastChannel != null)
				MessageAPI.sendMessage(new MessageBuilder(Main.getBot().getClient()).withChannel(broadcastChannel).appendContent(message));
		}
	}

	private IChannel getBraodcastChannel(IGuild guild) {
		for(IChannel channel : guild.getChannels()) {
			if(channel.getName().equalsIgnoreCase("bot-testing"))
				return channel;
		}
		return null;
	}

	@Override
	public void onLoad() {

	}

	@Override
	public String[] getNames() {
		String[] out = {"broadcast","bc"};
		return out;
	}

}