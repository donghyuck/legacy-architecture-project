package architecture.common.event.internal;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;

import architecture.common.event.config.EventThreadPoolConfiguration;
import architecture.common.event.spi.EventExecutorFactory;

/**
 * @author    donghyuck
 */
public class EventExecutorFactoryImpl implements EventExecutorFactory
{
    /**
	 * @uml.property  name="configuration"
	 * @uml.associationEnd  
	 */
    private final EventThreadPoolConfiguration configuration;

    public EventExecutorFactoryImpl(EventThreadPoolConfiguration configuration)
    {
        this.configuration = checkNotNull(configuration);
    }

    public Executor getExecutor()
    {
        return new ThreadPoolExecutor(
                configuration.getCorePoolSize(),
                configuration.getMaximumPoolSize(),
                configuration.getKeepAliveTime(),
                configuration.getTimeUnit(),
                new LinkedBlockingQueue<Runnable>(),
                new EventThreadFactory());
    }
}
