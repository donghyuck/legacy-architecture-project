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
package architecture.user;

import java.util.List;

import architecture.common.user.Company;
import architecture.common.user.Group;
import architecture.common.user.User;
import architecture.common.user.authentication.UnAuthorizedException;

public interface GroupManager {

	public abstract Group createGroup(String name) throws GroupAlreadyExistsException;

	public abstract Group createGroup(String name, String displayName) throws GroupAlreadyExistsException;

	public abstract Group createGroup(String name, String displayName, Company company) throws GroupAlreadyExistsException;
	
	public abstract Group createGroup(String name, String displayName, String description, Company company) throws GroupAlreadyExistsException;
	
	public abstract int getTotalGroupCount();

	public abstract List<Group> getGroups();

	public abstract List<Group> getGroups(int startIndex, int numResults);
	
	public abstract Group getGroup(long groupId) throws GroupNotFoundException;

	public abstract Group getGroup(String name) throws GroupNotFoundException;
	
	public abstract void updateGroup(Group group) throws GroupNotFoundException, GroupAlreadyExistsException;
	
	/**
	 * 그룹에 해당하는 전체 멤버를 리턴한다.
	 * 
	 * @param group
	 * @return
	 */
	public abstract List<User> getGroupUsers(Group group) ;
	
	/**
	 * 그룹에 해당하는 멤버를 리턴한다.
	 * 
	 * @param group
	 * @param startIndex 
	 * @param numResults 
	 * @return
	 */
	public abstract List<User> getGroupUsers(Group group, int startIndex, int numResults) ;
	
	/**
	 * 그룹에 해당하는 멤버 수를 리턴한다.
	 * @param group
	 * @return
	 */
	public abstract int getTotalGroupUserCount(Group group);
	
	/**
	 * 그룹 멤버쉽을 추가한다.
	 * 
	 * @param group
	 * @param user
	 * @throws UnAuthorizedException
	 */
	public abstract void addMembership(Group group, User user) throws UnAuthorizedException;

	/**
	 * 그룹 멤버쉽을 추가한다.
	 * @param group
	 * @param user
	 * @throws UnAuthorizedException
	 */
	public abstract void addMembership(Group group, List<User> user) throws UnAuthorizedException;
	
	/**
	 * 그룹 멤버쉽이 있는가를 확인한다.
	 * 
	 * @param group
	 * @param user
	 * @return
	 * @throws UnAuthorizedException
	 */
	public abstract boolean hasMembership(Group group, User user) throws UnAuthorizedException;

	/**
	 * 그룹 멤버쉽을 제거한다.
	 * 
	 * @param group
	 * @param user
	 * @throws UnAuthorizedException
	 */
	public abstract void removeMembership(Group group, User user) throws UnAuthorizedException;
	
	public abstract void removeMembership(Group group, List<User> user) throws UnAuthorizedException;
	
	public abstract List<Group> getUserGroups(User user);
	
	/*
	
	public abstract Group getGroup(long groupId) throws RoleNotFoundException;

	public abstract Group getGroup(String name) throws RoleNotFoundException;

	public abstract Group getGroup(long groupId, boolean flag) throws RoleNotFoundException;





	public abstract void updateGroup(Group group) throws RoleNotFoundException, RoleAlreadyExistsException;

	public abstract void deleteGroup(Group group) throws UnAuthorizedException;
*/

}
