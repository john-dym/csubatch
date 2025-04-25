package tests.controller;

import controller.Command;
import controller.CommandFactory;
import controller.RunCommand;
import controller.ListCommand;
import controller.StatusCommand;
import controller.FcfsCommand;
import controller.SjfCommand;
import controller.PriorityCommand;
import controller.HelpCommand;
import controller.ExitCommand;
import controller.TestCommand;
import controller.UnknownCommand;
import multithreading.JobScheduler;

/**
 * Test class for the CommandFactory.
 */
public class CommandFactoryTest {
    
    private CommandFactory commandFactory;
    
    /**
     * Set up for tests.
     */
    public void setUp() {
        JobScheduler jobScheduler = JobScheduler.getInstance();
        commandFactory = new CommandFactory(jobScheduler);
    }
    
    /**
     * Test creating a run command.
     */
    public void testCreateRunCommand() {
        String input = "run testJob 10 1";
        Command command = commandFactory.createCommand(input);
        
        if (!(command instanceof RunCommand)) {
            throw new AssertionError("Factory should create a RunCommand for 'run' input");
        }
    }
    
    /**
     * Test creating a list command.
     */
    public void testCreateListCommand() {
        String input = "list";
        Command command = commandFactory.createCommand(input);
        
        if (!(command instanceof ListCommand)) {
            throw new AssertionError("Factory should create a ListCommand for 'list' input");
        }
    }
    
    /**
     * Test creating a status command.
     */
    public void testCreateStatusCommand() {
        String input = "status";
        Command command = commandFactory.createCommand(input);
        
        if (!(command instanceof StatusCommand)) {
            throw new AssertionError("Factory should create a StatusCommand for 'status' input");
        }
    }
    
    /**
     * Test creating a fcfs command.
     */
    public void testCreateFcfsCommand() {
        String input = "fcfs";
        Command command = commandFactory.createCommand(input);
        
        if (!(command instanceof FcfsCommand)) {
            throw new AssertionError("Factory should create a FcfsCommand for 'fcfs' input");
        }
    }
    
    /**
     * Test creating a sjf command.
     */
    public void testCreateSjfCommand() {
        String input = "sjf";
        Command command = commandFactory.createCommand(input);
        
        if (!(command instanceof SjfCommand)) {
            throw new AssertionError("Factory should create a SjfCommand for 'sjf' input");
        }
    }
    
    /**
     * Test creating a priority command.
     */
    public void testCreatePriorityCommand() {
        String input = "priority";
        Command command = commandFactory.createCommand(input);
        
        if (!(command instanceof PriorityCommand)) {
            throw new AssertionError("Factory should create a PriorityCommand for 'priority' input");
        }
    }
    
    /**
     * Test creating a help command.
     */
    public void testCreateHelpCommand() {
        String input = "help";
        Command command = commandFactory.createCommand(input);
        
        if (!(command instanceof HelpCommand)) {
            throw new AssertionError("Factory should create a HelpCommand for 'help' input");
        }
    }
    
    /**
     * Test creating an exit command.
     */
    public void testCreateExitCommand() {
        String input = "exit";
        Command command = commandFactory.createCommand(input);
        
        if (!(command instanceof ExitCommand)) {
            throw new AssertionError("Factory should create an ExitCommand for 'exit' input");
        }
    }
    
    /**
     * Test creating a test command.
     */
    public void testCreateTestCommand() {
        String input = "test 5";
        Command command = commandFactory.createCommand(input);
        
        if (!(command instanceof TestCommand)) {
            throw new AssertionError("Factory should create a TestCommand for 'test' input");
        }
    }
    
    /**
     * Test creating an unknown command.
     */
    public void testCreateUnknownCommand() {
        String input = "invalidcommand";
        Command command = commandFactory.createCommand(input);
        
        if (!(command instanceof UnknownCommand)) {
            throw new AssertionError("Factory should create an UnknownCommand for invalid input");
        }
    }
    
    /**
     * Test case insensitivity.
     */
    public void testCommandCaseInsensitivity() {
        String input = "LIST";
        Command command = commandFactory.createCommand(input);
        
        if (!(command instanceof ListCommand)) {
            throw new AssertionError("Factory should be case insensitive");
        }
    }
} 