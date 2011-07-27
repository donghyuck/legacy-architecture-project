package architecture.ee.component;

import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

import architecture.ee.g11n.Country;
import architecture.ee.g11n.I18nLocale;
import architecture.ee.g11n.I18nText;

public interface GlobalizationService {

	public abstract ResourceBundle getResourceBundle(String bundleName, Locale locale);
	
	public abstract List<Country> getCountries();
	
	public abstract List<Country> getCountries(boolean enabled);
	
	public abstract Country getCountryByA2(String code); 
	
	public abstract Country getCountryByA3(String code); 

	public abstract List<I18nText> getTexts();
	
	public abstract List<I18nText> getTexts(int objectType, String localeCode);
	
	public abstract List<I18nText> getTexts(int objectType, long objectId);
	
	public abstract I18nText getText(long textID);
	
	public abstract void deleteTexts(List<I18nText> texts);
	
	public abstract void saveTexts(List<I18nText> texts);
	
	public abstract void saveTexts(List<I18nText> textList, int objectType, long objectID);
	
	public abstract List<I18nLocale> getAvailableLocales();	
	
    public abstract I18nLocale getLocaleById(long localeId);
    
    public abstract I18nLocale getLocale(Locale locale, boolean createIfNotExist);
    
    public abstract I18nLocale getLocale(Locale locale);
    
    public abstract void deleteLocale(I18nLocale locale);
    
    public abstract I18nLocale updateLocale(I18nLocale locale); 
    
}