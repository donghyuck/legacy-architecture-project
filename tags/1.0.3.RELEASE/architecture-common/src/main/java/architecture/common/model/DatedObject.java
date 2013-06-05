package architecture.common.model;

import java.util.Date;

public interface DatedObject {

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
