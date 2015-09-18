package architecture.user.permission;

import java.util.Map;

import architecture.common.user.Group;
import architecture.common.user.User;

public interface PermissionsManager {

	//
	//public abstract void addAnonymousUserPermission(PermissionType permissiontype, long l) throws UnAuthorizedException;

	// no
	//public abstract void addGroupPermission(Group group, PermissionType permissiontype, long l) throws UnAuthorizedException;

	public abstract void addGroupPermission(int objectType, long objectId, Group group, PermissionType permissionType, long permission);

	// NO
	//public abstract void addRegisteredUserPermission(PermissionType permissiontype, long l) throws UnAuthorizedException;

	public abstract void addUserPermission(int objectType, long objectId, User user, PermissionType permissionType, long permission);

	// no
	//public abstract void addUserPermission(User user, PermissionType permissiontype, long l) throws UnAuthorizedException;

	//no
	//public abstract boolean anonymousUserHasPermission(PermissionType permissiontype, long l);

	public abstract long assignPermissionMask(String key);

	public abstract Map getAssignedPermissions();

	public abstract Permissions getFinalGroupPerms(int objectType, long objectId, long groupId, PermissionType permissionType);

	public abstract Permissions getFinalUserPerms(int objectType, long objectId, long userId, PermissionType permissionType);

	public abstract long getPermissionMask(String key);

	// no
	//public abstract Iterable groupsWithPermission(PermissionType permissionType, long l);

	// no
	//public abstract int groupsWithPermissionCount(PermissionType permissionType, long l);	
	
	public abstract boolean hasPermissionsSet(int objectType, long objectId);

	//
	//public abstract boolean registeredUserHasPermission(PermissionType permissiontype, long l);
	
	
	

	// no
	//public abstract void removeAllGroupPermissions(PermissionType permissiontype) throws UnAuthorizedException;

	public abstract void removeAllPermissions(int objectType, long objectId);

	//no
	//public abstract void removeAllUserPermissions(PermissionType permissiontype) throws UnAuthorizedException;

	//NO
	//public abstract void removeAnonymousUserPermission(PermissionType permissiontype, long l) throws UnAuthorizedException;

	//no
	//public abstract void removeGroupPermission(Group group, PermissionType permissiontype, long l) throws UnAuthorizedException;

	public abstract void removeGroupPermission(int objectType, long objectId, Group group, PermissionType permissionType, long permission);

	//NO
	//public abstract void removeRegisteredUserPermission(PermissionType permissiontype, long l) throws UnAuthorizedException;

	public abstract void removeUserPermission(int objectType, long objectId, User user, PermissionType permissionType, long permission);

	//NO
	//public abstract void removeUserPermission(User user, PermissionType permissiontype, long l) throws UnAuthorizedException;

	//no
	//public abstract Iterator usersWithPermission(PermissionType permissiontype, long l);

	//no
	//public abstract int usersWithPermissionCount(PermissionType permissiontype, long l);
}
