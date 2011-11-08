package architecture.ee.model.impl;

import java.io.Serializable;

import architecture.ee.model.ModelConstants;
import architecture.ee.model.PermissionMaskModel;
import architecture.ee.security.permission.PermissionMask;

/**
 * @author  donghyuck
 */
public class PermissionMaskModelImpl  extends BaseModelObject<PermissionMask> implements PermissionMaskModel {
	
	/**
	 * @uml.property  name="name"
	 */
	private String name;
	
    /**
	 * @uml.property  name="mask"
	 */
    private int mask;
    
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
	 * @uml.property  name="mask"
	 */
	public int getMask() {
		return mask;
	}
	/**
	 * @param mask
	 * @uml.property  name="mask"
	 */
	public void setMask(int mask) {
		this.mask = mask;
	}

	@Override
	public Object clone() {
		return null;
	}

	public int compareTo(PermissionMask o) {
		return 0;
	}
	public Serializable getPrimaryKeyObject() {
		return null;
	}
	public void setPrimaryKeyObject(Serializable primaryKeyObj) {

	}
	public int getObjectType() {
		return ModelConstants.PERMISSION_MASK;
	}
}
