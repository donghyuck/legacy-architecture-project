package architecture.common.event.internal;

import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.google.common.base.Function;
import com.google.common.base.Preconditions;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Iterables;
import com.google.common.collect.MapMaker;

import architecture.common.event.api.EventPublisher;
import architecture.common.event.config.ListenerHandlersConfiguration;
import architecture.common.event.spi.EventDispatcher;
import architecture.common.event.spi.ListenerHandler;
import architecture.common.event.spi.ListenerInvoker;
import architecture.common.util.ClassUtils;

/**
 * @author donghyuck
 */
public class LockFreeEventPublisher implements EventPublisher {

    private final InvokerBuilder invokerBuilder;

    private final Publisher publisher;

    private final Listeners listeners = new Listeners();

    static final class InvokerBuilder {

	private final Iterable<ListenerHandler> listenerHandlers;

	InvokerBuilder(Iterable<ListenerHandler> listenerHandlers) {
	    this.listenerHandlers = (Iterable<ListenerHandler>) Preconditions.checkNotNull(listenerHandlers);
	}

	Iterable<ListenerHandler> build(Object listener) throws IllegalArgumentException {

	    com.google.common.collect.ImmutableList.Builder<ListenerHandler> builder = ImmutableList.builder();

	    for (ListenerHandler listenerHandler : listenerHandlers) {
		builder.addAll((Iterable) listenerHandler.getInvokers(listener));
	    }

	    List<ListenerHandler> invokers = builder.build();
	    if (invokers.isEmpty())
		throw new IllegalArgumentException(
			(new StringBuilder()).append("No listener invokers were found for listener <").append(listener)
				.append(">").toString());
	    else
		return invokers;
	}

    }

    /**
     * @author donghyuck
     */
    static final class Publisher {
	private final Log log = LogFactory.getLog(getClass());

	private final Listeners listeners;

	private final EventDispatcher dispatcher;

	private final Function eventClassToInvokersTransformer = new Function() {
	    public Object apply(Object from) {
		return listeners.get(from.getClass());
	    }
	};

	Publisher(EventDispatcher dispatcher, Listeners listeners) {
	    this.dispatcher = (EventDispatcher) Preconditions.checkNotNull(dispatcher);
	    this.listeners = (Listeners) Preconditions.checkNotNull(listeners);
	}

	public void dispatch(Object event) {
	    for (ListenerInvoker invoker : getInvokers(event)) {
		try {
		    dispatcher.dispatch(invoker, event);
		} catch (Throwable t) {
		    log.error((new StringBuilder()).append("There was an exception thrown trying to dispatch event '")
			    .append(event).append("' from the invoker '").append(invoker).append("'.").toString(), t);
		}
	    }
	}

	Iterable<ListenerInvoker> getInvokers(Object event) {
	    Set allEventTypes = ClassUtils.findAllTypes(event.getClass());
	    return ImmutableSet
		    .copyOf(Iterables.concat(Iterables.transform(allEventTypes, eventClassToInvokersTransformer)));
	}

    }

    static final class Invokers {

	private final ConcurrentMap listeners = (new MapMaker()).weakKeys().makeMap();

	Invokers() {
	}

	Iterable all() {
	    return listeners.values();
	}

	public void remove(Object key) {
	    listeners.remove(key);
	}

	public void add(Object key, ListenerInvoker invoker) {
	    listeners.put(key, invoker);
	}

    }

    static final class Listeners {
	private final ConcurrentMap invokers = null;/*
						     * CacheBuilder.newBuilder()
						     * .build( new CacheLoader()
						     * { public Object
						     * load(Object from) throws
						     * Exception { return new
						     * Invokers(); } });
						     */

	/**
	 * (new MapMaker()).makeComputingMap( new Function() { public Object
	 * apply(Object from) { return new Invokers(); }});
	 **/

	Listeners() {
	}

	void register(Object listener, Iterable invokers) {
	    ListenerInvoker invoker;
	    for (Iterator iter = invokers.iterator(); iter.hasNext(); register(listener, invoker))
		invoker = (ListenerInvoker) iter.next();

	}

	private void register(Object listener, ListenerInvoker invoker) {
	    if (invoker.getSupportedEventTypes().isEmpty()) {
		((Invokers) invokers.get(Object.class)).add(listener, invoker);
	    } else {
		Class eventClass;
		for (Iterator iter = invoker.getSupportedEventTypes().iterator(); iter
			.hasNext(); ((Invokers) invokers.get(eventClass)).add(listener, invoker))
		    eventClass = (Class) iter.next();

	    }
	}

	void remove(Object listener) {
	    Invokers entry;
	    for (Iterator iter = invokers.values().iterator(); iter.hasNext(); entry.remove(listener))
		entry = (Invokers) iter.next();

	}

	void clear() {
	    invokers.clear();
	}

	public Iterable get(Class eventClass) {
	    return ((Invokers) invokers.get(eventClass)).all();
	}

    }

    public LockFreeEventPublisher(EventDispatcher eventDispatcher,
	    ListenerHandlersConfiguration listenerHandlersConfiguration) {
	invokerBuilder = new InvokerBuilder(
		((ListenerHandlersConfiguration) Preconditions.checkNotNull(listenerHandlersConfiguration))
			.getListenerHandlers());
	publisher = new Publisher(eventDispatcher, listeners);
    }

    public void publish(Object event) {
	Preconditions.checkNotNull(event);
	publisher.dispatch(event);
    }

    public void register(Object listener) {
	Preconditions.checkNotNull(listener);
	listeners.register(listener, invokerBuilder.build(listener));
    }

    public void unregister(Object listener) {
	Preconditions.checkNotNull(listener);
	listeners.remove(listener);
    }

    public void unregisterAll() {
	listeners.clear();
    }
}
