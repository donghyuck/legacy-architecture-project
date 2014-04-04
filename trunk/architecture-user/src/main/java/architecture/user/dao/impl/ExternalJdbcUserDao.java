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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.SqlParameterValue;

import architecture.common.user.Company;
import architecture.common.user.CompanyNotFoundException;
import architecture.common.user.User;
import architecture.common.user.UserTemplate;
import architecture.ee.jdbc.property.dao.ExtendedPropertyDao;
import architecture.ee.spring.jdbc.support.ExtendedJdbcDaoSupport;
import architecture.user.dao.ExternalUserProfileDao;
import architecture.user.dao.UserDao;
import architecture.user.util.CompanyUtils;

import com.google.common.collect.Lists;

public class ExternalJdbcUserDao extends ExtendedJdbcDaoSupport implements UserDao {
	
	private final RowMapper<User> userMapper = new RowMapper<User>(){

		public User mapRow(ResultSet rs, int rowNum) throws SQLException {			
			
			UserTemplate ut = new UserTemplate();			 			
						
			ut.setCompanyId( rs.getLong("COMPANY_ID") );			
			ut.setUserId(rs.getLong("USER_ID")); 
			ut.setUsername(rs.getString("USERNAME")); 
			ut.setPasswordHash(rs.getString("PASSWORD_HASH")); 
			ut.setName(rs.getString("NAME")); 
			ut.setNameVisible( rs.getInt("NAME_VISIBLE") == 1 ); 
			ut.setFirstName( rs.getString("FIRST_NAME") ); 
			ut.setLastName( rs.getString("LAST_NAME") );
			ut.setEmail( rs.getString("EMAIL") );
			
			ut.setEmailVisible( rs.getInt("EMAIL_VISIBLE") == 1 ); 
			ut.setLastLoggedIn( rs.getDate("LAST_LOGINED_IN") ); 
			ut.setLastProfileUpdate( rs.getDate("LAST_PROFILE_UPDATE") );  
			
			String enabledString = StringUtils.defaultString(rs.getString( "USER_ENABLED" ), "N");
			if( "Y".equals( enabledString.toUpperCase() )){
				ut.setEnabled(true);
			}else{
				ut.setEnabled(false);
			}
			// rs.getInt("VISIBLE");
			ut.setExternal( rs.getInt("IS_EXTERNAL") == 1 );  
			ut.setFederated( rs.getInt("FEDERATED") == 1 );
			ut.setCreationDate( rs.getDate("CREATION_DATE") ); 
			ut.setModifiedDate( rs.getDate("MODIFIED_DATE") ); 			
			ut.setStatus( UserTemplate.Status.getById( rs.getInt("STATUS") ));
			
			return ut;
		}		
	};	
	
	
	private ExternalUserProfileDao externalUserProfileDao = null ;
	private RowMapper<User> externalUserRowMapper = null ;
	private ExtendedPropertyDao extendedPropertyDao;
	
	private String sqlSetName = "EXTENDED_SECURITY" ;
	
	private String sequencerName = "USER";
	private String userPropertyTableName = "V2_USER_PROPERTY";
	private String userPropertyPrimaryColumnName = "USER_ID";
	
	
    public ExternalJdbcUserDao()
    {
    }

    
	public String getSqlSetName() {
		return sqlSetName;
	}


	public void setSqlSetName(String sqlSetName) {
		this.sqlSetName = sqlSetName;
	}


	public RowMapper<User> getExternalUserRowMapper() {
		if( externalUserRowMapper == null )
			externalUserRowMapper = userMapper ;
		return externalUserRowMapper;
	}


	public void setExternalUserRowMapper(RowMapper<User> externalUserRowMapper) {
		this.externalUserRowMapper = externalUserRowMapper;
	}

	/**
	 * @param userPropertyTableName
	 */
	public void setUserPropertyTableName(String userPropertyTableName) {
		this.userPropertyTableName = userPropertyTableName;
	}

