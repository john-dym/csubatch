package multithreading;

import java.util.Random;

/**
 * A dummy runnable module which represents the JobScheduler / DispatchingModule
 * @author Himanshu Bohra
 */
public class DummyRunnableModule implements Runnable
{
    private final String _moduleID;

    // Volatile boolean will allow immediate reflection of changes across threads.
    private volatile boolean _running = true;
    public DummyRunnableModule(String moduleID)
    {
        _moduleID = moduleID;
    }
    @Override
    public void run()
    {
        // Notify that this is running on a separate thread.
        periodicallyPrintThreadInfo();

    }

    private void periodicallyPrintThreadInfo()
    {
        Random random = new Random();
        while (true)
        {
            try
            {
                Thread.sleep(random.nextInt(4000) + 5000); // sleep for 1-2 seconds

                System.out.println("DUMMY MODULE: " + _moduleID + ". Running on a separate thread. Thread INFO:\n");
                System.out.println("Thread ID: " + Thread.currentThread().threadId());
                System.out.println("Thread Name: " + Thread.currentThread().getName());
                System.out.println("________________________________________________\n");
            }
            catch (InterruptedException e)
            {
                Thread.currentThread().interrupt();
            }
        }
    }

    public void stopRunning()
    {
        _running = false;
    }
}
