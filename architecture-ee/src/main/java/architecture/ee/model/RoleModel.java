package architecture.ee.model;

import java.util.Date;

import architecture.ee.security.role.Role;

public interface RoleModel extends ModelObject<Role> {
	
	public abstract long getRoleId();
	
	public abstract void setRoleId(long roleId);
	
	public abstract String getName();
	
	public abstract void setName(String name);
	
	public abstract String getDescription();
	
	public abstract void setDescription(String description);
		
    public abstract Date getCreationDate();

    public abstract Date getModifiedDate();

    public abstract void setCreationDate(Date date);

    public abstract void setModifiedDate(Date date);
    
}
