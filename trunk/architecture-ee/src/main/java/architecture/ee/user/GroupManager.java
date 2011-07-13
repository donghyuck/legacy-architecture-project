package architecture.ee.user;

import java.util.List;

public interface GroupManager {
	
	public abstract List<Group> getUserGroups(User user);
	
}
