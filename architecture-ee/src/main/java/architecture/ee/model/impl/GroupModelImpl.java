package architecture.ee.model.impl;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import architecture.ee.model.GroupModel;
import architecture.ee.model.ModelConstants;
import architecture.ee.security.authentication.UnauthorizedException;
import architecture.ee.user.Group;
import architecture.ee.user.User;
import architecture.ee.user.UserNotFoundException;

public class GroupModelImpl extends AbstractModelObject<Group> implements GroupModel {

	private long groupId;
	
	private String name;
	
	private String description;
	
	private Map<String, String> properties;
	
	private Date creationDate;
	
	private Date modifiedDate;

	private List<Long> administrators;

	private Set<Long> addedAdministrators;

	private Set<Long> removedAdministrators;

	private List<Long> members;

	private Set<Long> addedMembers;

	private Set<Long> removedMembers;
		
	public List<Long> getAdministratorIds() {
		return administrators;
	}

	public void setAdministratorIds(List<Long> administrators) {
		this.administrators = administrators;
	}

	public List<Long> getMemberIds() {
		return members;
	}

	public void setMemberIds(List<Long> members) {
		this.members = members;
	}

	public void setGroupId(long groupId) {
		this.groupId = groupId;
	}

	public void setProperties(Map<String, String> properties) {
		this.properties = properties;
	}

	public long getGroupId() {
		return groupId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) throws UnauthorizedException {
		if (name == this.name || (name != null && name.equals(this.name))) {
			// Do nothing
			return;
		}
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) throws UnauthorizedException {
		if (description == this.description
				|| (description != null && description.equals(this.description))) {
			return;
		}
		this.description = description;
	}

	public Date getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Date date) throws UnauthorizedException {
		this.creationDate = date;
	}

	public Date getModifiedDate() {

		return modifiedDate;
	}

	public void setModifiedDate(Date date) throws UnauthorizedException {
		this.modifiedDate = date;
	}

	public Map<String, String> getProperties() {
		synchronized (this) {
			if (properties == null) {
				properties = new ConcurrentHashMap<String, String>();
			}
		}
		// Return a wrapper that will intercept add and remove commands.
		return properties;
	}

	public boolean equals(Object object) {
		if (this == object)
			return true;
		if (object != null && (object instanceof Group))
			return groupId == ((Group) object).getGroupId();
		else
			return false;
	}

	@Override
	public Object clone() {
		return null;
	}

	@Override
	public int hashCode() {
		return name.hashCode();
	}

	private User getUser(long userId) throws UserNotFoundException {
		return null;
	}

	public boolean isMember(User user) {
		return members.contains(user.getUserId());
	}

	public boolean isAdministrator(User user) {
		return administrators.contains(user.getUserId());
	}

	
	public Serializable getPrimaryKeyObject() {
		return getGroupId();
	}

	public void setPrimaryKeyObject(Serializable primaryKeyObj) {
		setGroupId(((Long)primaryKeyObj).longValue());
	}

	public int getObjectType() {
		return ModelConstants.GROUP;
	}

	public int compareTo(Group o) {		
		long primaryKey = o.getGroupId();
		if (getGroupId() < primaryKey) {
			return -1;
		}
		else if (getGroupId() > primaryKey) {
			return 1;
		}
		else {
			return 0;
		}
	}
}
