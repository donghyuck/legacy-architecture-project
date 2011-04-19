package architecture.common.event.internal;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

/**
 * <p>A thread factory that will name the threads <strong>Event::[thread_name]</strong>.</p>
 * <p>If you need your own {@link java.util.concurrent.ThreadFactory} we recommend delegating the Thread creation to
 * this implementation.</p>
 */
public final class EventThreadFactory implements ThreadFactory
{
    private final ThreadFactory delegateThreadFactory;

    public EventThreadFactory()
    {
        this(Executors.defaultThreadFactory());
    }

    public EventThreadFactory(ThreadFactory delegateThreadFactory)
    {
        this.delegateThreadFactory = checkNotNull(delegateThreadFactory);
    }

    public Thread newThread(Runnable r)
    {
        final Thread thread = delegateThreadFactory.newThread(r);
        thread.setName("ArchitectureEvent::" + thread.getName());
        return thread;
    }
}
