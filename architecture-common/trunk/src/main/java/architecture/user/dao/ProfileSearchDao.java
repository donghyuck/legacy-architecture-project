package architecture.user.dao;

import java.util.Date;
import java.util.List;
import java.util.Set;

public interface ProfileSearchDao extends UserDao {
	
	public abstract List<Long> getUserBeforeDate(Long minUserID, Long maxUserID, Date date);

	public abstract int getUsersBeforeDateCount(Date date);

	public abstract List<Long> getUsersSinceDate(Date start, Date end);

	public abstract int getUsersSinceDateCount(Date start, Date end);

	public abstract int getHighestUserID();

	public abstract Set<String> getPrefixCharacters(boolean filterSymbols);
	
}
