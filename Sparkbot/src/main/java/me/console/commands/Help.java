package me.console;

import me.main.Main;

public class Help implements ConsoleCommand {

	public String getHelp() {
		return "Displays all the console commands this bot supports.";
	}

	@Override
	public void run(String... args) {
		if (args.length == 2) {
			boolean worked = false;
			for (ConsoleCommand command : Main.getCommandManager().getCommands()) {

				boolean bool = false;
				for (String name : command.getNames())
					if (name.equalsIgnoreCase(args[1])) {
						bool = true;
						break;
					}

				if (bool) {
					worked = true;
					System.out.println(" - Help - ");
					System.out.println("Command: " + command.getNames()[0]);
					System.out.println(" - Description: " + command.getHelp());
				}
			}
			if (!worked) {
				System.out.println("Uh oh, '" + args[0] + "' wasn't found as a Command.");
			}
		} else {
			System.out.println(" - Help - ");
			for (ConsoleCommand command : Main.getCommandManager().getCommands()) {
				System.out.println("Command: " + command.getNames()[0]);
				System.out.println(" - Description: " + command.getHelp());
			}
		}
	}

	@Override
	public void onLoad() {

	}

	@Override
	public String[] getNames() {
		String[] out = {"help", "?"};
		return out;
	}
}
