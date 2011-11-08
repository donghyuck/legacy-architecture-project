package architecture.ee.model;

import java.io.Serializable;
import java.util.Date;

/**
 * @author                 donghyuck
 */
public interface ModelObject<T> extends Comparable<T>, Serializable {

	/**
	 * Returns <code>true</code> if this model instance does not yet exist in the database.
	 * @return                 <code>true</code> if this model instance does not yet exist in  the database; <code>false</code> otherwise
	 * @uml.property  name="new"
	 */
	public boolean isNew();
	
	/**
	 * Sets whether this model instance does not yet exist in the database.
	 * @param n                 whether this model instance does not yet exist in the database
	 * @uml.property  name="new"
	 */
	public void setNew(boolean n);	
	
	/**
	 * @return
	 * @uml.property  name="primaryKeyObject"
	 */
	public Serializable getPrimaryKeyObject();	
	
	/**
	 * @param  primaryKeyObj
	 * @uml.property  name="primaryKeyObject"
	 */
	public void setPrimaryKeyObject(Serializable primaryKeyObj);	
	
	public int getObjectType();

	/**
	 * @return
	 * @uml.property  name="creationDate"
	 */
	public Date getCreationDate() ;
	
	/**
	 * @param  creationDate
	 * @uml.property  name="creationDate"
	 */
	public void setCreationDate(Date creationDate);
	
	/**
	 * @return
	 * @uml.property  name="modifiedDate"
	 */
	public Date getModifiedDate();
	
	/**
	 * @param  modifiedDate
	 * @uml.property  name="modifiedDate"
	 */
	public void setModifiedDate(Date modifiedDate);
	
}