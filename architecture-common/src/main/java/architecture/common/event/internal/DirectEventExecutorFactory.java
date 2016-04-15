package architecture.common.event.internal;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;

import architecture.common.event.config.EventThreadPoolConfiguration;

/**
 * <p>Uses a {@link SynchronousQueue} to hand off tasks to the {@link Executor}. An attempt to to queue a task will fail if no threads are immediately available to run it</p>
 *
 * <p>See {@link ThreadPoolExecutor} for more information.</p>
 *
 * @since 2.1
 */
public class DirectEventExecutorFactory extends AbstractEventExecutorFactory {
    public DirectEventExecutorFactory(final EventThreadPoolConfiguration configuration, final EventThreadFactory eventThreadFactory) {
        super(configuration, eventThreadFactory);
    }

    public DirectEventExecutorFactory(final EventThreadPoolConfiguration configuration) {
        super(configuration);
    }

    /**
     * @return a new {@link SynchronousQueue<Runnable>}
     */
    @Override
    protected BlockingQueue<Runnable> getQueue() {
        return new SynchronousQueue<Runnable>();
    }
}
