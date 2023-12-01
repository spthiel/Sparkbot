package me.main;

import me.bot.base.Bot;
import me.console.ConsoleCommandManager;

import reactor.core.publisher.Hooks;

import java.io.File;
import java.io.PrintStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main {
	
	private static final Logger logger = LoggerFactory.getLogger(Main.class);
	
	private static ConsoleCommandManager commands;
	public static boolean testInstance = false;
	
	public static void main(String[] args) {
		
		for(String s : args) {
			if(s.equalsIgnoreCase("-test")) {
				System.out.println("Detected test flag, starting in test mode");
				testInstance = true;
				break;
			}
		}
		
		System.out.println("Starting " + (testInstance ? "test" : "normal") + " instance");
		
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
			System.out.println("Base-file: " + base);
			new Bot(
					base,
					"sparkbot.config"
			);
			
			Hooks.onOperatorDebug();
			
			Bot.foreach(Bot:: login);
			
		} catch (Exception e) {
			logger.error("Exit main with error", e);
		}
		
	}
	
	public static void exit() {
		
		Bot.foreach(bot -> bot.getResourceManager().saveAll());
		System.out.println("Yep");
		System.out.close();
		System.err.close();
		System.exit(1);
	}
	
	public static ConsoleCommandManager getCommandManager() {
		
		return commands;
	}
	
}
