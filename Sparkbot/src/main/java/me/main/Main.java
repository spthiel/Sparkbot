package me.main;

import me.bot.base.Bot;
import me.console.ConsoleCommandManager;

import java.io.File;
import java.io.PrintStream;
import java.util.regex.Matcher;
import java.util.stream.Stream;

public class Main {

    private static ConsoleCommandManager commands;

    public static void main(String[] args) {

    	//System.setOut();


	    System.out.println("Stuff");

        commands = new ConsoleCommandManager();

	    try {

		    String base = new File(Main.class.getProtectionDomain().getCodeSource().getLocation().toURI()).getParent() + "/storage/";
		    PrintStream out = new PrintStream(new File(base,"/latest.txt"));
		    PrintStream err = new PrintStream(new File(base, "/errors.txt"));
		    System.setOut(new Printer(System.out,out));
		    System.setErr(new Printer(System.err,err));
		    System.out.println("Basefile: " + base);
		    System.err.println("test");
	        Bot bot = new Bot(Constants.TOKEN,"Sparkbot",base, "https://www.twitch.tv/discordsparkbot","me.bot.commands");

		    bot.login();

		    System.out.println("Exit main without error");
	    } catch (Exception e) {
		    e.printStackTrace();
		    System.out.println("Exit main with error");
	    }

	    System.out.close();
	    System.err.close();

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
