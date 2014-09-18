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

import java.util.List;

import architecture.common.event.config.ListenerHandlersConfiguration;
import architecture.common.event.spi.ListenerHandler;

import com.google.common.collect.Lists;

/**
 * <p>The default configuration that only uses the {@link architecture.common.event.internal.AnnotatedMethodsListenerHandler}.</p>
 * <p>Products that need to remain backward compatible will have to override this configuration</p>
 */
public class ListenerHandlerConfigurationImpl implements ListenerHandlersConfiguration
{
    public List<ListenerHandler> getListenerHandlers()
    {
        return Lists.<ListenerHandler>newArrayList(new AnnotatedMethodsListenerHandler());
    }
}
