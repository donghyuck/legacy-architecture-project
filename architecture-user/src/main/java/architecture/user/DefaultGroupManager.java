package architecture.user;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import architecture.common.exception.CodeableException;
import architecture.common.user.Company;
import architecture.common.user.CompanyNotFoundException;
import architecture.common.user.Group;
import architecture.common.user.User;
import architecture.common.user.UserManager;
import architecture.common.user.UserNotFoundException;
import architecture.common.user.authentication.UnAuthorizedException;
import architecture.user.dao.GroupDao;
import architecture.user.util.CompanyUtils;

import com.google.common.collect.Sets;

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
        	Group g = new DefaultGroup();
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
	public Group createGroup(String name, String displayName) throws GroupAlreadyExistsException {
		try
        {
            getGroup(name);
            throw new GroupAlreadyExistsException();
        }
        catch(GroupNotFoundException unfe)
        {
        	Group g = new DefaultGroup();
        	g.setDisplayName(displayName);
        	g.setDescription(displayName);
        	g.setName(name);
        	Date groupCreateDate = new Date();
        	g.setCreationDate(groupCreateDate);
        	g.setModifiedDate(groupCreateDate);
        	groupDao.createGroup(g);        	
        	return g;
        }
	}
	
	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
	public Group createGroup(String name, String displayName, Company company) throws GroupAlreadyExistsException {
		try
        {
            getGroup(name);
            throw new GroupAlreadyExistsException();
        }
        catch(GroupNotFoundException unfe)
        {
        	Group g = new DefaultGroup();
        	g.setDisplayName(displayName);
        	g.setDescription(displayName);
        	g.setName(name);
        	g.setCompanyId(company.getCompanyId());
        	g.setCompany(company);
        	Date groupCreateDate = new Date();
        	g.setCreationDate(groupCreateDate);
        	g.setModifiedDate(groupCreateDate);
        	groupDao.createGroup(g);        	
        	return g;
        }
	}
	
	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
	public Group createGroup(String name, String displayName, String description, Company company) throws GroupAlreadyExistsException {
		try
        {
            getGroup(name);
            throw new GroupAlreadyExistsException();
        }
        catch(GroupNotFoundException unfe)
        {
        	Group g = new DefaultGroup();
        	g.setDisplayName(displayName);
        	g.setDescription(description);
        	g.setName(name);
        	g.setCompanyId(company.getCompanyId());
        	g.setCompany(company);
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
	        throw CodeableException.newException(GroupNotFoundException.class, 5142, name);//new GroupNotFoundException((new StringBuilder()).append("No group found for with name ").append(name).toString());
	    if(g.getCompanyId() != -1L){
	    	try {
				g.setCompany(CompanyUtils.getCompany(g.getCompanyId()));
			} catch (CompanyNotFoundException e) {
			}
	    }
	    return g;
	}


	@Override
	protected Group lookupGroup(long groupId) throws GroupNotFoundException {
	    if(groupId == -2L)
	        return null ; //new RegisteredUsersGroup();
	    Group group = groupDao.getGroupById(groupId);
	    if(group == null)
	        throw CodeableException.newException(GroupNotFoundException.class, 5141, groupId); //new GroupNotFoundException((new StringBuilder()).append("No group found for with id ").append(groupId).toString());
	    if(group.getCompanyId() != -1L){
	    	try {
	    		group.setCompany(CompanyUtils.getCompany(group.getCompanyId()));
			} catch (CompanyNotFoundException e) {
			}
	    }
        return group;
	}
	
	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
	public void updateGroup(Group group) throws GroupNotFoundException, GroupAlreadyExistsException {
		Group original = groupDao.getGroupById(group.getGroupId());
		if (original == null)
			throw CodeableException.newException(GroupAlreadyExistsException.class, 5141, group.getGroupId()); //new GroupNotFoundException();		
		String oldGroupName = null;
		String newGroupName = null;		
		if( !nameEquals(original, group)){
			try{
			Group checked = getGroup(caseGroupName(group.getName()));
			if( checked.getGroupId() == group.getGroupId() ){
				throw CodeableException.newException(GroupAlreadyExistsException.class, 5143); //new GroupAlreadyExistsException("Group with this name already exists.");
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

	
	public List<User> getGroupUsers(Group group) {		
		List<Long> userIds = groupDao.getMembersIds(group.getGroupId());
		List<User> list = new ArrayList<User>(userIds.size());
		for( Long userId : userIds ){
			try {
				list.add(userManager.getUser(userId));
			} catch (UserNotFoundException e) {
				// 잘못된 데이터이다.
			}
		}		
		return list;
	}

	public List<User> getGroupUsers(Group group, int startIndex, int numResults) {
		List<Long> userIds = groupDao.getMembersIds(group.getGroupId(), startIndex, numResults );
		List<User> list = new ArrayList<User>(userIds.size());
		for( Long userId : userIds ){
			try {
				list.add(userManager.getUser(userId));
			} catch (UserNotFoundException e) {
				// 잘못된 데이터이다.
			}
		}		
		return list;
	}

	public int getTotalGroupUserCount(Group group) {
		return groupDao.getGroupMemberCount(group.getGroupId());
	}

	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
	public void addMembership(Group group, User user) throws UnAuthorizedException {
		groupDao.addMember(group.getGroupId(), user.getUserId());
	}

	public boolean hasMembership(Group group, User user) throws UnAuthorizedException {
		return groupDao.isMember(group.getGroupId(), user.getUserId());
	}

	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
	public void removeMembership(Group group, User user)
			throws UnAuthorizedException {
		groupDao.removeMember(group.getGroupId(), user.getUserId());
		
	}

	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
	public void addMembership(Group group, List<User> users)
			throws UnAuthorizedException {
		Set<Long> userIds = Sets.newHashSetWithExpectedSize(users.size());
		
		for( User u : users)
			userIds.add(u.getUserId());
		
		groupDao.addMembers(group.getGroupId(), userIds);
		
	}

	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
	public void removeMembership(Group group, List<User> users)
			throws UnAuthorizedException {
		Set<Long> userIds = Sets.newHashSetWithExpectedSize(users.size());
		
		for( User u : users)
			userIds.add(u.getUserId());
		
		groupDao.removeMembers(group.getGroupId(), userIds);		
	}
		
	public List<Group> getUserGroups(User user) {		
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


	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
	public void deleteGroup(Group group) throws UnAuthorizedException {
		long groupId = group.getGroupId();
		List<Long> memberIds = group.getMemberIds();
		/**
		groupDao.deleteGroupUsers(groupId);		
		groupDao.deleteGroupProperties(groupId);
		groupDao.deleteGroup(groupId);
		groupCache.remove(Long.valueOf(groupId));
		groupIdCache.remove(caseGroupName(group.getName()));
		*/
		for( long memberId : memberIds ){
			//groupMemberCache.remove((new StringBuilder()).append("userGroups-").append(memberId).toString());
		}
		
	}

	
/*
	    protected Group lookupGroup(String name) throws RoleNotFoundException
	{
	    Group g = groupDao.getGroupByName(name, caseInsensitiveGroupNameMatch);
	    if(g == null)
	        throw new RoleNotFoundException((new StringBuilder()).append("No group found for with name ").append(name).toString());
	    else
	        return g;
	}	
	
    protected Group lookupGroup(long groupId) throws RoleNotFoundException
	{
	    if(groupId == -2L)
	        return null ; //new RegisteredUsersGroup();
	    Group group = groupDao.getGroupById(groupId);
	    if(group == null)
	        throw new RoleNotFoundException((new StringBuilder()).append("No group found for with id ").append(groupId).toString());
	    else
	        return group;
	}


	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
	public void updateGroup(Group group) throws RoleNotFoundException,
			RoleAlreadyExistsException {
		
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
