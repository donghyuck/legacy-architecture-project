package architecture.user.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.SqlParameterValue;

import architecture.common.user.Company;
import architecture.common.user.CompanyNotFoundException;
import architecture.common.user.User;
import architecture.common.user.UserTemplate;
import architecture.ee.jdbc.property.dao.ExtendedPropertyDao;
import architecture.ee.spring.jdbc.support.ExtendedJdbcDaoSupport;
import architecture.user.dao.UserDao;
import architecture.user.util.CompanyUtils;

/**
 * @author  donghyuck
 */
public class JdbcUserDao extends ExtendedJdbcDaoSupport implements UserDao {
		
	private final RowMapper<UserTemplate> userMapper = new RowMapper<UserTemplate>(){

		public UserTemplate mapRow(ResultSet rs, int rowNum) throws SQLException {			
			UserTemplate ut = new UserTemplate();						
			ut.setCompanyId( Long.parseLong(rs.getString("COMPANY_ID")) );
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

			ut.setCompanyId( Long.parseLong(rs.getString("COMPANY_ID")) );
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
	private String sequencerName = "USER";
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

	/**
	 * 
	 * @param propertyName
	 * @param propertyValue
	 * @return 프로퍼티에 해당하는 사용자 아이디 값들을 리턴한다.
	 */
	public List<Integer> getUserIdsWithUserProperty(String propertyName, String propertyValue) {
		return getExtendedJdbcTemplate().queryForList( 
				getBoundSql("ARCHITECTURE_SECURITY.SELECT_USER_ID_BY_PROPERTY").getSql(),  
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
	    	
			getExtendedJdbcTemplate().update(getBoundSql("ARCHITECTURE_SECURITY.CREATE_USER").getSql(), new Object[]{
				user.getCompanyId(),
				userId,
				user.getUsername(), 
				user.getPasswordHash(), 
				useLastNameFirstName ? null : user.getName(), 
				user.isNameVisible() ? 1 : 0 ,		
				useLastNameFirstName ? user.getFirstName() : null, 
				useLastNameFirstName ? user.getLastName() : null,
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
				Types.NUMERIC,
				Types.VARCHAR,
				Types.VARCHAR,
				Types.VARCHAR,
				Types.NUMERIC,
				Types.VARCHAR,
				Types.VARCHAR,
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
			

			try{
				user.setCompany(CompanyUtils.getCompany(user.getCompanyId()));		
			} catch (CompanyNotFoundException e) { }
			
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
			
			getExtendedJdbcTemplate().update(getBoundSql("ARCHITECTURE_SECURITY.UPDATE_USER").getSql(), 
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
				    user.getPasswordHash(),
				    user.getUserId()				
			});		
			

			try{
				((UserTemplate)user).setCompany(CompanyUtils.getCompany(user.getCompanyId()));		
			} catch (CompanyNotFoundException e) { }
			
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
			user = getExtendedJdbcTemplate().queryForObject(getBoundSql("ARCHITECTURE_SECURITY.SELECT_USER_BY_USERNAME").getSql(), new Object[] {username}, new int[] {Types.VARCHAR} , userMapper );
			try{
				user.setCompany(CompanyUtils.getCompany(user.getCompanyId()));		
			} catch (CompanyNotFoundException e) { }			
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
			user = getExtendedJdbcTemplate().queryForObject(getBoundSql("ARCHITECTURE_SECURITY.SELECT_USER_BY_USERNAME").getSql(), new Object[] { username.toLowerCase()}, new int[] {Types.VARCHAR}, userMapper );
			try{
				user.setCompany(CompanyUtils.getCompany(user.getCompanyId()));		
			} catch (CompanyNotFoundException e) { }			
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
				
				UserTemplate user = getExtendedJdbcTemplate().queryForObject(getBoundSql("ARCHITECTURE_SECURITY.SELECT_USER_BY_EMAIL").getSql(), userMapper, new SqlParameterValue(Types.VARCHAR, emailMatch ) );
				try{
					user.setCompany(CompanyUtils.getCompany(user.getCompanyId()));		
				} catch (CompanyNotFoundException e) {					
				}				
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
			user = getExtendedJdbcTemplate().queryForObject(getBoundSql("ARCHITECTURE_SECURITY.SELECT_USER_BY_ID").getSql(), userMapper, new SqlParameterValue(Types.NUMERIC, userId ) );			
			try{
				user.setCompany(CompanyUtils.getCompany(user.getCompanyId()));		
			} catch (CompanyNotFoundException e) { }
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
        getExtendedJdbcTemplate().update(getBoundSql("ARCHITECTURE_SECURITY.DELETE_GROUP_MEMBERSHIP").getSql(), new Object[]{ user.getUserId()}, new int [] {Types.NUMERIC});
        extendedPropertyDao.deleteProperties(userPropertyTableName, userPropertyPrimaryColumnName, user.getUserId());
        getExtendedJdbcTemplate().update(getBoundSql("ARCHITECTURE_SECURITY.DELETE_USER_BY_ID").getSql(), new Object[]{ user.getUserId()}, new int [] {Types.NUMERIC});        
	}

	public Map<String, String> getUserProperties(long userId) {
		return extendedPropertyDao.getProperties(userPropertyTableName, userPropertyPrimaryColumnName, userId);
	}

	public void setUserProperties(long userId, Map<String, String> props) {
		extendedPropertyDao.updateProperties(userPropertyTableName, userPropertyPrimaryColumnName, userId, props);
	}

	public List<User> getApplicationUsers() {	
		List<User> users = getExtendedJdbcTemplate().query(getBoundSql("ARCHITECTURE_SECURITY.SELECT_ALL_ENABLED_USER").getSql(), userMapper2);
		for(User user : users){
			try {
				((UserTemplate)user).setCompany( CompanyUtils.getCompany(user.getCompanyId()));
			} catch (CompanyNotFoundException e) {
			}
			((UserTemplate)user).setProperties(this.getUserProperties(user.getUserId()));
		}
		return users;
	}

	public List<User> getApplicationUsers(int startIndex, int numResults) {
		List<User> users = getExtendedJdbcTemplate().queryScrollable(getBoundSql("ARCHITECTURE_SECURITY.SELECT_ALL_ENABLED_USER").getSql(), startIndex, numResults, new Object[0], new int[0], userMapper2);
		for(User user : users){
			try {
				((UserTemplate)user).setCompany( CompanyUtils.getCompany(user.getCompanyId()));
			} catch (CompanyNotFoundException e) {
			}
			((UserTemplate)user).setProperties(this.getUserProperties(user.getUserId()));
		}
		return users;
	}

	public int getTotalUserCount() {
		return getExtendedJdbcTemplate().queryForInt(getBoundSql("ARCHITECTURE_SECURITY.COUNT_VISIBLE_USER").getSql());
	}

	public int getApplicationUserCount() {	
		return getExtendedJdbcTemplate().queryForInt(getBoundSql("ARCHITECTURE_SECURITY.COUNT_ENABLED_USER").getSql());
	}
	
	public int getAuthenticatedUserCount() {
		return getExtendedJdbcTemplate().queryForInt(getBoundSql("ARCHITECTURE_SECURITY.COUNT_AUTHENTICATED_USER").getSql());
	}

	public int getRecentUserCount(Date date) {
		return getExtendedJdbcTemplate().queryForInt(getBoundSql("ARCHITECTURE_SECURITY.COUNT_RECENT_USER").getSql(), new Object[]{date}, new int[]{Types.DATE});
	}

	public List<User> getAllUsers() {
		List<User> users = getExtendedJdbcTemplate().query(getBoundSql("ARCHITECTURE_SECURITY.SELECT_ALL_VISIBLE_USER").getSql(), userMapper2);
		for(User user : users){
			try {
				((UserTemplate)user).setCompany( CompanyUtils.getCompany(user.getCompanyId()));
			} catch (CompanyNotFoundException e) {
			}
			((UserTemplate)user).setProperties(this.getUserProperties(user.getUserId()));
		}
		return users;
	}

	public List<User> getAllUsers(int startIndex, int numResults) {
		List<User> users = getExtendedJdbcTemplate().queryScrollable(getBoundSql("ARCHITECTURE_SECURITY.SELECT_ALL_VISIBLE_USER").getSql(), startIndex, numResults, new Object[0], new int[0], userMapper2);
		for(User user : users){
			try {
				((UserTemplate)user).setCompany( CompanyUtils.getCompany(user.getCompanyId()));
			} catch (CompanyNotFoundException e) {
			}
			((UserTemplate)user).setProperties(this.getUserProperties(user.getUserId()));
		}
		return users;
	}

	public List<Integer> getUserIdsWithStatuses(int[] status) {
		return getExtendedJdbcTemplate().queryForList(getBoundSqlWithAdditionalParameter("ARCHITECTURE_SECURITY.SELECT_USER_ID_BY_STATUS", status).getSql(), Integer.class);
	}

	public List<User> findUsers(String nameOrEmail) {
		List<User> users = getExtendedJdbcTemplate().query(getBoundSql("ARCHITECTURE_SECURITY.SELECT_USERS_BY_EMAIL_OR_NAME").getSql(), userMapper2, new SqlParameterValue(Types.VARCHAR, nameOrEmail ), new SqlParameterValue(Types.VARCHAR, nameOrEmail ) );
		for(User user : users){
			try {
				((UserTemplate)user).setCompany( CompanyUtils.getCompany(user.getCompanyId()));
			} catch (CompanyNotFoundException e) {
			}
			((UserTemplate)user).setProperties(this.getUserProperties(user.getUserId()));
		}
		return users;
	}

	public List<User> findUsers(String nameOrEmail, int startIndex, int numResults) {		
		List<User> users = getExtendedJdbcTemplate().queryScrollable(getBoundSql("ARCHITECTURE_SECURITY.SELECT_USERS_BY_EMAIL_OR_NAME").getSql(), startIndex, numResults, new Object[]{nameOrEmail, nameOrEmail}, new int[]{Types.VARCHAR, Types.VARCHAR}, userMapper2);
		for(User user : users){
			try {
				((UserTemplate)user).setCompany( CompanyUtils.getCompany(user.getCompanyId()));
			} catch (CompanyNotFoundException e) {
			}
			((UserTemplate)user).setProperties(this.getUserProperties(user.getUserId()));
		}
		return users;
	}

	public int getFoundUserCount(String nameOrEmail) {
		return getExtendedJdbcTemplate().queryForInt(getBoundSql("ARCHITECTURE_SECURITY.COUNT_USERS_BY_EMAIL_OR_NAME").getSql(), new SqlParameterValue(Types.VARCHAR, nameOrEmail ), new SqlParameterValue(Types.VARCHAR, nameOrEmail ) );
	}

	public int getUserCount(Company company) {
		return getExtendedJdbcTemplate().queryForInt(getBoundSql("ARCHITECTURE_SECURITY.COUNT_COMPANY_USERS").getSql(), new SqlParameterValue(Types.INTEGER, company.getCompanyId() ));
	}

	public List<User> getUsers(Company company) {
		List<User> users = getExtendedJdbcTemplate().query(getBoundSql("ARCHITECTURE_SECURITY.SELECT_ALL_COMPANY_VISIBLE_USER").getSql(), userMapper2);
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
		List<User> users = getExtendedJdbcTemplate().queryScrollable(getBoundSql("ARCHITECTURE_SECURITY.SELECT_ALL_COMPANY_VISIBLE_USER").getSql(), startIndex, numResults, 
				new Object[]{company.getCompanyId()}, 
				new int[]{Types.INTEGER}, 
				userMapper2);
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
		List<User> users = getExtendedJdbcTemplate().query(getBoundSql("ARCHITECTURE_SECURITY.SELECT_COMPANY_USERS_BY_EMAIL_OR_NAME").getSql(), 
				userMapper2, 
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
		List<User> users = getExtendedJdbcTemplate().queryScrollable(getBoundSql("ARCHITECTURE_SECURITY.SELECT_COMPANY_USERS_BY_EMAIL_OR_NAME").getSql(), startIndex, numResults, 
				new Object[]{company.getCompanyId(), nameOrEmail, nameOrEmail}, 
				new int[]{Types.INTEGER, Types.VARCHAR, Types.VARCHAR}, userMapper2);
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
		return getExtendedJdbcTemplate().queryForInt(getBoundSql("ARCHITECTURE_SECURITY.COUNT_COMPANY_USERS_BY_EMAIL_OR_NAME").getSql(), new SqlParameterValue(Types.INTEGER, company.getCompanyId() ), new SqlParameterValue(Types.VARCHAR, nameOrEmail ), new SqlParameterValue(Types.VARCHAR, nameOrEmail ) );
	}

	public void switchCompanies(long companyId, Set<Long> users) {
		
	}
    
}
