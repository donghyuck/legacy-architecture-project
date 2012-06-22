package architecture.ee.model;

import architecture.ee.i18n.I18nText;

/**
 * @author                   donghyuck
 */
public interface I18nTextModel  extends ModelObject<I18nText> {

	/**
	 * @return
	 * @uml.property  name="textId"
	 */
	public abstract long getTextId();
	
	/**
	 * @param  textId
	 * @uml.property  name="textId"
	 */
	public abstract void setTextId(long textId);
	
	/**
	 * @return
	 * @uml.property  name="localizerId"
	 */
	public abstract long getLocalizerId();

	/**
	 * @param  localizerId
	 * @uml.property  name="localizerId"
	 */
	public abstract void setLocalizerId(long localizerId);	
	
	/**
	 * @return
	 * @uml.property  name="text"
	 */
	public abstract String getText();
	
	/**
	 * @param  text
	 * @uml.property  name="text"
	 */
	public abstract void setText(String text);
	
}