	/**
	 * @param userPropertyPrimaryColumnName
	 */
	public void setUserPropertyPrimaryColumnName(
			String userPropertyPrimaryColumnName) {
		this.userPropertyPrimaryColumnName = userPropertyPrimaryColumnName;
	}
	
	/**
	 * @param extendedPropertyDao
	 */
	public void setExtendedPropertyDao(ExtendedPropertyDao extendedPropertyDao) {
		this.extendedPropertyDao = extendedPropertyDao;
	}
	
	public ExternalUserProfileDao getExternalUserProfileDao() {
		return externalUserProfileDao;
	}

	public void setExternalUserProfileDao(
			ExternalUserProfileDao externalUserProfileDao) {
		this.externalUserProfileDao = externalUserProfileDao;
	}

	public boolean isSetExternalUserProfileDao () {
		return externalUserProfileDao == null ? false : true;
	}
	
	protected String getSql( String name ){
		StringBuilder sb = new StringBuilder(sqlSetName);
		sb.append(".").append(name);
		return getBoundSql( sb.toString() ).getSql();
	}
	
	protected String getSql( String name, Object obj ){
		StringBuilder sb = new StringBuilder(sqlSetName);
		sb.append(".").append(name);
		return getBoundSqlWithAdditionalParameter( sb.toString() , obj ).getSql();
	}
	
	/**
	 * 
	 * @param propertyName
	 * @param propertyValue
	 * @return 프로퍼티에 해당하는 사용자 아이디 값들을 리턴한다.
	 */
	public List<Integer> getUserIdsWithUserProperty(String propertyName, String propertyValue) {
		return getExtendedJdbcTemplate().queryForList( 
				getSql("SELECT_USER_ID_BY_PROPERTY"),  
				new Object[]{ propertyName , propertyValue }, 
				new int [] {Types.VARCHAR, Types.VARCHAR}, Integer.class);
	}

	/**
	 * userID, username, email 순서로 사용자를 검색한다.
	 * 
	 * @param template
	 * @return
	 */
	public User getUser(User template) {
		User user = null;
		User retVal = null;
		// 1. ID 로 검색
		if (template.getUserId() > 0L)
			retVal = getUserById(template.getUserId());
		// 2. 로그인 아이디로 검색
		if (null == retVal)
			retVal = getUserByUsername(template.getUsername());
		// 3. 메일로 검색
		if (null == retVal)
			retVal = getUserByEmail(template.getUsername());

		if (null == retVal)
			log.info((new StringBuilder()).append("No match found for user ")	.append(template).append(".").toString());
		user = retVal;
		return user;
	}
	
	/**
	 * 새로운 사용자를 생성한다.
	 * 
	 * @param u
	 */
	public User create(User u) {
		UserTemplate user = new UserTemplate(u);
		if(user.getEmail() == null)
            throw new IllegalArgumentException("User has no email address specified.  An email address is required to create a new user.");
		
		long userId = getNextId(sequencerName);
		
		user.setUserId(userId);
		if("".equals(user.getName()))
            user.setName(null);
		user.setEmail(user.getEmail().toLowerCase());
	    if(user.getStatus() == null)
	        user.setStatus(User.Status.registered);
	    
	    boolean useLastNameFirstName = user.getFirstName() != null && user.getLastName() != null;
	    	    
	    try {
	    	
	    	Date now = new Date();
	    	
			getExtendedJdbcTemplate().update(getSql("CREATE_USER"), new Object[]{
				userId,
				user.getUsername(), 
				user.getPasswordHash(), 
				useLastNameFirstName ? null : user.getName(), 
				user.isNameVisible() ? 1 : 0 ,
				user.getEmail(),
				user.isEmailVisible() ? 1 : 0,
				user.getLastLoggedIn() != null ? user.getLastLoggedIn() : now,		
						
				user.getLastProfileUpdate() != null ? user.getLastProfileUpdate() : now,
				user.isEnabled() ? 1 : 0,
				1,
				user.isExternal() ? 1 : 0,
				user.isFederated() ? 1 : 0,
				user.getStatus().getId(),
				user.getCreationDate() != null ? user.getCreationDate() : now,
				user.getModifiedDate() != null ? user.getModifiedDate() : now		
			},
			new int [] {
				Types.NUMERIC,
				Types.VARCHAR,
				Types.VARCHAR,
				Types.VARCHAR,
				Types.NUMERIC,
				Types.VARCHAR,
				Types.NUMERIC,			
				Types.DATE,
				
				Types.DATE,
				Types.NUMERIC,
				Types.NUMERIC,
				Types.NUMERIC,
				Types.NUMERIC,
				Types.NUMERIC,
				Types.DATE,
				Types.DATE
			});				
			
			setUserProfile(user.getUserId(), user.getProfile());
			setUserProperties(user.getUserId(), user.getProperties());			
			
		} catch (DataAccessException e) {
			String message = "Failed to create new user.";
			log.fatal(message, e);
			throw e;
		}	    
		return user;
	}

