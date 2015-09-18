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

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static org.apache.commons.lang3.ObjectUtils.identityToString;
import static org.apache.commons.lang3.StringUtils.isNotEmpty;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.google.common.base.Supplier;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;
import com.google.common.collect.Multimaps;
import com.google.common.collect.Sets;

import architecture.common.event.api.EventPublisher;
import architecture.common.event.config.ListenerHandlersConfiguration;
import architecture.common.event.spi.EventDispatcher;
import architecture.common.event.spi.ListenerHandler;
import architecture.common.event.spi.ListenerInvoker;
import architecture.common.util.ClassUtils;

/**
 * <p>
 * The default implementation of the
 * {@link architecture.ee.event.api.EventPublisher} interface.
 * </p>
 * <p>
 * <p>
 * One can customise the event listening by instantiating with custom
 * {@link architecture.ee.event.spi.ListenerHandler listener handlers} and the
 * event dispatching through {@link architecture.ee.event.spi.EventDispatcher} .
 * See the {@link architecture.ee.event.spi} package for more information.
 * </p>
 * 
 * @see architecture.ee.event.spi.ListenerHandler
 * @see architecture.ee.event.spi.EventDispatcher
 * @since 2.0
 */
public final class EventPublisherImpl implements EventPublisher {

	private final Log log = LogFactory.getLog(getClass());
	private final EventDispatcher eventDispatcher;
	private final List<ListenerHandler> listenerHandlers;

	/**
	 * <strong>Note:</strong> this field makes this implementation stateful
	 */
	private final Multimap<Class<?>, KeyedListenerInvoker> listenerInvokers = newMultimap();

	/**
	 * <p>
	 * If you need to customise the asynchronous handling, you should use the
	 * {@link com.atlassian.event.internal.AsynchronousAbleEventDispatcher}
	 * together with a custom executor. You might also want to have a look at
	 * using the {@link com.atlassian.event.internal.EventThreadFactory} to keep
	 * the naming of event threads consistent with the default naming of the
	 * Atlassian Event library.
	 * <p>
	 * 
	 * @param eventDispatcher
	 *            the event dispatcher to be used with the publisher
	 * @param listenerHandlersConfiguration
	 *            the list of listener handlers to be used with this publisher
	 * @see architecture.ee.event.internal.AsynchronousAbleEventDispatcher
	 * @see architecture.ee.event.internal.EventThreadFactory
	 */
	public EventPublisherImpl(EventDispatcher eventDispatcher, ListenerHandlersConfiguration listenerHandlersConfiguration) {
		this.eventDispatcher = checkNotNull(eventDispatcher);
		this.listenerHandlers = checkNotNull(checkNotNull(listenerHandlersConfiguration).getListenerHandlers());
	}

	public void publish(Object event) {
		invokeListeners(findListenerInvokersForEvent(checkNotNull(event)), event);
	}

	public void register(Object listener) {
		registerListener(identityToString(checkNotNull(listener)), listener);
	}

	public void unregister(Object listener) {
		unregisterListener(identityToString(checkNotNull(listener)));
	}

	public void unregisterAll() {
		synchronized (listenerInvokers) {
			listenerInvokers.clear();
		}
	}

	private void unregisterListener(String listenerKey) {
		checkArgument(isNotEmpty(listenerKey),
				"Key for the listener must not be empty");

		/**
		 * see {@link Multimaps#synchronizedMultimap(Multimap)} for why this
		 * synchronize block is there
		 */
		synchronized (listenerInvokers) {
			for (Iterator<Map.Entry<Class<?>, KeyedListenerInvoker>> invokerIterator = listenerInvokers
					.entries().iterator(); invokerIterator.hasNext();) {
				if (invokerIterator.next().getValue().getKey()
						.equals(listenerKey)) {
					invokerIterator.remove();
				}
			}
		}
	}

	private void registerListener(String listenerKey, Object listener) {
		synchronized (listenerInvokers) /*
										 * Because we need to un-register an
										 * re-register in one 'atomic' operation
										 */
		{
			unregisterListener(listenerKey);

			final List<ListenerInvoker> invokers = Lists.newArrayList();
			for (ListenerHandler listenerHandler : listenerHandlers) {
				invokers.addAll(listenerHandler.getInvokers(listener));
			}
			if (!invokers.isEmpty()) {
				registerListenerInvokers(listenerKey, invokers);
			} else {
				throw new IllegalArgumentException(
						"No listener invokers were found for listener <"
								+ listener + ">");
			}
		}
	}

	private Set<KeyedListenerInvoker> findListenerInvokersForEvent(Object event) {
		
		final Set<KeyedListenerInvoker> invokersForEvent = Sets.newHashSet();

		for (Class<?> eventClass : ClassUtils.findAllTypes(checkNotNull(event).getClass())) {
			invokersForEvent.addAll(listenerInvokers.get(eventClass));
		}
		return invokersForEvent;
	}

	private void invokeListeners(
			Collection<KeyedListenerInvoker> listenerInvokers, Object event) {
		
		log.debug("publish [" + event + "]" );
		
		for (KeyedListenerInvoker keyedInvoker : listenerInvokers) {
			eventDispatcher.dispatch(keyedInvoker.getInvoker(), event);
		}
	}

	private void registerListenerInvokers(String listenerKey,
			List<? extends ListenerInvoker> invokers) {
		for (ListenerInvoker invoker : invokers) {
			registerListenerInvoker(listenerKey, invoker);
		}
	}

	private void registerListenerInvoker(String listenerKey,
			ListenerInvoker invoker) {
		// if supported classes is empty, then all events are supported.
		if (invoker.getSupportedEventTypes().isEmpty()) {
			listenerInvokers.put(Object.class, new KeyedListenerInvoker(
					listenerKey, invoker));
		}

		// if it it empty, we won't loop, otherwise register the invoker against
		// all its classes
		for (Class<?> eventClass : invoker.getSupportedEventTypes()) {
			listenerInvokers.put(eventClass, new KeyedListenerInvoker(
					listenerKey, invoker));
		}
	}

	private Multimap<Class<?>, KeyedListenerInvoker> newMultimap() {
		return Multimaps.synchronizedMultimap(Multimaps.newMultimap(
				Maps.<Class<?>, Collection<KeyedListenerInvoker>> newHashMap(),
				new Supplier<Collection<KeyedListenerInvoker>>() {
					public Collection<KeyedListenerInvoker> get() {
						return Sets.newHashSet();
					}
				}));
	}

	/**
	 * @author donghyuck
	 */
	private static final class KeyedListenerInvoker {

		/**
		 * @uml.property name="key"
		 */
		private final String key;

		private final ListenerInvoker invoker;

		KeyedListenerInvoker(String key, ListenerInvoker invoker) {
			this.invoker = invoker;
			this.key = key;
		}

		String getKey() {
			return key;
		}

		ListenerInvoker getInvoker() {
			return invoker;
		}

		@Override
		public int hashCode() {
			return new HashCodeBuilder(5, 23).append(key).append(invoker)
					.toHashCode();
		}

		@Override
		public boolean equals(Object obj) {
			if (obj == this) {
				return true;
			}
			if (obj == null || obj.getClass() != getClass()) {
				return false;
			}
			final KeyedListenerInvoker kli = (KeyedListenerInvoker) obj;
			return new EqualsBuilder().append(key, kli.key)
					.append(invoker, kli.invoker).isEquals();
		}
	}

}