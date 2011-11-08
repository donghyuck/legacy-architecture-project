package architecture.ee.model.impl;

import java.util.Date;

import architecture.ee.model.ModelObject;

/**
 * @author  donghyuck
 */
public abstract class BaseModelObject<T> implements ModelObject<T> {

	/**
	 * @uml.property  name="isNew"
	 */
	private boolean isNew = false;
	/**
	 * @uml.property  name="creationDate"
	 */
	private Date creationDate = null;
	/**
	 * @uml.property  name="modifiedDate"
	 */
	private Date modifiedDate = null;	
	
	public BaseModelObject() {}
		
	/**
	 * @return
	 * @uml.property  name="isNew"
	 */
	public boolean isNew() {
		return isNew;
	}
	
	/**
	 * @param isNew
	 * @uml.property  name="isNew"
	 */
	public void setNew(boolean isNew) {
		this.isNew = isNew;
	}

	public abstract Object clone();
	
	/**
	 * @return
	 * @uml.property  name="creationDate"
	 */
	public Date getCreationDate() {
		return creationDate;
	}
	/**
	 * @param creationDate
	 * @uml.property  name="creationDate"
	 */
	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}
	/**
	 * @return
	 * @uml.property  name="modifiedDate"
	 */
	public Date getModifiedDate() {
		return modifiedDate;
	}
	/**
	 * @param modifiedDate
	 * @uml.property  name="modifiedDate"
	 */
	public void setModifiedDate(Date modifiedDate) {
		this.modifiedDate = modifiedDate;
	}
	
}
