package architecture.security.user.dao.impl;

import java.util.Date;
import java.util.List;
import java.util.Set;

import architecture.security.user.dao.ProfileSearchDao;

public class JdbcProfileSearchDao extends JdbcUserDao implements ProfileSearchDao {

	public List<Long> getUserBeforeDate(Long minUserID, Long maxUserID, Date date) {
		return null;
	}

	public int getUsersBeforeDateCount(Date date) {
		return 0;
	}

	public List<Long> getUsersSinceDate(Date start, Date end) {
		return null;
	}

	public int getUsersSinceDateCount(Date start, Date end) {
		return 0;
	}

	public int getHighestUserID() {
		return 0;
	}

	public Set<String> getPrefixCharacters(boolean filterSymbols) {
		return null;
	}

}
