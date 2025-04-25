package controller;

import Model.datastructures.Job;
import Model.datastructures.JobQueue;
import Model.datastructures.Queue;
import multithreading.JobScheduler;
import util.Logger;
import util.JobQueueLogger;

/**
 * Command to list all jobs in the queue
 */
public class ListCommand implements Command {
    private final JobScheduler jobScheduler;
    private final Logger logger;
    private final JobQueueLogger jobQueueLogger;
    private final String moduleID = "ListCommand";
    private final String[] commandParts;

    public ListCommand() {
        this.jobScheduler = JobScheduler.getInstance();
        this.logger = Logger.getInstance();
        this.jobQueueLogger = JobQueueLogger.getInstance();
        this.commandParts = new String[]{"list"};
    }
    
    public ListCommand(String[] commandParts) {
        this.jobScheduler = JobScheduler.getInstance();
        this.logger = Logger.getInstance();
        this.jobQueueLogger = JobQueueLogger.getInstance();
        this.commandParts = commandParts;
    }
    
    @Override
    public boolean validate() {
        // Check if job scheduler is set
        if (jobScheduler == null) {
            System.out.println("Error: Job scheduler not set");
            logger.error(moduleID, "Validation failed: Job scheduler not set");
            return false;
        }
        
        // Check that list command has no arguments
        if (commandParts.length > 1) {
            System.out.println("Error: list command takes no arguments");
            logger.warning(moduleID, "Validation failed: list command has unexpected arguments");
            return false;
        }
        
        return true;
    }

    @Override
    public boolean execute() {
        JobQueue jobQueue = JobQueue.getInstance();
        Queue queue = jobQueue.requestQueue();
        int jobCount = queue.getSize();
        Job runningJob = jobQueue.getRunningJob();
        
        // Add the running job to the count if there is one
        if (runningJob != null) {
            jobCount++;
        }
        
        if (jobCount == 0) {
            System.out.println("No jobs in the queue.");
            logger.info(moduleID, "Listed jobs: queue is empty");
            jobQueueLogger.queueListed(0);
            return true;
        }
        
        // First line: Total jobs
        System.out.println("Total number of jobs in the queue: " + jobCount);
        
        // Second line: Scheduling policy
        String schedulingPolicy = jobScheduler.getSchedulingPolicyName();
        System.out.println("Scheduling Policy: " + schedulingPolicy + ".");
        
        // Use JobQueue's toString method to display job information (formatted table)
        System.out.print(jobQueue.toString());
        
        logger.info(moduleID, "Listed " + jobCount + " jobs");
        jobQueueLogger.queueListed(jobCount);

        jobQueue.unlock();
        return true;
    }
    
    @Override
    public String getDescription() {
        return "list: display the job status.";
    }
} 