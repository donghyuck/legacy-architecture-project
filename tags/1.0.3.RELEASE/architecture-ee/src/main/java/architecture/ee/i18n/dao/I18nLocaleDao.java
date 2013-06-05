package architecture.ee.i18n.dao;

import java.util.List;
import java.util.Locale;

import architecture.ee.i18n.I18nLocale;

public interface I18nLocaleDao {
	
	public abstract int getAvailableI18nLocaleCount();
       
	public abstract List<I18nLocale> getAvailableI18nLocales();	
	
    public abstract I18nLocale getI18nLocaleById(long localeId);
    
    public abstract I18nLocale getI18nLocale(Locale locale, boolean createIfNotExist);
    
    public abstract I18nLocale getI18nLocale(Locale locale);
    
    public abstract I18nLocale addI18nLocale(Locale locale);    
    
    public abstract void deleteI18nLocale(I18nLocale locale);
    
    public abstract I18nLocale updateI18nLocale(I18nLocale locale);   
    
}
