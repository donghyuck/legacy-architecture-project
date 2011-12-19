package architecture.ee.web.model;

import java.io.Serializable;
import java.util.Date;

public interface ModelObject<T> extends Serializable {

	/**
	 * @return
	 */
	public Serializable getPrimaryKeyObject();	

	/**
	 * 
	 * @return
	 */
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
