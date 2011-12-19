package architecture.ee.model.impl;

import java.util.Date;

import architecture.ee.model.ModelObject;

/**
 * @author  donghyuck
 */
public abstract class BaseModelObject<T> implements ModelObject<T> {

	/**
	 */
	private boolean isNew = false;
	/**
	 */
	private Date creationDate = null;
	/**
	 */
	private Date modifiedDate = null;	
	
	public BaseModelObject() {}
		
	/**
	 * @return
	 */
	public boolean isNew() {
		return isNew;
	}
	
	/**
	 * @param isNew
	 */
	public void setNew(boolean isNew) {
		this.isNew = isNew;
	}

	public abstract Object clone();
	
	/**
	 * @return
	 */
	public Date getCreationDate() {
		return creationDate;
	}
	/**
	 * @param creationDate
	 */
	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}
	/**
	 * @return
	 */
	public Date getModifiedDate() {
		return modifiedDate;
	}
	/**
	 * @param modifiedDate
	 */
	public void setModifiedDate(Date modifiedDate) {
		this.modifiedDate = modifiedDate;
	}
	
}
