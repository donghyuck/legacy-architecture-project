package architecture.ee.model;

import java.util.Date;

import architecture.ee.security.role.Role;

/**
 * @author                 donghyuck
 */
public interface RoleModel extends ModelObject<Role> {
	
	/**
	 * @return
	 * @uml.property  name="roleId"
	 */
	public abstract long getRoleId();
	
	/**
	 * @param  roleId
	 * @uml.property  name="roleId"
	 */
	public abstract void setRoleId(long roleId);
	
	/**
	 * @return
	 * @uml.property  name="name"
	 */
	public abstract String getName();
	
	/**
	 * @param  name
	 * @uml.property  name="name"
	 */
	public abstract void setName(String name);
	
	/**
	 * @return
	 * @uml.property  name="description"
	 */
	public abstract String getDescription();
	
	/**
	 * @param  description
	 * @uml.property  name="description"
	 */
	public abstract void setDescription(String description);
		
    /**
	 * @return
	 * @uml.property  name="creationDate"
	 */
    public abstract Date getCreationDate();

    /**
	 * @return
	 * @uml.property  name="modifiedDate"
	 */
    public abstract Date getModifiedDate();

    /**
	 * @param  date
	 * @uml.property  name="creationDate"
	 */
    public abstract void setCreationDate(Date date);

    /**
	 * @param  date
	 * @uml.property  name="modifiedDate"
	 */
    public abstract void setModifiedDate(Date date);
    
}
