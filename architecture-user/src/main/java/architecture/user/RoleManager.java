package architecture.user;

import java.util.List;

import architecture.common.user.Group;
import architecture.common.user.User;

public interface RoleManager {

	public abstract List<Role> getFinalUserRoles(long userId);
		
	public abstract List<Role> getFinalGroupRoles(long groupId);
		
	public abstract Role getRole(String name) throws RoleNotFoundException ;
	
	public abstract Role getRole(long roleId) throws RoleNotFoundException ;
	
	public abstract List<Role> getRoles();
	
	public abstract int getTotalRoleCount();
	
	public abstract void createRole(String name, String description);
	
	public abstract void updateRole(Role role) throws RoleNotFoundException, RoleAlreadyExistsException ;
	
	public abstract void removeRole(Role role) throws RoleNotFoundException ;
		
	public abstract void removeUserRole( User user, List<Role> roles);
	
	public abstract void addUserRole( User user, List<Role> roles);
	
	public abstract void removeGroupRole( Group group, List<Role> roles);
	
	public abstract void addGroupRole( Group group, List<Role> roles);
	
	public abstract void removeUserRole( User user, Role role);
	
	public abstract void addUserRole( User user, Role role);
	
	public abstract void removeGroupRole( Group group, Role role);
	
	public abstract void addGroupRole( Group group, Role role);

	public abstract List<Role> getUserRolesFromGroup(long userId);
		
}
