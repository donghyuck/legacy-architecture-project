package architecture.ee.model.impl;

import java.io.Serializable;

import architecture.ee.i18n.I18nText;
import architecture.ee.model.I18nTextModel;

public class I18nTextModelImpl  extends BaseModelObject<I18nText>  implements I18nTextModel, I18nText {

	private long localizerId = -1L ;
    private long textId = -1L;    
    private String key;
    private String text; 
	

	public long getTextId() {
		return textId;
	}
	
	public void setTextId(long textId) {
		this.textId = textId;
	}

	public long getLocalizerId() {
		return localizerId;
	}

	public void setLocalizerId(long localizerId) {
		this.localizerId = localizerId;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}


	public Serializable getPrimaryKeyObject() {
		return getTextId();
	}

	public void setPrimaryKeyObject(Serializable primaryKeyObj) {
		this.setTextId(((Long)primaryKeyObj).longValue());
	}

	public int getObjectType() {
		return 13;
	}

	
	public int compareTo(I18nText o) {
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
	
	@Override
	public Object clone() {
		return null;
	}
		
}
