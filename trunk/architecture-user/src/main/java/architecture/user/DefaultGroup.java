package architecture.user;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Set;

import architecture.common.cache.CacheSizes;
import architecture.common.model.factory.ModelTypeFactory;
import architecture.common.model.support.BaseModelObjectSupport;
import architecture.common.user.Company;
import architecture.common.user.User;
import architecture.common.user.UserNotFoundException;
import architecture.common.user.authentication.UnAuthorizedException;


public class DefaultGroup extends BaseModelObjectSupport implements Group {

	private Company company;
	
	private String displayName;
	
	private long companyId ;

	private long groupId;


	/**
	 */
	private String name;
	
	/**
	 */
	private String description;
	
	
	/**
	 */
	private Date creationDate;
	
	/**
	 */
	private Date modifiedDate;

	private List<Long> administrators;

	private Set<Long> addedAdministrators;

	private Set<Long> removedAdministrators;

	private List<Long> members;

	private Set<Long> addedMembers;

	private Set<Long> removedMembers;		
	
	public DefaultGroup () {
		groupId = -1L;
		companyId = -1L;
	}

	public Company getCompany() {
		return company;
	}

	public void setCompany(Company company) {
		this.company = company;
	}

	public List<Long> getAdministratorIds() {
		return administrators;
	}

	public long getCompanyId() {
		return companyId;
	}

	public void setCompanyId(long companyId) {
		this.companyId = companyId;
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
	 */
	public void setGroupId(long groupId) {
		this.groupId = groupId;
	}


	/**
	 * @return
	 */
	public long getGroupId() {
		return groupId;
	}

	/**
	 * @return
	 */
	public String getName() {
		return name;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	/**
	 * @param name
	 * @throws UnAuthorizedException
	 */
	public void setName(String name) throws UnAuthorizedException {
		if (name == this.name || (name != null && name.equals(this.name))) {
			// Do nothing
			return;
		}
		this.name = name;
	}

	/**
	 * @return
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description
	 * @throws UnAuthorizedException
	 */
	public void setDescription(String description) throws UnAuthorizedException {
		if (description == this.description
				|| (description != null && description.equals(this.description))) {
			return;
		}
		this.description = description;
	}

	/**
	 * @return
	 */
	public Date getCreationDate() {
		return creationDate;
	}

	/**
	 * @param date
	 * @throws UnAuthorizedException
	 */
	public void setCreationDate(Date date) throws UnAuthorizedException {
		this.creationDate = date;
	}

	/**
	 * @return
	 */
	public Date getModifiedDate() {

		return modifiedDate;
	}

	/**
	 * @param date
	 * @throws UnAuthorizedException
	 */
	public void setModifiedDate(Date date) throws UnAuthorizedException {
		this.modifiedDate = date;
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

	public int getModelObjectType() {
		return ModelTypeFactory.getTypeIdFromCode("GROUP");
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

	public int getCachedSize() {
        int size = CacheSizes.sizeOfObject();
        size += CacheSizes.sizeOfLong();
        size += CacheSizes.sizeOfString(name);
        size += CacheSizes.sizeOfString(displayName);
        size += CacheSizes.sizeOfString(description);
        size += CacheSizes.sizeOfDate();
        size += CacheSizes.sizeOfDate();
        size += CacheSizes.sizeOfCollection(members);
        size += CacheSizes.sizeOfCollection(administrators);
        return size;
	}

	public int getMemberCount() {
		if( members == null)
			return 0 ;
		
		return members.size();
	}

	public int getAdministratorCount() {
		if(administrators == null)
			return 0;
		return administrators.size();
	}
}
