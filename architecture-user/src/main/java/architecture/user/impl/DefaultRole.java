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
package architecture.user.impl;

import java.io.Serializable;

import architecture.common.cache.CacheSizes;
import architecture.common.model.factory.ModelTypeFactory;
import architecture.common.model.support.BaseModelObjectSupport;
import architecture.user.Role;


public class DefaultRole extends BaseModelObjectSupport  implements Role  {

	private Long roleId;
	
	private int mask;
	
	private String description;
	
	public Serializable getPrimaryKeyObject() {
		return getRoleId();
	}

	public int getModelObjectType() {
		return ModelTypeFactory.getTypeIdFromCode("ROLE");
	}

	public int getCachedSize() {
		return CacheSizes.sizeOfLong() + CacheSizes.sizeOfString(getName()) + CacheSizes.sizeOfString( getDescription() ) + CacheSizes.sizeOfDate() + CacheSizes.sizeOfDate() ;
	}

	/**
	 * @return description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description 설정할 description
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	public long getRoleId() {
		return roleId;
	}

	public void setRoleId(long roleId) {
		this.roleId = roleId;
	}
	public int getMask() {
		return mask;
	}

	@Override
	public boolean equals(Object obj) {
		if( obj instanceof Role){
			if( ((Role)obj).getRoleId() == this.roleId )
				return true;
			else 
				return false;
		}
		return super.equals(obj);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("User [ roleId=");
		sb.append(roleId);
		sb.append(", name=").append(getName());
		sb.append("]");
		return sb.toString();
	}

	public void setMask(int mask) {
		this.mask = mask;
	}


}