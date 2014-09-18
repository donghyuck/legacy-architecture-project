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

import java.util.List;

import architecture.ee.web.struts2.action.support.FrameworkActionSupport;
import architecture.user.Role;
import architecture.user.RoleManager;
import architecture.user.RoleNotFoundException;

public class RoleManagementAction extends FrameworkActionSupport  {
	
	private Long roleId;
	private Integer pageSize = 15;
	private Integer startIndex = 0 ;
	private Role targetRole ;
	private RoleManager roleManager ;
	
	public RoleManager getRoleManager() {
		return roleManager;
	}
	public void setRoleManager(RoleManager roleManager) {
		this.roleManager = roleManager;
	}
	
	

    public Role getTargetRole() {
		if (roleId == null)
			log.warn("Edit profile for unspecified role.");
		
		if(targetRole == null){
			try {
				targetRole = roleManager.getRole(roleId.longValue());
			} catch (RoleNotFoundException e) {
				log.warn((new StringBuilder()).append("Could not load company object for id: ").append(roleId).toString());
				return null;
			}
		}
		return targetRole ;
	}    
    

    public List<Role> getRoles(){    	
    	List<Role> list ;    	
        if( pageSize > 0 ){        
        	list = roleManager.getRoles();
        }else{
        	list = roleManager.getRoles();
        }        
        return list;
    }
    
    public int getTotalRoleCount(){
    	return roleManager.getTotalRoleCount();
    }
    
    public String execute() throws Exception {      	
        return success();
    }  
	
	
}
