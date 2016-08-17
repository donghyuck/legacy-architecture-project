/*
 * Copyright 2012, 2013 Donghyuck, Son
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
package architecture.ee.event;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.List;

import architecture.common.event.api.EventPublisher;
import architecture.common.event.config.ListenerHandlersConfiguration;
import architecture.common.event.spi.ListenerHandler;

public class EventListenerRegister {

    private final List<ListenerHandler> listenerHandlers;

    public EventListenerRegister(EventPublisher eventPublisher, ListenerHandlersConfiguration listenerHandlersConfiguration) {

	listenerHandlers = checkNotNull(checkNotNull(listenerHandlersConfiguration).getListenerHandlers());

    }

}
