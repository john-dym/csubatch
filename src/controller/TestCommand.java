package controller;

import Model.algorithms.FirstComeFirstServePolicy;
import Model.algorithms.PriorityPolicy;
import Model.algorithms.ShortestJobFirstPolicy;
import Model.datastructures.Job;
import Model.datastructures.JobQueue;
import Model.statistics.JobStatistics;
import multithreading.JobScheduler;
import util.Logger;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * Command to run benchmark tests with various parameters
 */
public class TestCommand implements Command {
    private final Logger logger;
    private final String moduleID = "TestCommand";
    private final String[] commandParts;
    private static final Set<String> VALID_POLICIES = new HashSet<>(Arrays.asList("fcfs", "sjf", "priority"));
    private final JobScheduler jobScheduler;
    private final JobStatistics jobStatistics;
    private final Random random = new Random();
    private final JobQueue jobQueue;

    public TestCommand() {
        this.logger = Logger.getInstance();
        this.commandParts = new String[]{"test"};
        this.jobScheduler = JobScheduler.getInstance();
        this.jobStatistics = JobStatistics.getInstance();
        this.jobQueue = JobQueue.getInstance();
    }
    
    public TestCommand(String[] commandParts) {
        this.logger = Logger.getInstance();
        this.commandParts = commandParts;
        this.jobScheduler = JobScheduler.getInstance();
        this.jobStatistics = JobStatistics.getInstance();
        this.jobQueue = JobQueue.getInstance();
    }
    
    @Override
    public boolean validate() {
        // Check for correct number of arguments
        if (commandParts.length != 7) {
            System.out.println("Error: test command requires 6 arguments");
            System.out.println("Usage: test <benchmark> <policy> <num_of_jobs> <priority_levels> <min_CPU_time> <max_CPU_time>");
            logger.warning(moduleID, "Validation failed: Invalid number of arguments");
            return false;
        }
        
        // Validate policy (must be one of the supported policies)
        String policy = commandParts[2].toLowerCase();
        if (!VALID_POLICIES.contains(policy)) {
            System.out.println("Error: Invalid policy. Policy must be one of: fcfs, sjf, priority");
            logger.warning(moduleID, "Validation failed: Invalid policy: " + policy);
            return false;
        }
        
        try {
            // Validate num_of_jobs (must be a positive integer)
            int numOfJobs = Integer.parseInt(commandParts[3]);
            if (numOfJobs <= 0) {
                System.out.println("Error: Number of jobs must be a positive integer");
                logger.warning(moduleID, "Validation failed: Number of jobs not positive");
                return false;
            }
            
            // Validate priority_levels (must be a positive integer)
            int priorityLevels = Integer.parseInt(commandParts[4]);
            if (priorityLevels <= 0) {
                System.out.println("Error: Priority levels must be a positive integer");
                logger.warning(moduleID, "Validation failed: Priority levels not positive");
                return false;
            }
            
            // Validate min_CPU_time (must be a positive number)
            double minCPUTime = Double.parseDouble(commandParts[5]);
            if (minCPUTime <= 0) {
                System.out.println("Error: Minimum CPU time must be positive");
                logger.warning(moduleID, "Validation failed: Min CPU time not positive");
                return false;
            }
            
            // Validate max_CPU_time (must be a positive number)
            double maxCPUTime = Double.parseDouble(commandParts[6]);
            if (maxCPUTime <= 0) {
                System.out.println("Error: Maximum CPU time must be positive");
                logger.warning(moduleID, "Validation failed: Max CPU time not positive");
                return false;
            }
            
            // Validate min_CPU_time <= max_CPU_time
            if (minCPUTime > maxCPUTime) {
                System.out.println("Error: Minimum CPU time must be less than or equal to maximum CPU time");
                logger.warning(moduleID, "Validation failed: Min CPU time > Max CPU time");
                return false;
            }
        } catch (NumberFormatException e) {
            System.out.println("Error: Invalid number format. Numeric arguments must be numbers.");
            logger.error(moduleID, "Validation failed: Invalid number format - " + e.getMessage());
            return false;
        }
        
        return true;
    }

