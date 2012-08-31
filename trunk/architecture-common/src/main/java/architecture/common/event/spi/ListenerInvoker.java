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
package architecture.common.event.spi;

import java.util.Set;

/**
 * <p>
 * Implementation of this interface know how to invoke 'given types' of
 * listeners so that they handle given events.
 * </p>
 * <p>
 * <strong>Note:</strong> Implementations <strong>MUST</strong> provide correct
 * implementations of the {@link #equals(Object)} and {@link #hashCode()}
 * method.
 * </p>
 * 
 * @since 2.0
 */
public interface ListenerInvoker {
	/**
	 * The types of events supported by this invoker. I.e.
	 * {@link #invoke(Object)} can be safely called with any object that is an
	 * instance of at least one of those types.
	 * 
	 * @return the set of supported event types.
	 */
	public abstract Set<Class<?>> getSupportedEventTypes();

	/**
	 * Invokes the underlying listener for the given event.
	 * 
	 * @param event
	 *            the event to tell the listener about.
	 * @throws IllegalArgumentException
	 *             if the event is not an instance of any of the types returned
	 *             by {@link #getSupportedEventTypes()}
	 */
	public abstract void invoke(Object event);

	/**
	 * Whether or not the underlying listener can handle asynchronous event.
	 * 
	 * @return {@code true} if the underlying listener can handle asynchronous
	 *         events, {@code false} otherwise
	 */
	public abstract boolean supportAsynchronousEvents();
}
