package controller;

import util.Logger;
import multithreading.JobScheduler;
import Model.algorithms.PriorityPolicy;
import Model.datastructures.JobQueue;
/**
 * Command to change scheduling policy to Priority-based scheduling
 */
public class PriorityCommand implements Command {
    private final Logger logger;
    private final String moduleID = "PriorityCommand";
    private final String[] commandParts;
    private JobScheduler jobScheduler;

    public PriorityCommand() {
        this.logger = Logger.getInstance();
        this.commandParts = new String[]{"priority"};
        this.jobScheduler = JobScheduler.getInstance();
    }
    
    public PriorityCommand(String[] commandParts) {
        this.logger = Logger.getInstance();
        this.commandParts = commandParts;
        this.jobScheduler = JobScheduler.getInstance();
    }
    
    @Override
    public boolean validate() {
        // Check that priority command has no arguments
        if (commandParts.length > 1) {
            System.out.println("Error: priority command takes no arguments");
            logger.warning(moduleID, "Validation failed: priority command has unexpected arguments");
            return false;
        }
        
        return true;
    }

    @Override
    public boolean execute() {
        System.out.println("Scheduling policy is switched to Priority.");
        logger.info(moduleID, "Scheduling policy changed to Priority");
        
        jobScheduler.setSchedulingPolicy(new PriorityPolicy(JobQueue.getInstance().requestQueue()));
        return true;
    }
    
    @Override
    public String getDescription() {
        return "priority: change the scheduling policy to priority.";
    }
} 