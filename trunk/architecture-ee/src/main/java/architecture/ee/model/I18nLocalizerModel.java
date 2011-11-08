package architecture.ee.model;

import java.util.Map;
import java.util.ResourceBundle;

import architecture.ee.i18n.I18nLocale;
import architecture.ee.i18n.I18nLocalizer;

/**
 * @author                 donghyuck
 */
public interface I18nLocalizerModel extends ModelObject<I18nLocalizer> {

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
	 * @uml.property  name="description"
	 */
	public abstract String getDescription();
	
	/**
	 * @param  description
	 * @uml.property  name="description"
	 */
	public abstract void setDescription(String description);
	
	/**
	 * @return
	 * @uml.property  name="resourceBundle"
	 */
	public abstract ResourceBundle getResourceBundle();
	
	/**
	 * @param  resourceBundle
	 * @uml.property  name="resourceBundle"
	 */
	public abstract void setResourceBundle(ResourceBundle resourceBundle);
	
	/**
	 * @return
	 * @uml.property  name="name"
	 */
	public abstract String getName();
	
	/**
	 * @param  name
	 * @uml.property  name="name"
	 */
	public abstract void setName(String name);
	
	/**
	 * @uml.property  name="i18nLocale"
	 * @uml.associationEnd  
	 */
	public abstract I18nLocale getI18nLocale();
	
	/**
	 * @param  i18nLocale
	 * @uml.property  name="i18nLocale"
	 */
	public abstract void setI18nLocale(I18nLocale i18nLocale );
	
	/**
	 * @param  texts
	 * @uml.property  name="i18nTexts"
	 */
	public abstract void setI18nTexts(Map<String, String> texts);
	
	/**
	 * @return
	 * @uml.property  name="i18nTexts"
	 */
	public abstract Map<String, String> getI18nTexts();
	
	public abstract String getString(String key);
		
}
