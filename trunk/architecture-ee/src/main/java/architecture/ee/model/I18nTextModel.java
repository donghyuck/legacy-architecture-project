package architecture.ee.model;

import java.util.Date;

import architecture.ee.g11n.I18nText;

public interface I18nTextModel extends BaseModel<I18nText> {

	/**
	 * Gets the primary key of this account.
	 * 
	 * @return the primary key of this account
	 */
	public long getPrimaryKey();

	/**
	 * Sets the primary key of this account
	 * 
	 * @param pk
	 *            the primary key of this account
	 */
	public void setPrimaryKey(long pk);
	
	
	public long getTextId();
	
	public void setTextId(long textId);
	
	
	/**
	public int getObjectType();
	
	public void setObjectType(int objectType);
	
	public int getObjectAttribute();
	
	public void setObjectAttribute(int objectAttribute);
	
	**/
	
	public String getTextKey();
	
	public void setTextKey(String textKey);
	
	public String getLocaleCode();
	
	public void setLocaleCode(String localeCode);
	
	public String getText();
	
	public void setText(String text);
	
	public Date getCreationDate();
	
	public void setCreationDate(Date creationDate);
	
	public Date getModifiedDate();
	
	public void setModifiedDate(Date modifiedDate);
	
	//public abstract String getResourceBundleKey();
		
	public Object clone();

	public int compareTo(I18nText i18nText);

	public int hashCode();

	public String toString();

	public String toXmlString();
}
