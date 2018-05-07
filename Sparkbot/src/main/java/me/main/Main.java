package me.main;

import me.bot.base.Bot;
import me.console.ConsoleCommandManager;
import me.console.commands.*;

import java.nio.channels.Channel;

public class Main {
    
    private static Bot bot;
    private static ConsoleCommandManager commands;

    public static void main(String[] args) {

    	//System.setOut();

	    System.out.println("Stuff");

        commands = new ConsoleCommandManager();

        bot = new Bot(Constants.TOKEN,"Sparkbot","./resources/", "https://www.twitch.tv/discordsparkbot");

	    bot.login();

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
