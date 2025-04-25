package tests;

import tests.algorithms.FirstComeFirstServePolicyTest;
import tests.algorithms.ShortestJobFirstPolicyTest;
import tests.algorithms.PriorityPolicyTest;
import tests.Model.datastructures.JobTest;
import tests.Model.datastructures.JobQueueTest;
import tests.controller.CommandFactoryTest;
import tests.multithreading.JobSchedulerTest;
import tests.View.CommandLineInterfaceTest;
import tests.util.LoggerTest;

/**
 * Test runner for executing all tests.
 */
public class TestRunner {
    
    public static void main(String[] args) {
        System.out.println("Running all tests...");
        
        // Run algorithm tests
        runAlgorithmTests();
        
        // Run model/datastructure tests
        runModelTests();
        
        // Run controller tests
        runControllerTests();
        
        // Run multithreading tests
        runMultithreadingTests();
        
        // Run view tests
        runViewTests();
        
        // Run utility tests
        runUtilTests();

        System.out.println("\nAll tests completed!");
    }
    
    private static void runAlgorithmTests() {
        System.out.println("\n======= Scheduling Algorithm Tests =======");
        
        // FCFS tests
        System.out.println("\n--- First Come First Serve Policy Tests ---");
        FirstComeFirstServePolicyTest fcfsTest = new FirstComeFirstServePolicyTest();
        runTest(fcfsTest::setUp, fcfsTest::testEmptyQueue, "Testing empty queue");
        runTest(fcfsTest::setUp, fcfsTest::testSingleJob, "Testing single job");
        runTest(fcfsTest::setUp, fcfsTest::testJobsOrderedByArrivalTime, "Testing jobs ordered by arrival time");
        runTest(fcfsTest::setUp, fcfsTest::testJobsWithSameArrivalTime, "Testing jobs with same arrival time");
        runTest(fcfsTest::setUp, fcfsTest::testPolicyNameAndToString, "Testing policy name and toString");
        
        // SJF tests
        System.out.println("\n--- Shortest Job First Policy Tests ---");
        ShortestJobFirstPolicyTest sjfTest = new ShortestJobFirstPolicyTest();
        runTest(sjfTest::setUp, sjfTest::testEmptyQueue, "Testing empty queue");
        runTest(sjfTest::setUp, sjfTest::testSingleJob, "Testing single job");
        runTest(sjfTest::setUp, sjfTest::testJobsOrderedByExecutionTime, "Testing jobs ordered by execution time");
        runTest(sjfTest::setUp, sjfTest::testJobsWithSameExecutionTime, "Testing jobs with same execution time");
        runTest(sjfTest::setUp, sjfTest::testPolicyNameAndToString, "Testing policy name and toString");
        
        // Priority tests
        System.out.println("\n--- Priority Policy Tests ---");
        PriorityPolicyTest priorityTest = new PriorityPolicyTest();
        runTest(priorityTest::setUp, priorityTest::testEmptyQueue, "Testing empty queue");
        runTest(priorityTest::setUp, priorityTest::testSingleJob, "Testing single job");
        runTest(priorityTest::setUp, priorityTest::testJobsOrderedByPriority, "Testing jobs ordered by priority");
        runTest(priorityTest::setUp, priorityTest::testJobsWithSamePriority, "Testing jobs with same priority");
        runTest(priorityTest::setUp, priorityTest::testPriorityScales, "Testing priority scales");
        runTest(priorityTest::setUp, priorityTest::testPolicyNameAndToString, "Testing policy name and toString");
    }
    
    private static void runModelTests() {
        System.out.println("\n======= Model Data Structure Tests =======");
        
        // Job tests
        System.out.println("\n--- Job Tests ---");
        JobTest jobTest = new JobTest();
        runTest(jobTest::setUp, jobTest::testJobCreation, "Testing job creation");
        runTest(jobTest::setUp, jobTest::testJobStateTransitions, "Testing job state transitions");
        runTest(jobTest::setUp, jobTest::testJobComparison, "Testing job comparison");
        runTest(jobTest::setUp, jobTest::testUniqueJobIds, "Testing unique job IDs");
        runTest(jobTest::setUp, jobTest::testToString, "Testing toString method");
        
        // JobQueue tests
        System.out.println("\n--- JobQueue Tests ---");
        JobQueueTest jobQueueTest = new JobQueueTest();
        runTest(jobQueueTest::setUp, jobQueueTest::testAddJob, "Testing adding jobs");
        runTest(jobQueueTest::setUp, jobQueueTest::testSingleton, "Testing singleton pattern");
        runTest(jobQueueTest::setUp, jobQueueTest::testRemoveJob, "Testing removing jobs");
        runTest(jobQueueTest::setUp, jobQueueTest::testClearQueue, "Testing clearing queue");
        runTest(jobQueueTest::setUp, jobQueueTest::testGetJobs, "Testing getting all jobs");
    }
    
