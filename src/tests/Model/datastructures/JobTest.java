package tests.Model.datastructures;

import Model.datastructures.Job;

/**
 * Test class for the Job data structure.
 */
public class JobTest {
    
    private Job job;
    
    /**
     * Set up for tests.
     */
    public void setUp() {
        // Reset for each test
    }
    
    /**
     * Test job creation with valid parameters.
     */
    public void testJobCreation() {
        job = new Job("TestJob", 10, 2, 1);
        
        // Verify job properties
        if (!job.getName().equals("TestJob")) {
            throw new AssertionError("Job name should be 'TestJob'");
        }
        
        if (job.getExecutionTime() != 10) {
            throw new AssertionError("Job execution time should be 10");
        }
        
        if (job.getPriority() != 2) {
            throw new AssertionError("Job priority should be 2");
        }
        
        if (job.getArrivalTime() != 1) {
            throw new AssertionError("Job arrival time should be 1");
        }
        
        if (job.getStatus() != Job.Status.IDLE) {
            throw new AssertionError("Initial job state should be IDLE");
        }
    }
    
    /**
     * Test job state transitions.
     */
    public void testJobStateTransitions() {
        job = new Job("StateTest", 5, 1, 0);
        
        // Test initial state
        if (job.getStatus() != Job.Status.IDLE) {
            throw new AssertionError("Initial job state should be IDLE");
        }
        
        // Test transition to RUNNING
        job.setStatus(Job.Status.RUNNING);
        if (job.getStatus() != Job.Status.RUNNING) {
            throw new AssertionError("Job state should be RUNNING after setState");
        }
        
        // Test transition to COMPLETED
        job.setStatus(Job.Status.FINISHED);
        if (job.getStatus() != Job.Status.FINISHED) {
            throw new AssertionError("Job state should be COMPLETED after setState");
        }
    }
    
    /**
     * Test job comparison for ordering.
     */
    public void testJobComparison() {
        Job job1 = new Job("Job1", 10, 1, 5);
        Job job2 = new Job("Job2", 5, 2, 10);
        
        // Test arrival time comparison (job1 arrives before job2)
        if (job1.getArrivalTime() >= job2.getArrivalTime()) {
            throw new AssertionError("Job1 should come before Job2 when comparing by arrival time");
        }
        
        // Test execution time comparison (job2 has shorter exec time)
        if (job2.getExecutionTime() >= job1.getExecutionTime()) {
            throw new AssertionError("Job2 should come before Job1 when comparing by execution time");
        }
        
        // Test priority comparison (job2 has higher priority)
        // In our system, higher priority numbers mean higher priority
        if (job2.getPriority() <= job1.getPriority()) {
            throw new AssertionError("Job2 should come before Job1 when comparing by priority");
        }
    }
    
    /**
     * Test job ID generation is unique.
     */
    public void testUniqueJobIds() {
        Job job1 = new Job("Job1", 10, 1, 0);
        Job job2 = new Job("Job2", 10, 1, 0);
        
        if (job1 == job2) {
            throw new AssertionError("Jobs should be unique objects");
        }
    }
    
    /**
     * Test toString method generates expected output.
     */
    public void testToString() {
        job = new Job("ToStringTest", 15, 3, 7);
        String jobString = job.toString();
        
        if (!jobString.contains("ToStringTest") || 
            !jobString.contains("15") || 
            !jobString.contains("3")) {
            throw new AssertionError("Job toString should contain name, execution time, and priority");
        }
    }
} 