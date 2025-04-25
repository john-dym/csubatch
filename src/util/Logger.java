package util;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Logger utility class for writing log messages to a file.
 * Implements the Singleton pattern to ensure only one logger instance exists.
 * @author Group 1
 */
public class Logger {
    private static Logger instance;
    private final String logFilePath;
    private final SimpleDateFormat dateFormat;
    private boolean consoleOutput;

    /**
     * Private constructor to enforce Singleton pattern
     * @param logFilePath Path to the log file
     */
    private Logger(String logFilePath, boolean consoleOutput) {
        this.logFilePath = logFilePath;
        this.dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        this.consoleOutput = consoleOutput;
        
        // Create log directory if it doesn't exist
        File logFile = new File(logFilePath);
        File parentDir = logFile.getParentFile();
        if (parentDir != null && !parentDir.exists()) {
            parentDir.mkdirs();
        }
        
        // Initialize log file with header
        try (PrintWriter writer = new PrintWriter(new FileWriter(logFilePath, false))) {
            writer.println("=== CSUBatch Log File ===");
            writer.println("Started at: " + dateFormat.format(new Date()));
            writer.println("==============================");
        } catch (IOException e) {
            System.err.println("Error initializing log file: " + e.getMessage());
        }
    }

    /**
     * Get the singleton instance of the Logger
     * @param logFilePath Path to the log file
     * @return Logger instance
     */
    public static synchronized Logger getInstance(String logFilePath) {
        if (instance == null) {
            instance = new Logger(logFilePath, false);
        }
        return instance;
    }
    
    /**
     * Get the singleton instance with default log file path
     * @return Logger instance
     */
    public static synchronized Logger getInstance() {
        return getInstance("logs/currentlog.log");
    }

    /**
     * Log an information message
     * @param source Source of the log message (e.g., class name)
     * @param message Message to log
     */
    public synchronized void info(String source, String message) {
        log("INFO", source, message);
    }

    /**
     * Log a warning message
     * @param source Source of the log message (e.g., class name)
     * @param message Message to log
     */
    public synchronized void warning(String source, String message) {
        log("WARNING", source, message);
    }

    /**
     * Log an error message
     * @param source Source of the log message (e.g., class name)
     * @param message Message to log
     */
    public synchronized void error(String source, String message) {
        log("ERROR", source, message);
    }

    /**
     * Log a message with the specified level
     * @param level Log level (INFO, WARNING, ERROR)
     * @param source Source of the log message
     * @param message Message to log
     */
    private void log(String level, String source, String message) {
        String timestamp = dateFormat.format(new Date());
        String logEntry = String.format("[%s] [%s] [%s] %s", timestamp, level, source, message);
        
        // Write to log file
        try (PrintWriter writer = new PrintWriter(new FileWriter(logFilePath, true))) {
            writer.println(logEntry);
        } catch (IOException e) {
            System.err.println("Error writing to log file: " + e.getMessage());
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