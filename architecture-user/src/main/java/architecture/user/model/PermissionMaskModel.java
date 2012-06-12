package architecture.user.model;

import architecture.common.model.ModelObject;
import architecture.user.security.permission.PermissionMask;

/**
 * @author                 donghyuck
 */
public interface PermissionMaskModel extends ModelObject<PermissionMask> {
	
	/**
	 * @return
	 */
	public String getName();

	/**
	 * @return
	 */
	public int getMask();

	/**
	 * @param  name
	 */
	public void setName(String name);

	/**
	 * @param  mask
	 */
	public void setMask(int mask);
	
}
