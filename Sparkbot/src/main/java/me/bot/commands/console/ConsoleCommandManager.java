package me.bot.commands.console;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import sx.blah.discord.api.events.EventSubscriber;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IGuild;
import sx.blah.discord.handle.obj.IUser;

public class ConsoleCommandManager implements Runnable{

    private Scanner scanner;
    private List<ConsoleCommand> commands;

    public ConsoleCommandManager() {
        commands = new ArrayList<ConsoleCommand>();
        new Thread(this).start();
    }

    public void addCommand(ConsoleCommand command) {
        commands.add(command);
    }
    public List<ConsoleCommand> getCommands() {
        return commands;
    }

    @Override
    public void run() {
        while(true) {
            scanner = new Scanner(System.in);
            String[] args = scanner.nextLine().split(" ");
            boolean worked = false;
            for (ConsoleCommand command : commands) {
                if (args[0].equals(command.getName())) {
                    worked = true;
                    command.onLoad(args);
                    System.out.println("");
                }
                else
                if (args[0].equals(command.getName2())) {
                    worked = true;
                    command.onLoad(args);
                    System.out.println("");
                }
                else
                if (args[0].equals(command.getName3())) {
                    worked = true;
                    command.onLoad(args);
                    System.out.println("");
                }
            } 
            if (!worked) {
                System.out.println("Uh oh, '" + args[0] + "' wasn't found as a command!");
                System.out.println("");
            }
        }
    }
}