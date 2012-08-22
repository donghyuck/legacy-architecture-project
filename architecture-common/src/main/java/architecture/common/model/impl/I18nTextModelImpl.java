package architecture.common.model.impl;

import java.io.Serializable;

import architecture.common.cache.CacheSizes;
import architecture.common.i18n.I18nText;
import architecture.common.model.I18nTextModel;
import architecture.common.model.ModelObjectType;

public class I18nTextModelImpl extends BaseModelObject <I18nText> implements I18nTextModel {
	
	private long textId;
	private String localeCode;
	private String textKey;
	private String text;
	
	public Serializable getPrimaryKeyObject() {
		return textId;
	}

	public int getObjectType() {
		return ModelObjectType.Unknown.getTypeId();
	}
	
	public int getCachedSize() {		
	
		return CacheSizes.sizeOfLong() + CacheSizes.sizeOfInt() + CacheSizes.sizeOfString(localeCode) + CacheSizes.sizeOfString(textKey) + CacheSizes.sizeOfString(text) ;
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

	public String getTextKey() {
		return textKey;
	}

	public void setTextKey(String textKey) {
		this.textKey = textKey;
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
