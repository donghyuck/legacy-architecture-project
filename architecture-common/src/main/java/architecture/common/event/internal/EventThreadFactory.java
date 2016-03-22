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

import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

/**
 * <p>
 * A thread factory that will name the threads
 * <strong>Event::[thread_name]</strong>.
 * </p>
 * <p>
 * If you need your own {@link java.util.concurrent.ThreadFactory} we recommend
 * delegating the Thread creation to this implementation.
 * </p>
 */
public final class EventThreadFactory implements ThreadFactory {
    private final ThreadFactory delegateThreadFactory;

    public EventThreadFactory() {
	this(Executors.defaultThreadFactory());
    }

    public EventThreadFactory(ThreadFactory delegateThreadFactory) {
	this.delegateThreadFactory = checkNotNull(delegateThreadFactory);
    }

    public Thread newThread(Runnable r) {
	final Thread thread = delegateThreadFactory.newThread(r);
	thread.setName("Event::" + thread.getName());
	return thread;
    }
}
