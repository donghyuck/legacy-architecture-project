package architecture.user.permission.dao;

import java.io.Serializable;
import java.util.List;

import architecture.common.user.Group;
import architecture.user.permission.Permission;
import architecture.user.permission.PermissionMask;

public interface PermissionsDao {

	public static class Perm implements Serializable {
		
		private long objectId;
		private int type;
		private int permission;
		
		
		public Perm(long objectId, int type, int permission) {
			this.objectId = objectId;
			this.type = type;
			this.permission = permission;
		}
		
		public long getObjectId() {
			return objectId;
		}
		public void setObjectId(long objectId) {
			this.objectId = objectId;
		}
		public int getType() {
			return type;
		}
		public void setType(int type) {
			this.type = type;
		}
		public int getPermission() {
			return permission;
		}
		public void setPermission(int permission) {
			this.permission = permission;
		}
		
		
	}
	
	
    public abstract void removeAllUserPermissions(int objectType, long objectId, int permissionType);

    public abstract void removeAllPermissions(int objectType, long objectId);

    public abstract void addGroupPermission(int objectType, long objectId, Group group, int permissionType, int permission);

    public abstract void removeGroupPermission(int objectType, long objectId, Group group, int permissionType, int permission);

    public abstract void removeAllGroupPermissions(int objectType, long objectId, int permissionType);

    public abstract List<Long> getObjectIdsWithUserPermission(int objectType);

    public abstract List<Long> getObjectIdsWithGroupPermission(int objectType);

    public abstract List<Perm> getUserPermissions(int objectType, long objectId);

    public abstract List<Perm> getGroupPermissions(int objectType, long objectId);

    public abstract void addUserPermission(int objectType, long objectId, long userId, int permissionType, int permission);

    public abstract void removeUserPermission(int objectType, long objectId, long userId, int permissionType, int permission);

    
    
    
    public abstract void addPermissionMask(String name, int mask);

    public abstract void deletePermissionMask(String name);

    public abstract int getPermissionMask(String name);

    public abstract List<PermissionMask> getPermissionsMask();
    
}
