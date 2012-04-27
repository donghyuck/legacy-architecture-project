package architecture.ee.user.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.RowMapper;

import architecture.ee.jdbc.property.dao.ExtendedPropertyDao;
import architecture.ee.spring.jdbc.support.ExtendedJdbcDaoSupport;
import architecture.ee.user.User;
import architecture.ee.user.UserTemplate;
import architecture.ee.user.dao.UserDao;

/**
 * @author  donghyuck
 */
public class JdbcUserDao extends ExtendedJdbcDaoSupport implements UserDao {
   
	private final RowMapper<UserTemplate> userMapper = new RowMapper<UserTemplate>(){

		public UserTemplate mapRow(ResultSet rs, int rowNum) throws SQLException {			
			UserTemplate ut = new UserTemplate();			 
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
			ut.setEnabled( rs.getInt("USER_ENABLED") == 1 ); 
			
			ut.setExternal( rs.getInt("IS_EXTERNAL") == 1 );  
			ut.setFederated( rs.getInt("FEDERATED") == 1 );
			ut.setCreationDate( rs.getDate("CREATION_DATE") ); 
			ut.setModifiedDate( rs.getDate("MODIFIED_DATE") ); 			
			ut.setStatus( UserTemplate.Status.getById( rs.getInt("STATUS") ));
			return ut;
		}
		
	};
	
	private final RowMapper<User> userMapper2 = new RowMapper<User>(){

		public User mapRow(ResultSet rs, int rowNum) throws SQLException {			
			UserTemplate ut = new UserTemplate();			 
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
			ut.setEnabled( rs.getInt("USER_ENABLED") == 1 ); 
			// rs.getInt("VISIBLE");
			ut.setExternal( rs.getInt("IS_EXTERNAL") == 1 );  
			ut.setFederated( rs.getInt("FEDERATED") == 1 );
			ut.setCreationDate( rs.getDate("CREATION_DATE") ); 
			ut.setModifiedDate( rs.getDate("MODIFIED_DATE") ); 			
			ut.setStatus( UserTemplate.Status.getById( rs.getInt("STATUS") ));
			return ut;
		}
		
	};	
	
	/**
	 */
	private ExtendedPropertyDao extendedPropertyDao;
	
	private String sequencerName = "User";
	private String userPropertyTableName = "V2_USER_PROPERTY";
	private String userPropertyPrimaryColumnName = "USER_ID";
	
    public JdbcUserDao()
    {
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

	public List<Integer> getUserIdsWithUserProperty(String propertyName, String propertyValue) {
		return getExtendedJdbcTemplate().queryForList( getBoundSql("FRAMEWORK_V2.SELECT_USER_ID_BY_PROPERTY").getSql(),  new Object[]{ propertyName , propertyValue }, new int [] {Types.VARCHAR, Types.VARCHAR}, Integer.class);
	}

	public User getUser(User template){

		User user = null;
		try {
			User retVal = null;
			if (template.getUserId() > 0L)
				retVal = getUserById(template.getUserId());
			if (null == retVal)
				retVal = getUserByUsername(template.getUsername());
			if (null == retVal)
				retVal = getUserByEmail(template.getUsername());
			if (null == retVal)
				log.info((new StringBuilder()).append("No match found for user ").append(template).append(".").toString());
			user = retVal;
		} catch (Throwable throwable) {
//			/throw throwable;
		}
		return user;
	}
	
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
			getExtendedJdbcTemplate().update(getBoundSql("FRAMEWORK_V2.CREATE_USER").getSql(), new Object[]{
				userId,
				user.getUsername(), 
				user.getPasswordHash(), 
				useLastNameFirstName ? null : user.getName(), 
				user.isNameVisible() ? 1 : 0 ,		
				useLastNameFirstName ? user.getFirstName() : null, 
				useLastNameFirstName ? user.getLastName() : null,
				user.getEmail(),
				user.isEmailVisible() ? 1 : 0,
				user.getLastLoggedIn() != null ? user.getLastLoggedIn() : null,		
				user.getLastProfileUpdate() != null ? user.getLastProfileUpdate() : null,
				user.isEnabled() ? 1 : 0,
				1,
				user.isExternal() ? 1 : 0,
				user.isFederated() ? 1 : 0,
				user.getStatus().getId(),
				user.getCreationDate() != null ? user.getCreationDate() : null,
				user.getModifiedDate() != null ? user.getModifiedDate() : null		
			});				
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
			
			getExtendedJdbcTemplate().update(getBoundSql("FRAMEWORK_V2.UPDATE_USER").getSql(), 
				new Object[]{
				    useLastNameFirstName ? null : user.getName(),
				    useLastNameFirstName ? user.getFirstName() : null, 
				    useLastNameFirstName ? user.getLastName() : null,
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
				    user.getUserId()				
			});		
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
			user = getExtendedJdbcTemplate().queryForObject(getBoundSql("FRAMEWORK_V2.SELECT_USER_BY_USERNAME").getSql(), new Object[] {username}, new int[] {Types.VARCHAR} , userMapper );
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
			user = getExtendedJdbcTemplate().queryForObject(getBoundSql("FRAMEWORK_V2.SELECT_USER_BY_USERNAME").getSql(), new Object[] { username.toLowerCase()}, new int[] {Types.VARCHAR}, userMapper );
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
				UserTemplate user = getExtendedJdbcTemplate().queryForObject(getBoundSql("FRAMEWORK_V2.SELECT_USER_BY_ENAIL").getSql(), userMapper, new Object[]{emailMatch}, new int[]{Types.VARCHAR});
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
			user = getExtendedJdbcTemplate().queryForObject(getBoundSql("FRAMEWORK_V2.SELECT_USER_BY_ID").getSql(), userMapper, new Object[]{userId}, new int[]{Types.INTEGER});
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
        getExtendedJdbcTemplate().update(getBoundSql("FRAMEWORK_V2.DELETE_GROUP_MEMBERSHIP").getSql(), new Object[]{ user.getUserId()}, new int [] {Types.INTEGER});
        extendedPropertyDao.deleteProperties(userPropertyTableName, userPropertyPrimaryColumnName, user.getUserId());
        getExtendedJdbcTemplate().update(getBoundSql("FRAMEWORK_V2.DELETE_USER_BY_ID").getSql(), new Object[]{ user.getUserId()}, new int [] {Types.INTEGER});        
	}

