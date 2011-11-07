package architecture.ee.model.impl;

import java.io.Serializable;
import java.util.Date;

import architecture.ee.i18n.I18nText2;
import architecture.ee.model.I18nTextModel2;
import architecture.ee.util.I18nTextUtils;

public class I18nTextModel2Impl extends BaseModelObject<I18nText2> implements I18nTextModel2 {

	private String localeCode;
    private int objectAttribute = -1;
    private int objectType = -1;    
    private long objectId;
    private long textId = -1L;    
    private String textKey;
    private String textValue; 
    private Date creationDate;
    private Date modifiedDate;    

    public I18nTextModel2Impl() {
    	this.textId = -1L;
	}
    
	public long getObjectId() {
		return objectId;
	}

	public void setObjectId(long objectId) {
		this.objectId = objectId;
	}

	public int getObjectAttribute() {
		return objectAttribute;
	}

	public void setObjectAttribute(int objectAttribute) {
		this.objectAttribute = objectAttribute;
	}

	public int getObjectType() {
		return objectType;
	}

	public void setObjectType(int objectType) {
		this.objectType = objectType;
	}
	
	public String getLocaleCode() {
		return localeCode;
	}
	
	public void setLocaleCode(String localeCode) {
		this.localeCode = localeCode;
	}
	
	public long getTextId() {
		return textId;
	}
	
	public void setTextId(long textId) {
		this.textId = textId;
	}
	
	public String getTextKey() {
		return textKey;
	}
	
	public void setTextKey(String textKey) {
		this.textKey = textKey;
	}
	
	public String getText() {
		return textValue;
	}
	
	public void setText(String textValue) {
		this.textValue = textValue;
	}
	
	public Date getCreationDate() {
		return creationDate;
	}
	
	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}
	
	public Date getModifiedDate() {
		return modifiedDate;
	}
	
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
	
	public Object clone() {
		I18nTextModel2Impl impl = new I18nTextModel2Impl();
		impl.setTextId(textId);
		impl.setObjectId(objectId);
		impl.setObjectType(objectType);
		impl.setObjectAttribute(objectAttribute);
		impl.setLocaleCode(getLocaleCode());
		impl.setTextKey(getTextKey());
		impl.setText(getText());
		impl.setCreationDate(getCreationDate());
		impl.setModifiedDate(getModifiedDate());
		return impl;
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
	
}