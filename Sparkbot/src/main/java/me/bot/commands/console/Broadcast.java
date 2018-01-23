package me.bot.commands.console;


import me.main.Main;
import me.main.PermissionManager;
import me.main.Prefixes;
import sx.blah.discord.handle.obj.*;
import sx.blah.discord.util.DiscordException;
import sx.blah.discord.util.Image;
import sx.blah.discord.util.RequestBuffer;

import java.util.List;

public class Broadcast implements ConsoleCommand{

	@Override
	public String getHelp() {
		return "Broadcast a message (currently) to 1 channel.";
	}

	@Override
	public String[] getNames() {
		String[] out = {"broadcast","bc"};
		return out;
	}

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