package architecture.ee.model.internal;

import java.util.Date;

import architecture.ee.security.role.Role;

public class RoleModelImpl extends BaseModelImpl<Role> implements Role {

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

	public long getPrimaryKey() {
		return getRoleId();
	}

	public String toXmlString() {
		return "";
	}

	public int compareTo(Role o) {
		return 0;
	}

	@Override
	public Object clone() {
		return null;
	}

	@Override
	public void setPrimaryKey(long pk) {
		setRoleId(pk);
	}

}
