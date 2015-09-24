package architecture.user.permission.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.List;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.SqlParameterValue;

import architecture.common.user.Group;
import architecture.ee.spring.jdbc.support.ExtendedJdbcDaoSupport;
import architecture.user.DefaultPermissionMask;
import architecture.user.permission.PermissionMask;
import architecture.user.permission.dao.PermissionsDao;

public class JdbcPermissionsDao extends ExtendedJdbcDaoSupport implements PermissionsDao {

	private final RowMapper<PermissionMask> permMaskRowMapper = new RowMapper<PermissionMask>(){		
		public PermissionMask mapRow(ResultSet rs, int rowNum) throws SQLException {  			
			DefaultPermissionMask model = new DefaultPermissionMask();
			model.setName(rs.getString("NAME"));
			model.setMask(rs.getInt(2));
			return (PermissionMask)model;
		}};
		
	private static final RowMapper<Perm> permRowMapper = new RowMapper<Perm>(){
		public Perm mapRow(ResultSet rs, int rowNum) throws SQLException {
			Long objectId = rs.getLong(1);
			int type = rs.getInt(2);
			int permission = rs.getInt(3);
			return new Perm(objectId, type, permission);
		}};	
		
		
	public void removeAllUserPerms(int objectType, long objectId, int permissionType) {
		getExtendedJdbcTemplate().update(getBoundSql("ARCHITECTURE_SECURITY.REMOVE_ALL_USER_PERMS").getSql(),
				new SqlParameterValue(Types.NUMERIC, objectType),
				new SqlParameterValue(Types.NUMERIC, objectId),
				new SqlParameterValue(Types.NUMERIC, permissionType));
	}

	public void removeAllPerms(int objectType, long objectId) {
		getExtendedJdbcTemplate().update(getBoundSql("ARCHITECTURE_SECURITY.REMOVE_ALL_PERMS").getSql(),
				new SqlParameterValue(Types.NUMERIC, objectType),
				new SqlParameterValue(Types.NUMERIC, objectId));
	}

	public void addGroupPerms(int objectType, long objectId, Group group, int permissionType, int permission) {
		getExtendedJdbcTemplate().update(getBoundSql("ARCHITECTURE_SECURITY.ADD_GROUP_PERM").getSql(),
				new SqlParameterValue(Types.NUMERIC, objectType),
				new SqlParameterValue(Types.NUMERIC, objectId),
				new SqlParameterValue(Types.NUMERIC, group.getGroupId()),
				new SqlParameterValue(Types.NUMERIC, permissionType),
				new SqlParameterValue(Types.NUMERIC, permission)				
				);
	}

	public void removeGroupPerms(int objectType, long objectId, Group group, int permissionType, int permission) {
		getExtendedJdbcTemplate().update(getBoundSql("ARCHITECTURE_SECURITY.REMOVE_GROUP_PERM").getSql(),
				new SqlParameterValue(Types.NUMERIC, objectType),
				new SqlParameterValue(Types.NUMERIC, objectId),
				new SqlParameterValue(Types.NUMERIC, group.getGroupId()),
				new SqlParameterValue(Types.NUMERIC, permissionType),
				new SqlParameterValue(Types.NUMERIC, permission));
	}

	public void removeAllGroupPerms(int objectType, long objectId,int permissionType) {
		getExtendedJdbcTemplate().update(getBoundSql("ARCHITECTURE_SECURITY.REMOVE_ALL_GROUP_PERMS").getSql(),
				new SqlParameterValue(Types.NUMERIC, objectType),
				new SqlParameterValue(Types.NUMERIC, objectId),
				new SqlParameterValue(Types.NUMERIC, permissionType));
	}

	public List<Long> getObjectIdsWithUserPerms(int objectType) {
		return getExtendedJdbcTemplate().queryForList(getBoundSql("ARCHITECTURE_SECURITY.OBJECT_IDS_WITH_USER_PERMS").getSql(), 
				Long.class, 
				new SqlParameterValue(Types.NUMERIC, objectType));
	}

	public List<Long> getObjectIdsWithGroupPerms(int objectType) {
		return getExtendedJdbcTemplate().queryForList(getBoundSql("ARCHITECTURE_SECURITY.OBJECT_IDS_WITH_GROUP_PERMS").getSql(), 
				Long.class, 
				new SqlParameterValue(Types.NUMERIC, objectType));
	}

	public List<Perm> getUserPerms(int objectType, long objectId) {
		return getExtendedJdbcTemplate().query(getBoundSql("ARCHITECTURE_SECURITY.SELECT_USER_PERMS").getSql(), 
				permRowMapper,
				new SqlParameterValue(Types.NUMERIC, objectType),
				new SqlParameterValue(Types.NUMERIC, objectId));
	}

	public List<Perm> getGroupPerms(int objectType, long objectId) {
		return getExtendedJdbcTemplate().query(getBoundSql("ARCHITECTURE_SECURITY.SELECT_GROUP_PERMS").getSql(), 
				permRowMapper,
				new SqlParameterValue(Types.NUMERIC, objectType),
				new SqlParameterValue(Types.NUMERIC, objectId));
	}

	public void addUserPerms(int objectType, long objectId, long userId, int permissionType, int permission) {
		getExtendedJdbcTemplate().update(getBoundSql("ARCHITECTURE_SECURITY.ADD_USER_PERM").getSql(),
				new SqlParameterValue(Types.NUMERIC, objectType),
				new SqlParameterValue(Types.NUMERIC, objectId),
				new SqlParameterValue(Types.NUMERIC, userId),
				new SqlParameterValue(Types.NUMERIC, permissionType),
				new SqlParameterValue(Types.NUMERIC, permission)
				);		
	}

	public void removeUserPerms(int objectType, long objectId, long userId, int permissionType, int permission) {
		getExtendedJdbcTemplate().update(getBoundSql("ARCHITECTURE_SECURITY.REMOVE_USER_PERM").getSql(),
				new SqlParameterValue(Types.NUMERIC, objectType),
				new SqlParameterValue(Types.NUMERIC, objectId),
				new SqlParameterValue(Types.NUMERIC, userId),
				new SqlParameterValue(Types.NUMERIC, permissionType),
				new SqlParameterValue(Types.NUMERIC, permission)
				);			
	}
	
	
	
	
	
	public void addPermissionMask(String name, int mask) {
		getExtendedJdbcTemplate().update(getBoundSql("ARCHITECTURE_SECURITY.CREATE_PERM_MASK").getSql(), 
				new SqlParameterValue(Types.VARCHAR, name),
				new SqlParameterValue(Types.INTEGER, mask));
		
	}

	public void deletePermissionMask(String name) {
		getExtendedJdbcTemplate().update(getBoundSql("ARCHITECTURE_SECURITY.DELETE_PERM_MASK_BY_NAME").getSql(), 
				new SqlParameterValue(Types.VARCHAR, name));
	}

	public int getPermissionMask(String name) {
		try {
			return getExtendedJdbcTemplate().queryForObject(getBoundSql("ARCHITECTURE_SECURITY.SELECT_PERM_MASK_BY_NAME").getSql(),
					Integer.class,
					new SqlParameterValue(Types.VARCHAR, name));
		} catch (EmptyResultDataAccessException e) {
			log.debug((new StringBuilder()).append("No mask found for permission '").append(name).append("'").toString());
			return -1;
		}
	}
	
	public List<PermissionMask> getPermissionsMask() {
		return getExtendedJdbcTemplate().query(getBoundSql("ARCHITECTURE_SECURITY.SELECT_ALL_PERM_MASK").getSql(), permMaskRowMapper );
	}
}
