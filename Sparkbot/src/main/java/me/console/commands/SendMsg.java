package me.console.commands;

import discord4j.core.object.entity.MessageChannel;
import discord4j.core.object.util.Snowflake;
import discord4j.core.spec.MessageCreateSpec;
import me.bot.base.DiscordUtils;
import me.main.Main;
import me.console.ConsoleCommand;

public class SendMsg implements ConsoleCommand {
	@Override
	public String[] getNames() {
		String[] out = {"send"};
		return out;
	}

	@Override
	public String getHelp() {
		return "send <channelid> <message>";
	}

	@Override
	public void run(String... args) {
		if(args.length >= 2) {
			long id = Long.parseLong(args[0]);
			StringBuilder message = new StringBuilder();
			for(int i = 1; i < args.length; i++) {
				message.append(args[i]).append(" ");
			}

			MessageChannel channel = DiscordUtils.getMessageChannelOfChannel(Main.getBot().getClient().getChannelById(Snowflake.of(id)).block());
			if(channel != null)
				channel.createMessage(new MessageCreateSpec().setContent(message.toString().trim()));
			else
				System.out.println("Couldn't find that channel.");
		}
	}

	@Override
	public void onLoad() {

	}
}
