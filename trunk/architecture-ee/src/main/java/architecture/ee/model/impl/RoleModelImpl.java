package architecture.ee.model.impl;

import java.io.Serializable;
import java.util.Date;

import architecture.ee.model.ModelConstants;
import architecture.ee.model.RoleModel;
import architecture.ee.security.role.Role;

/**
 * @author  donghyuck
 */
public class RoleModelImpl extends BaseModelObject<Role>  implements RoleModel {

	/**
	 * @uml.property  name="roleId"
	 */
	private long roleId;
	/**
	 * @uml.property  name="name"
	 */
	private String name;
	/**
	 * @uml.property  name="description"
	 */
	private String description;
	/**
	 * @uml.property  name="creationDate"
	 */
	private Date creationDate;
	/**
	 * @uml.property  name="modifiedDate"
	 */
	private Date modifiedDate;

	/**
	 * @return
	 * @uml.property  name="roleId"
	 */
	public long getRoleId() {
		return roleId;
	}

	/**
	 * @param roleId
	 * @uml.property  name="roleId"
	 */
	public void setRoleId(long roleId) {
		this.roleId = roleId;
	}

	/**
	 * @return
	 * @uml.property  name="name"
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 * @uml.property  name="name"
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return
	 * @uml.property  name="description"
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description
	 * @uml.property  name="description"
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @return
	 * @uml.property  name="creationDate"
	 */
	public Date getCreationDate() {
		return creationDate;
	}

	/**
	 * @param creationDate
	 * @uml.property  name="creationDate"
	 */
	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	/**
	 * @return
	 * @uml.property  name="modifiedDate"
	 */
	public Date getModifiedDate() {
		return modifiedDate;
	}

	/**
	 * @param modifiedDate
	 * @uml.property  name="modifiedDate"
	 */
	public void setModifiedDate(Date modifiedDate) {
		this.modifiedDate = modifiedDate;
	}

	public int compareTo(Role o) {
		return 0;
	}

	public Serializable getPrimaryKeyObject() {
		return getRoleId();
	}

	public void setPrimaryKeyObject(Serializable primaryKeyObj) {
		setRoleId(((Long)primaryKeyObj).longValue());
	}

	public int getObjectType() {
		return ModelConstants.ROLE;
	}

	public int compareTo(RoleModel o) {
		return 0;
	}

	@Override
	public Object clone() {
		return null;
	}
}
