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
package architecture.ee.web.navigator;

import java.io.Serializable;

import architecture.common.cache.CacheSizes;
import architecture.common.model.factory.ModelTypeFactory;
import architecture.common.model.support.EntityModelObjectSupport;

public class DefaultMenu  extends EntityModelObjectSupport implements Menu {
	
	private long menuId = -1L;
	private String location ;
	private String title;
	private boolean enabled = false;
	private String menuData;
	
	public DefaultMenu() {
		super();
	}
	
	/**
	 * @param menuId
	 */
	public DefaultMenu(long menuId) {
		super();
		this.menuId = menuId;
	}



	public long getMenuId() {
		return menuId;
	}
	
	public void setMenuId(long menuId) {
		this.menuId = menuId;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public Serializable getPrimaryKeyObject() {
		return menuId;
	}

	public int getModelObjectType() {
		return ModelTypeFactory.getTypeIdFromCode("MENU");
	}

	public String getLocation() {
		return location;
	}

	public void setLoaction(String location) {
		this.location = location;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (menuId ^ (menuId >>> 32));
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DefaultMenu other = (DefaultMenu) obj;
		if (menuId != other.menuId)
			return false;
		return true;
	}

	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Menu [menuId=");
		builder.append(menuId);
		builder.append(", ");
		if (location != null) {
			builder.append("location=");
			builder.append(location);
			builder.append(", ");
		}
		if (title != null) {
			builder.append("title=");
			builder.append(title);
			builder.append(", ");
		}
		builder.append("enabled=");
		builder.append(enabled);
		builder.append(", ");
		if (getName() != null) {
			builder.append("getName()=");
			builder.append(getName());
			builder.append(", ");
		}
		if (getCreationDate() != null) {
			builder.append("getCreationDate()=");
			builder.append(getCreationDate());
			builder.append(", ");
		}
		if (getModifiedDate() != null) {
			builder.append("getModifiedDate()=");
			builder.append(getModifiedDate());
		}
		builder.append("]");
		return builder.toString();
	}

	public int getCachedSize() {
		return CacheSizes.sizeOfLong() 
				+ CacheSizes.sizeOfString(getName())
				+ CacheSizes.sizeOfString(title)
				+ CacheSizes.sizeOfString(location) 
				+ CacheSizes.sizeOfBoolean()
				+ CacheSizes.sizeOfDate()
				+ CacheSizes.sizeOfString(menuData)
				+ CacheSizes.sizeOfMap(getProperties())
				+ CacheSizes.sizeOfDate();	
	}

	public String getMenuData() {
		return menuData;
	}

	public void setMenuData(String data) {
		this.menuData = data;
	}

	
}
