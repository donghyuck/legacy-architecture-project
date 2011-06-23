package architecture.ee.security.dao;

import java.util.List;

import architecture.ee.security.Permission;
import architecture.ee.security.PermissionMask;
import architecture.ee.user.Group;

public interface PermissionDao {

    public abstract void removeAllUserPermissions(int objectType, long objectId, int permissionType);

    public abstract void removeAllPermissions(int objectType, long objectId);

    public abstract void addGroupPermission(int objectType, long objectId, Group group, int permissionType, int permission);

    public abstract void removeGroupPermission(int objectType, long objectId, Group group, int permissionType, int permission);

    public abstract void removeAllGroupPermissions(int objectType, long objectId, int permissionType);

    public abstract List<Permission> getObjectIdsWithUserPermission(int objectType);

    public abstract List<Permission> getObjectIdsWithGroupPermission(int objectType);

    public abstract List<Permission> getUserPermission(int objectType, long objectId);

    public abstract List<Permission> getGroupPermissions(int objectType, long objectId);

    public abstract void addUserPermission(int objectType, long objectId, long userId, int permissionType, int permission);

    public abstract void removeUserPermission(int objectType, long objectId, long userId, int permissionType, int permission);

    public abstract void addPermissionMask(String name, int mask);

    public abstract void deletePermissionMask(String name);

    public abstract int getPermissionMask(String name);

    public abstract List<PermissionMask> getPermissionsMask();
    
}
