package Model.statistics;

import Model.datastructures.Job;
import java.util.ArrayList;
import java.util.List;
import util.Logger;

/**
 * Singleton class to track and calculate job batch processing metrics.
 * This class collects information about all submitted and completed jobs
 * and provides methods to calculate various performance metrics.
 * @author Group 1
 */
public class JobStatistics {
    private static JobStatistics instance;
    private final List<Job> submittedJobs;
    private final List<Job> completedJobs;
    private final Logger logger;
    private final String _moduleID = "JobStats";
    private final long systemStartTime;
    
    /**
     * Private constructor for the singleton pattern
     */
    private JobStatistics() {
        submittedJobs = new ArrayList<>();
        completedJobs = new ArrayList<>();
        logger = Logger.getInstance();
        systemStartTime = System.currentTimeMillis();
    }
    
    /**
     * Get the singleton instance of JobStatistics
     * @return The JobStatistics instance
     */
    public static synchronized JobStatistics getInstance() {
        if (instance == null) {
            instance = new JobStatistics();
        }
        return instance;
    }
    
    /**
     * Record a job submission
     * @param job The job that was submitted
     */
    public synchronized void recordJobSubmission(Job job) {
        if (job != null) {
            submittedJobs.add(job);
            logger.info(_moduleID, "Recorded job submission: " + job.getName());
        }
    }
    
    /**
     * Record a job completion
     * @param job The job that was completed
     */
    public synchronized void recordJobCompletion(Job job) {
        if (job != null && job.isFinished() && !completedJobs.contains(job)) {
            completedJobs.add(job);
            logger.info(_moduleID, "Recorded job completion: " + job.getName());
        }
    }
    
    /**
     * Get the total number of submitted jobs
     * @return The number of submitted jobs
     */
    public synchronized int getTotalJobsSubmitted() {
        return submittedJobs.size();
    }
    
    /**
     * Get the total number of completed jobs
     * @return The number of completed jobs
     */
    public synchronized int getTotalJobsCompleted() {
        return completedJobs.size();
    }
    
    /**
     * Calculate the average turnaround time for all completed jobs
     * Turnaround time = completion time - arrival time
     * @return The average turnaround time in seconds
     */
    public synchronized double getAverageTurnaroundTime() {
        if (completedJobs.isEmpty()) {
            return 0;
        }
        
        double totalTurnaroundTime = 0;
        for (Job job : completedJobs) {
            totalTurnaroundTime += job.getTurnaroundTime();
        }
        
        return totalTurnaroundTime / completedJobs.size();
    }
    
    /**
     * Calculate the average CPU time (execution time) for all completed jobs
     * @return The average CPU time in seconds
     */
    public synchronized double getAverageCPUTime() {
        if (completedJobs.isEmpty()) {
            return 0;
        }
        
        double totalCPUTime = 0;
        for (Job job : completedJobs) {
            totalCPUTime += job.getExecutionTime();
        }
        
        return totalCPUTime / completedJobs.size();
    }
    
    /**
     * Calculate the average waiting time for all completed jobs
     * Waiting time = start time - arrival time
     * @return The average waiting time in seconds
     */
    public synchronized double getAverageWaitingTime() {
        if (completedJobs.isEmpty()) {
            return 0;
        }
        
        double totalWaitingTime = 0;
        for (Job job : completedJobs) {
            totalWaitingTime += job.getWaitingTime();
        }
        
        return totalWaitingTime / completedJobs.size();
    }
    
    /**
     * Calculate the throughput (jobs completed per second)
     * @return The throughput in jobs per second
     */
    public synchronized double getThroughput() {
        if (completedJobs.isEmpty()) {
            return 0;
        }
        
        long currentTime = System.currentTimeMillis();
        double elapsedTimeInSeconds = (currentTime - systemStartTime) / 1000.0;
        
        if (elapsedTimeInSeconds <= 0) {
            return 0;
        }
        
        return completedJobs.size() / elapsedTimeInSeconds;
    }
    
    /**
     * Print a summary of all job statistics
     * @return A formatted string with all statistics
     */
    public synchronized String getStatisticsSummary() {
        StringBuilder summary = new StringBuilder();
        summary.append("Total number of job submitted: ").append(getTotalJobsSubmitted()).append("\n");
        summary.append("Average turnaround time: ").append(String.format("%.2f", getAverageTurnaroundTime())).append(" seconds\n");
        summary.append("Average CPU time: ").append(String.format("%.2f", getAverageCPUTime())).append(" seconds\n");
        summary.append("Average waiting time: ").append(String.format("%.2f", getAverageWaitingTime())).append(" seconds\n");
        summary.append("Throughput: ").append(String.format("%.3f", getThroughput())).append(" No./second\n");
        
        return summary.toString();
    }
    
    /**
     * Reset all statistics
     */
    public synchronized void reset() {
        submittedJobs.clear();
        completedJobs.clear();
        logger.info(_moduleID, "Statistics reset");
    }
} 