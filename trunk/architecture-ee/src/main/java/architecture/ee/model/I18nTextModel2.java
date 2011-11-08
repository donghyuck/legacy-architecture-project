package architecture.ee.model;

import java.util.Date;

import architecture.ee.i18n.I18nText2;

/**
 * @author                 donghyuck
 */
public interface I18nTextModel2 extends ModelObject<I18nText2> {

	/**
	 * @return
	 * @uml.property  name="textId"
	 */
	public abstract long getTextId();
	
	/**
	 * @param  textId
	 * @uml.property  name="textId"
	 */
	public abstract void setTextId(long textId);
	
	/**
	 * @return
	 * @uml.property  name="objectType"
	 */
	public abstract int getObjectType();
	
	/**
	 * @param  objectType
	 * @uml.property  name="objectType"
	 */
	public abstract void setObjectType(int objectType);
	
	/**
	 * @return
	 * @uml.property  name="objectId"
	 */
	public abstract long getObjectId();

	/**
	 * @param  objectId
	 * @uml.property  name="objectId"
	 */
	public abstract void setObjectId(long objectId);
	
	/**
	 * @return
	 * @uml.property  name="objectAttribute"
	 */
	public abstract int getObjectAttribute();
	
	/**
	 * @param  objectAttribute
	 * @uml.property  name="objectAttribute"
	 */
	public abstract void setObjectAttribute(int objectAttribute);
		
	/**
	 * @return
	 * @uml.property  name="textKey"
	 */
	public abstract String getTextKey();
	
	/**
	 * @param  textKey
	 * @uml.property  name="textKey"
	 */
	public abstract void setTextKey(String textKey);
	
	/**
	 * @return
	 * @uml.property  name="localeCode"
	 */
	public abstract String getLocaleCode();
	
	/**
	 * @param  localeCode
	 * @uml.property  name="localeCode"
	 */
	public abstract void setLocaleCode(String localeCode);
	
	/**
	 * @return
	 * @uml.property  name="text"
	 */
	public abstract String getText();
	
	/**
	 * @param  text
	 * @uml.property  name="text"
	 */
	public abstract void setText(String text);
	
	/**
	 * @return
	 * @uml.property  name="creationDate"
	 */
	public abstract Date getCreationDate();
	
	/**
	 * @param  creationDate
	 * @uml.property  name="creationDate"
	 */
	public abstract void setCreationDate(Date creationDate);
	
	/**
	 * @return
	 * @uml.property  name="modifiedDate"
	 */
	public abstract Date getModifiedDate();
	
	/**
	 * @param  modifiedDate
	 * @uml.property  name="modifiedDate"
	 */
	public abstract void setModifiedDate(Date modifiedDate);
}
