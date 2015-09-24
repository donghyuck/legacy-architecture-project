package architecture.user.permission;

import java.util.Map;

import architecture.common.user.Group;
import architecture.common.user.User;

public interface PermissionsManager {

	public abstract void addGroupPermission(int objectType, long objectId, Group group, PermissionType permissionType, long permission);

	public abstract void addUserPermission(int objectType, long objectId, User user, PermissionType permissionType, long permission);

	public abstract long assignPermissionMask(String key);

	public abstract Map getAssignedPermissions();

	public abstract Permissions getFinalGroupPerms(int objectType, long objectId, long groupId, PermissionType permissionType);

	public abstract Permissions getFinalUserPerms(int objectType, long objectId, long userId, PermissionType permissionType);

	public abstract long getPermissionMask(String key);

	public abstract boolean hasPermissionsSet(int objectType, long objectId);

	public abstract void removeAllPermissions(int objectType, long objectId);

	public abstract void removeGroupPermission(int objectType, long objectId, Group group, PermissionType permissionType, long permission);

	public abstract void removeUserPermission(int objectType, long objectId, User user, PermissionType permissionType, long permission);

}
