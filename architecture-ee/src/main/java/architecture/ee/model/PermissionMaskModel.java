package architecture.ee.model;

import architecture.ee.security.permission.PermissionMask;

public interface PermissionMaskModel extends ModelObject<PermissionMask> {
	
	public String getName();

	public int getMask();

	public void setName(String name);

	public void setMask(int mask);
	
}
