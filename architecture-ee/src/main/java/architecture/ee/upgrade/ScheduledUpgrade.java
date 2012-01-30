package architecture.ee.upgrade;


public class ScheduledUpgrade
{

    private final UpgradeTask task;
    private final String taskClassName;
    private final int previousVersion;
    private final int targetVersion;
    private final String upgradeName;
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

    public String getUpgradeName()
    {
        return upgradeName;
    }

    public int getOrder()
    {
        return order;
    }

    public UpgradeTask getTask()
    {
        return task;
    }

    public String getTaskClassName()
    {
        return taskClassName;
    }

    public int getPreviousVersion()
    {
        return previousVersion;
    }

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

