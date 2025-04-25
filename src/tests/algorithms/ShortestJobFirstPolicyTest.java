package tests.algorithms;

import Model.algorithms.ShortestJobFirstPolicy;
import Model.datastructures.Job;
import Model.datastructures.Queue;

/**
 * Test class for the Shortest Job First scheduling policy.
 */
public class ShortestJobFirstPolicyTest {

    private Queue jobQueue;
    private ShortestJobFirstPolicy sjfPolicy;

    public void setUp() {
        jobQueue = new Queue();
        sjfPolicy = new ShortestJobFirstPolicy(jobQueue);
    }

    public void testEmptyQueue() {
        // Test with empty queue should still work
        sjfPolicy.execute();
        assertTrue("Queue should remain empty after executing policy on empty queue", jobQueue.isEmpty());
    }

    public void testSingleJob() {
        // Add a single job to the queue
        Job job = new Job("Job1", 5.0, 1, 100.0);
        jobQueue.enqueue(job);

        // Execute the policy
        sjfPolicy.execute();

        // The queue should still have the same job
        assertEquals("Queue size should be 1 after policy execution", 1, jobQueue.getSize());
        assertEquals("Job should remain in the queue", job, jobQueue.peek());
    }

    public void testJobsOrderedByExecutionTime() {
        // Add jobs with different execution times in random order
        Job job3 = new Job("Job3", 10.0, 1, 100.0); // Longest execution time
        Job job1 = new Job("Job1", 3.0, 3, 100.0); // Shortest execution time
        Job job2 = new Job("Job2", 5.0, 2, 100.0); // Medium execution time

        // Add in unordered sequence
        jobQueue.enqueue(job3);
        jobQueue.enqueue(job1);
        jobQueue.enqueue(job2);

        // Execute the policy
        sjfPolicy.execute();

        // Verify jobs are now ordered by execution time (shortest first)
        assertEquals("First job should be the one with shortest execution time", job1, jobQueue.dequeue());
        assertEquals("Second job should be the one with medium execution time", job2, jobQueue.dequeue());
        assertEquals("Third job should be the one with longest execution time", job3, jobQueue.dequeue());
        assertTrue("Queue should be empty after dequeuing all jobs", jobQueue.isEmpty());
    }

    public void testJobsWithSameExecutionTime() {
        // Add jobs with same execution time
        Job job1 = new Job("Job1", 5.0, 1, 100.0);
        Job job2 = new Job("Job2", 5.0, 2, 200.0);
        Job job3 = new Job("Job3", 5.0, 3, 300.0);

        jobQueue.enqueue(job1);
        jobQueue.enqueue(job2);
        jobQueue.enqueue(job3);

        // Execute the policy
        sjfPolicy.execute();

        // Since execution times are the same, original order should be preserved
        // (or other implementation-specific tie-breaking behavior should be tested)
        Object job = jobQueue.dequeue();
        assertNotNull("First job should not be null", job);
        job = jobQueue.dequeue();
        assertNotNull("Second job should not be null", job);
        job = jobQueue.dequeue();
        assertNotNull("Third job should not be null", job);
    }

    public void testPolicyNameAndToString() {
        assertEquals("Policy should have correct toString value", "Shortest Job First Policy", sjfPolicy.toString());
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
    
    private void assertEquals(String message, String expected, String actual) {
        if (expected == null && actual == null) {
            return;
        }
        if (expected == null || actual == null || !expected.equals(actual)) {
            throw new AssertionError(message + " - expected: " + expected + ", but was: " + actual);
        }
    }
    
    private void assertNotNull(String message, Object object) {
        if (object == null) {
            throw new AssertionError(message);
        }
    }
} 