	public User update(User user) {
		boolean useLastNameFirstName = user.getFirstName() != null && user.getLastName() != null;
		try{
			Date now = new Date();
			getExtendedJdbcTemplate().update(getSql("UPDATE_USER"), 
				new Object[]{
				    useLastNameFirstName ? null : user.getName(),
				    user.isNameVisible() ? 1 : 0,
				    user.getEmail(),
				    user.isEmailVisible() ? 1: 0,
				    user.getModifiedDate() != null ? user.getModifiedDate() : null,
				    user.getLastLoggedIn() != null ? user.getLastLoggedIn() : null,
				    user.getLastProfileUpdate() != null ? user.getLastProfileUpdate() : null,
				    user.getUsername(),
				    user.isExternal(),
				    user.isFederated(),
				    user.getStatus() != null ? user.getStatus().none.getId() : user.getStatus().getId(),
				    user.getPasswordHash(),		    
				    user.getUserId()				
			});		
			
			setUserProfile(user.getUserId(), user.getProfile());
			setUserProperties(user.getUserId(), user.getProperties());

		}catch(DataAccessException e){
			String message = "Failed to update user.";
			log.fatal(message, e);
			throw e;
		}
		return user;
	}
	

	public User getUserByUsername(String username) {
		UserTemplate user = null;		
		try {
			user = (UserTemplate) getExtendedJdbcTemplate().queryForObject(getSql("SELECT_USER_BY_USERNAME"), new Object[] {username}, new int[] {Types.VARCHAR} , getExternalUserRowMapper() );
			user.setProfile( getUserProfile(user.getUserId()) );
			user.setProperties(getUserProperties(user.getUserId()));
		} catch (EmptyResultDataAccessException e) {
			String message = (new StringBuilder()).append("Failure attempting to load user by case-insensitive username '").append(username).append("'.").toString();
	        log.fatal(message, e);	        
	        // 동작중인 경우 레거시 연계 ...
		} catch (DataAccessException e) {
			log.error(e);
		}	
	    return user;
	}

	public User getUserByUsernameNoCase(String username) {
		UserTemplate user = null;		
		try {
			user = (UserTemplate)getExtendedJdbcTemplate().queryForObject(getSql("SELECT_USER_BY_USERNAME"), new Object[] { username.toLowerCase()}, new int[] {Types.VARCHAR}, getExternalUserRowMapper() );
			user.setProfile( getUserProfile(user.getUserId()) );
			user.setProperties(getUserProperties(user.getUserId()));
		} catch (EmptyResultDataAccessException e) {
			String message = (new StringBuilder()).append("Failure attempting to load user by case-insensitive username '").append(username).append("'.").toString();
	        log.fatal(message, e);	        
	        // 동작중인 경우 레거시 연계 ...
		} catch (DataAccessException e) {
			
		}	
	    return user;
	}

