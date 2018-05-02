package me.main;

import me.bot.base.Bot;
import me.console.ConsoleCommandManager;
import me.console.commands.*;

import java.nio.channels.Channel;

public class Main {
    
    private static Bot bot;
    private static ConsoleCommandManager commands;

    public static void main(String[] args) {

	    System.out.println("Stuff");


	    //Level.toLevel(Level.INFO_INT);


        commands = new ConsoleCommandManager();
        commands.addCommand(new Help());
        commands.addCommand(new Broadcast());
        commands.addCommand(new Team());
        commands.addCommand(new Chatlog());
        commands.addCommand(new SendMsg());



        bot = new Bot(Constants.TOKEN,"Sparkbot","./resources/", "https://www.twitch.tv/discordsparkbot");

	    System.out.println("Bot set");

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
