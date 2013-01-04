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
package architecture.user.web.struts2.action.admin.ajax;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import architecture.common.user.User;
import architecture.common.user.UserAlreadyExistsException;
import architecture.common.user.UserManager;
import architecture.common.user.UserNotFoundException;
import architecture.common.user.UserTemplate;
import architecture.common.util.StringUtils;
import architecture.ee.web.struts2.action.support.FrameworkActionSupport;
import architecture.ee.web.util.ParamUtils;

public class UserManagementAction  extends FrameworkActionSupport  {

    private int pageSize = 0 ;
    
    private int startIndex = 0 ;  
    
	private User targetUser;

	private Long userId;
    
    private UserManager userManager ;

    public UserManager getUserManager() {
        return userManager;
    }

    public void setUserManager(UserManager userManager) {
        this.userManager = userManager;
    }
    
	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getStartIndex() {
        return startIndex;
    }

    public void setStartIndex(int startIndex) {
        this.startIndex = startIndex;
    }
    
    public int getTotalUserCount(){
        return userManager.getTotalUserCount();
    }

    public List<User> getUsers(){        
        if( pageSize > 0 ){
            return userManager.getUsers(startIndex, pageSize);            
        }else{            
            return userManager.getUsers();
        }
    }
    
    
    public List<User> findUsers(){
    	String nameOrEmail = ParamUtils.getParameter(request, "search-text");
		if (nameOrEmail == null)
			log.warn("No search condition..");
        if( pageSize > 0 ){
            return userManager.findUsers(nameOrEmail, startIndex, pageSize);            
        }else{            
            return userManager.findUsers(nameOrEmail);
        }
    }
    
    
    
	public User getTargetUser() {
		if (userId == null)
			log.warn("Edit profile for unspecified user.");
		if(targetUser == null){
			try {
				targetUser = userManager.getUser(userId.longValue());
			} catch (UserNotFoundException e) {
				log.warn((new StringBuilder()).append("Could not load user object for id: ").append(userId).toString());
				return null;
			}
		}
		return new UserTemplate(targetUser);
	}
	
	
	/** Action method **/
	
    @Override
    public String execute() throws Exception {  
        return success();
    }    
    

	public List<Property> getTargetUserProperty() {
		Map<String, String> properties = getTargetUser().getProperties();
		List<Property> list = new ArrayList<Property>();
		for (String key : properties.keySet()) {
			String value = properties.get(key);
			list.add(new Property(key, value));
		}
		return list;
	}

	public String deleteUserProperties() throws Exception {
		User user = getTargetUser();
		Map<String, String> properties = user.getProperties();
		List<Map> list = ParamUtils.getJsonParameter(request, "items", List.class);
		for (Map row : list) {
			String n = (String) row.get("name");
			String v = (String) row.get("value");
			properties.remove(n);
		}
		updateTargetUserProperties( user, properties );
		return success();
	}
	
	public String updateUserProperties() throws Exception {
		User user = getTargetUser();
		Map<String, String> properties = user.getProperties();
		List<Map> list = ParamUtils.getJsonParameter(request, "items", List.class);		
		for (Map row : list) {
			String n = (String) row.get("name");
			String v = (String) row.get("value");
			properties.put(n, v);
		}		
		updateTargetUserProperties(user, user.getProperties());
		return success();	
	}
	
	public String updateUser() throws Exception {
		
		try {
			User user = getTargetUser();
			Map map = ParamUtils.getJsonParameter(request, "item", Map.class);
			
			boolean enabled = StringUtils.stringToBoolean((String)map.get("enabled"));
			boolean emailVisible = StringUtils.stringToBoolean((String)map.get("emailVisible"));
			boolean nameVisible = StringUtils.stringToBoolean((String)map.get("nameVisible"));

			if( user instanceof UserTemplate ){
				((UserTemplate) user).setEnabled(enabled);
				((UserTemplate) user).setEmailVisible(emailVisible);				
				((UserTemplate) user).setNameVisible(nameVisible);
			}
			
			this.targetUser = user;			
			
			userManager.updateUser(user);
			
			return success();
		} catch (Throwable e) {
			e.printStackTrace();
			throw new Exception(e);
		}	
	}
	
	protected void updateTargetUserProperties(User user, Map<String, String> properties) throws UserNotFoundException, UserAlreadyExistsException {
		if (properties.size() > 0 && user instanceof UserTemplate) {
			((UserTemplate) user).setProperties(properties);
			this.targetUser = user;			
			userManager.updateUser(user);
		}
	}
}
