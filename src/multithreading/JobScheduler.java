package multithreading;

import Model.datastructures.Job;
import Model.datastructures.JobQueue;
import Model.datastructures.Queue;
import Model.algorithms.SchedulingPolicy;
import Model.algorithms.FirstComeFirstServePolicy;
import Model.statistics.JobStatistics;
import util.Logger;
import util.JobQueueLogger;

/**
 * JobScheduler is responsible for scheduling jobs according to a specified policy.
 * It runs as a separate thread and manages the job queue.
 * Implements the Singleton pattern to ensure only one scheduler exists.
 * @author Group 1
 */
public class JobScheduler implements Runnable {
    private final String _moduleID;
    private volatile boolean _running = true;
    private final JobQueue _jobQueue;
    private SchedulingPolicy _schedulingPolicy;
    private final Logger _logger;
    private final JobQueueLogger _jobQueueLogger;
    private final JobStatistics _jobStatistics;
    
    // Singleton instance
    private static JobScheduler instance = null;
    
    /**
     * Private constructor for JobScheduler (Singleton pattern)
     * @param moduleID The ID of this module
     */
    private JobScheduler(String moduleID) {
        _moduleID = moduleID;
        _jobQueue = JobQueue.getInstance();
        _logger = Logger.getInstance();
        _jobQueueLogger = JobQueueLogger.getInstance();
        _jobStatistics = JobStatistics.getInstance();
        this.setSchedulingPolicy(new FirstComeFirstServePolicy(_jobQueue.requestQueue()));
    }

    /**
     * Gets the singleton instance of JobScheduler
     * @param moduleID The ID of this module (only used on first creation)
     * @return The JobScheduler instance
     */
    public static synchronized JobScheduler getInstance() {
        if (instance == null) {
            instance = new JobScheduler("JSched");
        }
        return instance;
    }

    /**
     * Sets the scheduling policy to be used
     * @param policy The scheduling policy to use
     */
    public void setSchedulingPolicy(SchedulingPolicy policy) {
        _schedulingPolicy = policy;
        // execute the policy if job queue is not empty
        boolean empty = _jobQueue.isEmpty();
        if (!empty) {
            _schedulingPolicy.execute();}
        _jobQueue.unlock();
        if(!empty) {
            System.out.println("All the " + _jobQueue.size() + " waiting jobs have been rescheduled.");
        }
    }

    /**
     * Gets the current scheduling policy name
     * @return The name of the current scheduling policy or "FCFS" if none is set
     */
    public String getSchedulingPolicyName() {
        if (_schedulingPolicy == null) {
            return "FCFS"; // Default policy
        }
        return _schedulingPolicy.getPolicyName();
    }

    /**
     * Adds a job to the queue
     * @param job The job to add
     * @throws IllegalArgumentException if job is null
     */
    public void addJob(Job job) {
        if (job == null) {
            throw new IllegalArgumentException("Cannot add a null job");
        }
        
        // Record the job submission in statistics
        _jobStatistics.recordJobSubmission(job);
        
        // Use the JobQueue's built-in method which handles locking and signaling
        _jobQueue.addJob(job);
        _schedulingPolicy.execute();
        _jobQueue.unlock();

        _logger.info(_moduleID, "Job added to queue: " + job.getName());
        _jobQueueLogger.jobAdded(job.getName(), job.getExecutionTime(), job.getPriority());
    }

    /**
     * Gets the current size of the job queue
     * @return The number of jobs in the queue
     */
    public int getQueueSize() {
        return _jobQueue.size();
    }

    /**
     * Gets the estimated waiting time for a new job
     * @return The estimated waiting time in seconds
     */
    public double getEstimatedWaitingTime() {
        double totalWaitTime = 0;
        // add running job time to total wait time
        if (_jobQueue.getRunningJob() != null) {
            totalWaitTime += _jobQueue.getRunningJob().getExecutionTime();
        }
        Queue queue = _jobQueue.requestQueue();
        
        // Sum up execution times of all jobs in queue
        for (Object obj : queue) {
            if (obj instanceof Job) {
                Job job = (Job) obj;
                totalWaitTime += job.getExecutionTime();
            }
        }
        _jobQueue.unlock();
        
        return totalWaitTime;
    }

    /**
     * Gets the job statistics for display
     * @return A formatted string with job statistics
     */
    public String getJobStatistics() {
        return _jobStatistics.getStatisticsSummary();
    }

    @Override
    public void run() {
        _logger.info(_moduleID, "Job Scheduler started: " + _moduleID);
        
        while (_running) {
            try {
                // Check for new jobs periodically
                Thread.sleep(1000);
                
                // If a scheduling policy is set, apply it to the job queue
                if (_schedulingPolicy != null) {
                    // Scheduling policy should handle its own locking if needed
                    _schedulingPolicy.execute();
                    _logger.info(_moduleID, "Applied scheduling policy: " + _schedulingPolicy.getPolicyName());
                }
                
                // Print status information
                printStatus();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                _logger.error(_moduleID, "Job Scheduler interrupted: " + e.getMessage());
            }
        }
    }

    /**
     * Prints the current status of the job scheduler
     */
    private void printStatus() {
        StringBuilder status = new StringBuilder();
        status.append("Status: Queue size=").append(_jobQueue.size());
        status.append(", Thread ID=").append(Thread.currentThread().threadId());
        status.append(", Thread Name=").append(Thread.currentThread().getName());
        status.append(", Scheduling Policy=").append(getSchedulingPolicyName());
        
        _logger.info(_moduleID, status.toString());
    }

    /**
     * Stops the job scheduler
     */
    public void stopRunning() {
        _running = false;
        _logger.info(_moduleID, "Job Scheduler stopping");
    }
}