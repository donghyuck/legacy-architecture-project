package architecture.ee.spring.context.support;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.support.AbstractMessageSource;
import org.springframework.util.Assert;

import architecture.ee.util.I18nTextUtils;

/**
 * @author  donghyuck
 */
public class I18nTextResourceBundleMessageSource extends AbstractMessageSource {

	private String[] localizers = new String[0];

	private final Map<String, Map<Locale, ResourceBundle>> cachedResourceBundles = new HashMap<String, Map<Locale, ResourceBundle>>();

	private final Map<ResourceBundle, Map<String, Map<Locale, MessageFormat>>> cachedBundleMessageFormats = new HashMap<ResourceBundle, Map<String, Map<Locale, MessageFormat>>>();

	private Log log = LogFactory.getLog(getClass());
	
	public void setLocalizer(String name) {
		setLocalizers(new String[] { name });
	}

	/**
	 * @param names
	 * @uml.property  name="localizers"
	 */
	public void setLocalizers(String[] names) {
		if (names != null) {
			this.localizers = new String[names.length];
			for (int i = 0; i < names.length; i++) {
				String name = names[i];
				Assert.hasText(name, "Localizer Name must not be empty");
				this.localizers[i] = name.trim();
			}
		} else {
			this.localizers = new String[0];
		}
	}

	@Override
	protected MessageFormat resolveCode(String code, Locale locale) {
		MessageFormat messageFormat = null;
		for (int i = 0; messageFormat == null && i < this.localizers.length; i++) {
			ResourceBundle bundle = getResourceBundle(this.localizers[i],
					locale);
			if (bundle != null) {
				messageFormat = getMessageFormat(bundle, code, locale);
			}
		}
		return messageFormat;
	}

	protected ResourceBundle getResourceBundle(String basename, Locale locale) {
		synchronized (this.cachedResourceBundles) {
			Map<Locale, ResourceBundle> localeMap = this.cachedResourceBundles.get(basename);
			if (localeMap != null) {
				ResourceBundle bundle = localeMap.get(locale);
				if (bundle != null) {
					return bundle;
				}
			}
			try {
				ResourceBundle bundle = doGetBundle(basename, locale);
				if (localeMap == null) {
					localeMap = new HashMap<Locale, ResourceBundle>();
					this.cachedResourceBundles.put(basename, localeMap);
				}
				localeMap.put(locale, bundle);
				return bundle;
			} catch (MissingResourceException ex) {
				if (logger.isWarnEnabled()) {
					logger.warn("ResourceBundle [" + basename
							+ "] not found for MessageSource: "
							+ ex.getMessage());
				}
				// Assume bundle not found
				// -> do NOT throw the exception to allow for checking parent
				// message source.
				return null;
			}
		}
	}

	protected MessageFormat getMessageFormat(ResourceBundle bundle,
			String code, Locale locale) throws MissingResourceException {

		synchronized (this.cachedBundleMessageFormats) {
			Map<String, Map<Locale, MessageFormat>> codeMap = this.cachedBundleMessageFormats
					.get(bundle);
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
	
	protected ResourceBundle doGetBundle(String basename, Locale locale) throws MissingResourceException {		
		
		log.debug( "###############################" );
		log.debug(basename + ", " + locale );
		return I18nTextUtils.getResourceBundle(basename, locale);
		
		
		//return ResourceBundle.getBundle(basename, locale, ClassUtils.getDefaultClassLoader());
	}

}
