/*
 * Copyright 2016 donghyuck
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

package architecture.common.event.spring;

import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;

import architecture.common.event.api.EventPublisher;
import architecture.common.event.api.EventSource;

public class EventListenerSupport extends EventSourceSupport implements EventSource, InitializingBean, DisposableBean {

    private EventPublisher eventPublisher;

    public void setEventPublisher(EventPublisher eventPublisher) {
	this.eventPublisher = eventPublisher;
    }
    public void destroy() throws Exception {
	if(eventPublisher!=null)
	    eventPublisher.unregister(this);	
    }

    public void afterPropertiesSet() throws Exception {
	if(eventPublisher!=null)
	    eventPublisher.register(this);	
    }

}
