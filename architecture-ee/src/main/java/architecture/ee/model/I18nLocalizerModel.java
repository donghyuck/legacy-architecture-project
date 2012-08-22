package architecture.ee.model;

import java.util.Map;
import java.util.ResourceBundle;

import architecture.common.model.ModelObject;
import architecture.ee.i18n.I18nLocale;
import architecture.ee.i18n.I18nLocalizer;

/**
 * @author                   donghyuck
 */
public interface I18nLocalizerModel extends ModelObject<I18nLocalizer> {

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
	public abstract String getDescription();
	
	/**
	 * @param  description
	 */
	public abstract void setDescription(String description);
	
	/**
	 * @return
	 */
	public abstract ResourceBundle getResourceBundle();
	
	/**
	 * @param  resourceBundle
	 */
	public abstract void setResourceBundle(ResourceBundle resourceBundle);
	
	/**
	 * @return
	 */
	public abstract String getName();
	
	/**
	 * @param  name
	 */
	public abstract void setName(String name);
	
	/**
	 */
	public abstract I18nLocale getI18nLocale();
	
	/**
	 * @param  i18nLocale
	 */
	public abstract void setI18nLocale(I18nLocale i18nLocale );
	
	/**
	 * @param  texts
	 */
	public abstract void setI18nTexts(Map<String, String> texts);
	
	/**
	 * @return
	 */
	public abstract Map<String, String> getI18nTexts();
	
	public abstract String getString(String key);
		
}
