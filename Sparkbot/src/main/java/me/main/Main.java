package me.main;

import me.bot.base.Bot;
import me.console.ConsoleCommandManager;

import reactor.core.publisher.Hooks;

import java.io.File;
import java.io.PrintStream;
import java.util.regex.Matcher;
import java.util.stream.Stream;

public class Main {
	
	private static ConsoleCommandManager commands;
	
	public static void main(String[] args) {
		
		System.out.println("Stuff");
		
		commands = new ConsoleCommandManager();
		
		try {
			
			String base = new File(Main.class
										   .getProtectionDomain()
										   .getCodeSource()
										   .getLocation()
										   .toURI()).getParent() + "/storage/";
			if (!new File(base).exists()) {
				new File(base).mkdirs();
			}
			PrintStream out = new PrintStream(new File(base, "/latest.txt"));
			PrintStream err = new PrintStream(new File(base, "/errors.txt"));
			System.setOut(new Printer(System.out, out));
			System.setErr(new Printer(System.err, err));
			System.out.println("Basefile: " + base);
			new Bot(
					base,
					"sparkbot.config"
			);
			
			Hooks.onOperatorDebug();
			
			Bot.foreach(Bot:: login);
			
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Exit main with error");
		}
		
	}
	
	public static void exit() {
		
		System.out.close();
		System.err.close();
		System.exit(1);
	}
	
	public static ConsoleCommandManager getCommandManager() {
		
		return commands;
	}
	
}
