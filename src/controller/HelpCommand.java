package controller;

import util.Logger;
import java.util.HashMap;
import java.util.Map;

/**
 * Command to display help information with support for different help topics
 */
public class HelpCommand implements Command {
    private final Logger logger;
    private final String moduleID = "HelpCommand";
    private final String[] commandParts;
    
    // Static map of commands and their descriptions
    private static final Map<String, Command> commandMap = new HashMap<>();
    
    static {
        // Initialize the command map with command instances
        commandMap.put("run", new RunCommand(new String[]{"run"}));
        commandMap.put("list", new ListCommand(null));
        commandMap.put("fcfs", new FcfsCommand());
        commandMap.put("sjf", new SjfCommand());
        commandMap.put("priority", new PriorityCommand());
        commandMap.put("test", new TestCommand());
        commandMap.put("quit", new ExitCommand());
        // Help command is special, we'll handle it separately
    }

    public HelpCommand() {
        this.logger = Logger.getInstance();
        this.commandParts = new String[]{"help"};
    }

    public HelpCommand(String[] commandParts) {
        this.logger = Logger.getInstance();
        this.commandParts = commandParts;
    }
    
    @Override
    public boolean validate() {
        // Check if command is just "help" (no arguments)
        if (commandParts.length == 1) {
            return true;
        }
        
        // Check if command has exactly one argument
        if (commandParts.length != 2) {
            System.out.println("Error: Help command takes at most one argument.");
            logger.warning(moduleID, "Invalid help command format - too many arguments");
            return false;
        }
        
        // Check if the argument starts with a hyphen
        String arg = commandParts[1].toLowerCase();
        if (!arg.startsWith("-")) {
            System.out.println("Error: Help argument must start with a hyphen (e.g., help -test)");
            logger.warning(moduleID, "Invalid help command format - argument doesn't start with hyphen");
            return false;
        }
        
        // Check if the topic is valid
        String topic = arg.substring(1); // Remove the hyphen
        if (!commandMap.containsKey(topic)) {
            System.out.println("Error: Unknown help topic. Valid topics are: run, list, fcfs, sjf, priority, test, quit");
            logger.warning(moduleID, "Invalid help topic: " + topic);
            return false;
        }
        
        return true;
    }

    @Override
    public boolean execute() {
        // If the command is just "help" with no arguments
        if (commandParts.length == 1) {
            displayAllHelp();
        } else {
            // Extract the help topic (skip "help" and take the next argument)
            String topic = commandParts[1].toLowerCase();
            
            // Check if the topic starts with a hyphen (like help -test)
            if (topic.startsWith("-")) {
                topic = topic.substring(1); // Remove the hyphen
            }
            
            // Look up the command in the command map
            Command commandForHelp = commandMap.get(topic);
            
            if (commandForHelp != null) {
                // Display the command's description
                System.out.println(commandForHelp.getDescription());
            }
        }
        
        logger.info(moduleID, "Help information displayed");
        return true;
    }
    
    /**
     * Display help for all commands
     */
    private void displayAllHelp() {
        // Display the description of each command in a specific order
        System.out.println(commandMap.get("run").getDescription());
        System.out.println(commandMap.get("list").getDescription());
        System.out.println(commandMap.get("fcfs").getDescription());
        System.out.println(commandMap.get("sjf").getDescription());
        System.out.println(commandMap.get("priority").getDescription());
        System.out.println(commandMap.get("test").getDescription());
        System.out.println(commandMap.get("quit").getDescription());
    }
    
    @Override
    public String getDescription() {
        return "help [command]: display help information";
    }
} 