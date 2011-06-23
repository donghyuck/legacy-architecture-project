package architecture.ee.model;

import java.util.Date;
import java.util.List;
import java.util.Map;

import architecture.ee.security.UnauthorizedException;
import architecture.ee.user.Group;
import architecture.ee.user.User;

public interface GroupModel extends BaseModel<Group> {

    public abstract long getGroupId();
    
    public abstract void setGroupId(long groupId);

    public abstract String getName();

    public abstract void setName(String name) throws UnauthorizedException;

    public abstract String getDescription();

    public abstract void setDescription(String description) throws UnauthorizedException;

    public abstract Date getCreationDate();

    public abstract void setCreationDate(Date date) throws UnauthorizedException;

    public abstract Date getModifiedDate();

    public abstract void setModifiedDate(Date date) throws UnauthorizedException;

    public abstract Map<String, String> getProperties();
	
    public abstract void setProperties(Map<String, String> properties);
    
	public abstract List<Long> getAdministratorIds();

	public abstract void setAdministratorIds(List<Long> administrators) ;

	public abstract List<Long> getMemberIds();
	
	public abstract void setMemberIds(List<Long> members);
	
	public abstract boolean isMember(User user);
	
	public abstract boolean isAdministrator(User user);
	
}
