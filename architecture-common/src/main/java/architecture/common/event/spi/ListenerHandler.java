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

import java.util.List;

/**
 * Interface to find invokers for a given listener objects. A typical example
 * might be listeners that implement a specific interface or that have annotated
 * listener methods.
 * 
 * @since 2.0
 */
public interface ListenerHandler {
    /**
     * Retrieves the list of invokers for the given listener.
     * 
     * @param listener
     *            the listener object to get invokers for
     * @return a list of invokers linked to the listener object.
     */
    public abstract List<? extends ListenerInvoker> getInvokers(Object listener);
}
