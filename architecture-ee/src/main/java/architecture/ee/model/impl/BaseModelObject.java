package architecture.ee.model.impl;

import java.io.Serializable;
import java.util.Date;

import architecture.ee.model.ModelObject;

/**
 * @author  donghyuck
 */
public abstract class BaseModelObject<T> implements ModelObject<T> {

	/**
	 */
	private Date creationDate = null;
	/**
	 */
	private Date modifiedDate = null;	
	
	public boolean isNew(){		
		Object object = getPrimaryKeyObject();		
		if(object == null)
		{
			return true;
		}
		if( object instanceof Long && ((Long)object) == -1L ){
			return true;
		}		
		return false;		
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

	public abstract Serializable getPrimaryKeyObject();
}
