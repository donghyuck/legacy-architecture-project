package architecture.common.model.impl;

import java.io.Serializable;

import architecture.common.cache.CacheSizes;
import architecture.common.i18n.I18nText;
import architecture.common.model.I18nTextModel;
import architecture.common.util.I18nTextUtils;

public class I18nTextModelImpl extends BaseModelObject <I18nText> implements I18nTextModel {
	
	private long textId = -1L;
	
	private int objectType ;
	
	private long objectId ;
	
	private int objectAttribute ;
	
	private String localeCode;
		
	private String text;
	
	public String getResourceBundleKey(){
		return I18nTextUtils.generateResourceBundleKey(objectType, objectId, objectAttribute);
	}
	
	public Serializable getPrimaryKeyObject() {
		return textId;
	}
	
	public long getObjectId() {
		return objectId;
	}

	public void setObjectId(long objectId) {
		this.objectId = objectId;
	}

	public int getObjectType() {
		return objectType;
	}

	public int getObjectAttribute() {
		return objectAttribute;
	}

	public void setObjectAttribute(int objectAttribute) {
		this.objectAttribute = objectAttribute;
	}

	public void setObjectType(int objectType) {
		this.objectType = objectType;
	}

	public int getCachedSize() {
		int size = 0 ;
		size += CacheSizes.sizeOfLong();
		size += CacheSizes.sizeOfInt();
		size += CacheSizes.sizeOfString(localeCode);
		size += CacheSizes.sizeOfString(text) ;
		return size;
	}

	public long getTextId() {
		return textId;
	}

	public void setTextId(long textId) {
		this.textId = textId;
	}

	public String getLocaleCode() {
		return localeCode;
	}

	public void setLocaleCode(String localeCode) {
		this.localeCode = localeCode;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public int compareTo(I18nText o) {
		return 0;
	}
	
	
}
