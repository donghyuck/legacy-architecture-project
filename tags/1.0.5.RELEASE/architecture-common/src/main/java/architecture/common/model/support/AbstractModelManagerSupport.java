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
package architecture.common.model.support;

import net.sf.ehcache.Cache;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import architecture.common.event.api.EventPublisher;
import architecture.common.model.ModelObject;


public abstract class AbstractModelManagerSupport {
	
	protected Log log = LogFactory.getLog(getClass());
	
	protected EventPublisher eventPublisher;
	protected Cache objectCache;
    protected Cache objectIdCache ;
	/**
	 * @return eventPublisher
	 */
	public EventPublisher getEventPublisher() {
		return eventPublisher;
	}
	/**
	 * @param eventPublisher 설정할 eventPublisher
	 */
	public void setEventPublisher(EventPublisher eventPublisher) {
		this.eventPublisher = eventPublisher;
	}
	/**
	 * @return objectCache
	 */
	public Cache getObjectCache() {
		return objectCache;
	}
	/**
	 * @param objectCache 설정할 objectCache
	 */
	public void setObjectCache(Cache objectCache) {
		this.objectCache = objectCache;
	}
	/**
	 * @return objectIdCache
	 */
	public Cache getObjectIdCache() {
		return objectIdCache;
	}
	/**
	 * @param objectIdCache 설정할 objectIdCache
	 */
	public void setObjectIdCache(Cache objectIdCache) {
		this.objectIdCache = objectIdCache;
	}
    
    protected void objectNameUpdated(String oldGroupName){
    	objectIdCache.remove(oldGroupName);	
    }

    
	protected void clearObjectFromCache(ModelObject object){
		objectCache.remove(object.getPrimaryKeyObject());
	}

}
