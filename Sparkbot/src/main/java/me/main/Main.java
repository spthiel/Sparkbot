package me.main;

import me.bot.base.Bot;
import me.bot.commands.admin.Chars;
import me.bot.commands.admin.Delete;
import me.bot.commands.admin.ModHelp;
import me.bot.commands.superadmin.Avatar;
import me.bot.commands.superadmin.Remotelogout;
import me.bot.commands.superadmin.SuperAdminHelp;
import me.bot.commands.superadmin.Test;
import me.bot.commands.user.Invite;
import me.bot.commands.user.Macro;
import me.bot.commands.user.Ping;
import me.bot.commands.user.UserHelp;
import me.console.ConsoleCommandManager;
import me.console.commands.*;

public class Main {
    
    private static Bot bot;
    private static ConsoleCommandManager commands;

    public static void main(String[] args) {

        commands = new ConsoleCommandManager();
        commands.addCommand(new Help());
        commands.addCommand(new Broadcast());
        commands.addCommand(new Team());
        commands.addCommand(new Chatlog());
        commands.addCommand(new SendMsg());

        bot = new Bot(Constants.TOKEN,"Sparkbot","./resources/", "https://www.twitch.tv/discordsparkbot");

        bot.addCommands(
                new Ping(),
                new Delete(),
                new Invite(),
                new me.bot.commands.superadmin.Team(),
                new Remotelogout(),
                new Avatar(),
                new Test(),
                new Chars(),
		        new UserHelp(),
		        new ModHelp(),
		        new SuperAdminHelp(),
                new Macro());

        commands = new ConsoleCommandManager();
        commands.addCommand(new Help());
        commands.addCommand(new Broadcast());
        commands.addCommand(new Team());
        commands.addCommand(new Chatlog());
        commands.addCommand(new SendMsg());

        bot = new Bot(Constants.TOKEN,"Sparkbot","./resources/", "https://www.twitch.tv/discordsparkbot");

        bot.addCommands(
                new Ping(),
                new Delete(),
                new Invite(),
                new me.bot.commands.superadmin.Team(),
                new Remotelogout(),
                new Avatar(),
                new Test(),
                new Chars(),
		        new UserHelp(),
		        new ModHelp(),
		        new SuperAdminHelp(),
                new Macro());

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
