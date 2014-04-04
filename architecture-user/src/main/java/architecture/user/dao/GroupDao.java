package architecture.user.dao;

import java.util.List;
import java.util.Set;

import architecture.common.user.Group;

public interface GroupDao {
	
	public abstract void createGroup(Group group);
	
	public abstract void updateGroup(Group group);
	
	public abstract int getGroupCount();
	
	public abstract List<Group> getGroups();
	
	public abstract List<Group> getGroups(int start, int num);	
	
	public abstract List<Long> getGroupIds(int start, int num);
	
	public abstract List<Long> getAllGroupIds();
	
	public abstract Group getGroupById(long groupId);

	public abstract Group getGroupByName(String name, boolean caseInsensitive);	
	
	public abstract List<Long> getMembersIds(long groupId);
	
	public abstract List<Long> getMembersIds(long groupId, int start, int num );
	
	public abstract int getGroupMemberCount(long groupId);
	
	public abstract boolean isMember(long groupId, long userId);
	
	public abstract void addMember(long groupId, long userId);
	
	public abstract void removeMember(long groupId, long userId);
	 
    public abstract void addMembers(long groupId, Set<Long> userIds);
	
	public abstract void removeMembers(long groupId, Set<Long> userIds);
	
	public abstract List<Long> getUserGroupIds(long userId);
	
	/*
	public abstract void deleteGroupUsers(long groupId);

	public abstract void deleteGroupProperties(long groupId);

	public abstract void deleteGroup(long groupId);

	

	

	

	public abstract List<Long> getGroupIds(int start, int num);

	

	


	
	

	public abstract List<Long> getAdministratorIds(long groupId);



	public abstract void addAdministrator(long groupId, long userId);
	
	public abstract void removeAdministrator(long groupId, long userId);
	
	
	
	public abstract void addAdministrators(long groupId, Set<Long> userIds);
	
	public abstract void removeAdministrators(long groupId, Set<Long> userIds);	
	
	public abstract void deleteUserMemberships(long userId);
	*/

}