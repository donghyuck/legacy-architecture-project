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
package architecture.security.user;

import java.util.List;

import architecture.common.user.User;
import architecture.common.user.authentication.UnAuthorizedException;

public interface GroupManager {

	public abstract Group createGroup(String name) throws GroupAlreadyExistsException;

	public abstract int getGroupCount();

	public abstract List<Group> getGroups();

	public abstract List<Group> getGroups(int startIndex, int numResults);
	
	/*
	
	public abstract Group getGroup(long groupId) throws GroupNotFoundException;

	public abstract Group getGroup(String name) throws GroupNotFoundException;

	public abstract Group getGroup(long groupId, boolean flag) throws GroupNotFoundException;

	public abstract List<Group> getUserGroups(User user);



	public abstract void updateGroup(Group group) throws GroupNotFoundException, GroupAlreadyExistsException;

	public abstract void deleteGroup(Group group) throws UnAuthorizedException;
	
	public abstract void addMembership(Group group, User user) throws UnAuthorizedException;

	public abstract boolean hasMembership(Group group, User user) throws UnAuthorizedException;

	public abstract void removeMembership(Group group, User user) throws UnAuthorizedException;*/

}
