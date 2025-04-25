package util;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * JobQueueLogger utility class for writing job queue-related log messages to a dedicated file.
 * Implements the Singleton pattern to ensure only one logger instance exists.
 * @author Group 1
 */
public class JobQueueLogger {
    private static JobQueueLogger instance;
    private final String logFilePath;
    private final SimpleDateFormat dateFormat;
    private boolean consoleOutput;

    /**
     * Private constructor to enforce Singleton pattern
     * @param logFilePath Path to the log file
     */
    private JobQueueLogger(String logFilePath) {
        this.logFilePath = logFilePath;
        this.dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        this.consoleOutput = false;
        
        // Create log directory if it doesn't exist
        File logFile = new File(logFilePath);
        File parentDir = logFile.getParentFile();
        if (parentDir != null && !parentDir.exists()) {
            parentDir.mkdirs();
        }
        
        // Initialize log file with header
        try (PrintWriter writer = new PrintWriter(new FileWriter(logFilePath, false))) {
            writer.println("=== CSUBatch Job Queue Log File ===");
            writer.println("Started at: " + dateFormat.format(new Date()));
            writer.println("==============================");
        } catch (IOException e) {
            System.err.println("Error initializing job queue log file: " + e.getMessage());
        }
    }

    /**
     * Get the singleton instance of the JobQueueLogger
     * @param logFilePath Path to the log file
     * @return JobQueueLogger instance
     */
    public static synchronized JobQueueLogger getInstance(String logFilePath) {
        if (instance == null) {
            instance = new JobQueueLogger(logFilePath);
        }
        return instance;
    }
    
    /**
     * Get the singleton instance with default log file path
     * @return JobQueueLogger instance
     */
    public static synchronized JobQueueLogger getInstance() {
        return getInstance("logs/jobqueue.log");
    }

    /**
     * Log a job added event
     * @param jobName Name of the job
     * @param executionTime Execution time of the job
     * @param priority Priority of the job
     */
    public synchronized void jobAdded(String jobName, double executionTime, int priority) {
        log("ADDED", "Job added to queue: " + jobName + 
            " (execution_time=" + executionTime + 
            ", priority=" + priority + ")");
    }

    /**
     * Log a job dispatched event
     * @param jobName Name of the job
     */
    public synchronized void jobDispatched(String jobName) {
        log("DISPATCHED", "Job dispatched for execution: " + jobName);
    }

    /**
     * Log a job execution started event
     * @param jobName Name of the job
     */
    public synchronized void jobExecutionStarted(String jobName) {
        log("EXECUTING", "Job execution started: " + jobName);
    }

    /**
     * Log a job completed event
     * @param jobName Name of the job
     */
    public synchronized void jobCompleted(String jobName) {
        log("COMPLETED", "Job execution completed: " + jobName);
    }

    /**
     * Log a queue listing event
     * @param queueSize Size of the queue
     */
    public synchronized void queueListed(int queueSize) {
        log("LISTED", "Queue listed, size: " + queueSize);
    }

    /**
     * Log a message with the specified event type
     * @param eventType Type of event (ADDED, DISPATCHED, EXECUTING, COMPLETED, LISTED)
     * @param message Message to log
     */
    private void log(String eventType, String message) {
        String timestamp = dateFormat.format(new Date());
        String logEntry = String.format("[%s] [%s] %s", timestamp, eventType, message);
        
        // Write to log file
        try (PrintWriter writer = new PrintWriter(new FileWriter(logFilePath, true))) {
            writer.println(logEntry);
        } catch (IOException e) {
            System.err.println("Error writing to job queue log file: " + e.getMessage());
        }
        
        // Also print to console if enabled
        if (consoleOutput) {
            System.out.println(logEntry);
        }
    }

    /**
     * Enable or disable console output
     * @param enabled Whether console output should be enabled
     */
    public void setConsoleOutput(boolean enabled) {
        this.consoleOutput = enabled;
    }
} 