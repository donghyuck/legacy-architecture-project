package architecture.ee.util.task;


/**
 * @author  donghyuck
 */
public abstract class CancellableRunnable
    implements Runnable
{

    public CancellableRunnable()
    {
        doTask = true;
    }

    public void run()
    {
        if(doTask)
            doTask();
    }

    public abstract void doTask();

    /**
	 * @return
	 */
    public boolean isDoTask()
    {
        return doTask;
    }

    /**
	 * @param doTask
	 */
    public void setDoTask(boolean doTask)
    {
        this.doTask = doTask;
    }

    /**
	 */
    private boolean doTask;
    
}