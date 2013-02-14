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

import architecture.common.cache.CacheSizes;
import architecture.common.model.v2.BaseModelObject;
import architecture.user.Role;


public class RoleImpl extends BaseModelObject  implements Role  {

	private Long getRoleId;
	
	private int mask;
	
	public Serializable getPrimaryKeyObject() {
		return getRoleId();
	}

	public int getModelObjectType() {
		return 11;
	}

	public int getCachedSize() {
		return CacheSizes.sizeOfLong() + CacheSizes.sizeOfString(getName()) + CacheSizes.sizeOfString( getDescription() ) + CacheSizes.sizeOfDate() + CacheSizes.sizeOfDate() ;
	}

	public long getRoleId() {
		return getRoleId;
	}

	public void setRoleId(long roleId) {
		getRoleId = roleId;
	}
	public int getMask() {
		return mask;
	}

	public void setMask(int mask) {
		this.mask;
	}


}