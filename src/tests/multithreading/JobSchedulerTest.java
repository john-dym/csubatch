package tests.multithreading;

import multithreading.JobScheduler;
import Model.datastructures.Job;
import Model.datastructures.JobQueue;
// import Model.datastructures.Queue;
import Model.algorithms.FirstComeFirstServePolicy;
import Model.algorithms.ShortestJobFirstPolicy;
import Model.algorithms.PriorityPolicy;

/**
 * Test class for the JobScheduler.
 */
public class JobSchedulerTest {
    
    private JobScheduler jobScheduler;
    private JobQueue jobQueue;
    
    /**
     * Set up for tests.
     */
    public void setUp() {
        jobScheduler = JobScheduler.getInstance();
        jobQueue = JobQueue.getInstance();
        
        // Clear the queue by removing all jobs
        while (!jobQueue.isEmpty()) {
            jobQueue.getNextJob();
        }
        
        // Reset running status if needed
        jobScheduler.stopRunning();
    }
    
    /**
     * Test that the JobScheduler is a proper singleton.
     */
    public void testSingleton() {
        JobScheduler instance1 = JobScheduler.getInstance();
        JobScheduler instance2 = JobScheduler.getInstance();
        
        if (instance1 != instance2) {
            throw new AssertionError("JobScheduler should be a singleton, but multiple instances were created");
        }
    }
    
    /**
     * Test changing scheduling policy to FCFS.
     */
    public void testSetFCFSPolicy() {
        jobScheduler.setSchedulingPolicy(new FirstComeFirstServePolicy());
        
        String policyName = jobScheduler.getSchedulingPolicyName();
        if (!policyName.contains("FCFS")) {
            throw new AssertionError("Expected 'FCFS' policy name, but got: " + policyName);
        }
    }
    
    /**
     * Test changing scheduling policy to SJF.
     */
    public void testSetSJFPolicy() {
        jobScheduler.setSchedulingPolicy(new ShortestJobFirstPolicy());
        
        String policyName = jobScheduler.getSchedulingPolicyName();
        if (!policyName.contains("Shortest Job First")) {
            throw new AssertionError("Expected 'SJF' policy name, but got: " + policyName);
        }
    }
    
    /**
     * Test changing scheduling policy to Priority.
     */
    public void testSetPriorityPolicy() {
        jobScheduler.setSchedulingPolicy(new PriorityPolicy());
        
        String policyName = jobScheduler.getSchedulingPolicyName();
        if (!policyName.contains("Priority")) {
            throw new AssertionError("Expected 'Priority' policy name, but got: " + policyName);
        }
    }
    
    /**
     * Test scheduling a job.
     */
    public void testScheduleJob() {
        // Set a policy
        jobScheduler.setSchedulingPolicy(new FirstComeFirstServePolicy());
        
        // Create a job
        Job job = new Job("ScheduleTest", 10, 1, 0);
        
        // Schedule the job
        jobScheduler.addJob(job);
        
        // Check if the job is in the queue
        if (jobQueue.size() != 1) {
            throw new AssertionError("Job queue should have one job after scheduling");
        }
        
        // Get the job from the queue
        Job queuedJob = jobQueue.getNextJob();
        if (queuedJob == null) {
            throw new AssertionError("Expected one job in queue but got null");
        }
        
        if (!queuedJob.getName().equals("ScheduleTest")) {
            throw new AssertionError("Job in queue does not match scheduled job");
        }
    }
    
    /**
     * Test that the scheduler stops correctly.
     */
    public void testStop() {
        // Start the scheduler in a separate thread
        Thread schedulerThread = new Thread(jobScheduler);
        schedulerThread.start();
        
        // Stop the scheduler
        jobScheduler.stopRunning();
        
        // Give it a moment to stop
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            // Ignore
        }
    }
    
    } 