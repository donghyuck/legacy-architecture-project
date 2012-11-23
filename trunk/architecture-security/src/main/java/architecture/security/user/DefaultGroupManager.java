package architecture.security.user;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import architecture.common.user.UserManager;
import architecture.security.model.impl.GroupImpl;
import architecture.security.user.dao.GroupDao;

/**
 * @author  donghyuck
 */
public class DefaultGroupManager extends AbstractGroupManager {
	
	private GroupDao groupDao;
	
	private UserManager userManager;
	
	public DefaultGroupManager() {
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

	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
	public Group createGroup(String name) throws GroupAlreadyExistsException {
		try
        {
            getGroup(name);
            throw new GroupAlreadyExistsException();
        }
        catch(GroupNotFoundException unfe)
        {
        	Group g = new GroupImpl();
        	g.setName(name);
        	Date groupCreateDate = new Date();
        	g.setCreationDate(groupCreateDate);
        	g.setModifiedDate(groupCreateDate);
        	groupDao.createGroup(g);
        	return g;
        }
	}
		

	public int getGroupCount() {		
		return groupDao.getGroupCount();
	}

	public List<Group> getGroups() {
		List<Long> groupIds = groupDao.getAllGroupIds();
		List<Group> list = new ArrayList<Group>(groupIds.size());
		for( Long groupId : groupIds )
			try {
				list.add(getGroup(groupId));
			} catch (GroupNotFoundException e) {}
		
		return list;
	}

	public List<Group> getGroups(int startIndex, int numResults) {
		List<Long> groupIds = groupDao.getGroupIds(startIndex, numResults );
		List<Group> list = new ArrayList<Group>(groupIds.size());
		for( Long groupId : groupIds )
			try {
				list.add(getGroup(groupId));
			} catch (GroupNotFoundException e) {}
		
		return list;
	}

	@Override
	protected Group lookupGroup(String name) throws GroupNotFoundException {
	    Group g = groupDao.getGroupByName(name, caseInsensitiveGroupNameMatch);
	    if(g == null)
	        throw new GroupNotFoundException((new StringBuilder()).append("No group found for with name ").append(name).toString());
	    else
	        return g;
	}

	@Override
	protected Group lookupGroup(long groupId) throws GroupNotFoundException {
	    if(groupId == -2L)
	        return null ; //new RegisteredUsersGroup();
	    Group group = groupDao.getGroupById(groupId);
	    if(group == null)
	        throw new GroupNotFoundException((new StringBuilder()).append("No group found for with id ").append(groupId).toString());
	    else
	        return group;
	}
	
/*

	protected List<Group> lookupGroupsForUser(User user) {		
		List<Group> groups = new ArrayList<Group>();		
		List<Long> groupIds = groupDao.getUserGroupIds(user.getUserId());
		for(long groupId:groupIds){
			Group group;
			try {
				group = lookupGroup(groupId);
				groups.add(group);
			} catch (GroupNotFoundException e) {				
			}			
		}		
		return groups;
	}

    protected Group lookupGroup(String name) throws GroupNotFoundException
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
	    Group group = groupDao.getGroupById(groupId);
	    if(group == null)
	        throw new GroupNotFoundException((new StringBuilder()).append("No group found for with id ").append(groupId).toString());
	    else
	        return group;
	}

    

	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
	public void updateGroup(Group group) throws GroupNotFoundException,
			GroupAlreadyExistsException {
		
	}

	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
	public void deleteGroup(Group group) throws UnAuthorizedException {
		long groupId = group.getGroupId();
		List<Long> memberIds = group.getMemberIds();
		groupDao.deleteGroupUsers(groupId);		
		groupDao.deleteGroupProperties(groupId);
		groupDao.deleteGroup(groupId);
		groupCache.remove(Long.valueOf(groupId));
		groupIdCache.remove(caseGroupName(group.getName()));
		
		for( long memberId : memberIds ){
			groupMemberCache.remove((new StringBuilder()).append("userGroups-").append(memberId).toString());
		}
		
	}

	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
	public void addMembership(Group group, User user) throws UnAuthorizedException {

	}

	public boolean hasMembership(Group group, User user) throws UnAuthorizedException {
		return false;
	}

	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
	public void removeMembership(Group group, User user)
			throws UnAuthorizedException {
		groupDao.removeMember(group.getGroupId(), user.getUserId());
		
	}

	@Override
	public List<Group> getGroups() {
		// TODO 자동 생성된 메소드 스텁
		return super.getGroups();
	}

	@Override
	public List<Group> getGroups(int startIndex, int numResults) {
		return groupDao.getG.getGroups(startIndex, numResults);
	}
    */

	
}
