package architecture.user.permission;

public interface PermissionManager {

	
	public abstract Permissions getFinalUserPermissions(int objectType, long objectId, long userId, PermissionType permissionType);
	
	
}
