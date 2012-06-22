package architecture.ee.upgrade;


/**
 * @author  donghyuck
 */
public class ScheduledUpgrade
{

    /**
	 * @uml.property  name="task"
	 * @uml.associationEnd  
	 */
    private final UpgradeTask task;
    /**
	 * @uml.property  name="taskClassName"
	 */
    private final String taskClassName;
    /**
	 * @uml.property  name="previousVersion"
	 */
    private final int previousVersion;
    /**
	 * @uml.property  name="targetVersion"
	 */
    private final int targetVersion;
    /**
	 * @uml.property  name="upgradeName"
	 */
    private final String upgradeName;
    /**
	 * @uml.property  name="order"
	 */
    private final int order;
    
    public ScheduledUpgrade(UpgradeTask task, String taskClassName, String name, int order, int previousVersion, int targetVersion)
    {
        this.task = task;
        this.taskClassName = taskClassName;
        upgradeName = name;
        this.order = order;
        this.previousVersion = previousVersion;
        this.targetVersion = targetVersion;
    }

    /**
	 * @return
	 * @uml.property  name="upgradeName"
	 */
    public String getUpgradeName()
    {
        return upgradeName;
    }

    /**
	 * @return
	 * @uml.property  name="order"
	 */
    public int getOrder()
    {
        return order;
    }

    /**
	 * @return
	 * @uml.property  name="task"
	 */
    public UpgradeTask getTask()
    {
        return task;
    }

    /**
	 * @return
	 * @uml.property  name="taskClassName"
	 */
    public String getTaskClassName()
    {
        return taskClassName;
    }

    /**
	 * @return
	 * @uml.property  name="previousVersion"
	 */
    public int getPreviousVersion()
    {
        return previousVersion;
    }

    /**
	 * @return
	 * @uml.property  name="targetVersion"
	 */
    public int getTargetVersion()
    {
        return targetVersion;
    }

    public boolean equals(Object o)
    {
        if(this == o)
            return true;
        if(o == null || getClass() != o.getClass())
            return false;
        ScheduledUpgrade that = (ScheduledUpgrade)o;
        return task == null ? that.task == null : task.getClass() == that.task.getClass();
    }

    public int hashCode()
    {
        int result = task == null ? 0 : task.hashCode();
        result = 31 * result + previousVersion;
        result = 31 * result + targetVersion;
        result = 31 * result + (upgradeName == null ? 0 : upgradeName.hashCode());
        return result;
    }

}

