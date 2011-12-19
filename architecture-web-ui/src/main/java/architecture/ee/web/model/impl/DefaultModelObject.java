package architecture.ee.web.model.impl;

import java.io.Serializable;
import java.util.Date;
import architecture.ee.web.model.ModelObject;

public abstract class DefaultModelObject implements ModelObject {
	
	private Date creationDate = null;

	private Date modifiedDate = null;	

	private int objectType = 0 ;
	
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

	public abstract Serializable getPrimaryKeyObject();
	
	public int getObjectType() {
		return objectType;
	}

	protected void setObjectType(int objectType) {
		this.objectType = objectType;
	}
	
}
