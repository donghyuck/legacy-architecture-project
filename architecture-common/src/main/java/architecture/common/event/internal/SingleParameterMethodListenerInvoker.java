package architecture.common.event.internal;

import com.google.common.collect.Sets;

import architecture.common.event.spi.ListenerInvoker;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Set;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * A listener invoker that knows how to call a given single parameter method on a given object.
 *
 * @since 2.0
 */
final class SingleParameterMethodListenerInvoker implements ListenerInvoker {
    private final Method method;
    private final Object listener;

    public SingleParameterMethodListenerInvoker(Object listener, Method method) {
        this.listener = checkNotNull(listener);
        this.method = checkNotNull(method);
    }

    public Set<Class<?>> getSupportedEventTypes() {
        return Sets.newHashSet(method.getParameterTypes());
    }

    public void invoke(Object event) {
        try {
            method.invoke(listener, event);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (InvocationTargetException e) {
            if (e.getCause() == null) {
                throw new RuntimeException(e);
            } else if (e.getCause().getMessage() == null) {
                throw new RuntimeException(e.getCause());
            } else {
                throw new RuntimeException(e.getCause().getMessage(), e.getCause());
            }
        }
    }

    public boolean supportAsynchronousEvents() {
        return true;
    }

    @Override
    public String toString() {
        return "SingleParameterMethodListenerInvoker{method=" + method + ", listener=" + paranoidToString(listener) + '}';
    }

    /**
     * Calls the object's toString() method. If an exception is thrown from the object's toString() method then this
     * method falls back to the "identity" toString() (as per JVM defaults).
     *
     * @param object an Object
     * @return a human-readable String representation of the object
     */
    private String paranoidToString(Object object) {
        try {
            return String.valueOf(object);
        } catch (RuntimeException e) {
            return object.getClass().getName() + '@' + Integer.toHexString(System.identityHashCode(object));
        }
    }
}
