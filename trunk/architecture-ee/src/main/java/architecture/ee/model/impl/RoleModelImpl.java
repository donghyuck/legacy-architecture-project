package architecture.ee.model.impl;

import java.io.Serializable;
import java.util.Date;

import architecture.ee.model.ModelConstants;
import architecture.ee.model.RoleModel;
import architecture.ee.security.role.Role;

public class RoleModelImpl extends BaseModelObject<Role>  implements RoleModel {

	private long roleId;
	private String name;
	private String description;
	private Date creationDate;
	private Date modifiedDate;

	public long getRoleId() {
		return roleId;
	}

	public void setRoleId(long roleId) {
		this.roleId = roleId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Date getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	public Date getModifiedDate() {
		return modifiedDate;
	}

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
