package architecture.common.event.config;

import java.util.concurrent.TimeUnit;

/**
 * A configuration object for thread pools used by asynchronous event dispatchers
 */
public interface EventThreadPoolConfiguration
{
	
	public abstract int getCorePoolSize();

	public abstract int getMaximumPoolSize();

	public abstract long getKeepAliveTime();

	public abstract TimeUnit getTimeUnit();
	
}
