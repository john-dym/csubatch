package Model.algorithms;

import Model.datastructures.Queue;

/**
 * Abstract class which represents the Scheduling Policy archetype.
 * Implementation of the Strategy design pattern.
 * @author Himanshu Bohra
 */
public abstract class SchedulingPolicy
{
    protected String _policyName;

    protected Queue _jobQueueDataStructure;

    protected Queue _jobQueueDefensiveCopy;

    /**
     * This default constructor should never be use. Use the second ver to provide
     * the Model.datastructures.Queue object containing Model.datastructures.Job objects instead.
     */
    public SchedulingPolicy()
    {
        _jobQueueDataStructure = new Queue();
    }

    public SchedulingPolicy(Queue jobQueueIn)
    {
        createDefensiveCopy(jobQueueIn);
        _jobQueueDataStructure = jobQueueIn;
    }

    public abstract boolean execute();

    // Accessors and modifiers

    public String getPolicyName()
    {
        return _policyName;
    }

    // Utility functions

    /**
     * This will create a defensive copy of the instantiation-time job-queue.
     * Can be used later to retrieve the original copy.
     * Used only during the primary constructor.
     * @param jobQueueIn The job queue that will be given to the primary constructor.
     */
    protected void createDefensiveCopy(Queue jobQueueIn)
    {
        _jobQueueDefensiveCopy = new Queue();
        // Copy elements from input queue to defensive copy
        for (Object obj : jobQueueIn) {
            _jobQueueDefensiveCopy.enqueue(obj);
        }
    }

    public Queue getUnmodifiedJobQueue()
    {
        return _jobQueueDefensiveCopy;
    }
}
