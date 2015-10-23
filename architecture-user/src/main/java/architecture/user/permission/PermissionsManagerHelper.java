package architecture.user.permission;

import java.util.List;

import architecture.common.user.Group;
import architecture.common.user.User;
import architecture.ee.util.ApplicationHelper;

public class PermissionsManagerHelper {

	private PermissionsManager permissionsManager;
	private PermissionsBundle bundle = new PermissionsBundle();
	int objectType ;
	long objectId ;
	
	
	public PermissionsManagerHelper(int objectType, long objectId) {
		this.objectType = objectType;
		this.objectId = objectId;
	}


	public PermissionsManager getPermissionsManager() {
		
		if(permissionsManager == null)
			return ApplicationHelper.getComponent(PermissionsManager.class);
		
		return permissionsManager;
	}


	public void setPermissionsManager(PermissionsManager permissionsManager) {
		this.permissionsManager = permissionsManager;
	}
	
	public boolean anonymousUserHasPermission(PermissionType permissionType, long permission){
		return permissionsManager.anonymousUserHasPermission(objectType, objectId, permissionType, permission);
	}
	
	public boolean registeredUserHasPermission(PermissionType permissionType, long permission){
		return permissionsManager.registeredUserHasPermission(objectType, objectId, permissionType, permission);
	}
	
	public List<User> usersWithPermission(PermissionType permissionType, long permission){
		return permissionsManager.usersWithPermission(objectType, objectId, permissionType, permission);
	}
	
	public int usersWithPermissionCount(PermissionType permissionType, long permission) {
		return permissionsManager.usersWithPermissionCount(objectType, objectId, permissionType, permission);
	}
	
	public List<Group> groupsWithPermission(PermissionType permissionType, long permission){
		return permissionsManager.groupsWithPermission(objectType, objectId, permissionType, permission);
	}	

	public int groupsWithPermissionCount(PermissionType permissionType, long permission) {
		return permissionsManager.groupsWithPermissionCount(objectType, objectId, permissionType, permission);
	}
}
