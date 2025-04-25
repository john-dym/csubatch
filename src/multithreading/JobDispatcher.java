package multithreading;

import Model.datastructures.Job;
import Model.datastructures.JobQueue;
import Model.datastructures.Queue;
import Model.statistics.JobStatistics;
import util.Logger;
import util.JobQueueLogger;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * JobDispatcher is responsible for taking jobs from the queue and executing them.
 * It runs as a separate thread and manages job execution.
 * Only one job can run at a time.
 * @author Group 1
 */
public class JobDispatcher implements Runnable {
    private final String _moduleID;
    private volatile boolean _running = true;
    private final JobQueue _jobQueue;
    private final ExecutorService _executorService;
    private final Logger _logger;
    private final JobQueueLogger _jobQueueLogger;
    private final JobStatistics _jobStatistics;
    private volatile boolean _jobRunning = false;
    private volatile Job _currentRunningJob = null;

    /**
     * Constructor for JobDispatcher
     * @param moduleID The ID of this module
     */
    public JobDispatcher(String moduleID) {
        _moduleID = moduleID;
        _jobQueue = JobQueue.getInstance();
        // Always use a single thread executor to ensure only one job runs at a time
        _executorService = Executors.newSingleThreadExecutor();
        _logger = Logger.getInstance();
        _jobQueueLogger = JobQueueLogger.getInstance();
        _jobStatistics = JobStatistics.getInstance();
    }

    @Override
    public void run() {
        _logger.info(_moduleID, "Job Dispatcher started: " + _moduleID);
        
        while (_running) {
            try {
                // Check for jobs to dispatch
                dispatchJobs();
                
                // Print status information
                printStatus();
                
                // Sleep to prevent CPU hogging
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                _logger.error(_moduleID, "Job Dispatcher interrupted: " + e.getMessage());
            }
        }
        
        // Try to shutdown gracefully
        _executorService.shutdown();
        try {
            // Wait for running jobs to complete (with a timeout)
            if (!_executorService.awaitTermination(5, TimeUnit.SECONDS)) {
                // Force shutdown if jobs don't complete in time
                _executorService.shutdownNow();
            }
        } catch (InterruptedException e) {
            // If waiting is interrupted, force shutdown
            _executorService.shutdownNow();
            Thread.currentThread().interrupt();
        }
        
        _logger.info(_moduleID, "Executor service shutdown completed");
    }

    /**
     * Dispatches jobs from the queue to be executed
     */
    private void dispatchJobs() {
        // Only dispatch a new job if no job is currently running
        if (!_jobRunning && _currentRunningJob == null) {
            // Check if queue is empty before trying to dequeue
            if (!_jobQueue.isEmpty()) {
                // Get the next job from the queue (if any) - use non-blocking method
                Job job = _jobQueue.getNextJob();
                
                if (job != null && job.isIdle()) {
                    _jobRunning = true;
                    _currentRunningJob = job;
                    // Set the job as the running job in JobQueue - now separate from dequeuing
                    _jobQueue.setRunningJob(job);
                    
                    _logger.info(_moduleID, "Dispatching job: " + job.getName() + 
                                " with execution time: " + job.getExecutionTime() + " seconds");
                    _jobQueueLogger.jobDispatched(job.getName());
                    
                    // Use the executor service to run the job in a separate thread
                    _executorService.submit(() -> {
                        try {
                            _logger.info(_moduleID, "Executing job: " + job.getName());
                            _jobQueueLogger.jobExecutionStarted(job.getName());
                            
                            // This will block until the job completes its execution time
                            // The job.run() method will sleep for the job's execution time
                            boolean success = job.run();
                            
                            if (success) {
                                _logger.info(_moduleID, "Job completed successfully: " + job.getName());
                                job.setStatus(Job.Status.FINISHED);
                                // Record the job completion in the statistics
                                _jobStatistics.recordJobCompletion(job);
                            } else {
                                _logger.warning(_moduleID, "Job may not have completed successfully: " + job.getName());
                            }
                            _jobQueueLogger.jobCompleted(job.getName());
                        } catch (Exception e) {
                            _logger.error(_moduleID, "Error executing job: " + job.getName() + " - " + e.getMessage());
                        } finally {
                            // Always clean up, even if there was an exception
                            _jobRunning = false;
                            _currentRunningJob = null;
                            _jobQueue.setRunningJob(null);
                        }
                    });
                } else if (job != null && !job.isIdle()) {
                    _logger.warning(_moduleID, "Attempted to dispatch a job that was not idle: " + job.getName());
                    // Put the job back in the queue
                    _jobQueue.addJob(job);
                }
            }
        }
    }

    /**
     * Prints the current status of the job dispatcher
     */
    private void printStatus() {
        Job runningJob = _jobQueue.getRunningJob();
        
        StringBuilder status = new StringBuilder();
        status.append("Status: Queue size=").append(_jobQueue.size());
        status.append(", Job running=").append(_jobRunning);
        
        if (runningJob != null) {
            status.append(", Current job=").append(runningJob.getName());
            status.append(", Execution time=").append(runningJob.getExecutionTime()).append("s");
        }
        
        status.append(", Thread ID=").append(Thread.currentThread().threadId());
        status.append(", Thread Name=").append(Thread.currentThread().getName());
        
        _logger.info(_moduleID, status.toString());
    }

    /**
     * Stops the job dispatcher
     */
    public void stopRunning() {
        _running = false;
        _logger.info(_moduleID, "Job Dispatcher stopping");
    }
} 