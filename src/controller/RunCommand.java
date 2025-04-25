package controller;

import Model.datastructures.Job;
import multithreading.JobScheduler;
import util.Logger;

/**
 * Command to submit a new job
 */
public class RunCommand implements Command {
    private final JobScheduler jobScheduler;
    private final String[] commandParts;
    private final Logger logger;
    private final String moduleID = "RunCommand";

    public RunCommand( String[] commandParts) {
        this.jobScheduler = JobScheduler.getInstance();
        this.commandParts = commandParts;
        this.logger = Logger.getInstance();
    }
    
    @Override
    public boolean validate() {
        // Check if job scheduler is set
        if (jobScheduler == null) {
            System.out.println("Error: Job scheduler not set");
            logger.error(moduleID, "Validation failed: Job scheduler not set");
            return false;
        }
        
        // Check for the correct number of arguments
        if (commandParts.length != 4) {
            System.out.println("Error: run command requires 3 arguments");
            System.out.println("Usage: run <job> <time> <pri>");
            logger.warning(moduleID, "Validation failed: Invalid number of arguments");
            return false;
        }
        
        try {
            // Validate execution time (must be a positive number)
            double executionTime = Double.parseDouble(commandParts[2]);
            if (executionTime <= 0) {
                System.out.println("Error: execution time must be a positive number");
                logger.warning(moduleID, "Validation failed: Execution time not positive");
                return false;
            }
            
            // Validate priority (must be a non-negative integer)
            int priority = Integer.parseInt(commandParts[3]);
            if (priority < 0) {
                System.out.println("Error: priority must be a non-negative integer");
                logger.warning(moduleID, "Validation failed: Priority is negative");
                return false;
            }
        } catch (NumberFormatException e) {
            System.out.println("Error: Invalid number format. Execution time and priority must be numbers.");
            logger.error(moduleID, "Validation failed: Invalid number format - " + e.getMessage());
            return false;
        }
        
        return true;
    }

    @Override
    public boolean execute() {
        try {
            String name = commandParts[1];
            double executionTime = Double.parseDouble(commandParts[2]);
            int priority = Integer.parseInt(commandParts[3]);
            double arrivalTime = System.currentTimeMillis();

            Job job = new Job(name, executionTime, priority, arrivalTime);
            logger.info(moduleID, "Job being added to queue: " + name + 
                        " (execution_time=" + executionTime + 
                        ", priority=" + priority + 
                        ", arrival_time=" + arrivalTime + ")");
            double estimatedWaitingTime = jobScheduler.getEstimatedWaitingTime();
            jobScheduler.addJob(job);
            System.out.println("Job " + name + " was submitted");
            // print total number of jobs in queue
            System.out.println("Total number of jobs in queue: " + jobScheduler.getQueueSize());
            // estimated waiting time
            System.out.println("Estimated waiting time: " + estimatedWaitingTime);
            // print scheduler policy
            System.out.println("Scheduler policy: " + jobScheduler.getSchedulingPolicyName());
            
            logger.info(moduleID, "Job submitted: " + name + 
                        " (execution_time=" + executionTime + 
                        ", priority=" + priority + 
                        ", arrival_time=" + arrivalTime + ")");
        } catch (Exception e) {
            System.out.println("Error submitting job: " + e.getMessage());
            logger.error(moduleID, "Job submission failed: " + e.getMessage());
        }
        
        return true;
    }
    
    @Override
    public String getDescription() {
        return "run <job> <time> <pri>: submit a job named <job>,\n" +
               "                        execution time is <time>,\n" +
               "                        priority is <pri>.";
    }
} 