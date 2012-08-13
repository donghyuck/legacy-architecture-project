package architecture.common.model.impl;

import java.util.Date;

import architecture.common.model.ModelObject;

public abstract class BaseModelObject<T> implements ModelObject<T> {

	private Date creationDate = null;
	
	private Date modifiedDate = null;

	public BaseModelObject() {
	}
		
	public Date getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	public Date getModifiedDate() {
		return modifiedDate;
	}

	public void setModifiedDate(Date modifiedDate) {
		this.modifiedDate = modifiedDate;
	}	

}
