package architecture.security.user;

import java.util.List;

import architecture.common.user.User;

public interface GroupManager {
	
	public abstract List<Group> getUserGroups(User user);
	
}
