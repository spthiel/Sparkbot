package me.bot.commands.console;

import me.main.Main;

public class Help implements ConsoleCommand {

	@Override
	public String getName() {
		return "help";
	}

	@Override
	public String getHelp() {
		return "Displays the commands the bot supports.";
	}

	@Override
	public void onLoad(String[] args) {
		if (args.length == 2) {
			boolean worked = false;
			for (ConsoleCommand command : Main.getCommandManager().getCommands()) {
				if (command.getName().equalsIgnoreCase(args[1])) {
					worked = true;
					System.out.println(" - Help - ");
					System.out.println("Command: " + command.getName());
					System.out.println(" - Description: " + command.getHelp());
				}
			}
			if (!worked) {
				System.out.println("Uh oh, '" + args[0] + "' wasn't found as a Command.");
			}
		} else {
			System.out.println(" - Help - ");
			for (ConsoleCommand command : Main.getCommandManager().getCommands()) {
				System.out.println("Command: " + command.getName());
				System.out.println(" - Description: " + command.getHelp());
			}
		}
	}
}
