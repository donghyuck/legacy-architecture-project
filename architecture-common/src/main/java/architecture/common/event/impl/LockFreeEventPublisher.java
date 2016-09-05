package architecture.common.event.impl;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.collect.Iterables.concat;
import static com.google.common.collect.Iterables.transform;

import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentMap;

import javax.annotation.Nonnull;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Function;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.MapMaker;

import architecture.common.event.api.EventPublisher;
import architecture.common.event.config.ListenerHandlersConfiguration;
import architecture.common.event.spi.EventDispatcher;
import architecture.common.event.spi.ListenerHandler;
import architecture.common.event.spi.ListenerInvoker;



/**
 * A non-blocking implementation of the {@link architecture.common.event.api.EventPublisher} interface.
 * <p>
 * This class is a drop-in replacement for {@link EventPublisherImpl} except that it does not
 * synchronise on the internal map of event type to {@link ListenerInvoker}, and should handle
 * much higher parallelism of event dispatch.
 * <p>
 * One can customise the event listening by instantiating with custom
 * {@link architecture.common.event.spi.ListenerHandler listener handlers} and the event dispatching through
 * {@link architecture.common.event.spi.EventDispatcher}. See the {@link architecture.common.event.spi} package
 * for more information.
 *
 * @see architecture.common.event.spi.ListenerHandler
 * @see architecture.common.event.spi.EventDispatcher
 * @since 2.0.2
 */
public final class LockFreeEventPublisher implements EventPublisher {
    /**
     * Gets the {@link ListenerInvoker invokers} for a listener
     */
    private final InvokerBuilder invokerBuilder;

    /**
     * Publishes an event.
     */
    private final Publisher publisher;

    /**
     * <strong>Note:</strong> this field makes this implementation stateful
     */
    private final Listeners listeners = new Listeners();

    public LockFreeEventPublisher(final EventDispatcher eventDispatcher, final ListenerHandlersConfiguration listenerHandlersConfiguration) {
        this(eventDispatcher, listenerHandlersConfiguration, new InvokerTransformer() {
            @Nonnull
            @Override
            public Iterable<ListenerInvoker> transformAll(@Nonnull Iterable<ListenerInvoker> invokers, @Nonnull Object event) {
                return invokers;
            }
        });
    }

    /**
     * If you need to customise the asynchronous handling, you should use the
     * {@link architecture.common.event.impl.AsynchronousAbleEventDispatcher}
     * together with a custom executor.
     * <p>
     * You might also want to have a look at using the
     * {@link architecture.common.event.impl.EventThreadFactory} to keep the naming
     * of event threads consistent with the default naming of the Atlassian Event
     * library.
     *
     * @param eventDispatcher               the event dispatcher to be used with the publisher
     * @param listenerHandlersConfiguration the list of listener handlers to be used with this publisher
     * @param transformer                   the batcher for batching up listener invocations
     * @see architecture.common.event.impl.AsynchronousAbleEventDispatcher
     * @see architecture.common.event.impl.EventThreadFactory
     */

    public LockFreeEventPublisher(final EventDispatcher eventDispatcher,
                                  final ListenerHandlersConfiguration listenerHandlersConfiguration,
                                  final InvokerTransformer transformer) {
        invokerBuilder = new InvokerBuilder(checkNotNull(listenerHandlersConfiguration).getListenerHandlers());
        publisher = new Publisher(eventDispatcher, listeners, transformer);
    }

    public void publish(final @Nonnull Object event) {
        checkNotNull(event);
        publisher.dispatch(event);
    }

    public void register(final @Nonnull Object listener) {
        checkNotNull(listener);
        listeners.register(listener, invokerBuilder.build(listener));
    }

    public void unregister(final @Nonnull Object listener) {
        checkNotNull(listener);
        listeners.remove(listener);
    }

    public void unregisterAll() {
        listeners.clear();
    }

    //
    // inner classes
    //

    /**
     * Maps classes to the relevant {@link Invokers}
     */
    static final class Listeners {
        /**
         * We always want an {@link Invokers} created for any class requested, even if it is empty.
         * <b>Warning:</b> We need to use weakKeys here to ensure plugin event classes are GC'd, otherwise we leak...
         */
        private final LoadingCache<Class<?>, Invokers> invokers = CacheBuilder.newBuilder().build(new CacheLoader<Class<?>, Invokers>() {
            @Override
            public Invokers load(final Class<?> key) throws Exception {
                return new Invokers();
            }
        });

        void register(final Object listener, final Iterable<ListenerInvoker> invokers) {
            for (final ListenerInvoker invoker : invokers) {
                register(listener, invoker);
            }
        }

