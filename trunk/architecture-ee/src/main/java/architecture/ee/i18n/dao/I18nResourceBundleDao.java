package architecture.ee.i18n.dao;

import java.util.Locale;

import architecture.ee.i18n.I18nTextResourceBundle;

public interface I18nResourceBundleDao {

	public I18nTextResourceBundle getResourceBundle(String bundleName, Locale locale );
	
}
