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
import java.util.HashMap;
import java.util.Map;

import architecture.common.model.factory.ModelTypeFactory;
import architecture.common.model.support.BaseModelObjectSupport;
import architecture.common.user.Company;
import architecture.common.user.User;

/**
 * @author  donghyuck
 */
public class AnonymousUser extends BaseModelObjectSupport implements AuthToken, User {


	private static final long serialVersionUID = 6602216613448436301L;

	public static final long ANONYMOUS_ID = -1L;
	
	public static final String ANONYMOUS_USERNAME = "ANONYMOUS";
	
	private Company company  = null;
	/**
	 */
	private String username ;
	
	public AnonymousUser() {
		username = ANONYMOUS_USERNAME ;
	}
		

	public AnonymousUser(String username, String password) {
	}

	public AnonymousUser(Company company) {
		this.company = company;
	}
		
	public Object getPrimaryKey() {		
		return getUserId();
	}

	public int getModelObjectType() {
		return ModelTypeFactory.getTypeIdFromCode("ANONYMOUS");
	}
	
	/**
	 * @return
	 */
	public String getUsername() {
		return username;
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
		return "";
	}

	public String getPassword() throws UnAuthorizedException {
		return "";
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

	public Map<String, String> getProperties() {
		return new HashMap<String, String>(0);
	}

	public Date getLastLoggedIn() {
		return null;
	}

	public Date getLastProfileUpdate() {
		return null;
	}

	public boolean isEnabled() {
		return true;
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
		return ANONYMOUS_ID;
	}

	public boolean isAnonymous() {
		return true;
	}

	public Serializable getPrimaryKeyObject() {
		return ANONYMOUS_ID;
	}

	public void setPrimaryKeyObject(Serializable primaryKeyObj) {		
	}

	public int getCachedSize() {
		return 0;
	}


	public Date getLastProfileUpdated() {
		return null;
	}

	public boolean isSetNameVisbleSupported() {
		return false;
	}


	public Status getStatus() {
		return null;
	}


	public void setCreationDate(Date creationDate) {	
	}


	public void setModifiedDate(Date modifiedDate) {		
	}


	public long getCompanyId() {
		return -1L;
	}


	public Company getCompany() {
		return company;
	}


	public boolean isProfileSupported() {
		return false;
	}


	public boolean isProfileEditSupported() {
		return false;
	}


	public Map<String, Object> getProfile() {
		// TODO 자동 생성된 메소드 스텁
		return null;
	}


	public <T> T getProfileFieldValue(String fieldName, Class<T> elementType) {
		return null;
	}


	public String getProfileFieldValueString(String fieldName) {
		return null;
	}
}
