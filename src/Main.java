import View.CommandLineInterface;
import multithreading.JobDispatcher;
import multithreading.JobScheduler;
import multithreading.MultiThreader;
import util.Logger;
import util.JobQueueLogger;

import java.io.File;

public class Main
{
    public static void main(String[] args)
    {
        // Ensure logs directory exists
        File logsDir = new File("logs");
        if (!logsDir.exists()) {
            logsDir.mkdirs();
        }
        
        // Initialize the loggers
        Logger logger = Logger.getInstance();
        // JobQueueLogger jobQueueLogger = JobQueueLogger.getInstance();
        
        logger.info("Main", "Starting CSUBatch application");
        logger.info("Main", "General log file: logs/currentlog.log");
        logger.info("Main", "Job queue log file: logs/jobqueue.log");
        
        System.out.println(banner());
        programExecution();

        // Exit with a 0 / success status.
        System.exit(0);

    }

    private static String banner()
    {
        return """
                Welcome to Group 1's batch job scheduler Version 0.0.1.
                Type 'help' to find more about CSUbatch commands.""";
    }

    private static void programExecution()
    {
        Logger logger = Logger.getInstance();
        
        // Object instantiation / thread creation
        MultiThreader multiThreader = MultiThreader.getInstance();
        logger.info("Main", "MultiThreader initialized");

        // Create and start the job scheduler
        JobScheduler jobScheduler = JobScheduler.getInstance();
        multiThreader.createAndStartThread("JobScheduler", jobScheduler);
        logger.info("Main", "JobScheduler thread started");
        
        // Create and start the job dispatcher (only one job at a time)
        JobDispatcher jobDispatcher = new JobDispatcher("DispMod");
        multiThreader.createAndStartThread("JobDispatcher", jobDispatcher);
        logger.info("Main", "JobDispatcher thread started (single job mode)");

        // Create and configure the CLI with Command Design Pattern
        CommandLineInterface cli = new CommandLineInterface();
        cli.setJobScheduler(jobScheduler);
        // cli.setJobDispatcher(jobDispatcher);
        multiThreader.createAndStartThread("CommandLineInterface", cli);
        logger.info("Main", "CommandLineInterface thread started with Command Design Pattern");

        mainProgramLoop(cli);

        // DEBUG: list all threads
        // multiThreader.printThreadList();

        // Terminate all threads
        multiThreader.terminateAllThreads();
    }

    private static void mainProgramLoop(CommandLineInterface mainCLI)
    {
        Logger logger = Logger.getInstance();
        logger.info("Main", "Entering main program loop");
        
        // As long as the CLI is running, we will keep going.
        while (mainCLI.isRunning())
        {
            try
            {
                Thread.sleep(100);
            }
            catch (InterruptedException e)
            {
                Thread.currentThread().interrupt();
                logger.error("Main", "Main thread interrupted: " + e.getMessage());
            }
        }

        logger.info("Main", "CLI closed. Program exiting...");
        System.out.println("CLI closed. Program exiting...");
    }
}