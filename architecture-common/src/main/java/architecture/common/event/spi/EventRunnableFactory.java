package architecture.common.event.spi;

import javax.annotation.Nonnull;
import java.util.concurrent.Executor;

/**
 * An optional SPI that allows you to hook into the creation of the {@link Runnable} that gets supplied to the
 * {@link Executor} that's returned from the {@link EventExecutorFactory}.
 *
 * @since v2.3
 */
public interface EventRunnableFactory {
    /**
     * Returns a new {@link Runnable}. The {@link Runnable} should call invoker.invoke(event) at some stage.
     *
     * @param invoker
     * @param event
     * @return
     */
    @Nonnull
    Runnable getRunnable(final ListenerInvoker invoker, final Object event);
}