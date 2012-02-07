/*
 * Copyright 2010, 2011 INKIUM, Inc.
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
package architecture.common.lifecycle.event;

/**
 * @author    donghyuck
 */
public class ApplicationPropertyChangeEvent extends PropertyChangeEvent {
       
    /**
	 * @author               donghyuck
	 */
    public enum Type {
        ADDED, 
        REMOVED, 
        MODIFIED,
    }

	/**
	 * 
	 */
	private static final long serialVersionUID = -128656485808459081L;
    
    /**
	 * @uml.property  name="eventType"
	 * @uml.associationEnd  
	 */
    private Type eventType;
    
	public ApplicationPropertyChangeEvent(Object source, Type eventType, String propertyName, Object oldValue, Object newValue) {
		super(source, propertyName, oldValue, newValue);
		this.eventType = eventType;
	}

	/**
	 * @return
	 * @uml.property  name="eventType"
	 */
	public Type getEventType() {
		return eventType;
	}

}