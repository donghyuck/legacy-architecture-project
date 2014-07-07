package architecture.ee.web.contact;

import java.util.List;
import java.util.Set;

import architecture.common.model.NoNamedEntityModelObject;

public interface ContactGroup extends NoNamedEntityModelObject {
	public Long getGroupId() ;
	public void setGroupId(Long groupId) ;
	public String getGroupName() ;
	public void setGroupName(String groupName) ;
	public Long getCompanyId() ;
	public void setCompanyId(Long companyId) ;
	public int getTypeCode() ;
	public void setTypeCode(int typeCode);
	public Long getParentGroupId() ;
	public void setParentGroupId(Long parentGroupId) ;
	public Long getModifyId() ;
	public void setModifyId(Long modifyId);
	public boolean isDelYn();
	public void setDelYn(boolean delYn);
	public String getTypeName();
	public void setTypeName(String typeName);
	//public List<Long> getChildGroupIds();
	//public void setChildGroupIds(List<Long> childGroupIds);
	public boolean isHasChildren();
	public void setHasChildren(boolean hasChildren);
	public int getLev();
	public void setLev(int lev);
	public String getParentGroupName();
	public void setParentGroupName(String parentGroupName);
	public Set<Long> getChildGroupIds();
	public void setChildGroupIds(Set<Long> childGroupIds);
	public String getGroupIds();
	public void setGroupIds(String groupIds);
}
