package me.console;

import org.reflections.Reflections;

import java.lang.reflect.InvocationTargetException;
import java.util.*;

public class ConsoleCommandManager implements Runnable{
    
    @SuppressWarnings("FieldCanBeLocal")
    private       Scanner              scanner;
    private final List<ConsoleCommand> commands;

    public ConsoleCommandManager() {
        commands = new ArrayList<>();
        new Thread(this).start();
    }

    public void addCommand(ConsoleCommand command) {
        commands.add(command);
        command.onLoad();
        System.out.println("[Console] " + command.getNames()[0] + " command loaded.");
    }
    public List<ConsoleCommand> getCommands() {
        return commands;
    }

    @Override
    public void run() {
        Reflections reflections = new Reflections("me.console.commands");
        reflections.getSubTypesOf(ConsoleCommand.class).forEach(i -> {
            try {
	            ConsoleCommand command = i.getDeclaredConstructor().newInstance();
	            addCommand(command);
            } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException ignored) {
            }
        });
        //noinspection InfiniteLoopStatement
        while(true) {
            scanner = new Scanner(System.in);
            String line = scanner.nextLine().trim();
	        String commandName = line.replaceAll("^(.*?)(?: .*)?$","$1");
	        String[] args = line.replaceAll("^.*?(?: (.*))?$","$1").split(" ");
            boolean worked = false;
            for (ConsoleCommand command : commands) {

                boolean bool = false;
                for(String name : command.getNames())
                    if(name.equalsIgnoreCase(commandName)) {
                        bool = true;
                        break;
                    }

                if (bool) {
                    worked = true;
                    command.run(args);
                    System.out.println();
                }
            } 
            if (!worked) {
                System.out.println("Uh oh, '" + commandName + "' wasn't found as a command!");
                System.out.println();
            }
        }
    }
}