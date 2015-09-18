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

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Set;

import com.google.common.collect.Sets;

import architecture.common.event.spi.ListenerInvoker;

/**
 * A listener invoker that knows how to call a given single parameter method on
 * a given object.
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
			if (e.getCause() == null)
				throw new RuntimeException(e);
			if (e.getCause().getMessage() == null)
				throw new RuntimeException(e.getCause());
			else
				throw new RuntimeException(e.getCause().getMessage(),
						e.getCause());
		}
	}

	public boolean supportAsynchronousEvents() {
		return true;
	}
}
