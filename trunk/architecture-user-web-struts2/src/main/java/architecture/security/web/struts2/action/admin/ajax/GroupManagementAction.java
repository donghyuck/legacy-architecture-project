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
package architecture.security.web.struts2.action.admin.ajax;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import architecture.ee.web.struts2.action.support.FrameworkActionSupport;
import architecture.ee.web.util.ParamUtils;
import architecture.security.user.Group;
import architecture.security.user.GroupAlreadyExistsException;
import architecture.security.user.GroupManager;
import architecture.security.user.GroupNotFoundException;

public class GroupManagementAction  extends FrameworkActionSupport  {

    private int pageSize = 0 ;
    
    private int startIndex = 0 ;    

    private Long groupId;
    
    private Group targetGroup;
    
    private GroupManager groupManager ;

    public GroupManager getGroupManager() {
        return groupManager;
    }

    public void setGroupManager(GroupManager groupManager) {
        this.groupManager = groupManager;
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
		int totalCount = groupManager.getTotalGroupCount();
        return totalCount;
    }
    
    public List<Group> getGroups(){        
    	
    	List<Group> list ;
    	
        if( pageSize > 0 ){        
        	list = groupManager.getGroups(startIndex, pageSize);
        }else{
        	list = groupManager.getGroups();        
        }
        
       
        return list;
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
	
	
	public String createGroup() throws Exception {		
		Map map = ParamUtils.getJsonParameter(request, "item", Map.class);		
		String name = (String)map.get("name");
		String description = (String)map.get("description");
		this.targetGroup = groupManager.createGroup(name, description);
		return success();	
	}
	
	public String updateGroup() throws Exception {	
		
		try {
			Group group = getTargetGroup();
			Map map = ParamUtils.getJsonParameter(request, "item", Map.class);
			String name = (String)map.get("name");
			String description = (String)map.get("description");		
			if(!StringUtils.isEmpty(name))
			    group.setName(name);
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
