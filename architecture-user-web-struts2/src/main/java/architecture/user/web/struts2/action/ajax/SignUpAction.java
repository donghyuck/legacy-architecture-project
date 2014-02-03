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
package architecture.user.web.struts2.action.ajax;

import org.apache.commons.lang.StringUtils;

import architecture.common.user.UserManager;
import architecture.ee.web.struts2.action.support.FrameworkActionSupport;

public class SignUpAction extends  FrameworkActionSupport  {
	
	private UserManager userManager ;
	
	private String usernameOrEmail ;
	
	
	/**
	 * @return usernameOrEmail
	 */
	public String getUsernameOrEmail() {
		return usernameOrEmail;
	}

	/**
	 * @param usernameOrEmail 설정할 usernameOrEmail
	 */
	public void setUsernameOrEmail(String usernameOrEmail) {
		this.usernameOrEmail = usernameOrEmail;
	}

	/**
	 * @return userManager
	 */
	public UserManager getUserManager() {
		return userManager;
	}

	/**
	 * @param userManager 설정할 userManager
	 */
	public void setUserManager(UserManager userManager) {
		this.userManager = userManager;
	}

	public String execute() throws Exception {      	
	
		return success();
	
	}
	
	public boolean isUsernameAvailable() throws Exception {      	
		if( StringUtils.isNotEmpty( usernameOrEmail ))
			return userManager.getFoundUserCount(usernameOrEmail) > 0 ? false : true;
		else 
			return false;
	}

}
