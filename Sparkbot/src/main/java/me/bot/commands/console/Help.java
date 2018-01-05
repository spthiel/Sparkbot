package me.bot.commands.console;

import me.main.Main;
import me.main.PermissionManager;
import me.main.Prefixes;
import sx.blah.discord.handle.obj.IGuild;
import sx.blah.discord.handle.obj.IUser;
import sx.blah.discord.handle.obj.Permissions;

import java.util.List;

public class Help implements ConsoleCommand {

	public String getHelp() {
		return "Displays all the console commands this bot supports.";
	}

	@Override
	public String getName() {
		return "Help";
	}

	@Override
	public String getName2() {
		return "help";
	}

	@Override
	public String getName3() {
		return "?";
	}

	@Override
	public void onLoad(String[] args) {
		if (args.length == 2) {
			boolean worked = false;
			for (ConsoleCommand command : Main.getCommandManager().getCommands()) {
				if (command.getName().equals(args[1])) {
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
