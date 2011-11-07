package architecture.ee.model;

import java.io.Serializable;
import java.util.Date;

public interface ModelObject<T> extends Comparable<T>, Serializable {

	/**
	 * Returns <code>true</code> if this model instance does not yet exist in
	 * the database.
	 *
	 * @return <code>true</code> if this model instance does not yet exist in
	 *         the database; <code>false</code> otherwise
	 */
	public boolean isNew();
	
	/**
	 * Sets whether this model instance does not yet exist in the database.
	 *
	 * @param n whether this model instance does not yet exist in the database
	 */
	public void setNew(boolean n);	
	
	public Serializable getPrimaryKeyObject();	
	
	public void setPrimaryKeyObject(Serializable primaryKeyObj);	
	
	public int getObjectType();

	public Date getCreationDate() ;
	
	public void setCreationDate(Date creationDate);
	
	public Date getModifiedDate();
	
	public void setModifiedDate(Date modifiedDate);
	
}