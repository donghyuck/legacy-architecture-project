package architecture.ee.util.task;


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

    public boolean isDoTask()
    {
        return doTask;
    }

    public void setDoTask(boolean doTask)
    {
        this.doTask = doTask;
    }

    private boolean doTask;
    
}