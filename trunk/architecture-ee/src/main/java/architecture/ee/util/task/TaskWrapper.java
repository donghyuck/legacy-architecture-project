package architecture.ee.util.task;



/**
 * @author  donghyuck
 */
public class TaskWrapper implements Comparable
{

    /**
	 * @uml.property  name="task"
	 */
    private Runnable task;

    /**
	 * @uml.property  name="priority"
	 */
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

    /**
	 * @return
	 * @uml.property  name="priority"
	 */
    public int getPriority()
    {
        return priority;
    }

    /**
	 * @return
	 * @uml.property  name="task"
	 */
    public Runnable getTask()
    {
        return task;
    }

    /**
	 * @param priority
	 * @uml.property  name="priority"
	 */
    public void setPriority(int priority)
    {
        this.priority = priority;
    }
    /**
	 * @param task
	 * @uml.property  name="task"
	 */
    public void setTask(Runnable task)
    {
        this.task = task;
    }
}
