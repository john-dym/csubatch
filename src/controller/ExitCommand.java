package controller;

import util.Logger;
import multithreading.JobScheduler;
import Model.statistics.JobStatistics;

/**
 * Command to exit the CLI
 */
public class ExitCommand implements Command {
    private final Logger logger;
    private final String moduleID = "ExitCommand";
    private final String[] commandParts;
    private final JobStatistics jobStatistics;

    public ExitCommand() {
        this.logger = Logger.getInstance();
        this.commandParts = new String[]{"quit"};
        this.jobStatistics = JobStatistics.getInstance();
    }
    
    public ExitCommand(String[] commandParts) {
        this.logger = Logger.getInstance();
        this.commandParts = commandParts;
        this.jobStatistics = JobStatistics.getInstance();
    }
    
    @Override
    public boolean validate() {
        // Check that exit command has no arguments
        if (commandParts.length > 1) {
            System.out.println("Error: quit command takes no arguments");
            logger.warning(moduleID, "Validation failed: quit command has unexpected arguments");
            return false;
        }
        
        return true;
    }

    @Override
    public boolean execute() {
        // Get and display batch job processing statistics before exiting
        System.out.println(jobStatistics.getStatisticsSummary());
        
        System.out.println("Exiting...");
        logger.info(moduleID, "User initiated shutdown");
        return false; // Return false to signal the CLI to stop running
    }
    
    @Override
    public String getDescription() {
        return "quit: exit CSUbatch";
    }
} 