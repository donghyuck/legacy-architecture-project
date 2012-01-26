package architecture.common.event.internal;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.SynchronousQueue;

import architecture.common.event.config.EventThreadPoolConfiguration;

public class DirectEventExecutorFactory extends AbstractEventExecutorFactory {

    public DirectEventExecutorFactory(EventThreadPoolConfiguration configuration, EventThreadFactory eventThreadFactory)
    {
        super(configuration, eventThreadFactory);
    }

    public DirectEventExecutorFactory(EventThreadPoolConfiguration configuration)
    {
        super(configuration);
    }

    protected BlockingQueue getQueue()
    {
        return new SynchronousQueue();
    }

}
