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

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

import com.google.common.base.Preconditions;

import architecture.common.event.config.EventThreadPoolConfiguration;
import architecture.common.event.spi.EventExecutorFactory;

/**
 * @author donghyuck
 */
public abstract class AbstractEventExecutorFactory implements EventExecutorFactory {

    private final EventThreadPoolConfiguration configuration;

    private final EventThreadFactory eventThreadFactory;

    public AbstractEventExecutorFactory(EventThreadPoolConfiguration configuration,
	    EventThreadFactory eventThreadFactory) {
	this.configuration = (EventThreadPoolConfiguration) Preconditions.checkNotNull(configuration);
	this.eventThreadFactory = (EventThreadFactory) Preconditions.checkNotNull(eventThreadFactory);
    }

    public AbstractEventExecutorFactory(EventThreadPoolConfiguration configuration) {
	this(configuration, new EventThreadFactory());
    }

    protected abstract BlockingQueue getQueue();

    public Executor getExecutor() {
	return new ThreadPoolExecutor(configuration.getCorePoolSize(), configuration.getMaximumPoolSize(),
		configuration.getKeepAliveTime(), configuration.getTimeUnit(), getQueue(), eventThreadFactory);
    }

}
