package architecture.ee.i18n.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import architecture.ee.i18n.I18nLocale;
import architecture.ee.i18n.I18nLocalizer;

/**
 * @author  donghyuck
 */
public class I18nTextManagerImpl extends AbstractI18nTextManager {

	private List<I18nLocalizer> resourceBundles;	
	
	public void initialize(){
		reloadResourceBundes();
	}

	public List<I18nLocalizer> getI18nLocalizers(){
		return resourceBundles;
	}
	
    protected void reloadResourceBundes()
    {
    	List<I18nLocalizer> newResourceBundles = new ArrayList<I18nLocalizer>();    	
    	for( I18nLocalizer localizer : getAvailableI18nLocalizers() ){
    		newResourceBundles.add(localizer);
    	}
    	resourceBundles = newResourceBundles;
    }

    
	public List<I18nLocale> getAvailableI18nLocales() {
		return getI18nLocaleDao().getAvailableI18nLocales();
	}

	public int getAvailableI18nLocaleCount() {
		return getI18nLocaleDao().getAvailableI18nLocaleCount();
	}

	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
	public I18nLocale getI18nLocale(Locale locale, boolean createIfNotExist) {
		return getI18nLocaleDao().getI18nLocale(locale, createIfNotExist);
	}

	public I18nLocale getI18nLocaleById(long localeId) {
		return getI18nLocaleDao().getI18nLocaleById(localeId);
	}

	public int getAvailableI18nLocalizerCount() {
		return getI18nLocalizerDao().getAvailableI18nLocalizerCount();
	}

	public List<I18nLocalizer> getAvailableI18nLocalizers() {
		return getI18nLocalizerDao().getAvailableI18nLocalizers();
	}

	public I18nLocalizer getI18nLocalizerById(long localizerId) {
		return getI18nLocalizerDao().getI18nLocalizerById(localizerId);
	}

	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
	public void createI18nLocalizer(I18nLocalizer localizer) {
		getI18nLocalizerDao().addI18nLocalizer(localizer);
	}

	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
	public void updateI18nLocalizer(I18nLocalizer localizer) {
		getI18nLocalizerDao().updateI18nLocalizer(localizer);		
	}

	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
	public void deleteI18nLocalizer(I18nLocalizer localizer) {
		getI18nLocalizerDao().deleteI18nLocalizer(localizer);
	}

	public List<I18nLocalizer> getI18nLocalizersByName(String name) {
		return getI18nLocalizerDao().getI18nLocalizersByName(name);
	}
		
}
