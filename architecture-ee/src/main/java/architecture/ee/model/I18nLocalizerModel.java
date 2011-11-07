package architecture.ee.model;

import java.util.Map;
import java.util.ResourceBundle;

import architecture.ee.i18n.I18nLocale;
import architecture.ee.i18n.I18nLocalizer;

public interface I18nLocalizerModel extends ModelObject<I18nLocalizer> {

	public abstract long getLocalizerId();
	
	public abstract void setLocalizerId(long localizerId);
	
	public abstract String getDescription();
	
	public abstract void setDescription(String description);
	
	public abstract ResourceBundle getResourceBundle();
	
	public abstract void setResourceBundle(ResourceBundle resourceBundle);
	
	public abstract String getName();
	
	public abstract void setName(String name);
	
	public abstract I18nLocale getI18nLocale();
	
	public abstract void setI18nLocale(I18nLocale i18nLocale );
	
	public abstract void setI18nTexts(Map<String, String> texts);
	
	public abstract Map<String, String> getI18nTexts();
	
	public abstract String getString(String key);
		
}
