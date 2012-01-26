package architecture.common.event.internal;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import architecture.common.event.config.EventThreadPoolConfiguration;

public class UnboundedEventExecutorFactory extends AbstractEventExecutorFactory
{

    public UnboundedEventExecutorFactory(EventThreadPoolConfiguration configuration, EventThreadFactory eventThreadFactory)
    {
        super(configuration, eventThreadFactory);
    }

    public UnboundedEventExecutorFactory(EventThreadPoolConfiguration configuration)
    {
        super(configuration);
    }

    @SuppressWarnings("rawtypes")
	protected BlockingQueue getQueue()
    {
        return new LinkedBlockingQueue();
    }
}