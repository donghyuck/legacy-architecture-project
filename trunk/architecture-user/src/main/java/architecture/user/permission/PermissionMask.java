package architecture.user.permission;

import architecture.common.model.DateModelObject;

public interface PermissionMask extends DateModelObject {

	public String getName();

	public void setName(String name);
	
	/**
	 * @return
	 */
	public int getMask();

	/**
	 * @param  mask
	 */
	public void setMask(int mask);
	
}
