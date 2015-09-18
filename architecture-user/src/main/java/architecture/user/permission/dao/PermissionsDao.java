package architecture.user.permission.dao;

import java.io.Serializable;
import java.util.List;

import architecture.common.user.Group;
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
	
	
    public abstract void removeAllUserPerms(int objectType, long objectId, int permissionType);

    public abstract void removeAllPerms(int objectType, long objectId);

    public abstract void addGroupPerms(int objectType, long objectId, Group group, int permissionType, int permission);

    public abstract void removeGroupPerms(int objectType, long objectId, Group group, int permissionType, int permission);

    public abstract void removeAllGroupPerms(int objectType, long objectId, int permissionType);

    public abstract List<Long> getObjectIdsWithUserPerms(int objectType);

    public abstract List<Long> getObjectIdsWithGroupPerms(int objectType);

    public abstract List<Perm> getUserPerms(int objectType, long objectId);

    public abstract List<Perm> getGroupPerms(int objectType, long objectId);

    public abstract void addUserPerms(int objectType, long objectId, long userId, int permissionType, int permission);

    public abstract void removeUserPerms(int objectType, long objectId, long userId, int permissionType, int permission);

    
    
    
    public abstract void addPermissionMask(String name, int mask);

    public abstract void deletePermissionMask(String name);

    public abstract int getPermissionMask(String name);

    public abstract List<PermissionMask> getPermissionsMask();
    
}
