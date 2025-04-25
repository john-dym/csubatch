package Model.datastructures;

import java.util.List;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.Condition;
import java.lang.StringBuilder;

/**
 * Thread-safe implementation of a job queue using a single mutex lock and condition variables.
 */
public class JobQueue {
    private static JobQueue instance;
    // Single lock for the entire class
    private static final ReentrantLock lock = new ReentrantLock();
    private static final Condition notEmpty = lock.newCondition();
    private Queue _jobQueue;
    private Job _runningJob; // Track the currently running job

    private JobQueue() {
        _jobQueue = new Queue();
        _runningJob = null;
    }

    /**
     * Thread-safe singleton instance getter
     * @return The singleton instance of JobQueue
     */
    public static JobQueue getInstance() {
        if (instance == null) {
            instance = new JobQueue();
        }
        return instance;
    }

    /**
     * Thread-safe method to access the queue
     * @return A copy of the current queue to prevent concurrent modifications
     */
    public Queue requestQueue() {
        lock.lock();
        return _jobQueue;
    }

    /**
     * Check if the queue is empty
     * @return true if the queue is empty, false otherwise
     */
    public boolean isEmpty() {
        lock.lock();
        try {
            return _jobQueue.isEmpty();
        } finally {
            lock.unlock();
        }
    }

    /**
     * Set the currently running job
     * @param job The job that is currently running
     */
    public void setRunningJob(Job job) {
        lock.lock();
        try {
            _runningJob = job;
            // Update the job's status to RUNNING if it's a valid job
            if (_runningJob != null) {
                _runningJob.setStatus(Job.Status.RUNNING);
            }
        } finally {
            lock.unlock();
        }
    }

    /**
     * Get the currently running job
     * @return The currently running job or null if no job is running
     */
    public Job getRunningJob() {
        lock.lock();
        try {
            return _runningJob;
        } finally {
            lock.unlock();
        }
    }

    /**
     * Get the next job from the queue (non-blocking)
     * @return The next job or null if queue is empty
     */
    public Job getNextJob() {
        lock.lock();
        try {
            if (_jobQueue.isEmpty()) {
                return null;
            }
            Job job = (Job) _jobQueue.dequeue();
            return job;
        } finally {
            lock.unlock();
        }
    }

    /**
     * Wait for and get a job from the queue (blocking with timeout)
     * @param timeoutMillis Maximum time to wait in milliseconds (0 means wait indefinitely)
     * @return The next job from the queue or null if timeout occurs
     */
    public Job getJob(long timeoutMillis) {
        lock.lock();
        try {
            // First check if queue is already non-empty
            if (!_jobQueue.isEmpty()) {
                return (Job) _jobQueue.dequeue();
            }
            
            // If we need to wait, set up a timeout if requested
            if (timeoutMillis > 0) {
                try {
                    boolean notTimedOut = notEmpty.await(timeoutMillis, java.util.concurrent.TimeUnit.MILLISECONDS);
                    if (!notTimedOut || _jobQueue.isEmpty()) {
                        return null; // Timeout occurred or queue is still empty
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    return null;
                }
            } else {
                // Wait indefinitely
                try {
                    while (_jobQueue.isEmpty()) {
                        notEmpty.await();
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    return null;
                }
            }
            
            // At this point, we should have a job in the queue
            if (!_jobQueue.isEmpty()) {
                return (Job) _jobQueue.dequeue();
            } else {
                return null; // Defensive check
            }
        } finally {
            lock.unlock();
        }
    }

    /**
     * Wait for and get a job from the queue (blocking indefinitely)
     * @return The next job from the queue or null if interrupted
     */
    public Job getJob() {
        return getJob(0); // Wait indefinitely
    }

    /**
     * Add a job to the queue
     * @param job The job to add
     */
    public void addJob(Job job) {
        if (job == null) {
            throw new IllegalArgumentException("Cannot add a null job to the queue");
        }
        
        lock.lock();
        try {
            _jobQueue.enqueue(job);
            notEmpty.signalAll(); // Signal waiting threads that a job is available
        } finally {
            lock.unlock();
        }
    }

    public boolean unlock(){
        // print the current lock state
        // System.out.println("Lock state: " + lock.isLocked());
        try{
            lock.unlock();
            return true;
        } catch (Exception e){
            // System.out.println("Error unlocking the queue: " + e.getMessage());
            return false;
        }
        
    }

    public boolean lock(){
        lock.lock();
        return true;
    }

    public int size() {
        lock.lock();
        try {
            return _jobQueue.getSize();
        } finally {
            lock.unlock();
        }
    }
    
    /**
     * Get a string representation of the job queue
     * @return A string with all jobs in the queue
     */
    @Override
    public String toString() {
        StringBuilder results = new StringBuilder();
        lock.lock();
        try {
            if (_jobQueue.isEmpty() && _runningJob == null) {
                return "The Job Queue is empty.";
            } else {
                // Header for job details
                results.append("Name\t")
                        .append("CPU_Time\t")
                        .append("Pri\t")
                        .append("Arrival_time\t")
                        .append("Progress\n");

                // First add the running job if there is one
                if (_runningJob != null) {
                    results.append(_runningJob.getName()).append("\t")
                           .append(_runningJob.getExecutionTime()).append("\t")
                           .append(_runningJob.getPriority()).append("\t")
                           .append(new java.text.SimpleDateFormat("HH:mm:ss").format(new java.util.Date((long)_runningJob.getArrivalTime()))).append("\t")
                           .append("Run\n"); // Running job always shows "Run"
                }

                // Then add all queued jobs
                for (Object obj : _jobQueue) {
                    Job job = (Job) obj;
                    results.append(job.getName()).append("\t")
                            .append(job.getExecutionTime()).append("\t")
                            .append(job.getPriority()).append("\t")
                            .append(new java.text.SimpleDateFormat("HH:mm:ss").format(new java.util.Date((long)job.getArrivalTime()))).append("\t")
                            .append(job.getStatus()).append("\n");
                }
            }
            return results.toString();
        } finally {
            lock.unlock();
        }
    }
}