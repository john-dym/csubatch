package tests.algorithms;

import Model.algorithms.FirstComeFirstServePolicy;
import Model.datastructures.Job;
import Model.datastructures.Queue;

/**
 * Test class for the First Come First Serve scheduling policy.
 */
public class FirstComeFirstServePolicyTest {

    private Queue jobQueue;
    private FirstComeFirstServePolicy fcfsPolicy;

    public void setUp() {
        jobQueue = new Queue();
        fcfsPolicy = new FirstComeFirstServePolicy(jobQueue);
    }

    public void testEmptyQueue() {
        // Test with empty queue should still work
        fcfsPolicy.execute();
        assertTrue("Queue should remain empty after executing policy on empty queue", jobQueue.isEmpty());
    }

    public void testSingleJob() {
        // Add a single job to the queue
        Job job = new Job("Job1", 5.0, 1, 100.0);
        jobQueue.enqueue(job);

        // Execute the policy
        fcfsPolicy.execute();

        // The queue should still have the same job
        assertEquals("Queue size should be 1 after policy execution", 1, jobQueue.getSize());
        assertEquals("Job should remain in the queue", job, jobQueue.peek());
    }

    public void testJobsOrderedByArrivalTime() {
        // Add jobs with different arrival times in random order
        Job job3 = new Job("Job3", 3.0, 1, 300.0); // Arrived last
        Job job1 = new Job("Job1", 5.0, 3, 100.0); // Arrived first
        Job job2 = new Job("Job2", 2.0, 2, 200.0); // Arrived second

        // Add in unordered sequence
        jobQueue.enqueue(job3);
        jobQueue.enqueue(job1);
        jobQueue.enqueue(job2);

        // Execute the policy
        fcfsPolicy.execute();

        // Verify jobs are now ordered by arrival time
        assertEquals("First job should be the one that arrived first", job1, jobQueue.dequeue());
        assertEquals("Second job should be the one that arrived second", job2, jobQueue.dequeue());
        assertEquals("Third job should be the one that arrived last", job3, jobQueue.dequeue());
        assertTrue("Queue should be empty after dequeuing all jobs", jobQueue.isEmpty());
    }

    public void testJobsWithSameArrivalTime() {
        // Add jobs with same arrival time
        Job job1 = new Job("Job1", 5.0, 1, 100.0);
        Job job2 = new Job("Job2", 2.0, 2, 100.0);
        Job job3 = new Job("Job3", 3.0, 3, 100.0);

        jobQueue.enqueue(job1);
        jobQueue.enqueue(job2);
        jobQueue.enqueue(job3);

        // Execute the policy
        fcfsPolicy.execute();

        // Since arrival times are the same, original order should be preserved
        // (or implementation-specific behavior should be documented and tested)
        assertEquals("First job should remain first", job1, jobQueue.dequeue());
        assertEquals("Second job should remain second", job2, jobQueue.dequeue());
        assertEquals("Third job should remain third", job3, jobQueue.dequeue());
    }

    public void testPolicyNameAndToString() {
        assertEquals("Policy should have correct toString value", "FCFS", fcfsPolicy.toString());
    }
    
    // Custom assertion methods
    private void assertTrue(String message, boolean condition) {
        if (!condition) {
            throw new AssertionError(message);
        }
    }
    
    private void assertEquals(String message, Object expected, Object actual) {
        if (expected == null && actual == null) {
            return;
        }
        if (expected == null || actual == null || !expected.equals(actual)) {
            throw new AssertionError(message + " - expected: " + expected + ", but was: " + actual);
        }
    }
    
    private void assertEquals(String message, int expected, int actual) {
        if (expected != actual) {
            throw new AssertionError(message + " - expected: " + expected + ", but was: " + actual);
        }
    }
    
    private void assertNotNull(String message, Object object) {
        if (object == null) {
            throw new AssertionError(message);
        }
    }
} 