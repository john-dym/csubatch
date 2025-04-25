package controller;

import util.Logger;
import multithreading.JobScheduler;
import Model.algorithms.FirstComeFirstServePolicy;
import Model.datastructures.JobQueue;
/**
 * Command to change scheduling policy to First Come, First Served (FCFS)
 */
public class FcfsCommand implements Command {
    private final Logger logger;
    private final String moduleID = "FcfsCommand";
    private final String[] commandParts;
    private JobScheduler jobScheduler;
    
    public FcfsCommand() {
            this.logger = Logger.getInstance();
            this.commandParts = new String[]{"fcfs"};
            this.jobScheduler = JobScheduler.getInstance();
    }
    
    public FcfsCommand(String[] commandParts) {
        this.logger = Logger.getInstance();
        this.commandParts = commandParts;
        this.jobScheduler = JobScheduler.getInstance();
    }
    
    @Override
    public boolean validate() {
        // Check that fcfs command has no arguments
        if (commandParts.length > 1) {
            System.out.println("Error: fcfs command takes no arguments");
            logger.warning(moduleID, "Validation failed: fcfs command has unexpected arguments");
            return false;
        }
        
        return true;
    }

    @Override
    public boolean execute() {
        System.out.println("Scheduling policy is switched to FCFS (First Come, First Served)");
        logger.info(moduleID, "Scheduling policy changed to FCFS");
        
        jobScheduler.setSchedulingPolicy(new FirstComeFirstServePolicy(JobQueue.getInstance().requestQueue()));
        return true;
    }
    
    @Override
    public String getDescription() {
        return "fcfs: change the scheduling policy to FCFS.";
    }
} 