	public Map<String, String> getUserProperties(long userId) {
		return extendedPropertyDao.getProperties(userPropertyTableName, userPropertyPrimaryColumnName, userId);
	}

	public void setUserProperties(long userId, Map<String, String> props) {
		extendedPropertyDao.updateProperties(userPropertyTableName, userPropertyPrimaryColumnName, userId, props);
	}

	public List<User> getApplicationUsers() {	
		List<User> users = getExtendedJdbcTemplate().query(getBoundSql("FRAMEWORK_V2.SELECT_ALL_ENABLED_USER").getSql(), userMapper2);
		for(User user : users){
			((UserTemplate)user).setProperties(this.getUserProperties(user.getUserId()));
		}
		return users;
	}

	public List<User> getApplicationUsers(int startIndex, int numResults) {
		List<User> users = getExtendedJdbcTemplate().queryScrollable(getBoundSql("FRAMEWORK_V2.SELECT_ALL_ENABLED_USER").getSql(), startIndex, numResults, new Object[0], new int[0], userMapper2);
		for(User user : users){
			((UserTemplate)user).setProperties(this.getUserProperties(user.getUserId()));
		}
		return users;
	}

	public int getTotalUserCount() {
		return getExtendedJdbcTemplate().queryForInt(getBoundSql("FRAMEWORK_V2.COUNT_VISIBLE_USER").getSql());
	}

	public int getApplicationUserCount() {	
		return getExtendedJdbcTemplate().queryForInt(getBoundSql("FRAMEWORK_V2.COUNT_ENABLED_USER").getSql());
	}
	
	public int getAuthenticatedUserCount() {
		return getExtendedJdbcTemplate().queryForInt(getBoundSql("FRAMEWORK_V2.COUNT_AUTHENTICATED_USER").getSql());
	}

	public int getRecentUserCount(Date date) {
		return getExtendedJdbcTemplate().queryForInt(getBoundSql("FRAMEWORK_V2.COUNT_RECENT_USER").getSql(), new Object[]{date}, new int[]{Types.DATE});
	}

	public List<User> getAllUsers() {
		List<User> users = getExtendedJdbcTemplate().query(getBoundSql("FRAMEWORK_V2.SELECT_ALL_VISIBLE_USER").getSql(), userMapper2);
		for(User user : users){
			((UserTemplate)user).setProperties(this.getUserProperties(user.getUserId()));
		}
		return users;
	}

	public List<User> getAllUsers(int startIndex, int numResults) {
		List<User> users = getExtendedJdbcTemplate().queryScrollable(getBoundSql("FRAMEWORK_V2.SELECT_ALL_VISIBLE_USER").getSql(), startIndex, numResults, new Object[0], new int[0], userMapper2);
		for(User user : users){
			((UserTemplate)user).setProperties(this.getUserProperties(user.getUserId()));
		}
		return users;
	}

	public List<Integer> getUserIdsWithStatuses(int[] status) {
		//SELECT_USER_ID_BY_STATUS
		return getExtendedJdbcTemplate().queryForList(getBoundSqlWithAdditionalParameter("FRAMEWORK_V2.SELECT_USER_ID_BY_STATUS", status).getSql(), Integer.class);
	}
    
}