    private static void runControllerTests() {
        System.out.println("\n======= Controller Tests =======");
        
        // CommandFactory tests
        System.out.println("\n--- CommandFactory Tests ---");
        CommandFactoryTest commandFactoryTest = new CommandFactoryTest();
        runTest(commandFactoryTest::setUp, commandFactoryTest::testCreateRunCommand, "Testing run command creation");
        runTest(commandFactoryTest::setUp, commandFactoryTest::testCreateListCommand, "Testing list command creation");
        runTest(commandFactoryTest::setUp, commandFactoryTest::testCreateStatusCommand, "Testing status command creation");
        runTest(commandFactoryTest::setUp, commandFactoryTest::testCreateFcfsCommand, "Testing fcfs command creation");
        runTest(commandFactoryTest::setUp, commandFactoryTest::testCreateSjfCommand, "Testing sjf command creation");
        runTest(commandFactoryTest::setUp, commandFactoryTest::testCreatePriorityCommand, "Testing priority command creation");
        runTest(commandFactoryTest::setUp, commandFactoryTest::testCreateHelpCommand, "Testing help command creation");
        runTest(commandFactoryTest::setUp, commandFactoryTest::testCreateExitCommand, "Testing exit command creation");
        runTest(commandFactoryTest::setUp, commandFactoryTest::testCreateTestCommand, "Testing test command creation");
        runTest(commandFactoryTest::setUp, commandFactoryTest::testCreateUnknownCommand, "Testing unknown command creation");
        runTest(commandFactoryTest::setUp, commandFactoryTest::testCommandCaseInsensitivity, "Testing command case insensitivity");
    }
    
    private static void runMultithreadingTests() {
        System.out.println("\n======= Multithreading Tests =======");
        
        // JobScheduler tests
        System.out.println("\n--- JobScheduler Tests ---");
        JobSchedulerTest jobSchedulerTest = new JobSchedulerTest();
        runTest(jobSchedulerTest::setUp, jobSchedulerTest::testSingleton, "Testing singleton pattern");
        runTest(jobSchedulerTest::setUp, jobSchedulerTest::testSetFCFSPolicy, "Testing setting FCFS policy");
        runTest(jobSchedulerTest::setUp, jobSchedulerTest::testSetSJFPolicy, "Testing setting SJF policy");
        runTest(jobSchedulerTest::setUp, jobSchedulerTest::testSetPriorityPolicy, "Testing setting priority policy");
        runTest(jobSchedulerTest::setUp, jobSchedulerTest::testScheduleJob, "Testing job scheduling");
    }
    
    private static void runViewTests() {
        System.out.println("\n======= View Tests =======");
        
        // CommandLineInterface tests
        System.out.println("\n--- CommandLineInterface Tests ---");
        CommandLineInterfaceTest cliTest = new CommandLineInterfaceTest();
        runTest(cliTest::setUp, cliTest::testInitialization, "Testing CLI initialization");
        
        // Note: The following tests may be dependent on CLI implementation
        // and might need modifications based on actual implementation
        try {
            runTest(cliTest::setUp, cliTest::testParseValidCommand, "Testing parsing valid command");
            runTest(cliTest::setUp, cliTest::testParseInvalidCommand, "Testing parsing invalid command");
            runTest(cliTest::setUp, cliTest::testHandleEmptyInput, "Testing handling empty input");
            runTest(cliTest::setUp, cliTest::testStopCLI, "Testing stopping CLI");
        } catch (UnsupportedOperationException e) {
            System.out.println("Some CLI tests skipped due to implementation limitations");
        }
        
        // Always run tearDown
        try {
            cliTest.tearDown();
        } catch (Exception e) {
            System.out.println("Error in CLI test tearDown: " + e.getMessage());
        }
    }
    
    private static void runUtilTests() {
        System.out.println("\n======= Utility Tests =======");
        
        // Logger tests
        System.out.println("\n--- Logger Tests ---");
        LoggerTest loggerTest = new LoggerTest();
        runTest(loggerTest::setUp, loggerTest::testSingleton, "Testing singleton pattern");
        runTest(loggerTest::setUp, loggerTest::testInfoLogging, "Testing info logging");
        runTest(loggerTest::setUp, loggerTest::testWarningLogging, "Testing warning logging");
        runTest(loggerTest::setUp, loggerTest::testErrorLogging, "Testing error logging");
        
        // Debug logging might not be implemented or enabled
        try {
            runTest(loggerTest::setUp, loggerTest::testDebugLogging, "Testing debug logging");
        } catch (UnsupportedOperationException e) {
            System.out.println("Debug logging test skipped - not supported");
        }
    }
    

    /**
     * Helper method to run a test with setup and proper error handling.
     */
    private static void runTest(Runnable setup, Runnable test, String testName) {
        System.out.print(testName + ": ");
        try {
            if (setup != null) {
                setup.run();
            }
            test.run();
            System.out.println("PASSED");
        } catch (AssertionError e) {
            System.out.println("FAILED");
            System.out.println("  - " + e.getMessage());
        } catch (Exception e) {
            System.out.println("ERROR");
            System.out.println("  - " + e.getMessage());
            e.printStackTrace();
        }
    }
} 