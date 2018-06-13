package me.main;

import me.bot.base.Bot;
import me.console.ConsoleCommandManager;
import me.console.commands.*;

import java.io.File;
import java.net.URISyntaxException;
import java.nio.channels.Channel;
import java.nio.file.Paths;
import java.util.List;

public class Main {

    private static ConsoleCommandManager commands;

    public static void main(String[] args) {

    	//System.setOut();

	    System.out.println("Stuff");

        commands = new ConsoleCommandManager();

	    try {
		    String base = new File(Main.class.getProtectionDomain().getCodeSource().getLocation().toURI()).getParent() + "/storage/";
		    System.out.println("Basefile: " + base);
	        Bot bot = new Bot(Constants.TOKEN,"Sparkbot",base, "https://www.twitch.tv/discordsparkbot","me.bot.commands");

		    bot.login();

		    System.out.println("Exit main without error");
	    } catch (Exception e) {
		    e.printStackTrace();
		    System.out.println("Exit main with error");
	    }


    }

    public static void exit() {
    	System.exit(1);
    }

    public static ConsoleCommandManager getCommandManager() {
        return commands;
    }
    
}
