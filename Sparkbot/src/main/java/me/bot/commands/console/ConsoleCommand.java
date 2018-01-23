package me.bot.commands.console;

public interface ConsoleCommand {
	String[] getNames();
	String getHelp();
	void onLoad(String[] args);
}