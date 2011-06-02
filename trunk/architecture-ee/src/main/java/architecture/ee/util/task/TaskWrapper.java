package architecture.ee.util.task;


public class TaskWrapper implements Comparable
{

    private Runnable task;

    private int priority;

    public TaskWrapper(int priority, Runnable task)
    {
        this.priority = priority;
        this.task = task;
    }

    public int compareTo(Object o)
    {
        if(o instanceof TaskWrapper)
        {
            TaskWrapper other = (TaskWrapper)o;
            if(other.priority == priority)
                return 0;
            else
                return priority <= other.priority ? -1 : 1;
        } else
        {
            return 1;
        }
    }

    public int getPriority()
    {
        return priority;
    }

    public Runnable getTask()
    {
        return task;
    }

    public void setPriority(int priority)
    {
        this.priority = priority;
    }
    public void setTask(Runnable task)
    {
        this.task = task;
    }
}
