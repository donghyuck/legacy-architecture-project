package architecture.ee.model.internal;

import architecture.ee.security.Permission;

public class PermissionModelImpl extends BaseModelImpl<Permission> implements Permission {
	
    /**
	 * 
	 */
	private static final long serialVersionUID = -4796215202307660687L;
	private long objectId;
    private int objectType;
    private int permission;
    
	public long getObjectId() {
		return objectId;
	}
	public void setObjectId(long objectId) {
		this.objectId = objectId;
	}
	public int getObjectType() {
		return objectType;
	}
	public void setObjectType(int objectType) {
		this.objectType = objectType;
	}
	public int getPermission() {
		return permission;
	}
	public void setPermission(int permission) {
		this.permission = permission;
	}
	public long getPrimaryKey() {
		return -1L;
	}
	public String toXmlString() {
		return null;
	}
	public int compareTo(Permission o) {
		return 0;
	}
	@Override
	public Object clone() {
		return null;
	}
	@Override
	public void setPrimaryKey(long pk) {		
	}
    
    
}
