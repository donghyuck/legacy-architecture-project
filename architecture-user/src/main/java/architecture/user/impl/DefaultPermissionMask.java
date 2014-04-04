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
import java.util.Date;

import architecture.common.model.factory.ModelTypeFactory;
import architecture.common.model.support.DateModelObjectSupport;
import architecture.user.permission.PermissionMask;

public class DefaultPermissionMask extends DateModelObjectSupport implements PermissionMask {

	public String getName() {
		// TODO 자동 생성된 메소드 스텁
		return null;
	}

	public void setName(String name) {
		// TODO 자동 생성된 메소드 스텁
		
	}

	public Serializable getPrimaryKeyObject() {
		// TODO 자동 생성된 메소드 스텁
		return null;
	}

	public int getModelObjectType() {
		return ModelTypeFactory.getTypeIdFromCode("UNKNOWN");
	}

	public Date getCreationDate() {
		// TODO 자동 생성된 메소드 스텁
		return null;
	}

	public void setCreationDate(Date creationDate) {
		// TODO 자동 생성된 메소드 스텁
		
	}

	public Date getModifiedDate() {
		// TODO 자동 생성된 메소드 스텁
		return null;
	}

	public void setModifiedDate(Date modifiedDate) {
		// TODO 자동 생성된 메소드 스텁
		
	}

	public int getCachedSize() {
		// TODO 자동 생성된 메소드 스텁
		return 0;
	}

	public int getMask() {
		// TODO 자동 생성된 메소드 스텁
		return 0;
	}

	public void setMask(int mask) {
		// TODO 자동 생성된 메소드 스텁
		
	}

}
