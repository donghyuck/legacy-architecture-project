package architecture.ee.i18n.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import architecture.ee.i18n.I18nLocale;
import architecture.ee.i18n.I18nLocalizer;
import architecture.ee.i18n.I18nTextManager;
import architecture.ee.i18n.dao.I18nLocaleDao;
import architecture.ee.i18n.dao.I18nLocalizerDao;

public class I18nTextManagerImpl implements I18nTextManager {

	private List<I18nLocalizer> resourceBundles;
	
	private I18nLocaleDao i18nLocaleDao;

	private I18nLocalizerDao i18nLocalizerDao;

	public I18nLocaleDao getI18nLocaleDao() {
		return i18nLocaleDao;
	}

	public void setI18nLocaleDao(I18nLocaleDao i18nLocaleDao) {
		this.i18nLocaleDao = i18nLocaleDao;
	}

	public I18nLocalizerDao getI18nLocalizerDao() {
		return i18nLocalizerDao;
	}

	public void setI18nLocalizerDao(I18nLocalizerDao i18nLocalizerDao) {
		this.i18nLocalizerDao = i18nLocalizerDao;
	}
	
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
		return i18nLocaleDao.getAvailableI18nLocales();
	}

	public int getAvailableI18nLocaleCount() {
		return i18nLocaleDao.getAvailableI18nLocaleCount();
	}

	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
	public I18nLocale getI18nLocale(Locale locale, boolean createIfNotExist) {
		return i18nLocaleDao.getI18nLocale(locale, createIfNotExist);
	}

	public I18nLocale getI18nLocaleById(long localeId) {
		return i18nLocaleDao.getI18nLocaleById(localeId);
	}

	public int getAvailableI18nLocalizerCount() {
		return i18nLocalizerDao.getAvailableI18nLocalizerCount();
	}

	public List<I18nLocalizer> getAvailableI18nLocalizers() {
		return i18nLocalizerDao.getAvailableI18nLocalizers();
	}

	public I18nLocalizer getI18nLocalizerById(long localizerId) {
		return i18nLocalizerDao.getI18nLocalizerById(localizerId);
	}

	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
	public void createI18nLocalizer(I18nLocalizer localizer) {
		i18nLocalizerDao.addI18nLocalizer(localizer);
	}

	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
	public void updateI18nLocalizer(I18nLocalizer localizer) {
		i18nLocalizerDao.updateI18nLocalizer(localizer);		
	}

	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
	public void deleteI18nLocalizer(I18nLocalizer localizer) {
		i18nLocalizerDao.deleteI18nLocalizer(localizer);
	}

	public List<I18nLocalizer> getI18nLocalizersByName(String name) {
		return i18nLocalizerDao.getI18nLocalizersByName(name);
	}
		
}
