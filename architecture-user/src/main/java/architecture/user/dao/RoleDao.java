package architecture.user.dao;

import java.util.List;

import architecture.user.Role;

public interface RoleDao {

    /**
     * 롤 객체를 생성한다.
     */
    public abstract void createRole(Role role);

    /**
     * 롤 정보를 업데이트 한다.
     * 
     * @param role
     */
    public abstract void updateRole(Role role);

    public abstract Role getRoleById(long roleId);

    public abstract Role getRoleByName(String name, boolean flag);

    public abstract void deleteRole(long roleId);

    public abstract void deleteUserRoles(long userId);

    public abstract void deleteGroupRoles(long groupId);

    public abstract int getRoleCount();

    public abstract List<Long> getAllRoleIds();

    public abstract List<Long> getRoleIds(int start, int num);

    public abstract List<Long> getUserRoleIds(long userId);

    public abstract List<Long> getGroupRoleIds(long groupId);

    public abstract void removeUserRole(long roleId, long userId);

    public abstract void removeGroupRole(long roleId, long groupId);

    public abstract void addUserRole(long roleId, long userId);

    public abstract void addGroupRole(long roleId, long groupId);

}
