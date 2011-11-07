package architecture.ee.model;

import java.util.Date;

import architecture.ee.i18n.I18nText2;

public interface I18nTextModel2 extends ModelObject<I18nText2> {

	public abstract long getTextId();
	
	public abstract void setTextId(long textId);
	
	public abstract int getObjectType();
	
	public abstract void setObjectType(int objectType);
	
	public abstract long getObjectId();

	public abstract void setObjectId(long objectId);
	
	public abstract int getObjectAttribute();
	
	public abstract void setObjectAttribute(int objectAttribute);
		
	public abstract String getTextKey();
	
	public abstract void setTextKey(String textKey);
	
	public abstract String getLocaleCode();
	
	public abstract void setLocaleCode(String localeCode);
	
	public abstract String getText();
	
	public abstract void setText(String text);
	
	public abstract Date getCreationDate();
	
	public abstract void setCreationDate(Date creationDate);
	
	public abstract Date getModifiedDate();
	
	public abstract void setModifiedDate(Date modifiedDate);
}
