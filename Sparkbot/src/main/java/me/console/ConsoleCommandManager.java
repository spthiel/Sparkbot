package me.console;

import java.util.*;

public class ConsoleCommandManager implements Runnable{

    private Scanner scanner;
    private List<ConsoleCommand> commands;

    public ConsoleCommandManager() {
        commands = new ArrayList<ConsoleCommand>();
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
        while(true) {
            scanner = new Scanner(System.in);
            String line = scanner.nextLine().trim();
	        String cmdname = line.replaceAll("^(.*?)(?: .*)?$","$1");
	        String[] args = line.replaceAll("^.*?(?: (.*))?$","$1").split(" ");
            boolean worked = false;
            for (ConsoleCommand command : commands) {

                boolean bool = false;
                for(String name : command.getNames())
                    if(name.equalsIgnoreCase(cmdname)) {
                        bool = true;
                        break;
                    }

                if (bool) {
                    worked = true;
                    command.run(args);
                    System.out.println("");
                }
            } 
            if (!worked) {
                System.out.println("Uh oh, '" + cmdname + "' wasn't found as a command!");
                System.out.println("");
            }
        }
    }
}