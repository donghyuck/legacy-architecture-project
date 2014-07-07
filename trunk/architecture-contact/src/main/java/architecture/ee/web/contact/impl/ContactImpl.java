package architecture.ee.web.contact.impl;

import java.io.Serializable;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;

import architecture.common.model.factory.ModelTypeFactory;
import architecture.common.model.support.BaseModelObjectSupport;
import architecture.common.user.User;
import architecture.ee.web.contact.Contact;
import architecture.ee.web.contact.ContactGroup;
import architecture.ee.web.contact.Tag;

public class ContactImpl extends BaseModelObjectSupport implements Contact {
	
	private Long contactId;
	private String name;
	private String email;
	private String phone;
	private String cellPhone;
	private Long groupId;
	private String tag;
	private String contactDesc;
	private Long userId;
	private Long modifyId;
	private boolean delYn;
	private int typeCode;
	private Long companyId;
	private String typeName;
	//private List<String> groupNames;
	//private List<Tag> tagList;
	//private List<ContactGroup> groupList;
	private ContactGroup contactGroup;
	private String groupIds;
	private User user;
	
	
	
	/*constructors*/
	public ContactImpl() {	
		contactId = -1L;
		name = "";
		companyId = -1L;
		typeCode = -1;
	}
	
	public ContactImpl(User user) {
		contactId = -1L;
		name = "";
		companyId = -1L;
		typeCode = -1;
		this.user = user;
		this.userId = user == null? -1L : user.getUserId();
	}
	
	
	
	public ContactGroup getContactGroup() {
		return contactGroup;
	}

	public void setContactGroup(ContactGroup contactGroup) {
		this.contactGroup = contactGroup;
	}

	public String getGroupIds() {
		return groupIds;
	}

	public void setGroupIds(String groupIds) {
		this.groupIds = groupIds;
	}

	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	/*public List<Tag> getTagList() {
		return tagList;
	}
	public void setTagList(List<Tag> tagList) {
		this.tagList = tagList;
	}
	public List<ContactGroup> getGroupList() {
		return groupList;
	}
	public void setGroupList(List<ContactGroup> groupList) {
		this.groupList = groupList;
	}
	public List<String> getGroupNames() {
		return groupNames;
	}
	public void setGroupNames(List<String> groupNames) {
		this.groupNames = groupNames;
	}
	*/
	public String getTypeName() {
		return typeName;
	}
	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}
	public int getTypeCode() {
		return typeCode;
	}
	public void setTypeCode(int typeCode) {
		this.typeCode = typeCode;
	}
	public Long getCompanyId() {
		return companyId;
	}
	public void setCompanyId(Long companyId) {
		this.companyId = companyId;
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
	public int getModelObjectType() {
		return ModelTypeFactory.getTypeIdFromCode("CONTACT");
	}
	
	public Serializable getPrimaryKeyObject() {
		return contactId;
	}
	
	
	
	
	public Long getContactId() {
		return contactId;
	}
	public void setContactId(Long contactId) {
		this.contactId = contactId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getCellPhone() {
		return cellPhone;
	}
	public void setCellPhone(String cellPhone) {
		this.cellPhone = cellPhone;
	}
	public Long getGroupId() {
		return groupId;
	}
	public void setGroupId(Long groupId) {
		this.groupId = groupId;
	}
	public String getTag() {
		return tag;
	}
	public void setTag(String tag) {
		this.tag = tag;
	}
	public String getContactDesc() {
		return contactDesc;
	}
	public void setContactDesc(String contactDesc) {
		this.contactDesc = contactDesc;
	}

	/*public void showGroupNames(Log log){
		if( groupNames != null && groupNames.size() > 0){
			log.debug(StringUtils.join(groupNames, ','));
		}else{
			log.debug("그룹명이 없습니다.");
		}
	}*/
	
	// 
	
}
