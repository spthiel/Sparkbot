package me.console.commands;

import me.console.ConsoleCommand;
import me.main.Main;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IGuild;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.util.RequestBuffer;

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

				IChannel channel = RequestBuffer.request(() -> {
					IGuild guild = Main.getBot().getClient().getGuildByID(guildID);
					for(IChannel c : guild.getChannels())
						if(c.getName().equalsIgnoreCase(args[1]))
							return c;
					return null;
				}).get();

				if(channel == null) {
					System.err.println("Couldn't find that channel");
					return;
				}

				List<IMessage> messageList = RequestBuffer.request(() -> {
					return channel.getMessageHistory(amount);
				}).get();

				System.out.println("Messages of #" + channel.getName() + " in " + channel.getGuild().getName());
				messageList.forEach(message -> System.out.println("\t" + message.getAuthor().getName() + ": " + message.getContent().replace("\n","\n\t\t")));
			} catch(NumberFormatException e) {
				System.err.println("Please enter valid ids.");
			}
		else
			System.err.println(getHelp());
	}

	@Override
	public void onLoad() {

	}
}
