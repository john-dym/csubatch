package controller;

/**
 * Command to handle empty input (no operation)
 */
public class NoOpCommand implements Command {
    
    @Override
    public boolean validate() {
        // No validation needed for no-op command
        return true;
    }
    
    @Override
    public boolean execute() {
        // No operation for empty commands
        return true;
    }
    
    @Override
    public String getDescription() {
        return ""; // Not displayed in help
    }
} 