    @Override
    public boolean execute() {
        try {
            // Extract test parameters
            String benchmark = commandParts[1];
            String policy = commandParts[2].toLowerCase();
            int numOfJobs = Integer.parseInt(commandParts[3]);
            int priorityLevels = Integer.parseInt(commandParts[4]);
            double minCpuTime = Double.parseDouble(commandParts[5]);
            double maxCpuTime = Double.parseDouble(commandParts[6]);
            
            // Log the test parameters
            logger.info(moduleID, "Running benchmark: " + benchmark + 
                       ", Policy: " + policy + 
                       ", Jobs: " + numOfJobs + 
                       ", Priority levels: " + priorityLevels + 
                       ", CPU time range: " + minCpuTime + "-" + maxCpuTime);
            
            // Print a brief running message
            // System.out.println("Running benchmark '" + benchmark + "' with " + policy.toUpperCase() + " policy...");
            
            // Reset statistics for this test
            jobStatistics.reset();
            
            // Set the scheduling policy
            applySchedulingPolicy(policy);
            
            // Generate and submit test jobs
            submitTestJobs(numOfJobs, priorityLevels, minCpuTime, maxCpuTime);
            
            // Wait for all jobs to complete
            waitForJobsToComplete(numOfJobs);
            
            // Small delay to ensure all statistics are properly collected
            try {
                logger.info(moduleID, "Finalizing statistics...");
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            
            // Display test results in a clean format
            logger.info(moduleID, "\nBenchmark results for: " + benchmark);
            logger.info(moduleID, jobStatistics.getStatisticsSummary());
            System.out.println(jobStatistics.getStatisticsSummary());
            
        } catch (Exception e) {
            System.out.println("Error running test: " + e.getMessage());
            logger.error(moduleID, "Test command failed: " + e.getMessage());
        }
        
        return true;
    }
    
    /**
     * Applies the specified scheduling policy
     * @param policy The policy to apply (fcfs, sjf, or priority)
     */
    private void applySchedulingPolicy(String policy) {
        switch (policy) {
            case "fcfs":
                jobScheduler.setSchedulingPolicy(new FirstComeFirstServePolicy(jobQueue.requestQueue()));
                break;
            case "sjf":
                jobScheduler.setSchedulingPolicy(new ShortestJobFirstPolicy(jobQueue.requestQueue()));
                break;
            case "priority":
                jobScheduler.setSchedulingPolicy(new PriorityPolicy(jobQueue.requestQueue()));
                break;
            default:
                // Default to FCFS if somehow an invalid policy gets here
                jobScheduler.setSchedulingPolicy(new FirstComeFirstServePolicy(jobQueue.requestQueue()));
                break;
        }
        logger.info(moduleID, "Applied scheduling policy: " + policy);
    }
    
    /**
     * Generates and submits test jobs with random parameters
     * @param numOfJobs Number of jobs to generate
     * @param priorityLevels Maximum priority level
     * @param minCpuTime Minimum CPU execution time
     * @param maxCpuTime Maximum CPU execution time
     */
    private void submitTestJobs(int numOfJobs, int priorityLevels, double minCpuTime, double maxCpuTime) {
        System.out.println("Submitting " + numOfJobs + " test jobs...");
        
        for (int i = 0; i < numOfJobs; i++) {
            try {
                // Generate random CPU time between min and max
                double cpuTime = minCpuTime + Math.round((maxCpuTime - minCpuTime) * random.nextDouble());
                
                // Generate random priority between 1 and priorityLevels
                int priority = random.nextInt(priorityLevels) + 1;
                
                // Create and submit the job
                // Use current system time in ms as arrival time (converted to double)
                double arrivalTime = System.currentTimeMillis();
                
                Job job = new Job("TestJob-" + i, cpuTime, priority, arrivalTime);
                jobScheduler.addJob(job);
                
                logger.info(moduleID, "Generated and submitted job: " + job.getName() + 
                           ", CPU time: " + cpuTime + 
                           ", Priority: " + priority);
                
                // Add a small random delay between job submissions (10-100ms)
                Thread.sleep(10 + random.nextInt(90));
                
            } catch (InterruptedException e) {
                logger.warning(moduleID, "Interrupted while submitting jobs: " + e.getMessage());
                Thread.currentThread().interrupt();
                break;
            }
        }
        
        logger.info(moduleID, "Submitted all " + numOfJobs + " test jobs");
    }
    
    /**
     * Waits for all test jobs to complete
     * @param numOfJobs The number of jobs to wait for
     */
    private void waitForJobsToComplete(int numOfJobs) {
        logger.info(moduleID, "Waiting for all jobs to complete...");
        
        // Poll for completion
        int lastCompleted = 0;
        long startTime = System.currentTimeMillis();
        while (jobStatistics.getTotalJobsCompleted() < numOfJobs) {
            // Show progress if new jobs have completed
            int currentCompleted = jobStatistics.getTotalJobsCompleted();
            if (currentCompleted > lastCompleted) {
                logger.info(moduleID, "Completed: " + currentCompleted + "/" + numOfJobs + " jobs");
                lastCompleted = currentCompleted;
            }
            
            // Wait a bit before checking again
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                logger.warning(moduleID, "Interrupted while waiting for jobs: " + e.getMessage());
                Thread.currentThread().interrupt();
                break;
            }
            
            // Add a timeout to avoid infinite waiting (60 seconds)
            if (System.currentTimeMillis() - startTime > 60000) {
                logger.warning(moduleID, "Timeout waiting for jobs to complete");
                logger.warning(moduleID, "Warning: Timeout waiting for all jobs to complete.");
                break;
            }

            // TODO : check if all jobs are completed
            if (jobStatistics.getTotalJobsCompleted() == numOfJobs) {
                logger.info(moduleID, "Completed: " + (currentCompleted+1) + "/" + numOfJobs + " jobs");
                logger.info(moduleID, "All jobs completed");
                break;
            }
        }
        
        logger.info(moduleID, "All jobs completed or timeout reached. Completed " + 
                  jobStatistics.getTotalJobsCompleted() + "/" + numOfJobs + " jobs");
    }
    
    @Override
    public String getDescription() {
        return "test <benchmark> <policy> <num_of_jobs> <priority_levels>\n" +
               "     <min_CPU_time> <max_CPU_time>";
    }
} 