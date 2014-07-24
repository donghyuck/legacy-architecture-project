package architecture.ee.web.contact;

import java.util.List;

import org.apache.commons.logging.Log;

import architecture.common.model.NoNamedEntityModelObject;
import architecture.common.user.User;

public interface Contact extends NoNamedEntityModelObject{
	
	public Long getContactId() ;
	public void setContactId(Long contactId) ;
	public String getName() ;
	public void setName(String name) ;
	public String getEmail() ;
	public void setEmail(String email) ;
	public String getPhone() ;
	public void setPhone(String phone) ;
	public String getCellPhone() ;
	public void setCellPhone(String cellPhone) ;
	public Long getGroupId() ;
	public void setGroupId(Long groupId) ;
	public String getTag() ;
	public void setTag(String tag) ;
	public String getContactDesc() ;
	public void setContactDesc(String contactDesc) ;
	//public List<String> getGroupNames() ;
	//public void setGroupNames(List<String> groupNames) ;
	public Long getUserId();
	public void setUserId(Long userId);
	public Long getModifyId() ;
	public void setModifyId(Long modifyId);
	public boolean isDelYn();
	public void setDelYn(boolean delYn);
	//public void showGroupNames(Log log);
	public String getTypeName();
	public void setTypeName(String typeName);
	public int getTypeCode();
	public void setTypeCode(int typeCode);
	public Long getCompanyId();
	public void setCompanyId(Long companyId);
	//public List<Tag> getTagList();
	//public void setTagList(List<Tag> tagList);
	//public List<ContactGroup> getGroupList();
	//public void setGroupList(List<ContactGroup> groupList);
	public String getGroupIds();
	public void setGroupIds(String groupIds);
	
	//public ContactGroup getContactGroup();
	//public void setContactGroup(ContactGroup contactGroup);
	public String[] getGroupNames();
	public void setGroupNames(String[] groupNames);
}
