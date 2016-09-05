package architecture.common.event.impl;

import com.google.common.util.concurrent.MoreExecutors;

import architecture.common.event.spi.EventDispatcher;
import architecture.common.event.spi.EventExecutorFactory;
import architecture.common.event.spi.EventRunnableFactory;
import architecture.common.event.spi.ListenerInvoker;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Executor;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * This dispatcher will dispatch event asynchronously if: <ul> <li>the event 'is' asynchronous, as resolved by the
 * {@link AsynchronousEventResolver} and</li> <li>the invoker {@link architecture.common.event.spi.ListenerInvoker#supportAsynchronousEvents()
 * supports asynchronous events}</li> </ul>
 *
 * @since 2.0
 */
public class AsynchronousAbleEventDispatcher implements EventDispatcher {
    private static final Logger log = LoggerFactory.getLogger(AsynchronousAbleEventDispatcher.class);

    /**
     * An executor that execute commands synchronously
     */
    private static final Executor SYNCHRONOUS_EXECUTOR = MoreExecutors.sameThreadExecutor();

    /**
     * {@link EventRunnableFactory} that creates a simple runnable that runs the invoker.
     */
    private static final EventRunnableFactory SIMPLE_RUNNABLE_FACTORY = new EventRunnableFactory() {
        public Runnable getRunnable(final ListenerInvoker invoker, final Object event) {
            return new Runnable() {
                public void run() {
                    try {
                        invoker.invoke(event);
                    } catch (Exception e) {
                        // Cannot use placeholder syntax until we upgrade to SLF4J 1.6+ - see EVENT-26
                        // Add an enabled guard to avoid unnecessary String construction
                        if (log.isErrorEnabled()) {
                            log.error("There was an exception thrown trying to dispatch event [" + event + "] from the invoker [" + invoker + "]", e);
                        }
                    }
                }
            };
        }
    };

    /**
     * An asynchronous executor
     */
    private final Executor asynchronousExecutor;

    private final AsynchronousEventResolver asynchronousEventResolver;

    private final EventRunnableFactory runnableFactory;

    public AsynchronousAbleEventDispatcher(Executor executor, AsynchronousEventResolver asynchronousEventResolver, EventRunnableFactory runnableFactory) {
        this.asynchronousEventResolver = checkNotNull(asynchronousEventResolver);
        this.asynchronousExecutor = checkNotNull(executor);
        this.runnableFactory = checkNotNull(runnableFactory);
    }


    public AsynchronousAbleEventDispatcher(Executor executor, AsynchronousEventResolver asynchronousEventResolver) {
        this(executor, asynchronousEventResolver, SIMPLE_RUNNABLE_FACTORY);
    }


    public AsynchronousAbleEventDispatcher(EventExecutorFactory executorFactory, AsynchronousEventResolver asynchronousEventResolver, EventRunnableFactory runnableFactory) {
        this(checkNotNull(executorFactory).getExecutor(), asynchronousEventResolver, runnableFactory);
    }


    public AsynchronousAbleEventDispatcher(EventExecutorFactory executorFactory, AsynchronousEventResolver asynchronousEventResolver) {
        this(executorFactory, asynchronousEventResolver, SIMPLE_RUNNABLE_FACTORY);
    }


    public AsynchronousAbleEventDispatcher(EventExecutorFactory executorFactory) {
        this(executorFactory, new AnnotationAsynchronousEventResolver(), SIMPLE_RUNNABLE_FACTORY);
    }


    public void dispatch(final ListenerInvoker invoker, final Object event) {
        executorFor(checkNotNull(invoker), checkNotNull(event)).execute(runnableFactory.getRunnable(invoker, event));
    }

    protected Executor executorFor(ListenerInvoker invoker, Object event) {
        return asynchronousEventResolver.isAsynchronousEvent(event) && invoker.supportAsynchronousEvents()
                ? getAsynchronousExecutor()
                : getSynchronousExecutor();
    }

    protected Executor getSynchronousExecutor() {
        return SYNCHRONOUS_EXECUTOR;
    }

    protected Executor getAsynchronousExecutor() {
        return asynchronousExecutor;
    }

}
