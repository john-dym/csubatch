package controller;

import Model.datastructures.Job;
import multithreading.JobScheduler;
import util.Logger;
import java.util.Map;
import java.util.HashMap;

/**
 * Factory class for creating command objects
 * Implements the Factory design pattern to create appropriate command instances
 */
public class CommandFactory {
    private final JobScheduler jobScheduler;
    private final Logger logger;
    private final String moduleID = "CommandFactory";
    private static final Map<String, Command> commandMap = new HashMap<>();

    /**
     * Constructor for CommandFactory
     * @param jobScheduler The job scheduler to use for job-related commands
     */
    public CommandFactory(JobScheduler jobScheduler) {
        this.jobScheduler = jobScheduler;
        this.logger = Logger.getInstance();
        
        // Initialize command map
        commandMap.put("run", new RunCommand(null));
        commandMap.put("list", new ListCommand());
        commandMap.put("status", new StatusCommand());
        commandMap.put("quit", new ExitCommand());
        commandMap.put("exit", new ExitCommand());
        commandMap.put("", new NoOpCommand());
        commandMap.put("fcfs", new FcfsCommand());
        commandMap.put("sjf", new SjfCommand());
        commandMap.put("priority", new PriorityCommand());
    }

    /**
     * Create a command based on user input
     * @param input The user input string
     * @return The appropriate command object, or null if the command is not recognized
     */
    public Command createCommand(String input) {
        String[] parts = input.trim().split("\\s+");
        String cmd = parts.length > 0 ? parts[0].toLowerCase() : "";

        logger.info(moduleID, "Creating command: " + cmd);

        // Special handling for help command with arguments
        if (cmd.equals("help")) {
            return new HelpCommand(parts);
        }
        
        // Special handling for test command with arguments
        if (cmd.equals("test")) {
            return new TestCommand(parts);
        }

        // Special handling for run command to include arguments
        if (cmd.equals("run")) {
            return new RunCommand(parts);
        }

        Command command = commandMap.get(cmd);
        return command != null ? command : new UnknownCommand(cmd);
    }
    
    /**
     * Execute a command with validation
     * @param input The user input to process
     * @return true if CLI should continue, false if it should exit
     */
    public boolean executeCommand(String input) {
        Command command = createCommand(input);
        
        // Only execute the command if validation passes
        if (command.validate()) {
            return command.execute();
        }
        
        // If validation fails, continue CLI execution
        return true;
    }
}