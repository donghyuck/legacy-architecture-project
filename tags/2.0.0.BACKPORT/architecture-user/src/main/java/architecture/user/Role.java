package architecture.user;

import architecture.common.model.BaseModelObject;


public interface Role extends  BaseModelObject  {

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
	
	public String getDescription();
	
	public void setDescription(String description);
	
}