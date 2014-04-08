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

import architecture.common.user.Company;
import architecture.common.user.CompanyManager;
import architecture.common.user.CompanyNotFoundException;
import architecture.common.user.Group;
import architecture.common.user.User;
import architecture.common.user.UserAlreadyExistsException;
import architecture.common.user.UserManager;
import architecture.common.user.UserNotFoundException;
import architecture.common.user.UserTemplate;
import architecture.common.util.StringUtils;
import architecture.ee.web.struts2.action.support.FrameworkActionSupport;
import architecture.ee.web.util.ParamUtils;
import architecture.ee.web.ws.Property;
import architecture.user.GroupManager;
import architecture.user.Role;
import architecture.user.RoleManager;

public class UserManagementAction  extends FrameworkActionSupport  {

	private Long companyId = 1L ;
	
	private String password ;
	
    private int pageSize = 0 ;
    
    private int startIndex = 0 ;  
    
	private User targetUser;

	private Long userId;
    
    private UserManager userManager ;
    
    private GroupManager groupManager ;
    
    private RoleManager roleManager ;

    private CompanyManager companyManager ;
    
    private Company targetCompany;
    
    public CompanyManager getCompanyManager() {
		return companyManager;
	}

	public void setCompanyManager(CompanyManager companyManager) {
		this.companyManager = companyManager;
	}

    public UserManager getUserManager() {
        return userManager;
    }

    public RoleManager getRoleManager() {
		return roleManager;
	}

	public void setRoleManager(RoleManager roleManager) {
		this.roleManager = roleManager;
	}

	public void setUserManager(UserManager userManager) {
        this.userManager = userManager;
    }

    
	public GroupManager getGroupManager() {
		return groupManager;
	}

	public void setGroupManager(GroupManager groupManager) {
		this.groupManager = groupManager;
	}
	
    public Long getCompanyId() {
		return companyId;
	}

	public void setCompanyId(Long companyId) {
		this.companyId = companyId;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

    public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
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
    	if( companyId == null ){
    		 return userManager.getTotalUserCount();
    	}else{
    		return userManager.getUserCount(getTargetCompany());
    	}    
    }

    public Company getTargetCompany(){
		if (companyId == null)
			log.warn("Edit profile for unspecified comany.");
		if(targetCompany == null){
			try {
				targetCompany = companyManager.getCompany(companyId.longValue());
			} catch (CompanyNotFoundException e) {
				log.warn((new StringBuilder()).append("Could not load company object for id: ").append(companyId).toString());
				return null;
			}
		}
		return targetCompany ;
    }
    
    public List<User> getUsers(){   
    	
    	log.debug(  request.getParameterMap() );
    	log.debug( "startIndex= " + startIndex + ", pageSize=" + pageSize  );

        if( pageSize > 0 ){
            return userManager.getUsers(getTargetCompany(), startIndex, pageSize);            
        }else{            
            return userManager.getUsers(getTargetCompany());
        }
    }

    
    public int getFoundUserCount(){
    	String nameOrEmail = ParamUtils.getParameter(request, "nameOrEmail");
        return userManager.getFoundUserCount(getTargetCompany(), nameOrEmail);
    }
    
    public List<User> findUsers(){
    	String nameOrEmail = ParamUtils.getParameter(request, "nameOrEmail");
		if (nameOrEmail == null)
			log.warn("No search condition..");
		else
			log.debug("search user : " + nameOrEmail);
		
        if( pageSize > 0 ){
            return userManager.findUsers(getTargetCompany(), nameOrEmail, startIndex, pageSize);            
        }else{            
            return userManager.findUsers(getTargetCompany(), nameOrEmail);
        }
    }
    
    public List<User> getFoundUsers(){
    	return findUsers ();
    }
    
    
    public List<Group> getUserGroups(){
    	return groupManager.getUserGroups(getTargetUser());    	
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
	
    
    public List<Role> getFinalUserRoles(){    	
    	List<Role> roles =  roleManager.getFinalUserRoles(getTargetUser().getUserId());
    	return roles;
    }

    public List<Role> getUserGroupRoles(){    	
    	List<Role> roles =  roleManager.getUserRolesFromGroup(getTargetUser().getUserId());
    	return roles;
    }
    
    public List<Role> getUserRoles(){
    	User user = getTargetUser();
    	List<Role> roleForUser = roleManager.getFinalUserRoles(user.getUserId());
    	
    	for( Role role : getUserGroupRoles() ){
    		roleForUser.remove(role);
    	}    	
    	return roleForUser;
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
	
	public String updateUserPassword() throws Exception {
		User user = getTargetUser(); 
		if( user instanceof UserTemplate ){
			if( !StringUtils.isEmpty(password))
			{ 
				((UserTemplate) user).setPassword(password);
				userManager.updateUser(user);
			}
		}
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

			String name = (String)map.get("name");
			String email = (String)map.get("email");
			boolean enabled = (Boolean)map.get("enabled");
			boolean emailVisible = (Boolean)map.get("emailVisible");
			boolean nameVisible = (Boolean)map.get("nameVisible");
			String password = (String)map.get("password");
			
			if( user instanceof UserTemplate ){
				((UserTemplate) user).setName(name);
				((UserTemplate) user).setEmail(email);
				((UserTemplate) user).setEnabled(enabled);
				((UserTemplate) user).setEmailVisible(emailVisible);				
				((UserTemplate) user).setNameVisible(nameVisible);
				
				if( StringUtils.isNotEmpty(password)){
					((UserTemplate) user).setPassword(password);
				}				
			}
			
			this.targetUser = user;			
			
			log.debug( user );
			
			userManager.updateUser(user);
			
			return success();
		} catch (Throwable e) {
			e.printStackTrace();
			throw new Exception(e);
		}	
	}
	
	public String updateUserRoles() throws Exception {	
		
		List<Role> oleRoles = getUserRoles();
		List<Role> groupRoles = getUserGroupRoles();
		
		List<Map> list = ParamUtils.getJsonParameter(request, "items", List.class);			
		List<Role> newRoles = new ArrayList<Role>(list.size()); 		
		for( Map map : list ){			
			long roleId = Long.parseLong(map.get("roleId").toString() );
			Role role = roleManager.getRole(roleId);
			if( !groupRoles.contains(role) )
				newRoles.add(role);
		}
		
		User user = getTargetUser();		
		if(oleRoles.size() > 0 ){
			roleManager.removeUserRole(user, oleRoles);
		}
		
		if(newRoles.size() > 0 ){
			roleManager.addUserRole(user, newRoles);
		}		
		
		log.debug( user.getName() + " : " + oleRoles + ">" + newRoles );
		
		return success();			
	}
	 
	protected void updateTargetUserProperties(User user, Map<String, String> properties) throws UserNotFoundException, UserAlreadyExistsException {
		if (user instanceof UserTemplate) {
			((UserTemplate) user).setProperties(properties);
			this.targetUser = user;			
			userManager.updateUser(user);
		}
	} 
}
