package architecture.ee.i18n.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import architecture.ee.i18n.I18nTextManager;
import architecture.ee.i18n.dao.I18nLocaleDao;
import architecture.ee.i18n.dao.I18nLocalizerDao;

public abstract class AbstractI18nTextManager implements I18nTextManager {

	protected Log log = LogFactory.getLog(getClass());
	
	/**
	 */
	private I18nLocaleDao i18nLocaleDao;

	/**
	 */
	private I18nLocalizerDao i18nLocalizerDao;

	/**
	 * @return
	 */
	public I18nLocaleDao getI18nLocaleDao() {
		return i18nLocaleDao;
	}

	/**
	 * @param i18nLocaleDao
	 */
	public void setI18nLocaleDao(I18nLocaleDao i18nLocaleDao) {
		this.i18nLocaleDao = i18nLocaleDao;
	}

	/**
	 * @return
	 */
	public I18nLocalizerDao getI18nLocalizerDao() {
		return i18nLocalizerDao;
	}

	/**
	 * @param i18nLocalizerDao
	 */
	public void setI18nLocalizerDao(I18nLocalizerDao i18nLocalizerDao) {
		this.i18nLocalizerDao = i18nLocalizerDao;
	}
	
}
