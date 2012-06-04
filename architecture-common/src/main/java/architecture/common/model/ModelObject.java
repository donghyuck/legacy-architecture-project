package architecture.common.model;

import java.io.Serializable;
import java.util.Date;

import architecture.common.cache.Cacheable;

public interface ModelObject<T> extends Cacheable, Comparable<T> {
	
	/**
	 * @return
	 */
	public Serializable getPrimaryKeyObject();	
	
	/**
	 * 
	 * @return 객체 유형을 리턴한다.
	 */
	public int getObjectType();

	/**
	 * @return 생성일을 리턴한다.
	 */
	public Date getCreationDate() ;
	
	/**
	 * 
	 * @param  creationDate 생성일
	 */
	public void setCreationDate(Date creationDate);
	
	/**
	 * @return 수정일을 리턴한다.
	 */
	public Date getModifiedDate();
	
	/**
	 * 
	 * @param  modifiedDate 수정일
	 */
	public void setModifiedDate(Date modifiedDate);
	
}
