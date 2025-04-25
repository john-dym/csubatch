import multithreading.JobDispatcher;
import Model.datastructures.Job;
import Model.datastructures.JobQueue;
import multithreading.MultiThreader;

public class MicroBenchmark {
    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println("No command-line arguments provided. Include a integer seconds argument for execution time.");
            return;
        }

        int execTime = 0;

        try {
            execTime = Integer.parseInt(args[0]);
        } catch (NumberFormatException e) {
            System.out.println("Error: The provided argument is not a valid integer.");
        }


        Job job = new Job("Job", execTime, 0, 0);
        JobQueue jobQueue = JobQueue.getInstance();
        jobQueue.addJob(job);

        MultiThreader multiThreader = MultiThreader.getInstance();
        JobDispatcher jobDispatcher = new JobDispatcher("Benchmark");
        multiThreader.createAndStartThread("JobScheduler", jobDispatcher);

        while (!job.isFinished())
        {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
        jobDispatcher.stopRunning();

    }
}

