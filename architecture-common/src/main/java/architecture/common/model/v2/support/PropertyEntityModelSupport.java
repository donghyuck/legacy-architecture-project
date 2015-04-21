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
package architecture.common.model.v2.support;

import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import architecture.common.model.v2.DateModel;
import architecture.common.model.v2.PropertyModel;
import architecture.common.util.StringUtils;


@SuppressWarnings("serial")
public abstract class PropertyEntityModelSupport<T> extends EntityModelSupport<T> implements PropertyModel, DateModel {

	private Map<String, String> properties = null;
	
	private Date creationDate = null;
	
	private Date modifiedDate = null;
	

	public Date getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	public Date getModifiedDate() {
		return modifiedDate;
	}

	public void setModifiedDate(Date modifiedDate) {
		this.modifiedDate = modifiedDate;
	}
	
	/**
	 * @param properties
	 */
	public void setProperties(Map<String, String> properties) {
		this.properties = properties;
	}

	public Map<String, String> getProperties() {
		synchronized (this) {
			if (properties == null) {
				properties = new ConcurrentHashMap<String, String>();
			}
		}
		return properties;
	}

	public boolean getBooleanProperty(String name, boolean defaultValue) {
		String value = getProperties().get(name);		
		String valueToUse = StringUtils.defaultString(value, Boolean.toString(defaultValue));
		return Boolean.parseBoolean(valueToUse);
	}

	public long getLongProperty(String name, long defaultValue) {
		String value = getProperties().get(name);		
		String valueToUse = StringUtils.defaultString(value, Long.toString(defaultValue));
		return Long.parseLong(valueToUse);
	}

	public int getIntProperty(String name, int defaultValue) {
		String value = getProperties().get(name);		
		String valueToUse = StringUtils.defaultString(value, Integer.toString(defaultValue));
		return Integer.parseInt(valueToUse);
	}

	public String getProperty(String name, String defaultString) {
		String value = getProperties().get(name);
		return StringUtils.defaultString(value, defaultString);
	}
	
}
