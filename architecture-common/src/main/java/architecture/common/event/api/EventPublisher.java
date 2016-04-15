package architecture.common.event.api;

/**
 * Interface to publish events. It allows the decoupling of listeners which handle events and
 * publishers which dispatch events.
 *
 * @see EventListener annotation which can be used to indicate event listener methods
 * @since 2.0
 */
public interface EventPublisher extends EventListenerRegistrar {
    /**
     * Publish an event that will be consumed by all listeners which have registered to receive it.
     * Implementations must dispatch events to listeners which have a public method annotated with
     * {@link EventListener} and one argument which is assignable from the event type (i.e. a superclass
     * or interface implemented by the event object). Implementations may also dispatch events
     * to legacy {@link architecture.common.event.EventListener} implementations based on the types returned
     * from {@link architecture.common.event.EventListener#getHandledEventClasses()}.
     *
     * This method should process all event listeners, despite any errors or exceptions that are generated
     * as a result of dispatching the event.
     *
     * @param event the event to publish
     * @throws NullPointerException if the event is {@code null}
     */
    void publish(Object event);
}