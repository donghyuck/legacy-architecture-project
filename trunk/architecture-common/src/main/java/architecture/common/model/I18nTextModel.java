package architecture.common.model;

import architecture.common.i18n.I18nText;

public interface I18nTextModel extends ModelObject<I18nText> {
	
	/**
	 * @return textId
	 */
	public abstract long getTextId();
	
	/**
	 * @param  textId
	 */
	public abstract void setTextId(long textId);
	
	/**
	 * @return localeCode
	 */
	public abstract String getLocaleCode();
	
	/**
	 * @param  localeCode
	 */
	public abstract void setLocaleCode(String localeCode);
	
	/**
	 * @return textKey
	 */
	public abstract String getTextKey();
	
	/**
	 * @param  textKey
	 */
	public abstract void setTextKey(String textKey);
	
	/**
	 * 
	 * @return text
	 */
	public abstract String getText();
	
	/**
	 * 
	 * @param  text
	 */
	public abstract void setText(String text);

}
