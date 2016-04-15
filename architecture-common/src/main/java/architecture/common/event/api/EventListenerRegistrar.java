package architecture.common.event.api;

public interface EventListenerRegistrar {
    /**
     * Register a listener to receive events. All implementations must support registration of listeners
     * where event handling methods are indicated by the {@link architecture.common.event.api.EventListener} annotation. Legacy
     * implementations may also support listeners which implement the now-deprecated
     * {@link architecture.common.event.EventListener} interface.
     *
     * @param listener The listener that is being registered
     * @throws NullPointerException     if the listener is {@code null}
     * @throws IllegalArgumentException if the parameter is not found to be an actual listener
     * @see architecture.common.event.api.EventListener annotation which can be used to indicate event listener methods
     */
    void register(Object listener);

    /**
     * Un-register a listener so that it will no longer receive events. If the given listener is not registered nothing
     * will happen.
     *
     * @param listener The listener to un-register
     * @throws NullPointerException if the listener is {@code null}
     */
    void unregister(Object listener);

    /**
     * Un-register all listeners that this registrar knows about.
     */
    void unregisterAll();
}
