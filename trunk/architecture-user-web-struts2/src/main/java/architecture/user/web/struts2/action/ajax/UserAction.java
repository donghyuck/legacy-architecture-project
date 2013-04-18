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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import architecture.common.user.User;
import architecture.common.user.authentication.AuthToken;
import architecture.ee.web.struts2.action.support.FrameworkActionSupport;
import architecture.user.web.struts2.action.admin.ajax.Property;

public class UserAction extends FrameworkActionSupport {
	
	public List<Property> getCurrentUserProperty() {
		Map<String, String> properties = getCurrentUser().getProperties();
		List<Property> list = new ArrayList<Property>();
		for (String key : properties.keySet()) {
			String value = properties.get(key);
			list.add(new Property(key, value));
		}
		return list;
	}
	
	public User getCurrentUser(){
		User targetUser = getUser();		
		
		Object obj = getCurrentAuthToken();
		
		log.debug(obj.getClass().getName());
		
		return targetUser;
	}
	
	public AuthToken getCurrentAuthToken(){
		return getAuthToken();
	}
	
    public String execute() throws Exception {    	
        return SUCCESS;
    }
	
}
