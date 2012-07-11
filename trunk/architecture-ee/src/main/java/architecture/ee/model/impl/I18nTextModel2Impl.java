package architecture.ee.model.impl;

import java.io.Serializable;
import java.util.Date;

import architecture.common.model.impl.BaseModelObject;
import architecture.ee.i18n.I18nText2;
import architecture.ee.model.I18nTextModel2;
import architecture.ee.util.I18nTextUtils;

/**
 * @author   donghyuck
 */
public class I18nTextModel2Impl extends BaseModelObject<I18nText2> implements I18nTextModel2 {

	/**
	 * @uml.property  name="localeCode"
	 */
	private String localeCode;
    /**
	 * @uml.property  name="objectAttribute"
	 */
    private int objectAttribute = -1;
    /**
	 * @uml.property  name="objectType"
	 */
    private int objectType = -1;    
    /**
	 * @uml.property  name="objectId"
	 */
    private long objectId;
    /**
	 * @uml.property  name="textId"
	 */
    private long textId = -1L;    
    /**
	 * @uml.property  name="textKey"
	 */
    private String textKey;
    private String textValue; 
    /**
	 * @uml.property  name="creationDate"
	 */
    private Date creationDate;
    /**
	 * @uml.property  name="modifiedDate"
	 */
    private Date modifiedDate;    

    public I18nTextModel2Impl() {
    	this.textId = -1L;
	}
    
	/**
	 * @return
	 * @uml.property  name="objectId"
	 */
	public long getObjectId() {
		return objectId;
	}

	/**
	 * @param  objectId
	 * @uml.property  name="objectId"
	 */
	public void setObjectId(long objectId) {
		this.objectId = objectId;
	}

	/**
	 * @return
	 * @uml.property  name="objectAttribute"
	 */
	public int getObjectAttribute() {
		return objectAttribute;
	}

	/**
	 * @param  objectAttribute
	 * @uml.property  name="objectAttribute"
	 */
	public void setObjectAttribute(int objectAttribute) {
		this.objectAttribute = objectAttribute;
	}

	/**
	 * @return
	 * @uml.property  name="objectType"
	 */
	public int getObjectType() {
		return objectType;
	}

	/**
	 * @param  objectType
	 * @uml.property  name="objectType"
	 */
	public void setObjectType(int objectType) {
		this.objectType = objectType;
	}
	
	/**
	 * @return
	 * @uml.property  name="localeCode"
	 */
	public String getLocaleCode() {
		return localeCode;
	}
	
	/**
	 * @param  localeCode
	 * @uml.property  name="localeCode"
	 */
	public void setLocaleCode(String localeCode) {
		this.localeCode = localeCode;
	}
	
	/**
	 * @return
	 * @uml.property  name="textId"
	 */
	public long getTextId() {
		return textId;
	}
	
	/**
	 * @param  textId
	 * @uml.property  name="textId"
	 */
	public void setTextId(long textId) {
		this.textId = textId;
	}
	
	/**
	 * @return
	 * @uml.property  name="textKey"
	 */
	public String getTextKey() {
		return textKey;
	}
	
	/**
	 * @param  textKey
	 * @uml.property  name="textKey"
	 */
	public void setTextKey(String textKey) {
		this.textKey = textKey;
	}
	
	public String getText() {
		return textValue;
	}
	
	public void setText(String textValue) {
		this.textValue = textValue;
	}
	
	/**
	 * @return
	 * @uml.property  name="creationDate"
	 */
	public Date getCreationDate() {
		return creationDate;
	}
	
	/**
	 * @param  creationDate
	 * @uml.property  name="creationDate"
	 */
	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}
	
	/**
	 * @return
	 * @uml.property  name="modifiedDate"
	 */
	public Date getModifiedDate() {
		return modifiedDate;
	}
	
	/**
	 * @param  modifiedDate
	 * @uml.property  name="modifiedDate"
	 */
	public void setModifiedDate(Date modifiedDate) {
		this.modifiedDate = modifiedDate;
	}
	
	public int compareTo(I18nText2 o) {
		long pk = o.getTextId();

		if (getTextId() < pk) {
			return -1;
		}
		else if (getTextId() > pk) {
			return 1;
		}
		else {
			return 0;
		}
	}
	
	public String getResourceBundleKey() {
		return I18nTextUtils.generateResourceBundleKey(objectType, objectId, objectAttribute);
	}

	public Serializable getPrimaryKeyObject() {
		return getTextId();
	}

	public void setPrimaryKeyObject(Serializable primaryKeyObj) {
		this.setTextId(((Long)primaryKeyObj).longValue());
	}
	public int getCachedSize() {
		return 0;
	}
}