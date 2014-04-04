package architecture.user.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Date;
import java.util.List;

import org.springframework.jdbc.core.RowMapper;

import architecture.common.jdbc.incrementer.MaxValueIncrementer;
import architecture.ee.spring.jdbc.support.ExtendedJdbcDaoSupport;
import architecture.user.DefaultRole;
import architecture.user.Role;
import architecture.user.dao.RoleDao;

/**
 * @author  donghyuck
 */
public class JdbcRoleDao extends ExtendedJdbcDaoSupport implements RoleDao {

	private final RowMapper<Role> roleRowMapper = new RowMapper<Role>(){
		public Role mapRow(ResultSet rs, int rowNum) throws SQLException {  			
			DefaultRole model = new DefaultRole();			
			model.setRoleId( rs.getLong("ROLE_ID") );
			model.setName(rs.getString("NAME"));
			model.setMask(rs.getInt("MASK"));
			model.setDescription(rs.getString("DESCRIPTION"));
			model.setCreationDate(rs.getDate("CREATION_DATE"));
			model.setModifiedDate(rs.getDate("MODIFIED_DATE"));					
			return (Role) model;
		}};
	
	private String sequencerName = "Role";


	private MaxValueIncrementer sequenceDao;
	
	/**
	 * @param sequenceDao
	 */
	public void setMaxValueIncrementer(MaxValueIncrementer sequenceDao) {
		this.sequenceDao = sequenceDao;
	}
	
	/**
	 * @param sequencerName
	 */
	public void setSequencerName(String sequencerName) {
		this.sequencerName = sequencerName;
	}

	public void createRole(Role role) {
	
		long id = sequenceDao.nextLongValue(sequencerName);
		if("".equals(role.getDescription()))
			role.setDescription(null);	
		
		Date now = new Date(System.currentTimeMillis());
		role.setCreationDate(now);
		role.setModifiedDate(now);
		
		getExtendedJdbcTemplate().update(
			getBoundSql("ARCHITECTURE_SECURITY.CREATE_ROLE").getSql(), 
			new Object[]{id, role.getName(), role.getMask(),  role.getDescription(), role.getCreationDate(), role.getModifiedDate()}, 
			new int[]{Types.INTEGER, Types.VARCHAR, Types.INTEGER, Types.VARCHAR, Types.DATE, Types.DATE });
			
		role.setRoleId(id);

	}

	public void deleteRole(long roleId) {
		
		getExtendedJdbcTemplate().update(
			getBoundSql("ARCHITECTURE_SECURITY.DELETE_GROUP_ROLES").getSql(), 
			new Object[]{roleId}, new int[]{Types.INTEGER});
		
		getExtendedJdbcTemplate().update(
			getBoundSql("ARCHITECTURE_SECURITY.DELETE_USER_ROLES").getSql(), 
			new Object[]{roleId}, new int[]{Types.INTEGER});
		
		getExtendedJdbcTemplate().update(
			getBoundSql("ARCHITECTURE_SECURITY.DELETE_ROLE").getSql(), 
			new Object[]{roleId}, new int[]{Types.INTEGER});
	
	}

	public void updateRole(Role role) {
		getExtendedJdbcTemplate().update(getBoundSql("ARCHITECTURE_SECURITY.UPDATE_ROLE").getSql(), 
			new Object[]{role.getName(), role.getMask(), role.getDescription(), role.getModifiedDate(), role.getRoleId()}, 
			new int[]{Types.VARCHAR, Types.INTEGER, Types.VARCHAR, Types.DATE, Types.INTEGER});
	}

	public Role getRoleById(long roleId) {
		Role role = getExtendedJdbcTemplate().queryForObject(
				getBoundSql("ARCHITECTURE_SECURITY.SELECT_ROLE_BY_ID").getSql(), 
				new Object[]{roleId}, new int[]{Types.INTEGER}, roleRowMapper);		
		return role;
	}

	public Role getRoleByName(String name, boolean caseInsensitive) {
		String n = caseInsensitive ? name.toLowerCase() : name;
		Role role = getExtendedJdbcTemplate().queryForObject(
			getBoundSqlWithAdditionalParameter("ARCHITECTURE_SECURITY.SELECT_ROLE_BY_NAME", caseInsensitive).getSql(), 
			new Object[]{n}, new int[]{Types.VARCHAR}, roleRowMapper);				
		return role;
	}
		
	public void deleteUserRoles(long userId) {
		getExtendedJdbcTemplate().update(
			getBoundSql("ARCHITECTURE_SECURITY.REMOVE_USER_ROLES").getSql(), 
			new Object[]{userId}, 
			new int[]{Types.INTEGER});			
	}

	public void deleteGroupRoles(long groupId) {
		getExtendedJdbcTemplate().update(
			getBoundSql("ARCHITECTURE_SECURITY.REMOVE_USER_ROLES").getSql(), 
			new Object[]{groupId}, 
			new int[]{Types.INTEGER});			
	}

	public int getRoleCount() {
		return getExtendedJdbcTemplate().queryForInt(getBoundSql("ARCHITECTURE_SECURITY.COUNT_ALL_ROLES").getSql());
	}

	public List<Long> getAllRoleIds() {
		return getExtendedJdbcTemplate().queryForList(
			getBoundSql("ARCHITECTURE_SECURITY.SELECT_ALL_ROLE_IDS").getSql(), Long.class );
	}

	public List<Long> getRoleIds(int start, int num) {
		return getExtendedJdbcTemplate().queryScrollable(
			getBoundSql("ARCHITECTURE_SECURITY.SELECT_ALL_ROLE_IDS").getSql(), start, num, new Object[0], new int[0], Long.class );
	}
		
	public List<Long> getUserRoleIds(long userId) {
		return getExtendedJdbcTemplate().queryForList(
			getBoundSql("ARCHITECTURE_SECURITY.SELECT_USER_ROLE_IDS").getSql(), new Object[]{ userId }, new int[]{Types.INTEGER}, Long.class );
	}

	public List<Long> getGroupRoleIds(long groupId) {
		return getExtendedJdbcTemplate().queryForList(
			getBoundSql("ARCHITECTURE_SECURITY.SELECT_GROUP_ROLE_IDS").getSql(), new Object[]{ groupId }, new int[]{Types.INTEGER}, Long.class );
	}
	public void removeUserRole(long roleId, long userId) {
		getExtendedJdbcTemplate().update(
			getBoundSql("ARCHITECTURE_SECURITY.DELETE_USER_ROLE").getSql(), 
			new Object[]{roleId, userId}, 
			new int[]{Types.INTEGER, Types.INTEGER});
	}
	public void removeGroupRole(long roleId, long groupId) {
		getExtendedJdbcTemplate().update(
			getBoundSql("ARCHITECTURE_SECURITY.DELETE_GROUP_ROLE").getSql(), 
			new Object[]{roleId, groupId}, 
			new int[]{Types.INTEGER, Types.INTEGER});		
	}
	public void addUserRole(long roleId, long userId) {
		getExtendedJdbcTemplate().update(
			getBoundSql("ARCHITECTURE_SECURITY.CREATE_USER_ROLE").getSql(), 
			new Object[]{roleId, userId}, 
			new int[]{Types.INTEGER, Types.INTEGER});		
	}
	public void addGroupRole(long roleId, long groupId) {
		getExtendedJdbcTemplate().update(
			getBoundSql("ARCHITECTURE_SECURITY.CREATE_GROUP_ROLE").getSql(), 
			new Object[]{roleId, groupId}, 
			new int[]{Types.INTEGER, Types.INTEGER});			
	}		
}