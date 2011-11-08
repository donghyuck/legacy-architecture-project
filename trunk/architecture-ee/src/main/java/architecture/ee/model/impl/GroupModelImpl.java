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

/**
 * @author  donghyuck
 */
public class GroupModelImpl extends BaseModelObject<Group> implements GroupModel {

	/**
	 * @uml.property  name="groupId"
	 */
	private long groupId;
	
	/**
	 * @uml.property  name="name"
	 */
	private String name;
	
	/**
	 * @uml.property  name="description"
	 */
	private String description;
	
	/**
	 * @uml.property  name="properties"
	 */
	private Map<String, String> properties;
	
	/**
	 * @uml.property  name="creationDate"
	 */
	private Date creationDate;
	
	/**
	 * @uml.property  name="modifiedDate"
	 */
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

	/**
	 * @param groupId
	 * @uml.property  name="groupId"
	 */
	public void setGroupId(long groupId) {
		this.groupId = groupId;
	}

	/**
	 * @param properties
	 * @uml.property  name="properties"
	 */
	public void setProperties(Map<String, String> properties) {
		this.properties = properties;
	}

	/**
	 * @return
	 * @uml.property  name="groupId"
	 */
	public long getGroupId() {
		return groupId;
	}

	/**
	 * @return
	 * @uml.property  name="name"
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 * @throws UnauthorizedException
	 * @uml.property  name="name"
	 */
	public void setName(String name) throws UnauthorizedException {
		if (name == this.name || (name != null && name.equals(this.name))) {
			// Do nothing
			return;
		}
		this.name = name;
	}

	/**
	 * @return
	 * @uml.property  name="description"
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description
	 * @throws UnauthorizedException
	 * @uml.property  name="description"
	 */
	public void setDescription(String description) throws UnauthorizedException {
		if (description == this.description
				|| (description != null && description.equals(this.description))) {
			return;
		}
		this.description = description;
	}

	/**
	 * @return
	 * @uml.property  name="creationDate"
	 */
	public Date getCreationDate() {
		return creationDate;
	}

	/**
	 * @param date
	 * @throws UnauthorizedException
	 * @uml.property  name="creationDate"
	 */
	public void setCreationDate(Date date) throws UnauthorizedException {
		this.creationDate = date;
	}

	/**
	 * @return
	 * @uml.property  name="modifiedDate"
	 */
	public Date getModifiedDate() {

		return modifiedDate;
	}

	/**
	 * @param date
	 * @throws UnauthorizedException
	 * @uml.property  name="modifiedDate"
	 */
	public void setModifiedDate(Date date) throws UnauthorizedException {
		this.modifiedDate = date;
	}

	/**
	 * @return
	 * @uml.property  name="properties"
	 */
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
