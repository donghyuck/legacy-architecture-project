package architecture.common.event.impl;

import javax.annotation.Nonnull;

import architecture.common.event.spi.ListenerInvoker;

/**
 * {@link #transformAll(Iterable, Object) Transforms} a collection of {@link architecture.common.event.spi.ListenerInvoker ListenerInvokers}
 * into another collection of {@code ListenerInvokers} before they are dispatched. This may give implementing classes
 * the opportunity to, among other things, batch up the asynchronous invokers or perform other interesting
 * transformations.
 */
public interface InvokerTransformer {
    /**
     * Takes a collection of {@link architecture.common.event.spi.ListenerInvoker ListenerInvokers} and returns a potentially transformed version of
     * these.
     * <p/>
     * The only on the returned collection is that it must be non-null. It may have the same, a greater number or
     * smaller number of elements than the supplied collection and it may contain the original elements or completely
     * new elements or a combination of both.
     *
     * @param invokers the collection of {@code ListenerInvokers} to invoke
     * @param event    the event the supplied invokers were to be dispatched to and the returned invokers will be
     *                 dispatched to
     * @return the potentially transformed collection of {@code ListenerInvokers}
     */
    @Nonnull
    Iterable<ListenerInvoker> transformAll(@Nonnull Iterable<ListenerInvoker> invokers, @Nonnull Object event);
}
