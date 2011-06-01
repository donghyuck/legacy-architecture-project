package architecture.ee.g11n.dao;

import java.util.List;
import java.util.Locale;

import architecture.ee.g11n.I18nLocale;

public interface I18nLocaleDao {
	
	public abstract int getAvailableLocaleCount();
       
	public abstract List<I18nLocale> getAvailableLocales();	
	
    public abstract I18nLocale getLocaleById(long localeId);
    
    public abstract I18nLocale getLocale(Locale locale, boolean createIfNotExist);
    
    public abstract I18nLocale getLocale(Locale locale);
    
    public abstract I18nLocale addLocale(Locale locale);    
    
    public abstract void deleteLocale(I18nLocale locale);
    
    public abstract I18nLocale updateLocale(I18nLocale locale);   
}
