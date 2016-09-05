package architecture.common.event.impl;

import architecture.common.event.config.EventThreadPoolConfiguration;

/**
 * @deprecated use {@link DirectEventExecutorFactory}
 */
@Deprecated
public class EventExecutorFactoryImpl extends DirectEventExecutorFactory {
    public EventExecutorFactoryImpl(final EventThreadPoolConfiguration configuration, final EventThreadFactory eventThreadFactory) {
        super(configuration, eventThreadFactory);
    }

    public EventExecutorFactoryImpl(final EventThreadPoolConfiguration configuration) {
        super(configuration);
    }
}
