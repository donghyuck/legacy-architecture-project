package architecture.ee.model;

import java.util.List;
import java.util.Map;

import architecture.common.model.ModelObject;
import architecture.ee.security.authentication.UnAuthorizedException;
import architecture.ee.user.Group;
import architecture.ee.user.User;

/**
 * @author                 donghyuck
 */
public interface GroupModel extends ModelObject<Group>{

    /**
	 * @return
	 */
    public abstract long getGroupId();
    
    /**
	 * @param  groupId
	 */
    public abstract void setGroupId(long groupId);

    /**
	 * @return
	 */
    public abstract String getName();

    /**
	 * @param name
	 * @throws UnAuthorizedException
	 */
    public abstract void setName(String name) throws UnAuthorizedException;

    /**
	 * @return
	 */
    public abstract String getDescription();

    /**
	 * @param description
	 * @throws UnAuthorizedException
	 */
    public abstract void setDescription(String description) throws UnAuthorizedException;
    
    
    public abstract Map<String, String> getProperties();
	
    /**
	 * @param  properties
	 */
    public abstract void setProperties(Map<String, String> properties);
    
	/**
	 * @return
	 */
	public abstract List<Long> getAdministratorIds();

	/**
	 * @param  administrators
	 */
	public abstract void setAdministratorIds(List<Long> administrators) ;

	/**
	 * @return
	 */
	public abstract List<Long> getMemberIds();
	
	/**
	 * @param  members
	 */
	public abstract void setMemberIds(List<Long> members);
	
	public abstract boolean isMember(User user);
	
	public abstract boolean isAdministrator(User user);
	
}
