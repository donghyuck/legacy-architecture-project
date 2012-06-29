package architecture.security.user.impl;

import java.util.ArrayList;
import java.util.List;

import architecture.common.user.User;
import architecture.user.AbstractGroupManager;
import architecture.user.Group;
import architecture.user.GroupNotFoundException;
import architecture.user.UserManager;
import architecture.user.dao.GroupDao;

/**
 * @author  donghyuck
 */
public class GroupManagerImpl extends AbstractGroupManager {
	
	/**
	 */
	private GroupDao groupDao;
	
	/**
	 */
	private UserManager userManager;
	
	public GroupManagerImpl() {
		super();
	}	

	/**
	 * @param groupDao
	 */
	public void setGroupDao(GroupDao groupDao) {
		this.groupDao = groupDao;
	}



	/**
	 * @param userManager
	 */
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
