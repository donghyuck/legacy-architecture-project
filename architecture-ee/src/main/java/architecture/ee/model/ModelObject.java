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
	 */
	public boolean isNew();
	
	/**
	 * @return
	 */
	public Serializable getPrimaryKeyObject();	
	
	public int getObjectType();

	/**
	 * @return
	 */
	public Date getCreationDate() ;
	
	/**
	 * @param  creationDate
	 */
	public void setCreationDate(Date creationDate);
	
	/**
	 * @return
	 */
	public Date getModifiedDate();
	
	/**
	 * @param  modifiedDate
	 */
	public void setModifiedDate(Date modifiedDate);
	
}