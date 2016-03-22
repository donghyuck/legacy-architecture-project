/*
 * Copyright 2012 Donghyuck, Son
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
package architecture.user.security.simple.authentication.impl;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.JdbcUtils;
import org.springframework.security.authentication.encoding.PasswordEncoder;

import architecture.common.user.UserNotFoundException;
import architecture.common.user.authentication.AuthToken;
import architecture.common.user.authentication.UnAuthorizedException;
import architecture.common.util.L10NUtils;
import architecture.ee.spring.jdbc.support.ExtendedJdbcDaoSupport;
import architecture.user.security.simple.authentication.AuthProvider;
import architecture.user.security.simple.authentication.SimpleUserToken;

public class JdbcAuthProvider extends ExtendedJdbcDaoSupport implements AuthProvider {

    private PasswordEncoder passwordEncoder;

    private String queryStringForPassword;

    private String queryStringForUserProperty;

    private boolean digestSupported = true;

    public String getQueryStringForPassword() {
	return queryStringForPassword;
    }

    public void setQueryStringForPassword(String queryStringForPassword) {
	this.queryStringForPassword = queryStringForPassword;
    }

    public String getQueryStringForUserProperty() {
	return queryStringForUserProperty;
    }

    public void setQueryStringForUserProperty(String queryStringForUserProperty) {
	this.queryStringForUserProperty = queryStringForUserProperty;
    }

    public boolean isPlainSupported() {
	return true;
    }

    public void setDigestSupported(boolean digestSupported) {
	this.digestSupported = digestSupported;
    }

    public boolean isDigestSupported() {
	return digestSupported;
    }

    public PasswordEncoder getPasswordEncoder() {
	return passwordEncoder;
    }

    public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
	this.passwordEncoder = passwordEncoder;
    }

    public void authenticate(String username, String password) throws UnAuthorizedException {
	if (username == null || password == null) {
	    throw new UnAuthorizedException(L10NUtils.getMessage("005003"));
	}
	String passwordToUser = password;
	if (isDigestSupported()) {
	    passwordToUser = passwordEncoder.encodePassword(password, null);
	}
	try {
	    if (!passwordToUser.equals(getPassword(username))) {
		String msg = L10NUtils.format("005002", username);
		throw new UnAuthorizedException(msg);
	    }
	} catch (UserNotFoundException e) {
	    String msg = L10NUtils.format("005001", username);
	    throw new UnAuthorizedException(msg, e);
	}
	// Got this far, so the user must be authorized.
    }

    public AuthToken authenticateAndGetAuthToken(String username, String password) throws UnAuthorizedException {

	if (username == null || password == null) {
	    throw new UnAuthorizedException(L10NUtils.getMessage("005003"));
	}

	String passwordToUser = password;
	if (isDigestSupported()) {
	    passwordToUser = passwordEncoder.encodePassword(password, null);
	}

	if (log.isDebugEnabled())
	    log.debug(
		    String.format("authenticate username:%s, password:%s ", new Object[] { username, passwordToUser }));

	try {
	    if (!passwordToUser.equals(getPassword(username))) {
		String msg = L10NUtils.format("005002", username);
		throw new UnAuthorizedException(msg);
	    }
	} catch (UserNotFoundException e) {
	    String msg = L10NUtils.format("005001", username);
	    throw new UnAuthorizedException(msg, e);
	}

	SimpleUserToken token = new SimpleUserToken(username);
	token.setProperties(getUserProperties(username));
	return null;
    }

    public String getPassword(String username) throws UserNotFoundException {
	try {

	    return getJdbcTemplate().queryForObject(getQueryStringForPassword(), new String[] { username },
		    new int[] { Types.VARCHAR }, String.class);

	} catch (DataAccessException e) {
	    throw new UserNotFoundException(e);
	}
    }

    public Map<String, String> getUserProperties(String username) {
	try {

	    return getJdbcTemplate().queryForObject(getQueryStringForUserProperty(), new String[] { username },
		    new int[] { Types.VARCHAR }, new RowMapper<Map<String, String>>() {

			public Map<String, String> mapRow(ResultSet rs, int rowNum) throws SQLException {
			    ResultSetMetaData rsmd = rs.getMetaData();
			    int columnCount = rsmd.getColumnCount();
			    Map<String, String> mapOfColValues = new HashMap<String, String>();
			    for (int i = 1; i <= columnCount; i++) {
				String key = getColumnKey(JdbcUtils.lookupColumnName(rsmd, i));
				String value = rs.getString(i);
				mapOfColValues.put(key, value);
			    }
			    return mapOfColValues;
			}

			protected String getColumnKey(String columnName) {
			    return columnName;
			}
		    });

	} catch (DataAccessException e) {
	    return Collections.EMPTY_MAP;
	}
    }

}
