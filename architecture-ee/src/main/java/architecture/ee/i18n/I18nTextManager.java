package architecture.ee.i18n;

import java.util.List;
import java.util.Locale;

public interface I18nTextManager {

	public List<I18nLocale> getAvailableI18nLocales() ;	
	
	public int getAvailableI18nLocaleCount();
	
	public I18nLocale getI18nLocale(Locale locale, boolean createIfNotExist);

	public I18nLocale getI18nLocaleById(long localeId);	
	
	public List<I18nLocalizer> getI18nLocalizers();
		
	public int getAvailableI18nLocalizerCount();
	
	public List<I18nLocalizer> getAvailableI18nLocalizers();
	
	public I18nLocalizer getI18nLocalizerById(long localizerId);
	
	public List<I18nLocalizer> getI18nLocalizersByName(String name);
	
	public void createI18nLocalizer (I18nLocalizer localizer);
	
	public void updateI18nLocalizer (I18nLocalizer localizer);
	
	public void deleteI18nLocalizer (I18nLocalizer localizer);
	
	
}