	public User getUserByEmail(String email) {
		
		UserTemplate usertemplate = null;
		if(null != email)
		{
			String emailMatch = email.replace('*', '%');
			try {				
				UserTemplate user = (UserTemplate) getExtendedJdbcTemplate().queryForObject(getSql("SELECT_USER_BY_ENAIL"), getExternalUserRowMapper(), new Object[]{emailMatch}, new int[]{Types.VARCHAR});
				
				user.setProfile( getUserProfile(user.getUserId()) );
				user.setProperties(getUserProperties(user.getUserId()));
				
				usertemplate = user;
			} catch (IncorrectResultSizeDataAccessException e) {
				if(e.getActualSize() > 1){
					 log.warn((new StringBuilder()).append("Multiple occurrances of the same email found: ").append(email).toString());
					 throw e;
				}
			} catch (DataAccessException e) {
				String message = (new StringBuilder()).append("Failure attempting to load user by email '").append(emailMatch).append("'").toString();
				log.fatal(message, e);
				throw e;
			}
		}		
		return usertemplate;
	}

	public User getUserById(long userId) {
		
		if( userId <= 0L){
			return null;
		}
		
		UserTemplate user = null;
		try {
			user = (UserTemplate)getExtendedJdbcTemplate().queryForObject(getSql("SELECT_USER_BY_ID"), getExternalUserRowMapper(), new SqlParameterValue(Types.INTEGER, userId ) );
			user.setProfile( getUserProfile(user.getUserId()) );
			user.setProperties(getUserProperties(user.getUserId()));
		} catch (IncorrectResultSizeDataAccessException e) {
			if(e.getActualSize() > 1)
	        {
	            log.warn((new StringBuilder()).append("Multiple occurrances of the same user ID found: ").append(userId).toString());
	            throw e;
	        }
		} catch (DataAccessException e) {
			 String message = (new StringBuilder()).append("Failure attempting to load user by ID : ").append(userId).append(".").toString();
			 log.fatal(message, e);
		}
		return user;
		
	}

	public void delete(User user) {		
        getExtendedJdbcTemplate().update(getSql("DELETE_GROUP_MEMBERSHIP"), new Object[]{ user.getUserId()}, new int [] {Types.INTEGER});
        extendedPropertyDao.deleteProperties(userPropertyTableName, userPropertyPrimaryColumnName, user.getUserId());
        getExtendedJdbcTemplate().update(getSql("DELETE_USER_BY_ID"), new Object[]{ user.getUserId()}, new int [] {Types.INTEGER});        
	}

	public Map<String, String> getUserProperties(long userId) {
		return extendedPropertyDao.getProperties(userPropertyTableName, userPropertyPrimaryColumnName, userId);
	}

	public void setUserProperties(long userId, Map<String, String> props) {
		extendedPropertyDao.updateProperties(userPropertyTableName, userPropertyPrimaryColumnName, userId, props);
	}

	public List<User> getApplicationUsers() {	
		List<User> users = getExtendedJdbcTemplate().query(getSql("SELECT_ALL_ENABLED_USER"), getExternalUserRowMapper());
		for(User user : users){
			((UserTemplate)user).setProfile(this.getUserProfile(user.getUserId()));
			((UserTemplate)user).setProperties(this.getUserProperties(user.getUserId()));
		}
		return users;
	}

	public List<User> getApplicationUsers(int startIndex, int numResults) {
		List<User> users = getExtendedJdbcTemplate().queryScrollable(getSql("SELECT_ALL_ENABLED_USER"), startIndex, numResults, new Object[0], new int[0], getExternalUserRowMapper());
		for(User user : users){
			((UserTemplate)user).setProfile(this.getUserProfile(user.getUserId()));
			((UserTemplate)user).setProperties(this.getUserProperties(user.getUserId()));			
		}
		return users;
	}
	

