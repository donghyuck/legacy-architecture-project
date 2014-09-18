package architecture.ee.spring.context.support;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.concurrent.atomic.AtomicBoolean;

import org.springframework.context.support.AbstractMessageSource;

import architecture.ee.util.ApplicationHelper;

public class I18nTextMessageSource extends AbstractMessageSource {

	private final AtomicBoolean i18nTextBundlesLoaded = new AtomicBoolean(false);	
	
	private final Map<Locale, ResourceBundle> cachedResourceBundles = new HashMap<Locale, ResourceBundle>();
	
	private final Map<ResourceBundle, Map<String, Map<Locale, MessageFormat>>> cachedBundleMessageFormats = new HashMap<ResourceBundle, Map<String, Map<Locale, MessageFormat>>>();
	
	protected MessageFormat resolveCode(String code, Locale locale) {
		
		MessageFormat messageFormat = null;		
		ResourceBundle bundle = getResourceBundle(locale);
		if (bundle != null) {
			messageFormat = getMessageFormat(bundle, code, locale);
		}
		return messageFormat;
	}
	
	protected MessageFormat getMessageFormat(ResourceBundle bundle, String code, Locale locale) throws MissingResourceException {

		synchronized (this.cachedBundleMessageFormats) {
			Map<String, Map<Locale, MessageFormat>> codeMap = this.cachedBundleMessageFormats.get(bundle);
			Map<Locale, MessageFormat> localeMap = null;
			if (codeMap != null) {
				localeMap = codeMap.get(code);
				if (localeMap != null) {
					MessageFormat result = localeMap.get(locale);
					if (result != null) {
						return result;
					}
				}
			}

			String msg = getStringOrNull(bundle, code);
			if (msg != null) {
				if (codeMap == null) {
					codeMap = new HashMap<String, Map<Locale, MessageFormat>>();
					this.cachedBundleMessageFormats.put(bundle, codeMap);
				}
				if (localeMap == null) {
					localeMap = new HashMap<Locale, MessageFormat>();
					codeMap.put(code, localeMap);
				}
				MessageFormat result = createMessageFormat(msg, locale);
				localeMap.put(locale, result);
				return result;
			}
			return null;
		}
	}
	
	private String getStringOrNull(ResourceBundle bundle, String key) {
		try {
			return bundle.getString(key);
		} catch (MissingResourceException ex) {
			// Assume key not found
			// -> do NOT throw the exception to allow for checking parent
			// message source.
			return null;
		}
	}
	
	protected ResourceBundle getResourceBundle(Locale locale) {		
		
		if( ! i18nTextBundlesLoaded.get() ){
			cachedResourceBundles.putAll(
					ApplicationHelper.getI18nTextManager().getResourceBundles()
			);			
			i18nTextBundlesLoaded.set(true);
		}
		
		if( cachedResourceBundles.containsKey(locale) ){
			return cachedResourceBundles.get(locale);		
		}else{
			if (logger.isWarnEnabled()) {
				logger.warn("ResourceBundle not found for MessageSource.");
			}
		}
		return null;
	}
}