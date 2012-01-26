package architecture.common.event.spi;

import java.util.concurrent.Executor;

/**
 * <p>A factory to create executors for asynchronous event handling</p>
 */
public interface EventExecutorFactory
{
	public abstract Executor getExecutor();
}
