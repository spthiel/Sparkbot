package me.console.commands;

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
				message.append(args[i] + " ");
			}

			Main.getBot().getClient().getChannelByID(id).sendMessage(message.toString().trim());
		}
	}

	@Override
	public void onLoad() {

	}
}
