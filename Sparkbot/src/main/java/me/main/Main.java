package me.main;

import me.bot.base.Bot;
import me.console.ConsoleCommandManager;
import me.console.commands.*;

import java.io.File;
import java.net.URISyntaxException;
import java.nio.channels.Channel;
import java.nio.file.Paths;

public class Main {
    
    private static Bot bot;
    private static ConsoleCommandManager commands;

    public static void main(String[] args) {

    	//System.setOut();

	    System.out.println("Stuff");

        commands = new ConsoleCommandManager();

	    try {
		    String base = new File(Main.class.getProtectionDomain().getCodeSource().getLocation().toURI()).getParent() + "/storage/";
		    System.out.println("Basefile: " + base);
	        bot = new Bot(Constants.TOKEN,"Sparkbot",base, "https://www.twitch.tv/discordsparkbot");

		    bot.login();

		    System.out.println("Exit main without error");
	    } catch (Exception e) {
		    e.printStackTrace();
		    System.out.println("Exit main with error");
	    }

//		File file = Paths.get("storage").toFile();


    }

    public static void exit() {
    	System.exit(1);
    }

    public static ConsoleCommandManager getCommandManager() {
        return commands;
    }

    public static Bot getBot() {
        return bot;
    }
    
}
