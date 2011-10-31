package architecture.ee.user.impl;

import java.util.ArrayList;
import java.util.List;

import architecture.ee.user.AbstractGroupManager;
import architecture.ee.user.Group;
import architecture.ee.user.GroupNotFoundException;
import architecture.ee.user.User;
import architecture.ee.user.UserManager;
import architecture.ee.user.dao.GroupDao;

public class GroupManagerImpl extends AbstractGroupManager {
	
	private GroupDao groupDao;
	
	private UserManager userManager;
	
	public GroupManagerImpl() {
		super();
	}
	
	

	public void setGroupDao(GroupDao groupDao) {
		this.groupDao = groupDao;
	}



	public void setUserManager(UserManager userManager) {
		this.userManager = userManager;
	}



	protected List<Group> lookupGroupsForUser(User user) {
		
		List<Group> groups = new ArrayList<Group>();
		
		List<Long> groupIds = groupDao.getUserGroupIds(user.getUserId());
		
		for(long id:groupIds){
			Group group;
			try {
				group = lookupGroup(id);
				groups.add(group);
			} catch (GroupNotFoundException e) {
				
			}
			
		}		
		return groups;
	}

    protected Group lookupGroup(String name)
    throws GroupNotFoundException
	{
	    Group g = groupDao.getGroupByName(name, caseInsensitiveGroupNameMatch);
	    if(g == null)
	        throw new GroupNotFoundException((new StringBuilder()).append("No group found for with name ").append(name).toString());
	    else
	        return g;
	}	
	
    protected Group lookupGroup(long groupId) throws GroupNotFoundException
	{
	    if(groupId == -2L)
	        return null ; //new RegisteredUsersGroup();
	    Group g = groupDao.getGroupById(groupId);
	    if(g == null)
	        throw new GroupNotFoundException((new StringBuilder()).append("No group found for with id ").append(groupId).toString());
	    else
	        return g;
	}	
    
    
	
}
