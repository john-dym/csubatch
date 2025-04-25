package multithreading;

import java.util.HashMap;
import java.util.Map;

/**
 * Multi threading class that can take an Object with the 'Runnable' interface.
 * Both JobScheduler and DispatchingModule will require Runnable interface implementation.
 * createAndStartThread(Runnable) will be the primary method call.
 * @author Himanshu Bohra
 */
public class MultiThreader
{
    // Singleton instance
    private static MultiThreader _instance;
    
    // This will keep track of all the threads already started for clean program exit.
    private Map<String, Thread> _threadMap;
    
    private MultiThreader()
    {
        _threadMap = new HashMap<>();
    }
    
    /**
     * Gets the singleton instance of MultiThreader
     * @return The MultiThreader instance
     */
    public static synchronized MultiThreader getInstance() {
        if (_instance == null) {
            _instance = new MultiThreader();
        }
        return _instance;
    }
    
    private Thread createThread(Runnable runnable)
    {
        return new Thread(runnable);
    }

    public Thread createAndStartThread(String threadName, Runnable runnable)
    {
        Thread thread = createThread(runnable);
        _threadMap.put(threadName, thread);
        thread.start();

        // Return the Thread object in case program level thread management needs to be done elsewhere.
        return thread;
    }

    public boolean terminateAllThreads()
    {
        try
        {
            for (Thread thread : _threadMap.values())
            {
                try
                {
                    thread.interrupt();
                }
                catch (Exception e)
                {
                    System.out.println("An exception occurred while interrupt a thread in MultiThreader:\n" +
                            e.getMessage());
                }
            }

            // Set all references to null to ensure that non-daemon threads can be garbage collected,
            // so the main program can exit (JVM).
            _threadMap.clear();
            return true;
        }
        catch (Exception e)
        {
            System.out.println("An exception occured while trying to terminate all program threads:\n" +
                    e.getMessage());
            return false;
        }
    }

    // Accessors and modifiers
    public Map<String, Thread> getThreadMap()
    {
        return _threadMap;
    }

    public Thread getThread(String threadName)
    {
        return _threadMap.get(threadName);
    }

    public void printThreadList()
    {
        System.out.println(_threadMap.values().toString());
    }
}
