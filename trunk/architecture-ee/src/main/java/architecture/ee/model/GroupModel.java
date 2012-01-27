package architecture.ee.model;

import java.util.Date;
import java.util.List;
import java.util.Map;

import architecture.ee.security.authentication.UnauthorizedException;
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
	 * @throws UnauthorizedException
	 */
    public abstract void setName(String name) throws UnauthorizedException;

    /**
	 * @return
	 */
    public abstract String getDescription();

    /**
	 * @param description
	 * @throws UnauthorizedException
	 */
    public abstract void setDescription(String description) throws UnauthorizedException;
    
    
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
