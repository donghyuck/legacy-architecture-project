package architecture.common.util;
/**
 * 
 * @author donghyuck son
 *
 */
public interface CancellableTask
{
    public abstract boolean shouldCancel();
}