package tests.View;

import View.CommandLineInterface;
import multithreading.JobScheduler;
import controller.Command;
import controller.RunCommand;
import controller.ListCommand;
import controller.CommandFactory;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * Test class for the CommandLineInterface.
 */
public class CommandLineInterfaceTest {
    
    private CommandLineInterface cli;
    private InputStream originalIn;
    private CommandFactory commandFactory;
    
    /**
     * Set up for tests.
     */
    public void setUp() {
        // Save original System.in
        originalIn = System.in;
        
        // Create a new CLI
        cli = new CommandLineInterface();
        JobScheduler scheduler = JobScheduler.getInstance();
        cli.setJobScheduler(scheduler);
        
        // Create CommandFactory for tests
        commandFactory = new CommandFactory(scheduler);
    }
    
    /**
     * Clean up after tests.
     */
    public void tearDown() {
        // Restore original System.in
        System.setIn(originalIn);
        
        // Stop CLI if running
        if (cli.isRunning()) {
            cli.stopExecution();
        }
    }
    
    /**
     * Test CLI initialization.
     */
    public void testInitialization() {
        if (!cli.isRunning()) {
            throw new AssertionError("CLI should be running after initialization");
        }
    }
    
    /**
     * Test parsing a valid command.
     */
    public void testParseValidCommand() {
        // Simulate user input
        String input = "run testJob 10 1";
        
        // Use CommandFactory to create the command
        Command command = commandFactory.createCommand(input);
        
        if (!(command instanceof RunCommand)) {
            throw new AssertionError("CLI should parse 'run testJob 10 1' as a RunCommand");
        }
    }
    
    /**
     * Test parsing an invalid command.
     */
    public void testParseInvalidCommand() {
        // Simulate user input
        String input = "invalid command";
        
        // Use CommandFactory to create the command
        Command command = commandFactory.createCommand(input);
        
        if (command == null) {
            throw new AssertionError("CLI should not return null for invalid commands");
        }
        
        // Should be an UnknownCommand or similar
        if (command.execute() != false) {
            throw new AssertionError("Invalid command should not execute successfully");
        }
    }
    
    /**
     * Test handling empty input.
     */
    public void testHandleEmptyInput() {
        // Simulate empty input
        String input = "";
        
        // Use CommandFactory to create the command
        Command command = commandFactory.createCommand(input);
        
        // Should be a NoOpCommand or similar that does nothing but doesn't fail
        if (command == null || command.execute() == false) {
            throw new AssertionError("CLI should handle empty input gracefully");
        }
    }
    
    /**
     * Test stopping the CLI.
     */
    public void testStopCLI() {
        // CLI should be running initially
        if (!cli.isRunning()) {
            throw new AssertionError("CLI should be running after initialization");
        }
        
        // Stop the CLI
        cli.stopExecution();
        
        // CLI should no longer be running
        if (cli.isRunning()) {
            throw new AssertionError("CLI should not be running after stop");
        }
    }
} 