package architecture.ee.model;

import java.util.Date;

import architecture.ee.security.role.Role;

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
		
    /**
	 * @return
	 */
    public abstract Date getCreationDate();

    /**
	 * @return
	 */
    public abstract Date getModifiedDate();

    /**
	 * @param  date
	 */
    public abstract void setCreationDate(Date date);

    /**
	 * @param  date
	 */
    public abstract void setModifiedDate(Date date);
    
}
