package me.bot.commands.console;

public interface ConsoleCommand {
	String getName();
	String getName2();
	String getName3();
	String getHelp();
	void onLoad(String[] args);
}