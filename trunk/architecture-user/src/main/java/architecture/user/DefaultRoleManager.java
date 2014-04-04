package architecture.user;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import net.sf.ehcache.Cache;
import net.sf.ehcache.Element;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import architecture.common.event.api.EventPublisher;
import architecture.common.event.api.EventSource;
import architecture.common.exception.CodeableException;
import architecture.common.user.User;
import architecture.common.user.UserTemplate;
import architecture.ee.component.admin.AdminHelper;
import architecture.user.dao.RoleDao;
import architecture.user.spi.ExtendedRoleManager;

/**
 * @author  donghyuck
 */
public class DefaultRoleManager implements ExtendedRoleManager, EventSource {

	private Log log = LogFactory.getLog(getClass());

	private boolean caseInsensitiveRoleNameMatch = true;
	
	private GroupManager groupManager;

	private RoleDao roleDao;

	private EventPublisher eventPublisher;
	
	private Cache roleCache;

	protected Cache roleIdCache ;
	
	//private Cache userRoleCache;
		
	//private Cache groupRoleCache;	
	
	public DefaultRoleManager() {
		this.roleCache = AdminHelper.getCache("roleCache");
		//this.userRoleCache = AdminHelper.getCache("userRoleCache");
		//this.groupRoleCache = AdminHelper.getCache("groupRoleCache");
		this.roleIdCache = AdminHelper.getCache("roleIdCache");
	}

		
	/**
	 * @param roleCache
	 */
	public void setRoleCache(Cache roleCache) {
		this.roleCache = roleCache;
	}

	/**
	 * @param roleIdCache
	 */
	public void setRoleIdCache(Cache roleIdCache) {
		this.roleIdCache = roleIdCache;
	}

	/**
	 * @param groupManager
	 */
	public void setGroupManager(GroupManager groupManager) {
		this.groupManager = groupManager;
	}
	/**
	 * @param roleDao
	 */
	public void setRoleDao(RoleDao roleDao) {
		this.roleDao = roleDao;
	}
	/**
	 * @param eventPublisher
	 */
	public void setEventPublisher(EventPublisher eventPublisher) {
		this.eventPublisher = eventPublisher;
	}
	
	public List<Role> getUserRolesFromGroup(long userId){
		List<Role> roles = new ArrayList<Role>();		
		List<Group> groups = groupManager.getUserGroups(new UserTemplate(userId));			
			for(Group group : groups ){			
				List<Long> groupRoleIds = roleDao.getGroupRoleIds(group.getGroupId());
				for(long roleId : groupRoleIds ){
					try {
						Role r = getRole(roleId) ;
						roles.add( r );
					} catch (RoleNotFoundException e) { }
				}
			}		
			return roles;
	}
		
	public List<Role> getFinalUserRoles(long userId) {	
		
		List<Role> roles = new ArrayList<Role>();		
		List<Long> userRoleIds = roleDao.getUserRoleIds(userId);
		for(long roleId : userRoleIds ){
			try {
				roles.add( getRole(roleId) );
			} catch (RoleNotFoundException e) { }
		}
		
		List<Group> groups = groupManager.getUserGroups(new UserTemplate(userId));
		
		for(Group group : groups ){			
			List<Long> groupRoleIds = roleDao.getGroupRoleIds(group.getGroupId());
			for(long roleId : groupRoleIds ){
				try {
					Role r = getRole(roleId) ;
					roles.add( r );
				} catch (RoleNotFoundException e) { }
			}
		}		
		return roles;
	}
	
	public List<Role> getFinalGroupRoles(long groupId) {
		List<Role> roles = new ArrayList<Role>();	
		List<Long> groupRoleIds = roleDao.getGroupRoleIds(groupId);
		for(long roleId : groupRoleIds ){
			try {
				roles.add( getRole(roleId) );
			} catch (RoleNotFoundException e) { }
		}
		return roles;
	}
	

	public List<Role> getRoles() {
		List<Role> roles = new ArrayList<Role>();		
		List<Long> roleIds = roleDao.getAllRoleIds();
		for(long roleId : roleIds ){
			try {
				roles.add( getRole(roleId) );
			} catch (RoleNotFoundException e) { }			
		}
		return roles;
	}

