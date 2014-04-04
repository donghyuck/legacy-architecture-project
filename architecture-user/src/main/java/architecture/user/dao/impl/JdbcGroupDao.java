/*
 * Copyright 2012, 2013 Donghyuck, Son
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package architecture.user.dao.impl;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.SqlParameterValue;

import architecture.common.user.Group;
import architecture.ee.jdbc.property.dao.ExtendedPropertyDao;
import architecture.ee.spring.jdbc.support.ExtendedJdbcDaoSupport;
import architecture.user.DefaultGroup;
import architecture.user.dao.GroupDao;

import com.google.common.collect.Lists;

public class JdbcGroupDao  extends ExtendedJdbcDaoSupport implements GroupDao  {

	
	private final RowMapper<Group> groupMapper = new RowMapper<Group>(){
		public Group mapRow(ResultSet rs, int rowNum) throws SQLException {			
			Group g = new DefaultGroup();
			g.setCompanyId(rs.getLong("COMPANY_ID"));
			g.setGroupId(rs.getLong("GROUP_ID"));
			g.setName(rs.getString("NAME"));
			g.setDisplayName(rs.getString("DISPLAY_NAME"));
			g.setDescription(rs.getString("DESCRIPTION"));
			g.setCreationDate( rs.getDate("CREATION_DATE") ); 
			g.setModifiedDate( rs.getDate("MODIFIED_DATE") ); 		
			return g;
		}		
	};
	
	private ExtendedPropertyDao extendedPropertyDao;	
	private String sequencerName = "GROUP";
	private String groupPropertyTableName = "V2_GROUP_PROPERTY";
	private String groupPropertyPrimaryColumnName = "GROUP_ID";
	

	/**
	 * @param userPropertyTableName
	 */
	public void setGroupPropertyTableName(String groupPropertyTableName) {
		this.groupPropertyTableName = groupPropertyTableName;
	}

	/**
	 * @param userPropertyPrimaryColumnName
	 */
	public void setGroupPropertyPrimaryColumnName(String groupPropertyPrimaryColumnName) {
		this.groupPropertyPrimaryColumnName = groupPropertyPrimaryColumnName;
	}
	
	/**
	 * @param extendedPropertyDao
	 */
	public void setExtendedPropertyDao(ExtendedPropertyDao extendedPropertyDao) {
		this.extendedPropertyDao = extendedPropertyDao;
	}

	public void createGroup(Group group) {

		
		long groupId = getNextId(sequencerName);
		if("".equals(group.getDescription()))
            group.setDescription(null);
		
		getExtendedJdbcTemplate().update(getBoundSql("ARCHITECTURE_SECURITY.CREATE_GROUP").getSql(), 	
				new SqlParameterValue(Types.NUMERIC, group.getCompanyId()),
				new SqlParameterValue (Types.NUMERIC, groupId), 
				new SqlParameterValue(Types.VARCHAR, group.getName()),
				new SqlParameterValue(Types.VARCHAR, group.getDisplayName()),
				new SqlParameterValue(Types.VARCHAR, group.getDescription()),
				new SqlParameterValue(Types.DATE, group.getCreationDate()),
				new SqlParameterValue(Types.DATE, group.getModifiedDate()));		
		group.setGroupId(groupId);		
		setGroupProperties(group.getGroupId(), group.getProperties());		
	}	

	public void updateGroup(Group group) {
		getExtendedJdbcTemplate().update(getBoundSql("ARCHITECTURE_SECURITY.UPDATE_GROUP").getSql(), 	
				new SqlParameterValue(Types.VARCHAR, group.getName()),
				new SqlParameterValue(Types.VARCHAR, group.getDisplayName()),
				new SqlParameterValue(Types.VARCHAR, group.getDescription()),
				new SqlParameterValue(Types.DATE, group.getModifiedDate()),
				new SqlParameterValue(Types.NUMERIC, group.getGroupId()));
		setGroupProperties(group.getGroupId(), group.getProperties());
	}
	
	/**
	 * groupId 에 해당하는 그룹 객체를 리턴한다.
	 * 
	 * @param groupId 그룹 ID 값
	 */
	public Group getGroupById(long groupId) {

		Group group = null ;
		try {
			group = getExtendedJdbcTemplate().queryForObject(getBoundSql("ARCHITECTURE_SECURITY.SELECT_GROUP_BY_ID").getSql(), groupMapper, new SqlParameterValue(Types.NUMERIC, groupId ) );
			group.setProperties(getGroupProperties( group.getGroupId() ));
		} catch (IncorrectResultSizeDataAccessException e) {
			if(e.getActualSize() > 1)
	        {
	            log.warn((new StringBuilder()).append("Multiple occurrances of the same group ID found: ").append(groupId).toString());
	            throw e;
	        }
		} catch (DataAccessException e) {
			 String message = (new StringBuilder()).append("Failure attempting to load group by ID : ").append(groupId).append(".").toString();
			 log.fatal(message, e);
		}		
		return group;
	}

	public Group getGroupByName(String name, boolean caseInsensitive ) {
		
		if( StringUtils.isEmpty(name)){
			return null;
		}
		
		Group group = null ;
		try {
			group = getExtendedJdbcTemplate().queryForObject(
					getBoundSqlWithAdditionalParameter("ARCHITECTURE_SECURITY.SELECT_GROUP_BY_NAME", new Boolean( caseInsensitive ) ).getSql(), groupMapper, 
					new SqlParameterValue(Types.VARCHAR, caseInsensitive ? name.toLowerCase() :  name  ) );
			
			group.setProperties(getGroupProperties( group.getGroupId() ));
		} catch (IncorrectResultSizeDataAccessException e) {
			if(e.getActualSize() > 1)
	        {
	            log.warn((new StringBuilder()).append("Multiple occurrances of the same group ID found: ").append(name).toString());
	            throw e;
	        }
		} catch (DataAccessException e) {
			 String message = (new StringBuilder()).append("Failure attempting to load group by ID : ").append(name).append(".").toString();
			 log.fatal(message, e);
		}		
		return group;
	}
	
	public int getGroupCount() {
		return getExtendedJdbcTemplate().queryForInt(getBoundSql("ARCHITECTURE_SECURITY.COUNT_ALL_GROUP").getSql());
	}

	
	
	public List<Group> getGroups() {
		throw new UnsupportedOperationException("Group creation not supported.");
	}

	public List<Group> getGroups(int start, int num) {
		throw new UnsupportedOperationException("Group creation not supported.");
	}
	
	
	public List<Long> getAllGroupIds() {
		return getExtendedJdbcTemplate().queryForList(getBoundSql("ARCHITECTURE_SECURITY.SELECT_ALL_GROUP_ID").getSql(), Long.class);
	}

	public List<Long> getGroupIds(int startIndex, int numResults) {
		return getExtendedJdbcTemplate().queryScrollable(
				getBoundSql("ARCHITECTURE_SECURITY.SELECT_ALL_GROUP_ID").getSql(), 
				startIndex, 
				numResults, 
				new Object[0], 
				new int[0], 
				Long.class);
	}

	public Map<String, String> getGroupProperties(long groupId) {
		return extendedPropertyDao.getProperties(groupPropertyTableName, groupPropertyPrimaryColumnName, groupId);
	}

	public void setGroupProperties(long groupId, Map<String, String> props) {
		extendedPropertyDao.updateProperties(groupPropertyTableName, groupPropertyPrimaryColumnName, groupId, props);
	}

	public List<Long> getMembersIds(long groupId) {
		return getExtendedJdbcTemplate().queryForList(getBoundSql("ARCHITECTURE_SECURITY.SELECT_ALL_GROUP_MEMBER_ID").getSql(), 				
				new Object[ ] {groupId}, 
				new int[] {Types.NUMERIC}, 
				Long.class);
	}

	public List<Long> getMembersIds(long groupId, int startIndex, int numResults) {
		return getExtendedJdbcTemplate().queryScrollable(
				getBoundSql("ARCHITECTURE_SECURITY.SELECT_ALL_GROUP_MEMBER_ID").getSql(), 
				startIndex, 
				numResults, 
				new Object[ ] {groupId}, 
				new int[] {Types.NUMERIC}, 
				Long.class);
	}

	public int getGroupMemberCount(long groupId) {
		return getExtendedJdbcTemplate().queryForInt(getBoundSql("ARCHITECTURE_SECURITY.COUNT_ALL_GROUP_MEMBER").getSql(), new SqlParameterValue(Types.NUMERIC, groupId )  );
	}

	public boolean isMember(long groupId, long userId) {
		// SELECT_GROUPS_FOR_USER, UPDATE_GROUP_MODIFIED_DATE
		
		List<Long> userIds = getExtendedJdbcTemplate().queryForList(getBoundSql("ARCHITECTURE_SECURITY.SELECT_USER_ID_FROM_GROUP_BY_GROUP_ID_AND_USER_ID").getSql(), 
				Long.class, new SqlParameterValue(Types.NUMERIC, groupId ) , 
				new SqlParameterValue(Types.NUMERIC, userId ) );
		return !userIds.isEmpty();
		
	}

	public void addMember(long groupId, long userId) {
		// ADD_GROUP_MEMBER, UPDATE_GROUP_MODIFIED_DATE
		getExtendedJdbcTemplate().update(getBoundSql("ARCHITECTURE_SECURITY.ADD_GROUP_MEMBER").getSql(), 	
				new SqlParameterValue (Types.NUMERIC, groupId), 
				new SqlParameterValue(Types.NUMERIC, userId));
		
		getExtendedJdbcTemplate().update( getBoundSql("ARCHITECTURE_SECURITY.UPDATE_GROUP_MODIFIED_DATE").getSql(), new SqlParameterValue(Types.DATE, new Date() ), new SqlParameterValue(Types.NUMERIC, groupId )  );
	}

	public void removeMember(long groupId, long userId) {
		// REMOVE_GROUP_MEMBER, UPDATE_GROUP_MODIFIED_DATE
		getExtendedJdbcTemplate().update(getBoundSql("ARCHITECTURE_SECURITY.REMOVE_GROUP_MEMBER").getSql(), 	
				new SqlParameterValue (Types.NUMERIC, groupId), 
				new SqlParameterValue(Types.NUMERIC, userId));
		
		getExtendedJdbcTemplate().update( getBoundSql("ARCHITECTURE_SECURITY.UPDATE_GROUP_MODIFIED_DATE").getSql(), new SqlParameterValue(Types.DATE, new Date() ), new SqlParameterValue(Types.NUMERIC, groupId )  );
		
	}

	public void addMembers(long groupId, Set<Long> userIds) {
		final List<Membership> memberships = Lists.newArrayListWithExpectedSize(userIds.size());		
		for( Long userId : userIds ){
			memberships.add(new Membership(groupId, userId));
		}			
		getExtendedJdbcTemplate().batchUpdate( 
				getBoundSql("ARCHITECTURE_SECURITY.ADD_GROUP_MEMBER").getSql(), 
				new BatchPreparedStatementSetter() {
					public void setValues(PreparedStatement ps, int i)
							throws SQLException {
						ps.setLong(1, memberships.get(i).groupId);
		                ps.setLong(2, memberships.get(i).userId);						
					}
					public int getBatchSize() {
						return memberships.size();
					}}		
		); 		
		getExtendedJdbcTemplate().update( getBoundSql("ARCHITECTURE_SECURITY.UPDATE_GROUP_MODIFIED_DATE").getSql(), new SqlParameterValue(Types.DATE, new Date() ), new SqlParameterValue(Types.NUMERIC, groupId )  );		
	}

	public void removeMembers(long groupId, Set<Long> userIds) {
		// REMOVE_GROUP_MEMBER, UPDATE_GROUP_MODIFIED_DATE
		final List<Membership> memberships = Lists.newArrayListWithExpectedSize(userIds.size());		
		for( Long userId : userIds ){
			memberships.add(new Membership(groupId, userId));
		}			
		getExtendedJdbcTemplate().batchUpdate( getBoundSql("ARCHITECTURE_SECURITY.REMOVE_GROUP_MEMBER").getSql(), 
				new BatchPreparedStatementSetter() {
			public void setValues(PreparedStatement ps, int i)
					throws SQLException {
				ps.setLong(1, memberships.get(i).groupId);
                ps.setLong(2, memberships.get(i).userId);						
			}
			public int getBatchSize() {
				return memberships.size();
			}}						
	    ); 		
		getExtendedJdbcTemplate().update( getBoundSql("ARCHITECTURE_SECURITY.UPDATE_GROUP_MODIFIED_DATE").getSql(), new SqlParameterValue(Types.DATE, new Date() ), new SqlParameterValue(Types.NUMERIC, groupId )  );
	}

	
	static class Membership{
		private long groupId = -1L;
		private long userId = -1L;
		private boolean administrator = false;
		public Membership(long groupId, long userId) {
			this.groupId = groupId;
			this.userId = userId;
		}				
	}


	public List<Long> getUserGroupIds(long userId) {
		return getExtendedJdbcTemplate().queryForList(getBoundSql("ARCHITECTURE_SECURITY.SELECT_GROUPS_FOR_USER").getSql(), 				
					Long.class, new SqlParameterValue(Types.NUMERIC, userId ) );
	}

}
