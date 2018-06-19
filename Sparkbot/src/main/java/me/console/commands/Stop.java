package me.console.commands;

import me.console.ConsoleCommand;
import me.main.Main;

public class Stop implements ConsoleCommand {
	@Override
	public String[] getNames() {
		return new String[]{"stop"};
	}

	@Override
	public String getHelp() {
		return "safely stops shit";
	}

	@Override
	public void run(String... args) {

		Main.exit();

	}

	@Override
	public void onLoad() {

	}
}
