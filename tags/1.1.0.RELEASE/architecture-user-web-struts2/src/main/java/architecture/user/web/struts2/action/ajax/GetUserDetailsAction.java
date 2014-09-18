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

import org.springframework.security.core.GrantedAuthority;

import architecture.common.user.User;
import architecture.common.user.authentication.AuthToken;
import architecture.ee.web.struts2.action.support.FrameworkActionSupport;
import architecture.ee.web.ws.Property;

public class GetUserDetailsAction extends  FrameworkActionSupport {

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
		User targetUser = super.getUser();
		return targetUser;
	}
	
	public List<String> getRoles () {		
		List<String> list = new ArrayList<String>();
		for(GrantedAuthority auth : getAuthorities() ) {
			list.add(  auth.getAuthority() );
		}
		return list;
	}
	
	public List<GrantedAuthority> getAuthorities (){		
		AuthToken token = getCurrentAuthToken();
		List<GrantedAuthority> list = new ArrayList<GrantedAuthority>();		
		if( token instanceof org.springframework.security.core.userdetails.UserDetails ){			
			list.addAll(    
				((org.springframework.security.core.userdetails.UserDetails)token).getAuthorities()
			);
		}		
		return  list;
	}
	
	public AuthToken getCurrentAuthToken(){
		return super.getAuthToken();
	}
	
    public String execute() throws Exception {      	
        return super.success();
    }
    
}
