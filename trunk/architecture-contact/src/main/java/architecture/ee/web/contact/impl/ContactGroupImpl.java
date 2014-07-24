package architecture.ee.web.contact.impl;

import java.io.Serializable;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import architecture.common.model.factory.ModelTypeFactory;
import architecture.common.model.support.BaseModelObjectSupport;
import architecture.ee.web.contact.ContactGroup;

public class ContactGroupImpl extends BaseModelObjectSupport implements ContactGroup{
	
	private Long groupId;
	private String groupName;
	private Long companyId;
	private int typeCode;
	private Long parentGroupId;
	//private String parentGroupName;
	private Long userId;
	private Long modifyId;
	private boolean delYn;
	private String typeName;
	//private List<Long> childGroupIds;
	private Set<Long> childGroupIds;
	private boolean hasChildren;
	private int lev;
	private String groupIds;
	
	
	
	
	public String getGroupIds() {
		return groupIds;
	}

	public void setGroupIds(String groupIds) {
		this.groupIds = groupIds;
	}

	public int getLev() {
		return lev;
	}

	public void setLev(int lev) {
		this.lev = lev;
	}

	public ContactGroupImpl() {
		groupId = -1L;
		parentGroupId = -1L;
		//childGroupIds = new ArrayList<Long>();
		childGroupIds = new HashSet<Long>();
	}

	
	
	/*public String getParentGroupName() {
		return parentGroupName;
	}

	public void setParentGroupName(String parentGroupName) {
		this.parentGroupName = parentGroupName;
	}*/

	public boolean isHasChildren() {
		return hasChildren;
	}


	public void setHasChildren(boolean hasChildren) {
		this.hasChildren = hasChildren;
	}


	/*
	public List<Long> getChildGroupIds() {
		return childGroupIds;
	}
	public void setChildGroupIds(List<Long> childGroupIds) {
		this.childGroupIds = childGroupIds;
	}*/
	
	
	
	public String getTypeName() {
		return typeName;
	}
	public Set<Long> getChildGroupIds() {
		return childGroupIds;
	} 

	public void setChildGroupIds(Set<Long> childGroupIds) {
		this.childGroupIds = childGroupIds;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	public Long getModifyId() {
		return modifyId;
	}
	public void setModifyId(Long modifyId) {
		this.modifyId = modifyId;
	}
	public boolean isDelYn() {
		return delYn;
	}
	public void setDelYn(boolean delYn) {
		this.delYn = delYn;
	}
	public int getCachedSize() {
		return 0;
	}
	public Serializable getPrimaryKeyObject() {
		return groupId;
	}
	public int getModelObjectType() {
		return ModelTypeFactory.getTypeIdFromCode("CONTACT_GROUP");
	}
	
	public Long getGroupId() {
		return groupId;
	}
	public void setGroupId(Long groupId) {
		this.groupId = groupId;
	}
	public String getGroupName() {
		return groupName;
	}
	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}
	public Long getCompanyId() {
		return companyId;
	}
	public void setCompanyId(Long companyId) {
		this.companyId = companyId;
	}
	public Long getParentGroupId() {
		return parentGroupId;
	}
	public void setParentGroupId(Long parentGroupId) {
		this.parentGroupId = parentGroupId;
	}
	public int getTypeCode() {
		return typeCode;
	}
	public void setTypeCode(int typeCode) {
		this.typeCode = typeCode;
	}
	
}
