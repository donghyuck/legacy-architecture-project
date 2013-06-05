package architecture.user;

import architecture.common.model.v2.ModelObject;


public interface Role extends  ModelObject  {

	/**
	 * @return
	 */
	public abstract long getRoleId();
	
	/**
	 * @param  roleId
	 */
	public abstract void setRoleId(long roleId);
	
	/**
	 * @return
	 */
	public int getMask();

	/**
	 * @param  mask
	 */
	public void setMask(int mask);
	
	
}