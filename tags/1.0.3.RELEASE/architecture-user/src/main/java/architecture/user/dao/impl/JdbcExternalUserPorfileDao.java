package architecture.user.dao.impl;

import java.sql.Types;
import java.util.Collections;
import java.util.Map;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.SqlParameterValue;

import architecture.common.user.User;
import architecture.ee.spring.jdbc.support.ExtendedJdbcDaoSupport;
import architecture.user.dao.ExternalUserProfileDao;

public class JdbcExternalUserPorfileDao extends ExtendedJdbcDaoSupport implements ExternalUserProfileDao {

	private String sqlSetName = "EXTENDED_SECURITY" ;
	
	public Map<String, Object> getProfile(User user) {
		return getProfile(user.getUserId());
	}

	public Map<String, Object> getProfile(long userId) {
		try {
			return getExtendedJdbcTemplate().queryForMap(getSql("SELECT_USER_PROFILE_BY_USER_ID"), new SqlParameterValue(Types.INTEGER, userId ));
		} catch (DataAccessException e) {
			return Collections.EMPTY_MAP;
		}
	}

	public void setProfile(User user, Map<String, Object> profile) {
	}

	public void setProfile(long userId, Map<String, Object> profile) {
		
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

}
