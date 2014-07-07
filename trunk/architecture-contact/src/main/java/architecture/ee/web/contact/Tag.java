package architecture.ee.web.contact;

import java.io.Serializable;

import architecture.common.model.factory.ModelTypeFactory;

public interface Tag {

	public Long getTagId();

	public void setTagId(Long tagId);


	public String getTagName();


	public void setTagName(String tagName);


	public Long getModifyId();



	public void setModifyId(Long modifyId);



	public Long getCompanyId();



	public void setCompanyId(Long companyId);

	public Serializable getPrimaryKeyObject();

	public int getModelObjectType();
	public int getCachedSize();
	
	public Long getUserId();



	public void setUserId(Long userId);

}
