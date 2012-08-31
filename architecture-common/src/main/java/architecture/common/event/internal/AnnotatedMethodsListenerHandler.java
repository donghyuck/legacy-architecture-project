/*
 * Copyright 2012 Donghyuck, Son
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package architecture.common.event.internal;


import static com.google.common.base.Preconditions.checkNotNull;

import java.lang.reflect.Method;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import architecture.common.event.api.EventListener;
import architecture.common.event.spi.ListenerHandler;
import architecture.common.event.spi.ListenerInvoker;

import com.google.common.base.Function;
import com.google.common.collect.Lists;

/**
 * <p>A listener handler that will check for single parameter methods annotated with the given annotation.</p>
 * <p>The default annotation for methods is {@link architecture.common.event.api.EventListener}.</p>
 * @see com.atlassian.event.api.EventListener
 * @since 2.0
 */
public final class AnnotatedMethodsListenerHandler implements ListenerHandler
{
    private final Log log = LogFactory.getLog(this.getClass());

    private final Class annotation;

    public AnnotatedMethodsListenerHandler()
    {
        this(EventListener.class);
    }

    public AnnotatedMethodsListenerHandler(Class annotation)
    {
        this.annotation = checkNotNull(annotation);
    }

    public List<? extends ListenerInvoker> getInvokers(final Object listener)
    {
        final List<Method> validMethods = getValidMethods(checkNotNull(listener));

        if (validMethods.isEmpty())
        {
            log.debug( String.format("Couldn't find any valid listener methods on class <{}>",  listener.getClass().getName()));
        }

        return Lists.transform(validMethods, new Function<Method, ListenerInvoker>()
        {
            public ListenerInvoker apply(Method method)
            {
                return new SingleParameterMethodListenerInvoker(listener, method);
            }
        });
    }

    private List<Method> getValidMethods(Object listener)
    {
        final List<Method> annotatedMethods = Lists.newArrayList();
        for (Method method : listener.getClass().getMethods())
        {
            if (isValidMethod(method))
            {
                annotatedMethods.add(method);
            }
        }
        return annotatedMethods;
    }

    private boolean isValidMethod(Method method)
    {
        if (isAnnotated(method))
        {
            if (hasOneAndOnlyOneParameter(method))
            {
                return true;
            }
            else
            {
            	throw new RuntimeException((new StringBuilder()).append("Method <").append(method).append("> of class <").append(method.getDeclaringClass()).append("> ").append("is annotated with <").append(annotation.getClass().getName()).append("> but has 0 or more than 1 parameters! ").append("Listener methods MUST have 1 and only 1 parameter.").toString());
            }
        }else{
        	return false;
        }
    }

    @SuppressWarnings("unchecked")
	private boolean isAnnotated(Method method)
    {
        return method.getAnnotation(annotation) != null;
    }

    private boolean hasOneAndOnlyOneParameter(Method method)
    {
        return method.getParameterTypes().length == 1;
    }
}
