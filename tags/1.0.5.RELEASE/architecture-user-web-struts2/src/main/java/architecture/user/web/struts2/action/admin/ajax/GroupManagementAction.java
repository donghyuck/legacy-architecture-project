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
package architecture.user.web.struts2.action.admin.ajax;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import architecture.common.user.Company;
import architecture.common.user.CompanyManager;
import architecture.common.user.CompanyNotFoundException;
import architecture.common.user.Group;
import architecture.common.user.User;
import architecture.common.user.UserManager;
import architecture.ee.web.struts2.action.support.FrameworkActionSupport;
import architecture.ee.web.util.ParamUtils;
import architecture.ee.web.ws.Property;
import architecture.user.GroupAlreadyExistsException;
import architecture.user.GroupManager;
import architecture.user.GroupNotFoundException;
import architecture.user.Role;
import architecture.user.RoleManager;

import com.google.common.collect.Lists;

public class GroupManagementAction  extends FrameworkActionSupport  {

    private int pageSize = 0 ;
    
    private int startIndex = 0 ;    

    private Long companyId = 1L ;
    
    private Long groupId;
    
    private Company targetCompany;
    
    private Group targetGroup;
    
    private GroupManager groupManager ;

    private UserManager userManager;
    
    private RoleManager roleManager ;
    
    private CompanyManager companyManager ;
    
    public CompanyManager getCompanyManager() {
		return companyManager;
	}

	public void setCompanyManager(CompanyManager companyManager) {
		this.companyManager = companyManager;
	}

	public RoleManager getRoleManager() {
		return roleManager;
	}

	public void setRoleManager(RoleManager roleManager) {
		this.roleManager = roleManager;
	}

