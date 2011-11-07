package architecture.ee.model.impl;

import java.util.Date;

import architecture.ee.model.ModelObject;

public abstract class BaseModelObject<T> implements ModelObject<T> {

	private boolean isNew = false;
	private Date creationDate = null;
	private Date modifiedDate = null;	
	
	public BaseModelObject() {}
		
	public boolean isNew() {
		return isNew;
	}
	
	public void setNew(boolean isNew) {
		this.isNew = isNew;
	}

	public abstract Object clone();
	
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
