package controller;

/**
 * Command interface for the Command Design Pattern
 * This interface defines the contract for all command implementations
 */
public interface Command {
    /**
     * Validate the command arguments before execution
     * @return true if validation passes, false otherwise
     */
    boolean validate();
    
    /**
     * Execute the command
     * @return true if the command should continue execution, false to exit the CLI
     */
    boolean execute();
    
    /**
     * Get the description for help text
     * @return String description of the command
     */
    String getDescription();
} 