        private void register(final Object listener, final ListenerInvoker invoker) {
            // if supported classes is empty, then all events are supported.
            if (invoker.getSupportedEventTypes().isEmpty()) {
                invokers.getUnchecked(Object.class).add(listener, invoker);
            } else {
                // if it it empty, we won't loop, otherwise register the invoker against all its classes
                for (final Class<?> eventClass : invoker.getSupportedEventTypes()) {
                    invokers.getUnchecked(eventClass).add(listener, invoker);
                }
            }
        }

        void remove(final Object listener) {
            for (final Invokers entry : ImmutableList.copyOf(invokers.asMap().values())) {
                entry.remove(listener);
            }
        }

        void clear() {
            invokers.invalidateAll();
        }

        public Iterable<ListenerInvoker> get(final Class<?> eventClass) {
            return invokers.getUnchecked(eventClass).all();
        }
    }

    /**
     * map of Key to Set of ListenerInvoker
     */
    static final class Invokers {
        private final ConcurrentMap<Object, ListenerInvoker> listeners = new MapMaker().weakKeys().makeMap();

        Iterable<ListenerInvoker> all() {
            return listeners.values();
        }

        public void remove(final Object key) {
            listeners.remove(key);
        }

        public void add(final Object key, final ListenerInvoker invoker) {
            listeners.put(key, invoker);
        }
    }

    /**
     * Responsible for publishing an event.
     * <p>
     * Must first get the Set of all ListenerInvokers that
     * are registered for that event and then use the
     * {@link EventDispatcher} to send the event to them.
     */
    static final class Publisher {
        private final Logger log = LoggerFactory.getLogger(this.getClass());
        private final Listeners listeners;
        private final EventDispatcher dispatcher;
        private final InvokerTransformer transformer;

        /**
         * transform an event class into the relevant invokers
         */
        @SuppressWarnings("unchecked")
        private final Function<Class, Iterable<ListenerInvoker>> eventClassToInvokersTransformer = new Function<Class, Iterable<ListenerInvoker>>() {
            public Iterable<ListenerInvoker> apply(final Class eventClass) {
                return listeners.get(eventClass);
            }
        };

        Publisher(final EventDispatcher dispatcher, final Listeners listeners, final InvokerTransformer transformer) {
            this.dispatcher = checkNotNull(dispatcher);
            this.listeners = checkNotNull(listeners);
            this.transformer = checkNotNull(transformer);
        }

        public void dispatch(final Object event) {
            Iterable<ListenerInvoker> invokers = getInvokers(event);
            try {
                invokers = transformer.transformAll(invokers, event);
            } catch (Exception e) {
                log.error("Exception while transforming invokers. Dispatching original invokers instead.", e);
            }
            for (final ListenerInvoker invoker : invokers) {
                // EVENT-14 -  we should continue to process all listeners even if one throws some horrible exception
                try {
                    dispatcher.dispatch(invoker, event);
                } catch (Exception e) {
                    log.error("There was an exception thrown trying to dispatch event '" + event +
                            "' from the invoker '" + invoker + "'.", e);
                }
            }
        }

        /**
         * Get all classes and interfaces an object extends or implements and then find all ListenerInvokers that apply
         *
         * @param event to find its classes/interfaces
         * @return an iterable of the invokers for those classes.
         */
        Iterable<ListenerInvoker> getInvokers(final Object event) {
            final Set<Class<?>> allEventTypes = ClassUtils.findAllTypes(event.getClass());
            return ImmutableSet.copyOf(concat(transform(allEventTypes, eventClassToInvokersTransformer)));
        }
    }

    /**
     * Holds all configured {@link ListenerHandler handlers}
     */
    static final class InvokerBuilder {
        private final Iterable<ListenerHandler> listenerHandlers;

        InvokerBuilder(final @Nonnull Iterable<ListenerHandler> listenerHandlers) {
            this.listenerHandlers = checkNotNull(listenerHandlers);
        }

        Iterable<ListenerInvoker> build(final Object listener) throws IllegalArgumentException {
            final ImmutableList.Builder<ListenerInvoker> builder = ImmutableList.builder();
            for (final ListenerHandler listenerHandler : listenerHandlers) {
                builder.addAll(listenerHandler.getInvokers(listener));
            }
            final List<ListenerInvoker> invokers = builder.build();
            if (invokers.isEmpty()) {
                throw new IllegalArgumentException("No listener invokers were found for listener <" + listener + ">");
            }
            return invokers;
        }
    }
}