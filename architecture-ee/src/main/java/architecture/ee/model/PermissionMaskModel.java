package architecture.ee.model;

import architecture.ee.security.permission.PermissionMask;

/**
 * @author                 donghyuck
 */
public interface PermissionMaskModel extends ModelObject<PermissionMask> {
	
	/**
	 * @return
	 * @uml.property  name="name"
	 */
	public String getName();

	/**
	 * @return
	 * @uml.property  name="mask"
	 */
	public int getMask();

	/**
	 * @param  name
	 * @uml.property  name="name"
	 */
	public void setName(String name);

	/**
	 * @param  mask
	 * @uml.property  name="mask"
	 */
	public void setMask(int mask);
	
}
