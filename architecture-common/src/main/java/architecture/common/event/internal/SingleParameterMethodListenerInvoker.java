package architecture.common.event.internal;

import static com.google.common.base.Preconditions.checkNotNull;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Set;

import architecture.common.event.spi.ListenerInvoker;

import com.google.common.collect.Sets;

/**
 * A listener invoker that knows how to call a given single parameter method on a given object.
 * @since 2.0
 */
final class SingleParameterMethodListenerInvoker implements ListenerInvoker
{
    private final Method method;
    private final Object listener;

    public SingleParameterMethodListenerInvoker(Object listener, Method method)
    {
        this.listener = checkNotNull(listener);
        this.method = checkNotNull(method);
    }

    public Set<Class<?>> getSupportedEventTypes()
    {
        return Sets.newHashSet(method.getParameterTypes());
    }

    public void invoke(Object event)
    {
        try
        {
            method.invoke(listener, event);
        }
        catch (IllegalAccessException e)
        {
            throw new RuntimeException(e);
        }
        catch (InvocationTargetException e)
        {
            throw new RuntimeException(e);
        }
    }

    public boolean supportAsynchronousEvents()
    {
        return true;
    }
}
