package architecture.ee.model;

import architecture.ee.i18n.I18nText;

/**
 * @author                 donghyuck
 */
public interface I18nTextModel  extends ModelObject<I18nText> {

	/**
	 * @return
	 */
	public abstract long getTextId();
	
	/**
	 * @param  textId
	 */
	public abstract void setTextId(long textId);
	
	/**
	 * @return
	 */
	public abstract long getLocalizerId();

	/**
	 * @param  localizerId
	 */
	public abstract void setLocalizerId(long localizerId);	
	
	/**
	 * @return
	 */
	public abstract String getText();
	
	/**
	 * @param  text
	 */
	public abstract void setText(String text);
	
}
