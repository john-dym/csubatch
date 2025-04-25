package controller;

import util.Logger;
import multithreading.JobScheduler;
import Model.algorithms.ShortestJobFirstPolicy;
import Model.datastructures.JobQueue;
/**
 * Command to change scheduling policy to Shortest Job First (SJF)
 */
public class SjfCommand implements Command {
    private final Logger logger;
    private final String moduleID = "SjfCommand";
    private final String[] commandParts;
    private JobScheduler jobScheduler;

    public SjfCommand() {
        this.logger = Logger.getInstance();
        this.commandParts = new String[]{"sjf"};
        this.jobScheduler = JobScheduler.getInstance();
    }
    
    public SjfCommand(String[] commandParts) {
        this.logger = Logger.getInstance();
        this.commandParts = commandParts;
        this.jobScheduler = JobScheduler.getInstance();
    }
    
    @Override
    public boolean validate() {
        // Check that sjf command has no arguments
        if (commandParts.length > 1) {
            System.out.println("Error: sjf command takes no arguments");
            logger.warning(moduleID, "Validation failed: sjf command has unexpected arguments");
            return false;
        }
        
        return true;
    }

    @Override
    public boolean execute() {
        System.out.println("Scheduling policy is switched to SJF.");
        logger.info(moduleID, "Scheduling policy changed to SJF");
        
        jobScheduler.setSchedulingPolicy(new ShortestJobFirstPolicy(JobQueue.getInstance().requestQueue()));
        return true;
    }
    
    @Override
    public String getDescription() {
        return "sjf: change the scheduling policy to SJF.";
    }
} 