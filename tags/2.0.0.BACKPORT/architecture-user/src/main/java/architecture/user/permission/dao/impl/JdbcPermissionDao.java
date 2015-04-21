package architecture.user.permission.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.List;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;

import architecture.common.user.Group;
import architecture.ee.spring.jdbc.support.ExtendedJdbcDaoSupport;
import architecture.user.DefaultPermissionMask;
import architecture.user.permission.Permission;
import architecture.user.permission.PermissionMask;
import architecture.user.permission.dao.PermissionDao;

public class JdbcPermissionDao extends ExtendedJdbcDaoSupport implements PermissionDao {

	private final RowMapper<PermissionMask> permsRowMapper = new RowMapper<PermissionMask>(){		
		public PermissionMask mapRow(ResultSet rs, int rowNum) throws SQLException {  			
			DefaultPermissionMask model = new DefaultPermissionMask();
			model.setName(rs.getString("NAME"));
			model.setMask(rs.getInt(2));
			return (PermissionMask)model;
		}};
		
	public void removeAllUserPermissions(int objectType, long objectId, int permissionType) {
		// TODO Auto-generated method stub
		
	}

	public void removeAllPermissions(int objectType, long objectId) {
		// TODO Auto-generated method stub
		
	}

	public void addGroupPermission(int objectType, long objectId, Group group,
			int permissionType, int permission) {
		// TODO Auto-generated method stub
		
	}

	public void removeGroupPermission(int objectType, long objectId,
			Group group, int permissionType, int permission) {
		// TODO Auto-generated method stub
		
	}

	public void removeAllGroupPermissions(int objectType, long objectId,
			int permissionType) {
		// TODO Auto-generated method stub
		
	}

	public List<Permission> getObjectIdsWithUserPermission(int objectType) {
		// TODO Auto-generated method stub
		return null;
	}

	public List<Permission> getObjectIdsWithGroupPermission(int objectType) {
		// TODO Auto-generated method stub
		return null;
	}

	public List<Permission> getUserPermission(int objectType, long objectId) {
		// TODO Auto-generated method stub
		return null;
	}

	public List<Permission> getGroupPermissions(int objectType, long objectId) {

		return null;
	}

	public void addUserPermission(int objectType, long objectId, long userId,
			int permissionType, int permission) {
		
	}

	public void removeUserPermission(int objectType, long objectId,
			long userId, int permissionType, int permission) {
		
	}
	
	
	public void addPermissionMask(String name, int mask) {
		getExtendedJdbcTemplate().update(getBoundSql("FRAMEWORK_V2.CREATE_PERMISSION_MASK").getSql(), 
				new Object[]{name, mask}, 
				new int []{Types.VARCHAR, Types.INTEGER});
		
	}

	public void deletePermissionMask(String name) {
		getExtendedJdbcTemplate().update(getBoundSql("FRAMEWORK_V2.DELETE_PERMISSION_MASK_BY_NAME").getSql(), 
				new Object[]{name}, new int []{Types.VARCHAR});
	}

	public int getPermissionMask(String name) {
		try {
			return getExtendedJdbcTemplate().queryForInt(getBoundSql("FRAMEWORK_V2.SELECT_PERMISSION_MASK_BY_NAME").getSql(),
			    new Object[]{name}, new int [] {Types.VARCHAR}		
			);
		} catch (EmptyResultDataAccessException e) {
			log.debug((new StringBuilder()).append("No mask found for permission '").append(name).append("'").toString());
			return -1;
		}
	}
	
	public List<PermissionMask> getPermissionsMask() {
		return getExtendedJdbcTemplate().query(getBoundSql("FRAMEWORK_V2.SELECT_ALL_PERMISSION_MASK").getSql(), permsRowMapper );
	}
}
