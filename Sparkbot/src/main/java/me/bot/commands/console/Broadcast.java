package me.bot.commands.console;


import me.main.Main;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IGuild;

public class Broadcast implements ConsoleCommand{

	@Override
	public String getName() {
		return "broadcast";
	}

	@Override
	public String getHelp() {
		return "Broadcast a message (currently) to 1 channel.";
	}

	@Override
	public void onLoad(String[] args) {
		for (IGuild guild : Main.getBot().getClient().getGuilds()) {
			for (IChannel channel : guild.getChannels()) {
				if (channel.getName().equalsIgnoreCase("bot-testing")) {
					String message = "";
					for (String l : args) {
						if (l.equalsIgnoreCase("broadcast")) {
							continue;
						}
						message += l + " ";
					}
					message = message.trim();
					channel.sendMessage(message);
				}
			}
		}
	}

}