	public UserManager getUserManager() {
		return userManager;
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
    
    public Long getGroupId() {
		return groupId;
	}

	public void setGroupId(Long groupId) {
		this.groupId = groupId;
	}

	public int getTotalGroupCount(){		
		int totalCount = companyManager.getTotalCompanyGroupCount(getTargetCompany());
        return totalCount;
    }
    
    public List<Group> getGroups(){    	
    	List<Group> list ;    	
        if( pageSize > 0 ){        
        	list = companyManager.getCompanyGroups(getTargetCompany(), startIndex, pageSize);
        }else{
        	list = companyManager.getCompanyGroups(getTargetCompany());        
        }        
        return list;
    }
    
	public int getTotalGroupUserCount(){		
		int totalCount = groupManager.getTotalGroupUserCount(getTargetGroup());
        return totalCount;
    }
	
    public List<User> getGroupUsers(){    	
		if (groupId == null)
			log.warn("unspecified group.");
		
    	List<User> list ;    	
        if( pageSize > 0 ){        
        	list = groupManager.getGroupUsers(getTargetGroup(), startIndex, pageSize);
        }else{
        	list = groupManager.getGroupUsers(getTargetGroup());        
        }        
        return list;
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
    
    public Group getTargetGroup() {

		if (groupId == null)
			log.warn("Edit profile for unspecified group.");
		
		if(targetGroup == null){
			try {
				targetGroup = groupManager.getGroup(groupId.longValue());
			} catch (GroupNotFoundException e) {
				log.warn((new StringBuilder()).append("Could not load user object for id: ").append(groupId).toString());
				return null;
			}
		}
		return targetGroup ;
	}    

	public List<Property> getTargetGroupProperty() {
		Map<String, String> properties = getTargetGroup().getProperties();
		List<Property> list = new ArrayList<Property>();
		for (String key : properties.keySet()) {
			String value = properties.get(key);
			list.add(new Property(key, value));
		}
		return list;
	}	
	
	public List<Role> getGroupRoles(){
		return roleManager.getFinalGroupRoles(getTargetGroup().getGroupId());
	}
	
	public String createGroup() throws Exception {		
		
		Map map = ParamUtils.getJsonParameter(request, "item", Map.class);		
		String name = (String)map.get("name");
		String displayName = (String)map.get("displayName");
		String description = (String)map.get("description");		
		Company company = getTargetCompany();
		
		if( !name.startsWith( company.getName() )){
			StringBuilder sb = new StringBuilder();
			sb.append(company.getName());
			sb.append('_');
			sb.append(name);
			name = sb.toString();
		}
		
		this.targetGroup = groupManager.createGroup(name, displayName, description, company);
		return success();	
	}
	
	public String updateGroup() throws Exception {	
		
		try {
			Map map = ParamUtils.getJsonParameter(request, "item", Map.class);
			String name = (String)map.get("name");
			String displayName = (String)map.get("displayName");
			String description = (String)map.get("description");	
			
			if( groupId == null){
				Integer  selectedGroupId = (Integer)map.get("groupId");	
				groupId = selectedGroupId.longValue();
			}
			Group group = getTargetGroup();			
	
			if(!StringUtils.isEmpty(name))
			    group.setName(name);
			if(!StringUtils.isEmpty(displayName))
			    group.setDisplayName(displayName);
			if(!StringUtils.isEmpty(description))
			    group.setDescription(description);		
			
			groupManager.updateGroup(group);		
			this.targetGroup = null;		
			return success();
		} catch (Throwable e) {
			e.printStackTrace();
			throw new Exception(e);
		}		
	}
	
	public String addMember() throws Exception {	
		Group group = getTargetGroup();
		Map map = ParamUtils.getJsonParameter(request, "item", Map.class);	
		long userId = Long.parseLong(map.get("userId").toString() );
		User user = userManager.getUser(userId);		
		groupManager.addMembership(group, user);		
		return success();	
	}
	
	public String addMembers() throws Exception {	
		Group group = getTargetGroup();
		List<Map> list = ParamUtils.getJsonParameter(request, "items", List.class);	
		List<User> users = Lists.newArrayListWithExpectedSize(list.size());
		for( Map map : list ){
			
			long userId = Long.parseLong(map.get("userId").toString() );
			User user = userManager.getUser(userId);
			users.add(user);
		}
		groupManager.addMembership(group, users);		
		return success();	
	}
	
	public String removeMember() throws Exception {	
		Group group = getTargetGroup();
		Map map = ParamUtils.getJsonParameter(request, "item", Map.class);	
		long userId = Long.parseLong(map.get("userId").toString() );
		User user = userManager.getUser(userId);		
		groupManager.removeMembership(group, user);
		return success();	
	}
	
	public String removeMembers() throws Exception {	
		Group group = getTargetGroup();
		List<Map> list = ParamUtils.getJsonParameter(request, "items", List.class);	
		List<User> users = Lists.newArrayListWithExpectedSize(list.size());
		for( Map map : list ){
			long userId = Long.parseLong(map.get("userId").toString() );
			User user = userManager.getUser(userId);
			users.add(user);
		}
		groupManager.removeMembership(group, users);		
		return success();	
	}
	
	public String updateGroupRoles() throws Exception {	
		Group group = getTargetGroup();				
		List<Map> list = ParamUtils.getJsonParameter(request, "items", List.class);			
		List<Role> oleRoles = roleManager.getFinalGroupRoles(group.getGroupId());
		List<Role> newRoles = new ArrayList<Role>(list.size());		
		for( Map map : list ){			
			long roleId = Long.parseLong(map.get("roleId").toString() );
			Role role = roleManager.getRole(roleId);
			newRoles.add(role);
		}
		
		if(oleRoles.size() > 0 ){
			roleManager.removeGroupRole(group, oleRoles);
		}
		
		if(newRoles.size() > 0 ){
			roleManager.addGroupRole(group, newRoles);
		}
		
		log.debug( group.getName() + " : " + oleRoles + ">" + newRoles );
		
		return success();			
	}
	
	
	
	public String updateGroupProperties() throws Exception {		
		Group group = getTargetGroup();
		Map<String, String> properties = group.getProperties();
		List<Map> list = ParamUtils.getJsonParameter(request, "items", List.class);		
		for (Map row : list) {
			String n = (String) row.get("name");
			String v = (String) row.get("value");
			properties.put(n, v);
		}		
		updateTargetGroupProperties(group, group.getProperties());
		return success();	
	}
	
	public String deleteGroupProperties() throws Exception {
		Group group = getTargetGroup();
		Map<String, String> properties = group.getProperties();
		List<Map> list = ParamUtils.getJsonParameter(request, "items", List.class);
		for (Map row : list) {
			String n = (String) row.get("name");
			String v = (String) row.get("value");
			properties.remove(n);
		}
		updateTargetGroupProperties( group, properties );		
		return success();
	}

	protected void updateTargetGroupProperties(Group group, Map<String, String> properties) throws GroupNotFoundException, GroupAlreadyExistsException {
		if (properties.size() > 0) {
			group.setProperties(properties);
			this.targetGroup = group;
			groupManager.updateGroup(group);
		}
	}
	
	
	
    @Override
    public String execute() throws Exception {  
        return success();
    }  

}
