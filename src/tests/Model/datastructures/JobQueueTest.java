package tests.Model.datastructures;

import Model.datastructures.Job;
import Model.datastructures.JobQueue;
import Model.datastructures.Queue;

/**
 * Test class for the JobQueue singleton.
 */
public class JobQueueTest {
    
    private JobQueue jobQueue;
    
    /**
     * Set up for tests.
     */
    public void setUp() {
        jobQueue = JobQueue.getInstance();
        
        // Clear any existing jobs
        while (!jobQueue.isEmpty()) {
            jobQueue.getNextJob();
        }
    }
    
    /**
     * Test adding jobs to the queue.
     */
    public void testAddJob() {
        // Create and add a job
        Job job = new Job("TestJob", 10, 1, 0);
        jobQueue.addJob(job);
        
        // Verify queue size
        if (jobQueue.size() != 1) {
            throw new AssertionError("Expected queue size 1 after adding job, but got: " + jobQueue.size());
        }
        
        // Verify the job can be retrieved
        Job retrievedJob = jobQueue.getNextJob();
        if (retrievedJob == null) {
            throw new AssertionError("Failed to retrieve job after adding");
        }
        
        if (!retrievedJob.getName().equals("TestJob")) {
            throw new AssertionError("Retrieved job name doesn't match added job name");
        }
    }
    
    /**
     * Test that JobQueue is a proper singleton.
     */
    public void testSingleton() {
        JobQueue instance1 = JobQueue.getInstance();
        JobQueue instance2 = JobQueue.getInstance();
        
        if (instance1 != instance2) {
            throw new AssertionError("JobQueue should be a singleton, but multiple instances were created");
        }
    }
    
    /**
     * Test removing jobs from the queue.
     */
    public void testRemoveJob() {
        // Add two jobs
        Job job1 = new Job("Job1", 10, 1, 0);
        Job job2 = new Job("Job2", 20, 2, 0);
        
        jobQueue.addJob(job1);
        jobQueue.addJob(job2);
        
        // Verify queue size
        if (jobQueue.size() != 2) {
            throw new AssertionError("Expected queue size 2 after adding two jobs, but got: " + jobQueue.size());
        }
        
        // Remove one job
        Job removedJob = jobQueue.getNextJob();
        
        // Verify removal
        if (jobQueue.size() != 1) {
            throw new AssertionError("Expected queue size 1 after removing a job, but got: " + jobQueue.size());
        }
        
        if (removedJob == null || !removedJob.getName().equals("Job1")) {
            throw new AssertionError("First job not removed correctly");
        }
        
        // Remove second job
        removedJob = jobQueue.getNextJob();
        
        // Verify all removed
        if (jobQueue.size() != 0) {
            throw new AssertionError("Expected empty queue after removing all jobs, but size is: " + jobQueue.size());
        }
        
        if (removedJob == null || !removedJob.getName().equals("Job2")) {
            throw new AssertionError("Second job not removed correctly");
        }
    }
    
    /**
     * Test clearing the queue.
     */
    public void testClearQueue() {
        // Add multiple jobs
        for (int i = 0; i < 5; i++) {
            Job job = new Job("Job" + i, 10, 1, 0);
            jobQueue.addJob(job);
        }
        
        // Verify queue size
        if (jobQueue.size() != 5) {
            throw new AssertionError("Expected queue size 5 after adding five jobs, but got: " + jobQueue.size());
        }
        
        // Clear the queue by dequeuing all jobs
        while (!jobQueue.isEmpty()) {
            jobQueue.getNextJob();
        }
        
        // Verify queue is empty
        if (!jobQueue.isEmpty()) {
            throw new AssertionError("Queue should be empty after clearing");
        }
        
        if (jobQueue.size() != 0) {
            throw new AssertionError("Queue size should be 0 after clearing, but got: " + jobQueue.size());
        }
    }
    
    /**
     * Test getting all jobs from the queue.
     */
    public void testGetJobs() {
        // Add three jobs
        Job job1 = new Job("Job1", 10, 1, 0);
        Job job2 = new Job("Job2", 20, 2, 0);
        Job job3 = new Job("Job3", 30, 3, 0);
        
        jobQueue.addJob(job1);
        jobQueue.addJob(job2);
        jobQueue.addJob(job3);
        
        // Get all jobs through iteration over requested queue
        Queue queue = jobQueue.requestQueue();
        int count = 0;
        boolean job1Found = false;
        boolean job2Found = false;
        boolean job3Found = false;
        
        // Iterate through the queue checking for each job
        for (Object obj : queue) {
            count++;
            if (obj instanceof Job) {
                Job job = (Job) obj;
                if (job.getName().equals("Job1")) job1Found = true;
                if (job.getName().equals("Job2")) job2Found = true;
                if (job.getName().equals("Job3")) job3Found = true;
            }
        }
        
        // Release the lock after accessing the queue
        jobQueue.unlock();
        
        // Verify all jobs were found
        if (count != 3) {
            throw new AssertionError("Expected 3 jobs in queue, but found: " + count);
        }
        
        if (!job1Found || !job2Found || !job3Found) {
            throw new AssertionError("Not all jobs were found in the queue");
        }
    }
} 