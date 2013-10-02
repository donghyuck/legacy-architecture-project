package architecture.user.permission;

import architecture.common.model.SimpleModelObject;

public interface PermissionMask extends SimpleModelObject {

	/**
	 * @return
	 */
	public int getMask();

	/**
	 * @param  mask
	 */
	public void setMask(int mask);
	
}
