package me.bot.commands.console;


public interface ConsoleCommand {
	public String getName();

	public String getHelp();

	public void onLoad(String[] args);
}