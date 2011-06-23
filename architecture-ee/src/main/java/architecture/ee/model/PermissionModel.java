package architecture.ee.model;

import architecture.ee.security.Permission;

public interface PermissionModel extends BaseModel<Permission> {

	public int getObjectType();

	public long getObjectId();

	public int getPermission();

	public void setObjectType(int objType);

	public void setObjectId(long objId);

	public void setPermission(int permission);

}
