package architecture.ee.model.impl;

import java.io.Serializable;

import architecture.ee.i18n.I18nText;
import architecture.ee.model.I18nTextModel;

/**
 * @author   donghyuck
 */
public class I18nTextModelImpl  extends BaseModelObject<I18nText>  implements I18nTextModel, I18nText {

	/**
	 * @uml.property  name="localizerId"
	 */
	private long localizerId = -1L ;
    /**
	 * @uml.property  name="textId"
	 */
    private long textId = -1L;    
    /**
	 * @uml.property  name="key"
	 */
    private String key;
    /**
	 * @uml.property  name="text"
	 */
    private String text; 
	

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
	 * @uml.property  name="localizerId"
	 */
	public long getLocalizerId() {
		return localizerId;
	}

	/**
	 * @param  localizerId
	 * @uml.property  name="localizerId"
	 */
	public void setLocalizerId(long localizerId) {
		this.localizerId = localizerId;
	}

	/**
	 * @return
	 * @uml.property  name="key"
	 */
	public String getKey() {
		return key;
	}

	/**
	 * @param  key
	 * @uml.property  name="key"
	 */
	public void setKey(String key) {
		this.key = key;
	}

	/**
	 * @return
	 * @uml.property  name="text"
	 */
	public String getText() {
		return text;
	}

	/**
	 * @param  text
	 * @uml.property  name="text"
	 */
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
