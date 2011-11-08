package architecture.ee.security.role.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Date;
import java.util.List;

import org.springframework.jdbc.core.RowMapper;
import architecture.ee.jdbc.sequencer.dao.SequenceDao;
import architecture.ee.model.impl.RoleModelImpl;
import architecture.ee.security.role.Role;
import architecture.ee.security.role.dao.RoleDao;
import architecture.ee.spring.jdbc.support.ExtendedJdbcDaoSupport;

/**
 * @author  donghyuck
 */
public class JdbcRoleDao extends ExtendedJdbcDaoSupport implements RoleDao {

	private final RowMapper<Role> roleRowMapper = new RowMapper<Role>(){
		public Role mapRow(ResultSet rs, int rowNum) throws SQLException {  			
			RoleModelImpl model = new RoleModelImpl();			
			model.setRoleId( rs.getLong("ROLE_ID") );
			model.setName(rs.getString("NAME"));
			model.setDescription(rs.getString("DESCRIPTION"));
			model.setCreationDate(rs.getDate("CREATION_DATE"));
			model.setModifiedDate(rs.getDate("MODIFIED_DATE"));					
			return (Role) model;
		}};
	
	private String sequencerName = "Role";
	/**
	 * @uml.property  name="sequenceDao"
	 * @uml.associationEnd  
	 */
	private SequenceDao sequenceDao;
	
	/**
	 * @param sequenceDao
	 * @uml.property  name="sequenceDao"
	 */
	public void setSequenceDao(SequenceDao sequenceDao) {
		this.sequenceDao = sequenceDao;
	}
	
	/**
	 * @param sequencerName
	 * @uml.property  name="sequencerName"
	 */
	public void setSequencerName(String sequencerName) {
		this.sequencerName = sequencerName;
	}

	public void createRole(Role role) {
	
		long id = sequenceDao.nextID(sequencerName);
		if("".equals(role.getDescription()))
			role.setDescription(null);	
		
		Date now = new Date(System.currentTimeMillis());
		role.setCreationDate(now);
		role.setModifiedDate(now);
		
		getExtendedJdbcTemplate().update(
			getBoundSql("FRAMEWORK_V2.CREATE_ROLE").getSql(), 
			new Object[]{id, role.getName(), role.getDescription(), role.getCreationDate(), role.getModifiedDate()}, 
			new int[]{Types.INTEGER, Types.VARCHAR, Types.VARCHAR, Types.DATE, Types.DATE });
			
		role.setRoleId(id);

	}

	public void deleteRole(long roleId) {
		
		getExtendedJdbcTemplate().update(
			getBoundSql("FRAMEWORK_V2.DELETE_GROUP_ROLES").getSql(), 
			new Object[]{roleId}, new int[]{Types.INTEGER});
		
		getExtendedJdbcTemplate().update(
			getBoundSql("FRAMEWORK_V2.DELETE_USER_ROLES").getSql(), 
			new Object[]{roleId}, new int[]{Types.INTEGER});
		
		getExtendedJdbcTemplate().update(
			getBoundSql("FRAMEWORK_V2.DELETE_ROLE").getSql(), 
			new Object[]{roleId}, new int[]{Types.INTEGER});
	
	}

	public void update(Role role) {
		getExtendedJdbcTemplate().update(getBoundSql("FRAMEWORK_V2.UPDATE_ROLE").getSql(), 
			new Object[]{role.getName(), role.getDescription(), role.getModifiedDate(), role.getRoleId()}, 
			new int[]{Types.VARCHAR, Types.VARCHAR, Types.DATE, Types.INTEGER});
	}

	public Role getRoleById(long roleId) {
		Role role = getExtendedJdbcTemplate().queryForObject(
				getBoundSql("FRAMEWORK_V2.SELECT_ROLE_BY_ID").getSql(), 
				new Object[]{roleId}, new int[]{Types.INTEGER}, roleRowMapper);		
		return role;
	}

	public Role getRoleByName(String name, boolean caseInsensitive) {
		String n = caseInsensitive ? name.toLowerCase() : name;
		Role role = getExtendedJdbcTemplate().queryForObject(
			getBoundSqlWithAdditionalParameter("FRAMEWORK_V2.SELECT_ROLE_BY_NAME", caseInsensitive).getSql(), 
			new Object[]{n}, new int[]{Types.VARCHAR}, roleRowMapper);				
		return role;
	}
	
	
	public void deleteUserRoles(long userId) {
		getExtendedJdbcTemplate().update(
			getBoundSql("FRAMEWORK_V2.REMOVE_USER_ROLES").getSql(), 
			new Object[]{userId}, 
			new int[]{Types.INTEGER});			
	}

	public void deleteGroupRoles(long groupId) {
		getExtendedJdbcTemplate().update(
			getBoundSql("FRAMEWORK_V2.REMOVE_USER_ROLES").getSql(), 
			new Object[]{groupId}, 
			new int[]{Types.INTEGER});			
	}

	public int getRoleCount() {
		return getExtendedJdbcTemplate().queryForInt(getBoundSql("FRAMEWORK_V2.COUNT_ALL_ROLES").getSql());
	}

	public List<Long> getAllRoleIds() {
		return getExtendedJdbcTemplate().queryForList(
			getBoundSql("FRAMEWORK_V2.SELECT_ALL_ROLE_IDS").getSql(), Long.class );
	}

	public List<Long> getRoleIds(int start, int num) {
		return getExtendedJdbcTemplate().queryScrollable(
			getBoundSql("FRAMEWORK_V2.SELECT_ALL_ROLE_IDS").getSql(), 
			start, num, Long.class, new Object[]{}, 
			new int[]{} );
	}
		
	public List<Long> getUserRoleIds(long userId) {
		return getExtendedJdbcTemplate().queryForList(
			getBoundSql("FRAMEWORK_V2.SELECT_USER_ROLE_IDS").getSql(), 
			new Object[]{ userId }, new int[]{Types.INTEGER}, Long.class );
	}

	public List<Long> getGroupRoleIds(long groupId) {
		return getExtendedJdbcTemplate().queryForList(
			getBoundSql("FRAMEWORK_V2.SELECT_GROUP_ROLE_IDS").getSql(), 
			new Object[]{ groupId }, new int[]{Types.INTEGER}, Long.class );
	}
	public void removeUserRole(long roleId, long userId) {
		getExtendedJdbcTemplate().update(
			getBoundSql("FRAMEWORK_V2.DELETE_USER_ROLE").getSql(), 
			new Object[]{roleId, userId}, 
			new int[]{Types.INTEGER, Types.INTEGER});
	}
	public void removeGroupRole(long roleId, long groupId) {
		getExtendedJdbcTemplate().update(
			getBoundSql("FRAMEWORK_V2.DELETE_GROUP_ROLE").getSql(), 
			new Object[]{roleId, groupId}, 
			new int[]{Types.INTEGER, Types.INTEGER});		
	}
	public void addUserRole(long roleId, long userId) {
		getExtendedJdbcTemplate().update(
			getBoundSql("FRAMEWORK_V2.CREATE_USER_ROLE").getSql(), 
			new Object[]{roleId, userId}, 
			new int[]{Types.INTEGER, Types.INTEGER});	
		
	}
	public void addGroupRole(long roleId, long groupId) {
		getExtendedJdbcTemplate().update(
			getBoundSql("FRAMEWORK_V2.CREATE_GROUP_ROLE").getSql(), 
			new Object[]{roleId, groupId}, 
			new int[]{Types.INTEGER, Types.INTEGER});			
	}		
}