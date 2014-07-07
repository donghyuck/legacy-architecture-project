package architecture.ee.web.contact.impl;

import java.io.Serializable;

import architecture.common.model.factory.ModelTypeFactory;
import architecture.common.model.support.BaseModelObjectSupport;
import architecture.ee.web.contact.Tag;

public class TagImpl extends BaseModelObjectSupport implements Tag{

	private Long tagId;
	private String tagName;
	private Long modifyId;
	private Long companyId;
	private Long userId;
	
	
	
	
	public Long getUserId() {
		return userId;
	}



	public void setUserId(Long userId) {
		this.userId = userId;
	}



	public Long getTagId() {
		return tagId;
	}



	public void setTagId(Long tagId) {
		this.tagId = tagId;
	}



	public String getTagName() {
		return tagName;
	}



	public void setTagName(String tagName) {
		this.tagName = tagName;
	}



	public Long getModifyId() {
		return modifyId;
	}



	public void setModifyId(Long modifyId) {
		this.modifyId = modifyId;
	}



	public Long getCompanyId() {
		return companyId;
	}



	public void setCompanyId(Long companyId) {
		this.companyId = companyId;
	}

	public Serializable getPrimaryKeyObject() {
		return tagId;
	}

	public int getModelObjectType() {
		return ModelTypeFactory.getTypeIdFromCode("TAG");
	}
	public int getCachedSize() {
		return 0;
	}
}