	public int getTotalUserCount() {
		return getExtendedJdbcTemplate().queryForInt(getSql("COUNT_VISIBLE_USER"));
	}

	public int getApplicationUserCount() {	
		return getExtendedJdbcTemplate().queryForInt(getSql("COUNT_ENABLED_USER"));
	}
	
	public int getAuthenticatedUserCount() {
		return getExtendedJdbcTemplate().queryForInt(getSql("COUNT_AUTHENTICATED_USER"));
	}

	public int getRecentUserCount(Date date) {
		return getExtendedJdbcTemplate().queryForInt(getSql("COUNT_RECENT_USER"), new Object[]{date}, new int[]{Types.DATE});
	}

	public List<User> getAllUsers() {
		List<User> users = getExtendedJdbcTemplate().query(getSql("SELECT_ALL_VISIBLE_USER"), getExternalUserRowMapper());
		for(User user : users){
			((UserTemplate)user).setProperties(this.getUserProperties(user.getUserId()));
		}
		return users;
	}

	public List<User> getAllUsers(int startIndex, int numResults) {
		List<User> users = getExtendedJdbcTemplate().queryScrollable(getSql("SELECT_ALL_VISIBLE_USER"), startIndex, numResults, new Object[0], new int[0], getExternalUserRowMapper());
		for(User user : users){
			((UserTemplate)user).setProfile(this.getUserProfile(user.getUserId()));
			((UserTemplate)user).setProperties(this.getUserProperties(user.getUserId()));
		}
		return users;
	}

	public List<Integer> getUserIdsWithStatuses(int[] status) {
		return getExtendedJdbcTemplate().queryForList(getSql("SELECT_USER_ID_BY_STATUS", status), Integer.class);
	}

	public List<User> findUsers(String nameOrEmail) {
		List<User> users = getExtendedJdbcTemplate().query(getSql("SELECT_USERS_BY_EMAIL_OR_NAME"), getExternalUserRowMapper(), new SqlParameterValue(Types.VARCHAR, nameOrEmail ), new SqlParameterValue(Types.VARCHAR, nameOrEmail ) );
		for(User user : users){
			((UserTemplate)user).setProfile(this.getUserProfile(user.getUserId()));
			((UserTemplate)user).setProperties(this.getUserProperties(user.getUserId()));
		}
		return users;
	}

	public List<User> findUsers(String nameOrEmail, int startIndex, int numResults) {		
		List<User> users = getExtendedJdbcTemplate().queryScrollable(getSql("SELECT_USERS_BY_EMAIL_OR_NAME"), startIndex, numResults, new Object[]{nameOrEmail, nameOrEmail}, new int[]{Types.VARCHAR, Types.VARCHAR}, getExternalUserRowMapper());
		for(User user : users){
			((UserTemplate)user).setProfile(this.getUserProfile(user.getUserId()));
			((UserTemplate)user).setProperties(this.getUserProperties(user.getUserId()));
		}
		return users;
	}

	public int getFoundUserCount(String nameOrEmail) {
		return getExtendedJdbcTemplate().queryForInt(getSql("COUNT_USERS_BY_EMAIL_OR_NAME"), new SqlParameterValue(Types.VARCHAR, nameOrEmail ), new SqlParameterValue(Types.VARCHAR, nameOrEmail ) );
	}
	
	public Map<String, Object> getUserProfile(long userId){
		if (isSetExternalUserProfileDao() ){
			return externalUserProfileDao.getProfile(userId);
		}else{
			return new HashMap<String, Object>();
		}		
	}
    
	public void setUserProfile( long userId, Map<String, Object> profile){
		if (isSetExternalUserProfileDao() && profile.size() > 0 ){
			externalUserProfileDao.setProfile(userId, profile);
		}
	}


