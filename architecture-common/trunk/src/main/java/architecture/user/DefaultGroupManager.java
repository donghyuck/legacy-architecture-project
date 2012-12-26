package architecture.user;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import architecture.common.user.User;
import architecture.common.user.UserManager;
import architecture.user.dao.GroupDao;
import architecture.user.model.impl.GroupImpl;

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
        	g.setDescription(name);
        	g.setName(name);
        	Date groupCreateDate = new Date();
        	g.setCreationDate(groupCreateDate);
        	g.setModifiedDate(groupCreateDate);
        	groupDao.createGroup(g);
        	return g;
        }
	}
		
	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
	public Group createGroup(String name, String description) throws GroupAlreadyExistsException {
		try
        {
            getGroup(name);
            throw new GroupAlreadyExistsException();
        }
        catch(GroupNotFoundException unfe)
        {
        	Group g = new GroupImpl();
        	g.setDescription(description);
        	g.setName(name);
        	Date groupCreateDate = new Date();
        	g.setCreationDate(groupCreateDate);
        	g.setModifiedDate(groupCreateDate);
        	groupDao.createGroup(g);        	
        	return g;
        }
	}
		
	public int getTotalGroupCount() {		
		try {
			return groupDao.getGroupCount();
		} catch (Exception e) {
			// TODO 자동 생성된 catch 블록
			log.error(e);
		}
		return 0 ;
	}

	public List<Group> getGroups() {
		List<Long> groupIds = groupDao.getAllGroupIds();
		List<Group> list = new ArrayList<Group>(groupIds.size());
		for( Long groupId : groupIds )
			try {
				list.add(getGroup(groupId));
			} catch (GroupNotFoundException e) {}
		
		log.debug( list );
		
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
	
	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
	public void updateGroup(Group group) throws GroupNotFoundException,
			GroupAlreadyExistsException {		
		
		Group original = groupDao.getGroupById(group.getGroupId());
		if (original == null)
			throw new GroupNotFoundException();
		
		String oldGroupName = null;
		String newGroupName = null;
		
		if( !nameEquals(original, group)){
			try{
			Group checked = getGroup(caseGroupName(group.getName()));
			if( checked.getGroupId() == group.getGroupId() ){
				throw new GroupAlreadyExistsException("Group with this name already exists.");
			}
			}catch(GroupNotFoundException e){
				oldGroupName = original.getName();
				newGroupName = group.getName();
			}
		}
		
		group.setModifiedDate(new Date());
		groupDao.updateGroup(group);
		if( oldGroupName != null && newGroupName != null ){
			groupNameUpdated(oldGroupName);
		}
		clearGroupFromCache(group);		
	
	}

	public List<User> getGroupUsers(Group group) throws GroupNotFoundException {
		// TODO 자동 생성된 메소드 스텁
		return null;
	}

	public List<User> getGroupUsers(Group group, int startIndex, int numResults)
			throws GroupNotFoundException {
		// TODO 자동 생성된 메소드 스텁
		return null;
	}

	public int getTotalGroupUserCount(Group group) {
		// TODO 자동 생성된 메소드 스텁
		return 0;
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
