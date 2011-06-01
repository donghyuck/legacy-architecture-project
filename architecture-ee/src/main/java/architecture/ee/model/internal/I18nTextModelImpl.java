package architecture.ee.model.internal;

import java.io.Serializable;
import java.util.Date;

import architecture.ee.g11n.I18nText;
import architecture.ee.g11n.internal.I18nTextImpl;
import architecture.ee.model.I18nTextModel;

public class I18nTextModelImpl extends BaseModelImpl<I18nText> implements I18nTextModel {

    private String localeCode;
    private long textId = -1L;    
    private String textKey;
    private String textValue; 
    private Date creationDate;
    private Date modifiedDate;
    

	public long getPrimaryKey(){
		return getTextId();
	}

	public void setPrimaryKey(long pk){
		setTextId(pk);
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
	

	public Serializable getPrimaryKeyObj() {
		return new Long(textId);
	}

	public int compareTo(I18nText o) {
		long pk = o.getPrimaryKey();

		if (getPrimaryKey() < pk) {
			return -1;
		}
		else if (getPrimaryKey() > pk) {
			return 1;
		}
		else {
			return 0;
		}
	}
	
	public Object clone() {
		I18nTextImpl impl = new I18nTextImpl();
		impl.setTextId(textId);
		impl.setLocaleCode(getLocaleCode());
		impl.setTextKey(getTextKey());
		impl.setText(getText());
		impl.setCreationDate(getCreationDate());
		impl.setModifiedDate(getModifiedDate());
		return impl;
	}
	
	public String toXmlString() {
		return null;
	}
}