	public List<User> getUsers(Company company) {
		List<User> users = getExtendedJdbcTemplate().query(getSql("SELECT_ALL_COMPANY_VISIBLE_USER"), userMapper);
		for(User user : users){
			try {
				((UserTemplate)user).setCompany( CompanyUtils.getCompany(user.getCompanyId()));
			} catch (CompanyNotFoundException e) {
			}
			((UserTemplate)user).setProperties(this.getUserProperties(user.getUserId()));
		}
		return users;
	}

	public List<User> getUsers(Company company, int startIndex, int numResults) {
		List<User> users = getExtendedJdbcTemplate().queryScrollable(getSql("SELECT_ALL_COMPANY_VISIBLE_USER"), startIndex, numResults, 
				new Object[]{company.getCompanyId()}, 
				new int[]{Types.INTEGER}, 
				userMapper);
		for(User user : users){
			try {
				((UserTemplate)user).setCompany( CompanyUtils.getCompany(user.getCompanyId()));
			} catch (CompanyNotFoundException e) {
			}
			((UserTemplate)user).setProperties(this.getUserProperties(user.getUserId()));
		}
		return users;
	}

	public List<User> findUsers(Company company, String nameOrEmail) {
		List<User> users = getExtendedJdbcTemplate().query(getSql("SELECT_COMPANY_USERS_BY_EMAIL_OR_NAME"), 
				userMapper, 
				new SqlParameterValue(Types.INTEGER, company.getCompanyId() ),
				new SqlParameterValue(Types.VARCHAR, nameOrEmail ), 
				new SqlParameterValue(Types.VARCHAR, nameOrEmail ) );
		for(User user : users){
			try {
				((UserTemplate)user).setCompany( CompanyUtils.getCompany(user.getCompanyId()));
			} catch (CompanyNotFoundException e) {
			}
			((UserTemplate)user).setProperties(this.getUserProperties(user.getUserId()));
		}
		return users;
	}

	public List<User> findUsers(Company company, String nameOrEmail, int startIndex, int numResults) {
		List<User> users = getExtendedJdbcTemplate().queryScrollable(getSql("SELECT_COMPANY_USERS_BY_EMAIL_OR_NAME"), startIndex, numResults, 
				new Object[]{company.getCompanyId(), nameOrEmail, nameOrEmail}, 
				new int[]{Types.INTEGER, Types.VARCHAR, Types.VARCHAR}, userMapper);
		for(User user : users){
			try {
				((UserTemplate)user).setCompany( CompanyUtils.getCompany(user.getCompanyId()));
			} catch (CompanyNotFoundException e) {
			}
			((UserTemplate)user).setProperties(this.getUserProperties(user.getUserId()));
		}
		return users;
	}

	public int getFoundUserCount(Company company, String nameOrEmail) {
		return getExtendedJdbcTemplate().queryForInt(getSql("COUNT_COMPANY_USERS_BY_EMAIL_OR_NAME"), new SqlParameterValue(Types.INTEGER, company.getCompanyId() ), new SqlParameterValue(Types.VARCHAR, nameOrEmail ), new SqlParameterValue(Types.VARCHAR, nameOrEmail ) );
	}


	public int getUserCount(Company company) {
		return getExtendedJdbcTemplate().queryForInt(getSql("COUNT_COMPANY_USERS"), new SqlParameterValue(Types.INTEGER, company.getCompanyId() ));
	}
	
	public void switchCompanies(long companyId, Set<Long> users) {
		
		final List<Long> userIdsToUse = Lists.newArrayListWithExpectedSize(users.size());			
		for( Long userId : users ){
			userIdsToUse.add(userId);
		}	
		
		final Long companyIdToUse = companyId ;		
		getExtendedJdbcTemplate().batchUpdate( getSql("UPDATE_USER_COMPANY"),  new BatchPreparedStatementSetter() {
			public void setValues(PreparedStatement ps, int i)
					throws SQLException {
				ps.setLong(1, companyIdToUse );
                ps.setLong(2, userIdsToUse.get(i));						
			}
			public int getBatchSize() {
				return userIdsToUse.size();
			}}		
		); 		
	}
}
