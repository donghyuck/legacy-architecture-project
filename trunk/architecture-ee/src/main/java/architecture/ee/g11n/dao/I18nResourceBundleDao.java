package architecture.ee.g11n.dao;

import java.util.Locale;

import architecture.ee.g11n.I18nTextResourceBundle;

public interface I18nResourceBundleDao {

	public I18nTextResourceBundle getResourceBundle(String bundleName, Locale locale );
	
}
