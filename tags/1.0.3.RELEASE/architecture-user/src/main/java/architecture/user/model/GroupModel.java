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
package architecture.user.model;

import java.util.List;
import java.util.Map;

import architecture.common.model.ModelObject;
import architecture.common.user.Company;
import architecture.common.user.User;
import architecture.common.user.authentication.UnAuthorizedException;
import architecture.user.Group;

/**
 * @author                 donghyuck
 */
public interface GroupModel extends ModelObject<Group>{

    /**
	 * @return
	 */
    public abstract long getGroupId();
    
    /**
	 * @param  groupId
	 */
    public abstract void setGroupId(long groupId);

    public abstract void setCompanyId(long companyId);
    
    public abstract long getCompanyId();
    
    public abstract void setCompany(Company company);
    
    public abstract Company getCompany();
    
    public abstract String getDisplayName();
    
    public abstract void setDisplayName(String displayName);
    
    /**
	 * @return
	 */
    public abstract String getName();

    /**
	 * @param name
	 * @throws UnAuthorizedException
	 */
    public abstract void setName(String name) throws UnAuthorizedException;

    /**
	 * @return
	 */
    public abstract String getDescription();

    /**
	 * @param description
	 * @throws UnAuthorizedException
	 */
    public abstract void setDescription(String description) throws UnAuthorizedException;
        
    public abstract Map<String, String> getProperties();
	
    /**
	 * @param  properties
	 */
    public abstract void setProperties(Map<String, String> properties);
        
	/**
	 * @return
	 */
	public abstract List<Long> getAdministratorIds();

	/**
	 * @param  administrators
	 */
	public abstract void setAdministratorIds(List<Long> administrators) ;

	/**
	 * @return
	 */
	public abstract List<Long> getMemberIds();
	
	/**
	 * @param  members
	 */
	public abstract void setMemberIds(List<Long> members);
	
	public abstract boolean isMember(User user);
	
	public abstract boolean isAdministrator(User user);
	
	public abstract int getMemberCount();	
	
	public abstract int getAdministratorCount();
	
}