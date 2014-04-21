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
package architecture.ee.web.community.struts2.action;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.security.core.GrantedAuthority;

import architecture.common.user.Group;
import architecture.common.user.authentication.AuthToken;
import architecture.ee.web.struts2.action.support.FrameworkActionSupport;
import architecture.user.GroupManager;

public class UserProfileAction extends FrameworkActionSupport {

	public static final String VIEW_MODAL_DIALOG = "modal-dialog";
	
	private GroupManager groupManager ;
	
	
	/**
	 * @return groupManager
	 */
	public GroupManager getGroupManager() {
		return groupManager;
	}

	/**
	 * @param groupManager 설정할 groupManager
	 */
	public void setGroupManager(GroupManager groupManager) {
		this.groupManager = groupManager;
	}

	private String view;
	
	
	/**
	 * @return view
	 */
	public String getView() {
		return view;
	}

	/**
	 * @param view 설정할 view
	 */
	public void setView(String view) {
		this.view = view;
	}

	public String execute() throws Exception {		
		if( !StringUtils.isEmpty(getView())){
			return getView();
		}
		return super.success();
	}

	public List<String> getRoles () {		
		List<String> list = new ArrayList<String>();
		for(GrantedAuthority auth : getAuthorities() ) {
			list.add(auth.getAuthority());
		}
		return list;
	}
	
	public List<GrantedAuthority> getAuthorities (){		
		AuthToken token = getAuthToken();
		List<GrantedAuthority> list = new ArrayList<GrantedAuthority>();		
		if( token instanceof org.springframework.security.core.userdetails.UserDetails ){			
			list.addAll(    
				((org.springframework.security.core.userdetails.UserDetails)token).getAuthorities()
			);
		}		
		return  list;
	}
	
    public List<Group> getGroups(){
    	if( getUser().isAnonymous() )
    		return Collections.EMPTY_LIST;    		
    	return groupManager.getUserGroups(getUser());    	
    }
}
