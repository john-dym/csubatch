package Model.datastructures;

import util.Logger;
import java.lang.InterruptedException;

public class Job {
    private final String name;
    private final double executionTime;
    private final int priority;
    private final double arrivalTime;
    private final Logger logger;
    private long completedTime;
    private long startTime;
    
    public enum Status {
        IDLE,
        RUNNING,
        FINISHED
    }
    private Status status;

    /**
     * Constructor for the Model.datastructures.Job class
     * @param name The name of the job
     * @param executionTime The time it takes to execute the job (in seconds)
     * @param priority The priority of the job
     * @param arrivalTime The time the job arrives
     */
    public Job(String name, double executionTime, int priority, double arrivalTime) {
        this.name = name;
        this.executionTime = executionTime;
        this.priority = priority;
        this.arrivalTime = arrivalTime;
        this.status = Status.IDLE;
        this.logger = Logger.getInstance();
        this.completedTime = -1;
        this.startTime = -1;
        
        logger.info("Job-" + name, "Job created with execution time=" + executionTime + 
                   ", priority=" + priority + ", arrival time=" + arrivalTime);
    }

    public String getName() {
        return name;
    }

    public double getExecutionTime() {
        return executionTime;
    }

    public int getPriority() {
        return priority;
    }

    public double getArrivalTime() {
        return arrivalTime;
    }

    public Status getStatus() {
        return this.status;
    }

    /**
     * Get the time when the job started execution
     * @return The start time in milliseconds, or -1 if not started
     */
    public long getStartTime() {
        return startTime;
    }

    /**
     * Get the time when the job completed execution
     * @return The completion time in milliseconds, or -1 if not completed
     */
    public long getCompletedTime() {
        return completedTime;
    }

    /**
     * Calculate the turnaround time (completion time - arrival time)
     * @return The turnaround time in seconds, or -1 if job hasn't completed
     */
    public double getTurnaroundTime() {
        if (completedTime == -1) {
            return -1;
        }
        return (completedTime - (long)arrivalTime) / 1000.0;
    }

    /**
     * Calculate the waiting time (start time - arrival time)
     * @return The waiting time in seconds, or -1 if job hasn't started
     */
    public double getWaitingTime() {
        if (startTime == -1) {
            return -1;
        }
        return (startTime - (long)arrivalTime) / 1000.0;
    }

    /**
     * Runs the job
     * @return True if the job was successfully ran, false if the job was already running or finished
     */
    public boolean run() {
        setStatus(Status.RUNNING);
        logger.info("Job-" + name, "Job started execution for " + executionTime + " seconds");
        sleep(executionTime);
        setStatus(Status.FINISHED);
        logger.info("Job-" + name, "Job finished execution");
        return true; // Successfully ran the job
    }

    private void sleep(double seconds) {
        try {
            // Sleep for the specified number of seconds by converting to milliseconds
            Thread.sleep((long) (seconds * 1000));
        } catch (InterruptedException e) {
            logger.error("Job-" + name, "Job execution interrupted: " + e.getMessage());
            Thread.currentThread().interrupt();
        }
    }

    /**
     * Changes the status of the job
     * @param status Enum Status can be: IDLE, RUNNING, or FINISHED
     */
    public void setStatus(Status status) {
        if (status == Status.RUNNING) {
            this.startTime = System.currentTimeMillis();
            logger.info("Job-" + name, "Job started at " + this.startTime);
        } else if (status == Status.FINISHED) {
            this.completedTime = System.currentTimeMillis();
            logger.info("Job-" + name, "Job completed at " + this.completedTime + 
                       ", turnaround time: " + getTurnaroundTime() + "s, waiting time: " + getWaitingTime() + "s");
        }
        this.status = status;
        logger.info("Job-" + name, "Job status changed to " + statusToString());
    }

    public boolean isIdle() {
        return status == Status.IDLE;
    }

    public boolean isFinished() {
        return status == Status.FINISHED;
    }

    public boolean isRunning() {
        return status == Status.RUNNING;
    }

    @Override
    public String toString() {
        return "Job: " + name + " Priority: " + priority + " Arrival Time: " + arrivalTime + 
               " Execution Time: " + executionTime + " Status: " + statusToString();
    }

    private String statusToString(){
        return switch (status) {
            case IDLE -> "IDLE";
            case RUNNING -> "RUNNING";
            case FINISHED -> "FINISHED";
        };
    }
}

