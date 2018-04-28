package me.console;

public interface ConsoleCommand {

	String[] getNames();
	String getHelp();
	void run(String... args);
	void onLoad();

}