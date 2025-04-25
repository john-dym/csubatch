package controller;

import util.Logger;

/**
 * Command to handle unknown commands
 */
public class UnknownCommand implements Command {
    private final String commandName;
    private final Logger logger;
    private final String moduleID = "UnknownCommand";

    public UnknownCommand(String commandName) {
        this.commandName = commandName;
        this.logger = Logger.getInstance();
    }
    
    @Override
    public boolean validate() {
        // Always fail validation for unknown commands
        System.out.println("Error: Unknown command: " + commandName);
        logger.warning(moduleID, "Validation failed: Unknown command: " + commandName);
        return false;
    }

    @Override
    public boolean execute() {
        System.out.println("Unknown command: " + commandName + ". Type 'help' for available commands.");
        logger.warning(moduleID, "Unknown command entered: " + commandName);
        // Return false to indicate the command failed
        return false;
    }
    
    @Override
    public String getDescription() {
        return ""; // Not displayed in help
    }
} 