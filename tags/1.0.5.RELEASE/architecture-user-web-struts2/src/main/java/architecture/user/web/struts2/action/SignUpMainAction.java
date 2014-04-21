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
package architecture.user.web.struts2.action;

import java.util.Map;

import architecture.common.user.User;
import architecture.common.user.UserManager;
import architecture.common.user.UserTemplate;
import architecture.ee.web.struts2.action.support.FrameworkActionSupport;
import architecture.ee.web.util.ParamUtils;

public class SignUpMainAction  extends FrameworkActionSupport  {
	
	private UserManager userManager ;
	
	private User targetUser ;
	
    public UserManager getUserManager() {
		return userManager;
	}

	public void setUserManager(UserManager userManager) {
		this.userManager = userManager;
	}

	public User getTargetUser() {
		return targetUser;
	}

	public void setTargetUser(User targetUser) {
		this.targetUser = targetUser;
	}

	public String execute() throws Exception {
		return success();
	}
    
    public String register() throws Exception {    	
    	
		Map<String, Object> map = ParamUtils.getJsonParameter(request, "item", Map.class);		
		String username = (String)map.get("username");
		String name = (String)map.get("name");
		String email = (String)map.get("email");
		String password = (String)map.get("password");
		boolean nameVisible = (Boolean)map.get("nameVisible");
		boolean emailVisible = (Boolean)map.get("emailVisible");
		
		
		UserTemplate t = new UserTemplate(username, password, email, name );
		t.setNameVisible(nameVisible);
		t.setEmailVisible(emailVisible);		
		
		this.targetUser = userManager.createApplicationUser(t);		
		return success();	
    }

}
