package architecture.security.role;

import java.util.List;

public interface RoleManager {

	public abstract List<Role> getFinalUserRoles(long userId);
		
	public abstract List<Role> getFinalGroupRoles(long groupId);
		
	public abstract List<Role> getAllRoles();
	
}
