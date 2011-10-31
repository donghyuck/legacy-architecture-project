package architecture.ee.util.task;

import java.util.Date;
import java.util.List;

public interface Pollable
{
    public abstract boolean isRunning();

    public abstract int getTaskMinimum();

    public abstract int getTaskMaximum();

    public abstract int getTaskValue();

    public abstract Date getStartDate();

    public abstract Date getEndDate();

    public abstract double getPercentComplete();

    public abstract boolean isIndeterminate();

    public abstract boolean isFinished();

    public abstract boolean isSuccess();

    public abstract List getFailures();
}
