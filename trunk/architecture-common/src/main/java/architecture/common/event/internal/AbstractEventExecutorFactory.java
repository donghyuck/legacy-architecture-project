package architecture.common.event.internal;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

import architecture.common.event.config.EventThreadPoolConfiguration;
import architecture.common.event.spi.EventExecutorFactory;

import com.google.common.base.Preconditions;

/**
 * @author  donghyuck
 */
public abstract class AbstractEventExecutorFactory implements EventExecutorFactory {

    /**
	 * @uml.property  name="configuration"
	 * @uml.associationEnd  
	 */
    private final EventThreadPoolConfiguration configuration;
    /**
	 * @uml.property  name="eventThreadFactory"
	 * @uml.associationEnd  
	 */
    private final EventThreadFactory eventThreadFactory;
    
    public AbstractEventExecutorFactory(EventThreadPoolConfiguration configuration, EventThreadFactory eventThreadFactory)
    {
        this.configuration = (EventThreadPoolConfiguration)Preconditions.checkNotNull(configuration);
        this.eventThreadFactory = (EventThreadFactory)Preconditions.checkNotNull(eventThreadFactory);
    }

    public AbstractEventExecutorFactory(EventThreadPoolConfiguration configuration)
    {
        this(configuration, new EventThreadFactory());
    }

    protected abstract BlockingQueue getQueue();

    public Executor getExecutor()
    {
        return new ThreadPoolExecutor(configuration.getCorePoolSize(), configuration.getMaximumPoolSize(), configuration.getKeepAliveTime(), configuration.getTimeUnit(), getQueue(), eventThreadFactory);
    }


}
