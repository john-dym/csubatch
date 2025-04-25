package View;

import Model.datastructures.Job;
import Model.datastructures.JobQueue;
import Model.datastructures.Queue;
import controller.Command;
import controller.CommandFactory;
import multithreading.JobScheduler;
import util.Logger;
import util.JobQueueLogger;

import java.util.Scanner;

/**
 * Command Line Interface implementing Command Design pattern.
 * @author Himanshu Bohra, Group 1
 */
public class CommandLineInterface implements Runnable
{
    private boolean _isRunning = true;
    private Scanner _scanner = new Scanner(System.in);
    private JobScheduler _jobScheduler;
    private final Logger _logger;
    private final JobQueueLogger _jobQueueLogger;
    private final String _moduleID = "CLI";
    private CommandFactory _commandFactory;

    /**
     * Constructor for CommandLineInterface
     */
    public CommandLineInterface() {
        _logger = Logger.getInstance();
        _jobQueueLogger = JobQueueLogger.getInstance();
    }

    @Override
    public void run()
    {
        // System.out.println("Command Line Interface started. Type 'help' for available commands.");
        _logger.info(_moduleID, "Command Line Interface started");
        
        // Initialize the command factory
        _commandFactory = new CommandFactory(_jobScheduler);
        
        // Basic execution for testing the project.
        while (_isRunning)
        {
            System.out.print("> ");
            String commandInput = _scanner.nextLine();

            processCommand(commandInput);
        }
    }

    /**
     * Process the user command using Command pattern
     * @param commandInput The command entered by the user
     */
    private void processCommand(String commandInput) {
        _logger.info(_moduleID, "Processing command input: " + commandInput);
        
        // Use the command factory to execute the command with validation
        _isRunning = _commandFactory.executeCommand(commandInput);
    }

    /**
     * Set the job scheduler
     * @param scheduler The job scheduler
     */
    public void setJobScheduler(JobScheduler scheduler) {
        _jobScheduler = scheduler;
        _logger.info(_moduleID, "Job Scheduler set");
        
        // If the command factory already exists, update it with the new scheduler
        if (_commandFactory != null) {
            _commandFactory = new CommandFactory(_jobScheduler);
        }
    }

    public boolean isRunning() {
        return _isRunning;
    }

    /**
     * Stop execution of the CLI thread
     */
    public void stopExecution() {
        Thread.currentThread().interrupt();
        _isRunning = false;
    }

    /**
     * Resume execution (placeholder for future implementation)
     */
    public void resumeExecution() {
        // Reserved for future implementation
    }
}
