package architecture.ee.component;

import java.util.List;
import java.util.Locale;

import architecture.ee.g11n.Country;
import architecture.ee.g11n.I18nLocale;
import architecture.ee.g11n.I18nText;

public interface GlobalizationService {

	public List<Country> getCountries();
	
	public List<Country> getCountries(boolean enabled);
	
	public Country getCountryByA2(String code); 
	
	public Country getCountryByA3(String code); 
	
	
	
	
/*	public List<I18nText> getTexts(int objectType, String localeCode);
	
	public List<I18nText> getTexts(int objectType, long objectID);
	
	public List<I18nText> getTexts(int objectType);*/
	
	public List<I18nText> getTexts();
	
	public I18nText getText(long textID);
	
	public void deleteTexts(List<I18nText> texts);
	
	public void saveTexts(List<I18nText> texts);
	
	public void saveTexts(List<I18nText> textList, int objectType, long objectID);
	
	
	public abstract List<I18nLocale> getAvailableLocales();	
	
    public abstract I18nLocale getLocaleById(long localeId);
    
    public abstract I18nLocale getLocale(Locale locale, boolean createIfNotExist);
    
    public abstract I18nLocale getLocale(Locale locale);
    
    public abstract void deleteLocale(I18nLocale locale);
    
    public abstract I18nLocale updateLocale(I18nLocale locale); 
    
}