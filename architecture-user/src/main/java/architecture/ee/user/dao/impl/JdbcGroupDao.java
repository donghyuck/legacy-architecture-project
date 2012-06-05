package architecture.ee.user.dao.impl;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.RowMapper;

import architecture.ee.jdbc.property.dao.ExtendedPropertyDao;
import architecture.ee.spring.jdbc.support.ExtendedJdbcDaoSupport;
import architecture.ee.user.Group;
import architecture.ee.user.dao.GroupDao;
import architecture.ee.user.impl.GroupImpl;

/**
 * @author  donghyuck
 */
public class JdbcGroupDao extends ExtendedJdbcDaoSupport implements GroupDao {

	private final RowMapper<Group> groupRowMapper = new RowMapper<Group>(){
		public Group mapRow(ResultSet rs, int rowNum) throws SQLException {  			
			GroupImpl model = new GroupImpl();
			model.setGroupId( rs.getLong("GROUP_ID") );
			model.setName(rs.getString("NAME"));
			model.setDescription(rs.getString("DESCRIPTION"));
			model.setCreationDate(rs.getDate("CREATION_DATE"));
			model.setModifiedDate(rs.getDate("MODIFIED_DATE"));			
			return (Group)model;
		}};
	 
	/**
	 */
	private ExtendedPropertyDao extendedPropertyDao;
	
	/**
	 */
	private String sequencerName = "Group";
	/**
	 */
	private String groupPropertyTableName = "V2_GROUP_PROPERTY";
	/**
	 */
	private String groupPropertyPrimaryColumnName = "GROUP_ID";
	
	/**
	 * @param extendedPropertyDao
	 */
	public void setExtendedPropertyDao(ExtendedPropertyDao extendedPropertyDao) {
		this.extendedPropertyDao = extendedPropertyDao;
	}

	/**
	 * @return
	 */
	public String getSequencerName() {
		return sequencerName;
	}

	/**
	 * @param sequencerName
	 */
	public void setSequencerName(String sequencerName) {
		this.sequencerName = sequencerName;
	}

	/**
	 * @return
	 */
	public String getGroupPropertyTableName() {
		return groupPropertyTableName;
	}

	/**
	 * @param groupPropertyTableName
	 */
	public void setGroupPropertyTableName(String groupPropertyTableName) {
		this.groupPropertyTableName = groupPropertyTableName;
	}

	/**
	 * @return
	 */
	public String getGroupPropertyPrimaryColumnName() {
		return groupPropertyPrimaryColumnName;
	}

	/**
	 * @param groupPropertyPrimaryColumnName
	 */
	public void setGroupPropertyPrimaryColumnName(String groupPropertyPrimaryColumnName) {
		this.groupPropertyPrimaryColumnName = groupPropertyPrimaryColumnName;
	}
	

	public Map<String, String> getGroupProperties(long groupId) {
		return extendedPropertyDao.getProperties(groupPropertyTableName, groupPropertyPrimaryColumnName, groupId);
	}

	public void getGroupProperties(long groupId, Map<String, String> props) {
		extendedPropertyDao.updateProperties(groupPropertyTableName, groupPropertyPrimaryColumnName, groupId, props);
	}

	public void setGroupProperties(long groupId, Map<String, String> props) {
		extendedPropertyDao.updateProperties(groupPropertyTableName, groupPropertyPrimaryColumnName, groupId, props);
	}
	
	public void deleteGroupProperties(long groupId) {
		extendedPropertyDao.deleteProperties(groupPropertyTableName, groupPropertyPrimaryColumnName, groupId);	
	}

	
	public void deleteGroupUsers(long groupId) {
		getExtendedJdbcTemplate().update(getBoundSql("FRAMEWORK_V2.DELETE_ALL_GROUP_MEMBERS").getSql(), new Object[]{groupId}, new int[]{Types.INTEGER});
	}

	public void deleteGroup(long groupId) {
		
		getExtendedJdbcTemplate().update(getBoundSql("FRAMEWORK_V2.DELETE_ALL_GROUP_MEMBERS").getSql(), new Object[]{groupId}, new int[]{Types.INTEGER});
		getExtendedJdbcTemplate().update(getBoundSql("FRAMEWORK_V2.DELETE_GROUP").getSql(), new Object[]{groupId}, new int[]{Types.INTEGER});
		deleteGroupProperties(groupId);
	
	}

