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

/**
 * Dispatches an event to its listener (through the invoker). Implementations
 * can choose for example whether to dispatch events asynchronously.
 * 
 * @since 2.0
 */
public interface EventDispatcher {
	/**
	 * Dispatches the event using the invoker.
	 * 
	 * @param invoker
	 *            the invoker to use to dispatch the event
	 * @param event
	 *            the event to dispatch
	 * @throws NullPointerException
	 *             if either the {@code invoker} or the {@code event} is {@code
	 *             null}
	 */
	public abstract void dispatch(ListenerInvoker invoker, Object event);
}
