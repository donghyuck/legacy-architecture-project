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
package architecture.ee.component.core.lifecycle;

import org.apache.commons.lang3.math.NumberUtils;

import architecture.common.event.api.EventPublisher;
import architecture.common.lifecycle.ApplicationProperties;
import architecture.common.lifecycle.event.ApplicationPropertyChangeEvent;
import architecture.ee.spring.jdbc.support.ExtendedJdbcDaoSupport;

/**
 * @author   donghyuck
 */
public abstract class AbstractJdbcApplicationProperties extends ExtendedJdbcDaoSupport implements ApplicationProperties {


	private EventPublisher eventPublisher = null;
		
	public boolean getBooleanProperty(String propertyKey) {
		return Boolean.valueOf(get(propertyKey)).booleanValue();
	}

	public boolean getBooleanProperty(String propertyKey, boolean defaultValue) {
		String value = get(propertyKey);
		if (value != null)
			return Boolean.valueOf(value).booleanValue();
		else
			return defaultValue;
	}

	public int getIntProperty(String property, int defaultValue) {
		return NumberUtils.toInt(get(property), defaultValue);
	}	

	public long getLongProperty(String property, long defaultValue) {
		return NumberUtils.toLong(get(property), defaultValue);
	}	
	
    protected void firePropertyChangeEvent(Object source, ApplicationPropertyChangeEvent.Type eventType, String propertyName, Object oldValue, Object newValue){
       if(eventPublisher != null){
           eventPublisher.publish(new ApplicationPropertyChangeEvent(source, eventType, propertyName, oldValue, newValue));    	
       }       
    }
    

    protected EventPublisher getEventPublisher(){
    	return eventPublisher;
    }

	public void setEventPublisher(EventPublisher eventPublisher) {
		this.eventPublisher = eventPublisher;		
	}

}