	public int getGroupCount() {
		return getExtendedJdbcTemplate().queryForInt(getBoundSql("FRAMEWORK_V2.COUNT_ALL_GROUPS").getSql());
	}

	public List<Long> getAllGroupIds() {
		// SELECT_ALL_GROUP_ID
		return getExtendedJdbcTemplate().queryForList(
				getBoundSql("FRAMEWORK_V2.SELECT_ALL_GROUP_ID").getSql(), Long.class );
	}

	public List<Long> getUserGroupIds(long userId) {
		//"SELECT_GROUPS_FOR_USER"
		return getExtendedJdbcTemplate().queryForList(
				getBoundSql("FRAMEWORK_V2.SELECT_GROUPS_FOR_USER").getSql(), 
				new Object[]{ userId }, new int[]{Types.INTEGER}, Long.class );
	}

	public List<Long> getGroupIds(int start, int num) {
		return getExtendedJdbcTemplate().queryScrollable(getBoundSql("FRAMEWORK_V2.SELECT_ALL_GROUP_ID").getSql(), start, num, new Object[0], new int[0], Long.class );
	}

	public void createGroup(Group group) {		
		long id = getNextId(sequencerName);
		if("".equals(group.getDescription()))
            group.setDescription(null);	
		Date now = new Date(System.currentTimeMillis());
		group.setCreationDate(now);
		group.setModifiedDate(now);
	
		getExtendedJdbcTemplate().update(
			getBoundSql("FRAMEWORK_V2.CREATE_GROUP").getSql(), 
			new Object[]{id, group.getName(), group.getDescription(), group.getCreationDate(), group.getModifiedDate()}, 
			new int[]{Types.INTEGER, Types.VARCHAR, Types.VARCHAR, Types.DATE, Types.DATE });
		
		group.setGroupId(id);
		setGroupProperties(group.getGroupId(), group.getProperties());
	}

	public void update(Group group) {
		getExtendedJdbcTemplate().update(getBoundSql("FRAMEWORK_V2.UPDATE_GROUP").getSql(), new Object[]{group.getGroupId()}, new int[]{Types.INTEGER});
		setGroupProperties(group.getGroupId(), group.getProperties());
	}

	public Group getGroupById(long groupId) {
		
		Group group = getExtendedJdbcTemplate().queryForObject(
			getBoundSql("FRAMEWORK_V2.SELECT_GROUP_BY_ID").getSql(), 
			new Object[]{groupId}, new int[]{Types.INTEGER}, groupRowMapper);		
		
		group.setProperties(getGroupProperties(group.getGroupId()));
		group.setAdministratorIds(getAdministratorIds(group.getGroupId()));
		group.setMemberIds(getMembersIds(group.getGroupId()));
		
		return group;	
	}

	public Group getGroupByName(String name, boolean caseInsensitive) {
		
		String n = caseInsensitive ? name.toLowerCase() : name;
		Group group = getExtendedJdbcTemplate().queryForObject(
			getBoundSqlWithAdditionalParameter("FRAMEWORK_V2.SELECT_GROUP_BY_NAME", caseInsensitive).getSql(), 
			new Object[]{n}, new int[]{Types.VARCHAR}, groupRowMapper);		
		log.debug("1");
		group.setProperties(getGroupProperties(group.getGroupId()));
		log.debug("2");
		group.setAdministratorIds(getAdministratorIds(group.getGroupId()));
		log.debug("3");
		group.setMemberIds(getMembersIds(group.getGroupId()));
		
		return group;
	}

	public List<Long> getMembersIds(long groupId) {
		return getExtendedJdbcTemplate().queryForList(
				getBoundSql("FRAMEWORK_V2.SELECT_ALL_GROUP_MEMBER_ID").getSql(), 
				new Object[]{ groupId}, new int[]{Types.INTEGER}, Long.class );
	}

	public List<Long> getAdministratorIds(long groupId) {		
		return getExtendedJdbcTemplate().queryForList(
				getBoundSql("FRAMEWORK_V2.SELECT_ALL_GROUP_ADMIN_ID").getSql(), 
				new Object[]{ groupId}, new int[]{Types.INTEGER}, Long.class );
	}

