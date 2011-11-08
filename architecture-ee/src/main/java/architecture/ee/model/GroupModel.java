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
	 * @uml.property  name="groupId"
	 */
    public abstract long getGroupId();
    
    /**
	 * @param  groupId
	 * @uml.property  name="groupId"
	 */
    public abstract void setGroupId(long groupId);

    /**
	 * @return
	 * @uml.property  name="name"
	 */
    public abstract String getName();

    /**
	 * @param name
	 * @throws UnauthorizedException
	 * @uml.property  name="name"
	 */
    public abstract void setName(String name) throws UnauthorizedException;

    /**
	 * @return
	 * @uml.property  name="description"
	 */
    public abstract String getDescription();

    /**
	 * @param description
	 * @throws UnauthorizedException
	 * @uml.property  name="description"
	 */
    public abstract void setDescription(String description) throws UnauthorizedException;

    /**
	 * @return
	 * @uml.property  name="creationDate"
	 */
    public abstract Date getCreationDate();

    /**
	 * @param date
	 * @throws UnauthorizedException
	 * @uml.property  name="creationDate"
	 */
    public abstract void setCreationDate(Date date) throws UnauthorizedException;

    /**
	 * @return
	 * @uml.property  name="modifiedDate"
	 */
    public abstract Date getModifiedDate();

    /**
	 * @param date
	 * @throws UnauthorizedException
	 * @uml.property  name="modifiedDate"
	 */
    public abstract void setModifiedDate(Date date) throws UnauthorizedException;

    /**
	 * @return
	 * @uml.property  name="properties"
	 */
    public abstract Map<String, String> getProperties();
	
    /**
	 * @param  properties
	 * @uml.property  name="properties"
	 */
    public abstract void setProperties(Map<String, String> properties);
    
	/**
	 * @return
	 * @uml.property  name="administratorIds"
	 */
	public abstract List<Long> getAdministratorIds();

	/**
	 * @param  administrators
	 * @uml.property  name="administratorIds"
	 */
	public abstract void setAdministratorIds(List<Long> administrators) ;

	/**
	 * @return
	 * @uml.property  name="memberIds"
	 */
	public abstract List<Long> getMemberIds();
	
	/**
	 * @param  members
	 * @uml.property  name="memberIds"
	 */
	public abstract void setMemberIds(List<Long> members);
	
	public abstract boolean isMember(User user);
	
	public abstract boolean isAdministrator(User user);
	
}
