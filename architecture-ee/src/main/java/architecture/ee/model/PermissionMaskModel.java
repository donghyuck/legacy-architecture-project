package architecture.ee.model;

import architecture.ee.security.PermissionMask;

public interface PermissionMaskModel extends BaseModel<PermissionMask> {
	
	public String getName();

	public int getMask();

	public void setName(String name);

	public void setMask(int mask);
}