	public boolean isMember(long groupId, long userId) {		
		List<Long> list = getExtendedJdbcTemplate().queryForList(
				getBoundSql("FRAMEWORK_V2.TEST_GROUP_MEMBERSHIP").getSql(), 
				new Object[]{ groupId, userId}, 
				new int[]{Types.INTEGER, Types.INTEGER}, 
				Long.class );
		
		if(!list.isEmpty())
			return true;		
		return false;
	}

	
	public void deleteUserMemberships(long userId) {
		getExtendedJdbcTemplate().update(getBoundSql("FRAMEWORK_V2.DELETE_MEMBERSHIP_BY_USER").getSql(), new Object[]{userId}, new int[]{Types.INTEGER});		
	}

	public void addMember(long groupId, long userId) {
		addMembership(groupId, userId, false);		
	}

	public void removeMember(long groupId, long userId) {
		removeMembership(groupId, userId, false);		
	}

	public void addAdministrator(long groupId, long userId) {
		addMembership(groupId, userId, true);
	}

	public void removeAdministrator(long groupId, long userId) {
		removeMembership(groupId, userId, true);		
	}

	public void addMembers(long groupId, Set<Long> userIds) {
		addMembership(groupId, userIds, false);			
	}

	public void removeMembers(long groupId, Set<Long> userIds) {
		removeMembership(groupId, userIds, false);
		
	}

	public void addAdministrators(long groupId, Set<Long> userIds) {
		addMembership(groupId, userIds, true);		
	}

	public void removeAdministrators(long groupId, Set<Long> userIds) {
		removeMembership(groupId, userIds, true);		
	}
	
	private void addMembership(long groupId, long userId, boolean administrator) {
		//ADD_GROUP_ADMIN
		getExtendedJdbcTemplate().update(
				getBoundSql("FRAMEWORK_V2.ADD_GROUP_MEMBERSHIP").getSql(), 
				new Object[]{groupId, userId, administrator ? 1 : 0}, 
				new int[]{Types.INTEGER, Types.INTEGER, Types.INTEGER});	
	}

	private void removeMembership(long groupId, long userId, boolean administrator) {
		getExtendedJdbcTemplate().update(
				getBoundSql("FRAMEWORK_V2.REMOVE_GROUP_MEMBERSHIP").getSql(), 
				new Object[]{groupId, userId, administrator ? 1 : 0}, 
				new int[]{Types.INTEGER, Types.INTEGER, Types.INTEGER});			
	}

	private void addMembership(long groupId, Set<Long> set, boolean administrator) {
		
		final long groupIdToUse = groupId;		
		final Set<Long> usersToUse = set;
		final boolean isAdmin = administrator;
		
		// REMOVE_GROUP_MEMBERSHIP
		getExtendedJdbcTemplate().batchUpdate(getBoundSql("FRAMEWORK_V2.ADD_GROUP_MEMBERSHIP").getSql(),			    
				new BatchPreparedStatementSetter(){
						public void setValues(PreparedStatement ps, int i) throws SQLException {							
							for( Long user : usersToUse ){	
								ps.setLong( 1, groupIdToUse);
	                            ps.setLong( 2, user);  
	                            ps.setInt( 3, isAdmin ? 1 : 0);
							}							
						}
						
						public int getBatchSize() {
							return usersToUse.size();
						}});
	}

	private void removeMembership(long groupId, Set<Long> set, boolean administrator) {
		final long groupIdToUse = groupId;		
		final Set<Long> usersToUse = set;
		final boolean isAdmin = administrator;
		
		// REMOVE_GROUP_MEMBERSHIP
		getExtendedJdbcTemplate().batchUpdate(getBoundSql("FRAMEWORK_V2.REMOVE_GROUP_MEMBERSHIP").getSql(),			    
				new BatchPreparedStatementSetter(){
						public void setValues(PreparedStatement ps, int i) throws SQLException {							
							for( Long user : usersToUse ){	
								ps.setLong( 1, groupIdToUse);
	                            ps.setLong( 2, user);  
	                            ps.setInt( 3, isAdmin ? 1 : 0);
							}							
						}
						
						public int getBatchSize() {
							return usersToUse.size();
						}});		
	}
	
}