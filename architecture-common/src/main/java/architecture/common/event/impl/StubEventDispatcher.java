package architecture.common.event.impl;

import architecture.common.event.spi.EventDispatcher;
import architecture.common.event.spi.ListenerInvoker;

/**
 * A stub event dispatcher that simply calls the invoker.
 */
final class StubEventDispatcher implements EventDispatcher {
    public void dispatch(ListenerInvoker invoker, Object event) {
        invoker.invoke(event);
    }
}
