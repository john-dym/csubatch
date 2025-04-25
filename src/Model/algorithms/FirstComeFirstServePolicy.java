package Model.algorithms;

import Model.datastructures.Job;
import Model.datastructures.Queue;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * A scheduling policy object which re-orders a given job queue by their in-coming time
 * with the first come, first enqueue strategy.
 * @author Himanshu Bohra
 */
public class FirstComeFirstServePolicy extends SchedulingPolicy
{
    /**
     * Constructor for FirstComeFirstServePolicy
     */
    public FirstComeFirstServePolicy() {
        super();
        _policyName = "FCFS.";
    }

    public FirstComeFirstServePolicy(Queue jobQueueIn) {
        super(jobQueueIn);
        _policyName = "FCFS.";
    }

    /**
     * This method will modify the Model.datastructures.Queue containing job objects which was given to this
     * class object upon instantiation.
     * @return boolean, success or failure of the job
     */
    public boolean execute()
    {
        /* In this policy, a queue may be provided which is not in the FCFS order.
           This means that the Model.datastructures.Job object must have order/timestamps info to determine its
           sequence. First, we will need to determine ALL the jobs in the queue and find
           their sequence order in order to enqueue them back in the FCFS order.
         */

        try
        {
            // Holding obj
            List<Job> jobs = new ArrayList<>();

            // Get all jobs from queue
            while (!_jobQueueDataStructure.isEmpty()) {
                Object obj = _jobQueueDataStructure.dequeue();
                if (obj instanceof Job) {
                    jobs.add((Job) obj);
                }
            }

            // Sort the jobs based on their timestamps
            Collections.sort(jobs, Comparator.comparingDouble(Job::getArrivalTime));

            // Re-add the sorted jobs to the queue
            for (Job job : jobs) {
                _jobQueueDataStructure.enqueue(job);
            }

            return true;
        }
        catch (Exception e)
        {
            System.out.println("An exception has occurred. Trace:\n" + e.getMessage());
            return false;
        }
    }

    @Override
    public String toString() {
        return "FCFS";
    }
}
