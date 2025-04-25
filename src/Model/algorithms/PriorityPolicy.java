package Model.algorithms;

import Model.datastructures.Job;
import Model.datastructures.Queue;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * A scheduling policy object which re-orders a given job queue by their priority level.
 * Jobs with higher priority values are executed first.
 * @author Group 1
 */
public class PriorityPolicy extends SchedulingPolicy
{
    /**
     * Constructor for PriorityPolicy
     */
    public PriorityPolicy() {
        super();
        _policyName = "Priority";
    }

    public PriorityPolicy(Queue jobQueueIn) {
        super(jobQueueIn);
        _policyName = "Priority";
    }

    /**
     * This method will modify the Model.datastructures.Queue containing job objects which was given to this
     * class object upon instantiation.
     * @return boolean, success or failure of the job
     */
    public boolean execute()
    {
        /* In this policy, we sort jobs by their priority in descending order.
           The job with the highest priority will be executed first.
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

            // Sort the jobs based on their priority (highest first, so reversing natural order)
            Collections.sort(jobs, Comparator.comparingInt(Job::getPriority).reversed());

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
        return "Priority Policy";
    }
} 