	public int getTotalRoleCount() {		
		return roleDao.getRoleCount();
	}

	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
	public void createRole(String name, String description) {
		Role newRole = new DefaultRole();
		newRole.setName(name);
		newRole.setDescription(description);
		newRole.setMask(0);		
		roleDao.createRole(newRole);	
	}


	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
	public void updateRole(Role role) throws RoleNotFoundException, RoleAlreadyExistsException {		
		Role original = roleDao.getRoleById(role.getRoleId());
		if (original == null)
			throw CodeableException.newException(RoleNotFoundException.class, 5152, role.getRoleId() ); //throw new RoleNotFoundException();		

		String oldRoleName = null;
		String newRoleName = null;
		
		if( !nameEquals(original, role)){
			try{
			Role checked = getRole(caseRoleName(role.getName()));
			if( checked.getRoleId() == role.getRoleId() ){				
				throw CodeableException.newException(RoleAlreadyExistsException.class, 5151); //new RoleAlreadyExistsException("Role with this name already exists.");
			}
			}catch(RoleNotFoundException e){
				oldRoleName = original.getName();
				newRoleName = role.getName();
			}
		}
		
		role.setModifiedDate(new Date());
		roleDao.updateRole(role);
		if( oldRoleName != null && newRoleName != null ){
			roleNameUpdated(oldRoleName);
		}
		clearRoleFromCache(role);			
	}


	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
	public void removeRole(Role role) throws RoleNotFoundException {		
		Role original = roleDao.getRoleById(role.getRoleId());
		if (original == null)
			throw new RoleNotFoundException();		
		roleDao.deleteRole(role.getRoleId());		
		clearRoleFromCache(role);		
		roleIdCache.remove(caseRoleName(role.getName()));		
	}

	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
	public void removeUserRole(User user, List<Role> roles) {		
		for( Role role : roles )
			roleDao.removeUserRole(role.getRoleId(), user.getUserId());		
	}

	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
	public void addUserRole(User user, List<Role> roles) {		
		for( Role role : roles )
			roleDao.addUserRole(role.getRoleId(), user.getUserId());		
	}

	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
	public void removeGroupRole(Group group, List<Role> roles) {		
		for( Role role : roles )
			roleDao.removeGroupRole(role.getRoleId(), group.getGroupId());		
	}

	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
	public void addGroupRole(Group group, List<Role> roles) {		
		for( Role role : roles )
			roleDao.addGroupRole(role.getRoleId(), group.getGroupId());		
	}

	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
	public void removeUserRole(User user, Role role) {		
		roleDao.removeUserRole(role.getRoleId(), user.getUserId());
	}

	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
	public void addUserRole(User user, Role role) {		
		roleDao.addUserRole(role.getRoleId(), user.getUserId());
	}

	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
	public void removeGroupRole(Group group, Role role) {		
		roleDao.removeGroupRole(role.getRoleId(), group.getGroupId());
	}

	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
	public void addGroupRole(Group group, Role role) {		
		roleDao.addGroupRole(role.getRoleId(), group.getGroupId());
	}

	public Role getRole(String name) throws RoleNotFoundException {
		String nameToUse = caseRoleName(name);
		if (roleIdCache.get(nameToUse) != null) {
			log.debug(roleIdCache.get(nameToUse));
			Long groupId = (Long) roleIdCache.get(nameToUse).getValue();
			return getRole(groupId);
		} else {
			Role r = lookupRole(nameToUse);
			roleIdCache.put(new Element(nameToUse, r.getRoleId()));
			return getRole(r.getRoleId());
		}
	}

	public Role getRole(long roleId) throws RoleNotFoundException {
		Role role = getRoleInCache(roleId);
		if (role == null) {
			role = lookupRole(roleId);
			roleCache.put(new Element(roleId, role));
		}
		return role;
	}

	protected Role getRoleInCache(long roleId) {
		if (roleCache.get(roleId) != null)
			return (Role) roleCache.get(roleId).getValue();
		else
			return null;
	}

    protected String caseRoleName(String name)
    {
        return caseInsensitiveRoleNameMatch ? name.toUpperCase() : name;
    }
    
	protected Role lookupRole(String name) throws RoleNotFoundException {
	    Role r = roleDao.getRoleByName(name, caseInsensitiveRoleNameMatch);
	    if(r == null)
	        throw  CodeableException.newException(RoleNotFoundException.class, 5153, name);//new RoleNotFoundException((new StringBuilder()).append("No role found for with name ").append(name).toString());
	    else
	        return r;
	}

	protected Role lookupRole(long roleId) throws RoleNotFoundException {
	    if(roleId == -2L)
	        return null ; 
	    Role role = roleDao.getRoleById(roleId);
	    if(role == null)
	        throw  CodeableException.newException(RoleNotFoundException.class, 5152, roleId); //new RoleNotFoundException((new StringBuilder()).append("No role found for with id ").append(roleId).toString());
	    else
	        return role;
	}
    
    protected void roleNameUpdated(String oldRoleName){
        roleIdCache.remove(caseRoleName(oldRoleName));	
    }
    
    protected void clearRoleFromCache(Role role){
    	roleCache.remove(role.getRoleId());
    }
    
    protected boolean nameEquals(Role g1, Role g2){
    	return g1.getName() != null && g2.getName() != null && caseRoleName(g1.getName()).equals(caseRoleName(g2.getName()));
    }
    
}