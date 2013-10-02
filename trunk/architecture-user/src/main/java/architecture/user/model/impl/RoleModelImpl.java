package architecture.user.model.impl;

import java.io.Serializable;
import java.util.Date;

import architecture.common.model.ModelObjectType;
import architecture.common.model.impl.BaseModelObject;
import architecture.user.Role;
import architecture.user.model.RoleModel;

/**
 * @author  donghyuck
 */
public class RoleModelImpl extends SimpleModelObject<Role>  implements RoleModel {

	/**
	 */
	private long roleId;
	/**
	 */
	private String name;
	/**
	 */
	private String description;
	/**
	 */
	private Date creationDate;
	/**
	 */
	private Date modifiedDate;

	
	private int mask;
	
	/**
	 * @return
	 */
	public long getRoleId() {
		return roleId;
	}

	/**
	 * @param roleId
	 */
	public void setRoleId(long roleId) {
		this.roleId = roleId;
	}

	/**
	 * @return
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @return
	 */
	public Date getCreationDate() {
		return creationDate;
	}

	/**
	 * @param creationDate
	 */
	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	/**
	 * @return
	 */
	public Date getModifiedDate() {
		return modifiedDate;
	}

	/**
	 * @param modifiedDate
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

	public ModelObjectType getModelObjectType() {
		return ModelObjectType.ROLE ;
	}

	public int compareTo(RoleModel o) {
		return 0;
	}

	@Override
	public Object clone() {
		return null;
	}

	public int getCachedSize() {
		return 0;
	}

	public int getMask() {
		return mask;
	}

	public void setMask(int mask) {
		this.mask = mask;		
	}
}
