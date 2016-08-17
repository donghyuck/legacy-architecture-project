/*
 * Copyright 2016 donghyuck
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
import architecture.common.user.Group;
import architecture.common.user.User;
import architecture.common.user.UserTemplate;
import architecture.ee.jdbc.property.dao.ExtendedPropertyDao;
import architecture.ee.spring.jdbc.support.ExtendedJdbcDaoSupport;
import architecture.user.dao.UserDao;
import architecture.user.util.CompanyUtils;

public abstract class AbstractJdbcUserDao extends ExtendedJdbcDaoSupport implements UserDao {

    private ExtendedPropertyDao extendedPropertyDao;
    
    private String sequencerName = "USER";
    
    private String userPropertyTableName = "V2_USER_PROPERTY";
    
    private String userPropertyPrimaryColumnName = "USER_ID";
    
    public AbstractJdbcUserDao() {
    }

    public ExtendedPropertyDao getExtendedPropertyDao() {
        return extendedPropertyDao;
    }

    public void setExtendedPropertyDao(ExtendedPropertyDao extendedPropertyDao) {
        this.extendedPropertyDao = extendedPropertyDao;
    }

    public String getSequencerName() {
        return sequencerName;
    }

    public void setSequencerName(String sequencerName) {
        this.sequencerName = sequencerName;
    }

    public String getUserPropertyTableName() {
        return userPropertyTableName;
    }

    public void setUserPropertyTableName(String userPropertyTableName) {
        this.userPropertyTableName = userPropertyTableName;
    }

    public String getUserPropertyPrimaryColumnName() {
        return userPropertyPrimaryColumnName;
    }

    public void setUserPropertyPrimaryColumnName(String userPropertyPrimaryColumnName) {
        this.userPropertyPrimaryColumnName = userPropertyPrimaryColumnName;
    }


    /**
     * 새로운 사용자를 생성한다.
     * 
     * @param u
     */
    public abstract User create(User user) ;

    public abstract User update(User user) ;

    
    protected abstract RowMapper<User> getUserRowMapper();

    
    protected long getNextUserId(){
	return getNextId(sequencerName);
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
		new Object[] { propertyName, propertyValue }, new int[] { Types.VARCHAR, Types.VARCHAR },
		Integer.class);
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
	    log.info((new StringBuilder()).append("No match found for user ").append(template).append(".").toString());
	user = retVal;
	return user;
    }
    
    public User getUserByUsername(String username) {
	UserTemplate user = null;
	try {
	    user = (UserTemplate) getExtendedJdbcTemplate().queryForObject(
		    getBoundSql("ARCHITECTURE_SECURITY.SELECT_USER_BY_USERNAME").getSql(), new Object[] { username },
		    new int[] { Types.VARCHAR }, getUserRowMapper());
	    try {
		user.setCompany(CompanyUtils.getCompany(user.getCompanyId()));
	    } catch (CompanyNotFoundException e) {
	    }
	    user.setProperties(getUserProperties(user.getUserId()));
	} catch (EmptyResultDataAccessException e) {
	    String message = (new StringBuilder())
		    .append("Failure attempting to load user by case-insensitive username '").append(username)
		    .append("'.").toString();
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
	    user = (UserTemplate) getExtendedJdbcTemplate().queryForObject(
		    getBoundSql("ARCHITECTURE_SECURITY.SELECT_USER_BY_USERNAME").getSql(),
		    new Object[] { username.toLowerCase() }, new int[] { Types.VARCHAR }, getUserRowMapper());
	    try {
		user.setCompany(CompanyUtils.getCompany(user.getCompanyId()));
	    } catch (CompanyNotFoundException e) {
	    }
	    user.setProperties(getUserProperties(user.getUserId()));
	} catch (EmptyResultDataAccessException e) {
	    String message = (new StringBuilder())
		    .append("Failure attempting to load user by case-insensitive username '").append(username)
		    .append("'.").toString();
	    log.fatal(message, e);
	    // 동작중인 경우 레거시 연계 ...
	} catch (DataAccessException e) {
	}
	return user;
    }

    public User getUserByEmail(String email) {

	UserTemplate usertemplate = null;

	if (null != email) {
	    String emailMatch = email.replace('*', '%');
	    try {

		UserTemplate user = (UserTemplate)getExtendedJdbcTemplate().queryForObject(
			getBoundSql("ARCHITECTURE_SECURITY.SELECT_USER_BY_EMAIL").getSql(), getUserRowMapper(),
			new SqlParameterValue(Types.VARCHAR, emailMatch));
		try {
		    user.setCompany(CompanyUtils.getCompany(user.getCompanyId()));
		} catch (CompanyNotFoundException e) {
		}
		user.setProperties(getUserProperties(user.getUserId()));
		usertemplate = user;

	    } catch (IncorrectResultSizeDataAccessException e) {
		if (e.getActualSize() > 1) {
		    log.warn((new StringBuilder()).append("Multiple occurrances of the same email found: ")
			    .append(email).toString());
		    throw e;
		}
	    } catch (DataAccessException e) {
		String message = (new StringBuilder()).append("Failure attempting to load user by email '")
			.append(emailMatch).append("'").toString();
		log.fatal(message, e);
		throw e;
	    }
	}
	return usertemplate;
    }

    public User getUserById(long userId) {

	if (userId <= 0L) {
	    return null;
	}

	UserTemplate user = null;
	try {
	    user = (UserTemplate)getExtendedJdbcTemplate().queryForObject(
		    getBoundSql("ARCHITECTURE_SECURITY.SELECT_USER_BY_ID").getSql(), getUserRowMapper(),
		    new SqlParameterValue(Types.NUMERIC, userId));
	    try {
		user.setCompany(CompanyUtils.getCompany(user.getCompanyId()));
	    } catch (CompanyNotFoundException e) {
	    }
	    user.setProperties(getUserProperties(user.getUserId()));
	} catch (IncorrectResultSizeDataAccessException e) {
	    if (e.getActualSize() > 1) {
		log.warn((new StringBuilder()).append("Multiple occurrances of the same user ID found: ").append(userId)
			.toString());
		throw e;
	    }
	} catch (DataAccessException e) {
	    String message = (new StringBuilder()).append("Failure attempting to load user by ID : ").append(userId)
		    .append(".").toString();
	    log.fatal(message, e);
	}
	return user;

    }

    public void delete(User user) {
	getExtendedJdbcTemplate().update(getBoundSql("ARCHITECTURE_SECURITY.DELETE_GROUP_MEMBERSHIP").getSql(),
		new Object[] { user.getUserId() }, new int[] { Types.NUMERIC });
	extendedPropertyDao.deleteProperties(userPropertyTableName, userPropertyPrimaryColumnName, user.getUserId());
	getExtendedJdbcTemplate().update(getBoundSql("ARCHITECTURE_SECURITY.DELETE_USER_BY_ID").getSql(),
		new Object[] { user.getUserId() }, new int[] { Types.NUMERIC });
    }

    public Map<String, String> getUserProperties(long userId) {
	return extendedPropertyDao.getProperties(userPropertyTableName, userPropertyPrimaryColumnName, userId);
    }

    public void setUserProperties(long userId, Map<String, String> props) {
	extendedPropertyDao.updateProperties(userPropertyTableName, userPropertyPrimaryColumnName, userId, props);
    }

    public List<User> getApplicationUsers() {
	List<User> users = getExtendedJdbcTemplate().query(getBoundSql("ARCHITECTURE_SECURITY.SELECT_ALL_ENABLED_USER").getSql(), getUserRowMapper());
	for (User user : users) {
	    try {
		((UserTemplate) user).setCompany(CompanyUtils.getCompany(user.getCompanyId()));
	    } catch (CompanyNotFoundException e) {
	    }
	    ((UserTemplate) user).setProperties(this.getUserProperties(user.getUserId()));
	}
	return users;
    }

    public List<User> getApplicationUsers(int startIndex, int numResults) {
	List<User> users = getExtendedJdbcTemplate().queryScrollable(
		getBoundSql("ARCHITECTURE_SECURITY.SELECT_ALL_ENABLED_USER").getSql(), startIndex, numResults,
		new Object[0], new int[0], getUserRowMapper());
	for (User user : users) {
	    try {
		((UserTemplate) user).setCompany(CompanyUtils.getCompany(user.getCompanyId()));
	    } catch (CompanyNotFoundException e) {
	    }
	    ((UserTemplate) user).setProperties(this.getUserProperties(user.getUserId()));
	}
	return users;
    }

    public int getTotalUserCount() {
	return getExtendedJdbcTemplate().queryForObject(getBoundSql("ARCHITECTURE_SECURITY.COUNT_VISIBLE_USER").getSql(), Integer.class);
    }

    public int getApplicationUserCount() {
	return getExtendedJdbcTemplate().queryForObject(getBoundSql("ARCHITECTURE_SECURITY.COUNT_ENABLED_USER").getSql(), Integer.class);
    }

    public int getAuthenticatedUserCount() {
	return getExtendedJdbcTemplate().queryForObject(getBoundSql("ARCHITECTURE_SECURITY.COUNT_AUTHENTICATED_USER").getSql(), Integer.class);
    }

    public int getRecentUserCount(Date date) {
	return getExtendedJdbcTemplate().queryForObject(
		getBoundSql("ARCHITECTURE_SECURITY.COUNT_RECENT_USER").getSql(), 
		Integer.class,
		new SqlParameterValue(Types.DATE, date)
		);
    }

    public List<User> getAllUsers() {
	List<User> users = getExtendedJdbcTemplate().query(getBoundSql("ARCHITECTURE_SECURITY.SELECT_ALL_VISIBLE_USER").getSql(), getUserRowMapper());
	for (User user : users) {
	    try {
		((UserTemplate) user).setCompany(CompanyUtils.getCompany(user.getCompanyId()));
	    } catch (CompanyNotFoundException e) {
	    }
	    ((UserTemplate) user).setProperties(this.getUserProperties(user.getUserId()));
	}
	return users;
    }

    public List<User> getAllUsers(int startIndex, int numResults) {
	List<User> users = getExtendedJdbcTemplate().queryScrollable(
		getBoundSql("ARCHITECTURE_SECURITY.SELECT_ALL_VISIBLE_USER").getSql(), startIndex, numResults,
		new Object[0], new int[0], getUserRowMapper());
	for (User user : users) {
	    try {
		((UserTemplate) user).setCompany(CompanyUtils.getCompany(user.getCompanyId()));
	    } catch (CompanyNotFoundException e) {
	    }
	    ((UserTemplate) user).setProperties(this.getUserProperties(user.getUserId()));
	}
	return users;
    }

    public List<Long> getUserIdsWithStatuses(int[] status) {
	return getExtendedJdbcTemplate().queryForList(
		getBoundSqlWithAdditionalParameter("ARCHITECTURE_SECURITY.SELECT_USER_ID_BY_STATUS", status).getSql(),
		Long.class);
    }

    public List<User> findUsers(String nameOrEmail) {
	List<User> users = getExtendedJdbcTemplate().query(
		getBoundSql("ARCHITECTURE_SECURITY.SELECT_USERS_BY_EMAIL_OR_NAME").getSql(), getUserRowMapper(),
		new SqlParameterValue(Types.VARCHAR, nameOrEmail), new SqlParameterValue(Types.VARCHAR, nameOrEmail));
	for (User user : users) {
	    try {
		((UserTemplate) user).setCompany(CompanyUtils.getCompany(user.getCompanyId()));
	    } catch (CompanyNotFoundException e) {
	    }
	    ((UserTemplate) user).setProperties(this.getUserProperties(user.getUserId()));
	}
	return users;
    }

    public List<User> findUsers(String nameOrEmail, int startIndex, int numResults) {
	List<User> users = getExtendedJdbcTemplate().queryScrollable(
		getBoundSql("ARCHITECTURE_SECURITY.SELECT_USERS_BY_EMAIL_OR_NAME").getSql(), startIndex, numResults,
		new Object[] { nameOrEmail, nameOrEmail }, new int[] { Types.VARCHAR, Types.VARCHAR }, getUserRowMapper());
	for (User user : users) {
	    try {
		((UserTemplate) user).setCompany(CompanyUtils.getCompany(user.getCompanyId()));
	    } catch (CompanyNotFoundException e) {
	    }
	    ((UserTemplate) user).setProperties(this.getUserProperties(user.getUserId()));
	}
	return users;
    }

    public int getFoundUserCount(String nameOrEmail) {
	return getExtendedJdbcTemplate().queryForObject(
		getBoundSql("ARCHITECTURE_SECURITY.COUNT_USERS_BY_EMAIL_OR_NAME").getSql(),
		Integer.class,
		new SqlParameterValue(Types.VARCHAR, nameOrEmail), new SqlParameterValue(Types.VARCHAR, nameOrEmail));
    }

    public int getUserCount(Company company) {
	return getExtendedJdbcTemplate().queryForObject(getBoundSql("ARCHITECTURE_SECURITY.COUNT_COMPANY_USERS").getSql(),
		Integer.class,
		new SqlParameterValue(Types.INTEGER, company.getCompanyId()));
    }

    public List<User> getUsers(Company company) {
	List<User> users = getExtendedJdbcTemplate().query(getBoundSql("ARCHITECTURE_SECURITY.SELECT_ALL_COMPANY_VISIBLE_USER").getSql(), getUserRowMapper());
	for (User user : users) {
	    try {
		((UserTemplate) user).setCompany(CompanyUtils.getCompany(user.getCompanyId()));
	    } catch (CompanyNotFoundException e) {
	    }
	    ((UserTemplate) user).setProperties(this.getUserProperties(user.getUserId()));
	}
	return users;
    }

    public List<User> getUsers(Company company, int startIndex, int numResults) {
	List<User> users = getExtendedJdbcTemplate().queryScrollable(
		getBoundSql("ARCHITECTURE_SECURITY.SELECT_ALL_COMPANY_VISIBLE_USER").getSql(), startIndex, numResults,
		new Object[] { company.getCompanyId() }, new int[] { Types.INTEGER }, getUserRowMapper());
	for (User user : users) {
	    try {
		((UserTemplate) user).setCompany(CompanyUtils.getCompany(user.getCompanyId()));
	    } catch (CompanyNotFoundException e) {
	    }
	    ((UserTemplate) user).setProperties(this.getUserProperties(user.getUserId()));
	}
	return users;
    }

    public List<User> findUsers(Company company, String nameOrEmail) {
	List<User> users = getExtendedJdbcTemplate().query(
		getBoundSql("ARCHITECTURE_SECURITY.SELECT_COMPANY_USERS_BY_EMAIL_OR_NAME").getSql(), getUserRowMapper(),
		new SqlParameterValue(Types.INTEGER, company.getCompanyId()),
		new SqlParameterValue(Types.VARCHAR, nameOrEmail), new SqlParameterValue(Types.VARCHAR, nameOrEmail));
	for (User user : users) {
	    try {
		((UserTemplate) user).setCompany(CompanyUtils.getCompany(user.getCompanyId()));
	    } catch (CompanyNotFoundException e) {
	    }
	    ((UserTemplate) user).setProperties(this.getUserProperties(user.getUserId()));
	}
	return users;
    }

    public List<User> findUsers(Company company, String nameOrEmail, int startIndex, int numResults) {
	List<User> users = getExtendedJdbcTemplate().queryScrollable(
		getBoundSql("ARCHITECTURE_SECURITY.SELECT_COMPANY_USERS_BY_EMAIL_OR_NAME").getSql(), startIndex,
		numResults, new Object[] { company.getCompanyId(), nameOrEmail, nameOrEmail },
		new int[] { Types.INTEGER, Types.VARCHAR, Types.VARCHAR }, getUserRowMapper());
	for (User user : users) {
	    try {
		((UserTemplate) user).setCompany(CompanyUtils.getCompany(user.getCompanyId()));
	    } catch (CompanyNotFoundException e) {
	    }
	    ((UserTemplate) user).setProperties(this.getUserProperties(user.getUserId()));
	}
	return users;
    }

    public int getFoundUserCount(Company company, String nameOrEmail) {
	return getExtendedJdbcTemplate().queryForObject(
		getBoundSql("ARCHITECTURE_SECURITY.COUNT_COMPANY_USERS_BY_EMAIL_OR_NAME").getSql(),
		Integer.class,
		new SqlParameterValue(Types.INTEGER, company.getCompanyId()),
		new SqlParameterValue(Types.VARCHAR, nameOrEmail), new SqlParameterValue(Types.VARCHAR, nameOrEmail));
    }

    public void switchCompanies(long companyId, Set<Long> users) {

    }

    public List<Long> findUserIds(Company company, Group group, String nameOrEmail) {
	return getExtendedJdbcTemplate().queryForList(
		getBoundSql("ARCHITECTURE_SECURITY.SELECT_COMPANY_USER_IDS_BY_EMAIL_OR_NAME_WITH_GROUP_FILTER").getSql(),
		Long.class, new SqlParameterValue(Types.INTEGER, company.getCompanyId()),
		new SqlParameterValue(Types.VARCHAR, nameOrEmail), new SqlParameterValue(Types.VARCHAR, nameOrEmail),
		new SqlParameterValue(Types.INTEGER, group.getGroupId()));
    }

    public List<Long> findUserIds(Company company, Group group, String nameOrEmail, int startIndex, int numResults) {
	return getExtendedJdbcTemplate().queryScrollable(
		getBoundSql("ARCHITECTURE_SECURITY.SELECT_COMPANY_USER_IDS_BY_EMAIL_OR_NAME_WITH_GROUP_FILTER").getSql(),
		startIndex, numResults,
		new Object[] { company.getCompanyId(), nameOrEmail, nameOrEmail, group.getGroupId() },
		new int[] { Types.NUMERIC, Types.VARCHAR, Types.VARCHAR, Types.NUMERIC }, Long.class);
    }

    public int getFoundUserCount(Company company, Group group, String nameOrEmail) {
	return getExtendedJdbcTemplate().queryForObject(
		getBoundSql("ARCHITECTURE_SECURITY.COUNT_COMPANY_USERS_BY_EMAIL_OR_NAME_WITH_GROUP_FILTER").getSql(),
		Integer.class, 
		new SqlParameterValue(Types.INTEGER, company.getCompanyId()),
		new SqlParameterValue(Types.VARCHAR, nameOrEmail), 
		new SqlParameterValue(Types.VARCHAR, nameOrEmail),
		new SqlParameterValue(Types.INTEGER, group.getGroupId()));
    }
    
}
