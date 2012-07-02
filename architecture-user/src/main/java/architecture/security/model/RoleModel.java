package architecture.security.model;

import architecture.common.model.ModelObject;
import architecture.user.security.role.Role;

/**
 * @author                 donghyuck
 */
public interface RoleModel extends ModelObject<Role> {
	
	/**
	 * @return
	 */
	public abstract long getRoleId();
	
	/**
	 * @param  roleId
	 */
	public abstract void setRoleId(long roleId);
	
	/**
	 * @return
	 */
	public abstract String getName();
	
	/**
	 * @param  name
	 */
	public abstract void setName(String name);
	
	/**
	 * @return
	 */
	public abstract String getDescription();
	
	/**
	 * @param  description
	 */
	public abstract void setDescription(String description);

    
}