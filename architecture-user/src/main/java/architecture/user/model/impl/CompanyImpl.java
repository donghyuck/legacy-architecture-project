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
package architecture.user.model.impl;

import java.io.Serializable;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import architecture.common.cache.CacheSizes;
import architecture.common.model.v2.BaseModelObject;
import architecture.common.user.Company;

public class CompanyImpl  extends BaseModelObject implements Company {

	private Long companyId;
	private String displayName;
	
	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	private Map<String, String> properties;

	public long getCompanyId() {
		return companyId;
	}

	public Serializable getPrimaryKeyObject() {
		return companyId;
	}

	public int getModelObjectType() {
		return 12;
	}

	
	public void setCompanyId(long companyId) {
		this.companyId = companyId;
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

	public int getCachedSize() {
		return CacheSizes.sizeOfLong() + CacheSizes.sizeOfString(getName())  +  CacheSizes.sizeOfString(getDisplayName()) + CacheSizes.sizeOfString( getDescription() ) + CacheSizes.sizeOfDate() + CacheSizes.sizeOfDate() ;
	}

	
}
