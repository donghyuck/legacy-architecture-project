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
package architecture.common.user.authentication;

import java.io.Serializable;
import java.util.Date;
import java.util.Map;

import architecture.common.model.ModelObjectType;
<<<<<<< .mine
import architecture.common.model.support.BaseModelObjectSupport;
import architecture.common.user.Company;
=======
import architecture.common.model.support.PropertyAndDateModelObjectSupport;
import architecture.common.user.Company;
>>>>>>> .r989
import architecture.common.user.User;


<<<<<<< .mine
public class SystemUser extends BaseModelObjectSupport implements AuthToken, User {
=======
public class SystemUser extends PropertyAndDateModelObjectSupport implements AuthToken, User {
>>>>>>> .r989
	
	private static final long serialVersionUID = -2371386795832701297L;
	
	public static final String SYSTEM_USERNAME = "SYSTEM";
	
	public Object getPrimaryKey() {		
		return getUserId();
	}

	public ModelObjectType getModelObjectType() {
		return ModelObjectType.SYSTEM ;
	}
	
	public String getUsername() {
		return SYSTEM_USERNAME;
	}

	public String getName() {
		return null;
	}

	public String getFirstName() {
		return null;
	}

	public String getLastName() {
		return null;
	}

	public boolean isNameVisible() {
		return false;
	}

	public String getPasswordHash() throws UnAuthorizedException {
		return null;
	}

	public String getPassword() throws UnAuthorizedException {
		return null;
	}

	public String getEmail() {
		return null;
	}

	public boolean isEmailVisible() {
		return false;
	}

	public Date getCreationDate() {
		return null;
	}

	public Date getModifiedDate() {
		return null;
	}

	public Date getLastLoggedIn() {
		return null;
	}

	public Date getLastProfileUpdate() {
		return null;
	}

	public boolean isEnabled() {
		return false;
	}

	public boolean isFederated() {
		return false;
	}

	public boolean isExternal() {
		return false;
	}

	public boolean isSetPasswordSupported() {
		return false;
	}

	public boolean isGetPasswordHashSupported() {
		return false;
	}

	public boolean isSetPasswordHashSupported() {
		return false;
	}

	public boolean isSetNameSupported() {
		return false;
	}

	public boolean isSetUsernameSupported() {
		return false;
	}

	public boolean isSetEmailSupported() {
		return false;
	}

	public boolean isSetNameVisibleSupported() {
		return false;
	}

	public boolean isSetEmailVisibleSupported() {
		return false;
	}

	public boolean isPropertyEditSupported() {
		return false;
	}

	public int compareTo(User o) {
		return 0;
	}

	public long getUserId() {
		return 0;
	}

	public boolean isAnonymous() {
		return false;
	}

	public Serializable getPrimaryKeyObject() {
		return getUserId();
	}

	public void setCreationDate(Date creationDate) {		
	}

	public void setModifiedDate(Date modifiedDate) {
		
	}

	public int getCachedSize() {
		return 0;
	}

	public boolean isSetNameVisbleSupported() {
		return false;
	}

	public Status getStatus() {
		return null;
	}

	public Map<String, String> getProperties() {
		return null;
	}

	public long getCompanyId() {
		// TODO 자동 생성된 메소드 스텁
		return 0;
	}

	public Company getCompany() {
		// TODO 자동 생성된 메소드 스텁
		return null;
	}

	public boolean isProfileSupported() {
		// TODO 자동 생성된 메소드 스텁
		return false;
	}

	public boolean isProfileEditSupported() {
		// TODO 자동 생성된 메소드 스텁
		return false;
	}

	public Map<String, Object> getProfile() {
		// TODO 자동 생성된 메소드 스텁
		return null;
	}

	public <T> T getProfileFieldValue(String fieldName, Class<T> elementType) {
		// TODO 자동 생성된 메소드 스텁
		return null;
	}

	public String getProfileFieldValueString(String fieldName) {
		// TODO 자동 생성된 메소드 스텁
		return null;
	}

}