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
	 * @uml.property  name="doTask"
	 */
    public boolean isDoTask()
    {
        return doTask;
    }

    /**
	 * @param doTask
	 * @uml.property  name="doTask"
	 */
    public void setDoTask(boolean doTask)
    {
        this.doTask = doTask;
    }

    /**
	 * @uml.property  name="doTask"
	 */
    private boolean doTask;
    
}