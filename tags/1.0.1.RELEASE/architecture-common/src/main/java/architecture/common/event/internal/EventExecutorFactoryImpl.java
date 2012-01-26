package architecture.common.event.internal;

import architecture.common.event.config.EventThreadPoolConfiguration;

/**
 * @author    donghyuck
 */
public class EventExecutorFactoryImpl extends DirectEventExecutorFactory
{
	public EventExecutorFactoryImpl(EventThreadPoolConfiguration configuration, EventThreadFactory eventThreadFactory)
    {
        super(configuration, eventThreadFactory);
    }

    public EventExecutorFactoryImpl(EventThreadPoolConfiguration configuration)
    {
        super(configuration);
    }
}
