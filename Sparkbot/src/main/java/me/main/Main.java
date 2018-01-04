package me.main;

import me.bot.base.Bot;
import me.bot.commands.admin.Delete;
import me.bot.commands.console.Broadcast;
import me.bot.commands.console.ConsoleCommandManager;
import me.bot.commands.console.Help;
import me.bot.commands.superadmin.Avatar;
import me.bot.commands.superadmin.Remotelogout;
import me.bot.commands.superadmin.Team;
import me.bot.commands.user.Invite;
import me.bot.commands.user.Ping;

public class Main {
    
    private static Bot bot;
    private static ConsoleCommandManager commands;

    public static void main(String[] args) {

        commands = new ConsoleCommandManager();
        commands.addCommand(new Help());
        commands.addCommand(new Broadcast());

        bot = new Bot(Constants.TOKEN,"Sparkbot","./resources/", "https://www.twitch.tv/discordsparkbot");

        bot.addCommands(new Ping(),new Delete(),new Invite(),new Team(),new Remotelogout(),new Avatar());

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
