package controller;

import multithreading.JobScheduler;
import util.Logger;

/**
 * Command to display system status
 */
public class StatusCommand implements Command {
    private final JobScheduler jobScheduler;
    private final Logger logger;
    private final String moduleID = "StatusCommand";
    private final String[] commandParts;

    public StatusCommand() {
        this.jobScheduler = JobScheduler.getInstance();
        this.logger = Logger.getInstance();
        this.commandParts = new String[]{"status"};
    }
    
    public StatusCommand(String[] commandParts) {
        this.jobScheduler = JobScheduler.getInstance();
        this.logger = Logger.getInstance();
        this.commandParts = commandParts;
    }
    
    @Override
    public boolean validate() {
        // Check that status command has no arguments
        if (commandParts.length > 1) {
            System.out.println("Error: status command takes no arguments");
            logger.warning(moduleID, "Validation failed: status command has unexpected arguments");
            return false;
        }
        
        return true;
    }

    @Override
    public boolean execute() {
        System.out.println("\nSystem Status:");
        System.out.println("CLI is running: true");
        System.out.println("Job Scheduler is set: " + (jobScheduler != null));
        System.out.println();
        
        logger.info(moduleID, "Status displayed - CLI running: true, Scheduler set: " + (jobScheduler != null));
        
        return true;
    }
    
    @Override
    public String getDescription() {
        return "status: display the system status.";
    }
} 