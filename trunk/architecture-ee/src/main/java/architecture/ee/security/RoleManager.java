package architecture.ee.security;

import java.util.List;

import architecture.ee.security.role.Role;

public interface RoleManager {

	public abstract List<Role> getFinalUserRoles(long userId);
		
	public abstract List<Role> getFinalGroupRoles(long groupId);
		
	public abstract List<Role> getAllRoles();
	
}
