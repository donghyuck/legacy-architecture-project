package architecture.ee.component.task;


public interface CancellableTask
{
    public abstract boolean shouldCancel();
}