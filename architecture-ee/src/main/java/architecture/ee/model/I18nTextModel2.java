package architecture.ee.model;

import java.util.Date;

import architecture.ee.i18n.I18nText2;

/**
 * @author                 donghyuck
 */
public interface I18nTextModel2 extends ModelObject<I18nText2> {

	/**
	 * @return
	 */
	public abstract long getTextId();
	
	/**
	 * @param  textId
	 */
	public abstract void setTextId(long textId);
	
	/**
	 * @return
	 */
	public abstract int getObjectType();
	
	/**
	 * @param  objectType
	 */
	public abstract void setObjectType(int objectType);
	
	/**
	 * @return
	 */
	public abstract long getObjectId();

	/**
	 * @param  objectId
	 */
	public abstract void setObjectId(long objectId);
	
	/**
	 * @return
	 */
	public abstract int getObjectAttribute();
	
	/**
	 * @param  objectAttribute
	 */
	public abstract void setObjectAttribute(int objectAttribute);
		
	/**
	 * @return
	 */
	public abstract String getTextKey();
	
	/**
	 * @param  textKey
	 */
	public abstract void setTextKey(String textKey);
	
	/**
	 * @return
	 */
	public abstract String getLocaleCode();
	
	/**
	 * @param  localeCode
	 */
	public abstract void setLocaleCode(String localeCode);
	
	/**
	 * @return
	 */
	public abstract String getText();
	
	/**
	 * @param  text
	 */
	public abstract void setText(String text);
	
	/**
	 * @return
	 */
	public abstract Date getCreationDate();
	
	/**
	 * @param  creationDate
	 */
	public abstract void setCreationDate(Date creationDate);
	
	/**
	 * @return
	 */
	public abstract Date getModifiedDate();
	
	/**
	 * @param  modifiedDate
	 */
	public abstract void setModifiedDate(Date modifiedDate);
}
