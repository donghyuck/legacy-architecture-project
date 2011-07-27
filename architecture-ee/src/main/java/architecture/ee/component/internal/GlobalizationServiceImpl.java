package architecture.ee.component.internal;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import architecture.ee.component.GlobalizationService;
import architecture.ee.g11n.Country;
import architecture.ee.g11n.I18nLocale;
import architecture.ee.g11n.I18nText;
import architecture.ee.g11n.I18nTextResourceBundle;
import architecture.ee.g11n.dao.CountryDao;
import architecture.ee.g11n.dao.I18nLocaleDao;
import architecture.ee.g11n.dao.I18nResourceBundleDao;
import architecture.ee.g11n.dao.I18nTextDao;

public class GlobalizationServiceImpl implements GlobalizationService {
	
	private Log log = LogFactory.getLog(getClass());
	
	private CountryDao countryDao;
	private I18nTextDao i18nTextDao;
	private I18nLocaleDao i18nLocaleDao;
	private I18nResourceBundleDao i18nResourceBundleDao;
	
	//private Admin admin;
	
	//private Map<Locale, I18nTextResourceBundle> resourceBundles;
	
	private Map<String, I18nTextResourceBundle> resourceBundles;
	
	public GlobalizationServiceImpl() {
		//this.admin = admin;
		this.resourceBundles = new HashMap<String, I18nTextResourceBundle>();
	}

	public void initialize(){
		reloadResourceBundes();
	}	
		
	
	public void setI18nResourceBundleDao(I18nResourceBundleDao i18nResourceBundleDao) {
		this.i18nResourceBundleDao = i18nResourceBundleDao;
	}

	public void setCountryDao(CountryDao countryDao) {
		this.countryDao = countryDao;
	}

	public void setI18nTextDao(I18nTextDao i18nTextDao) {
		this.i18nTextDao = i18nTextDao;
	}
	
	public void setI18nLocaleDao(I18nLocaleDao i18nLocaleDao) {
		this.i18nLocaleDao = i18nLocaleDao;
	}

	public List<Country> getCountries() {
		return countryDao.findAll();
	}

	public List<Country> getCountries(boolean enabled) {
		
		return countryDao.findByEnabled(enabled);
	}

	public Country getCountryByA2(String code) {
		return countryDao.findByA2(code);
	}

	public Country getCountryByA3(String code) {
		return countryDao.findByA3(code);
	}	
	
	public List<I18nText> getTexts(int objectType, String localeCode){
		return i18nTextDao.getTexts(objectType, localeCode);
	}

	public List<I18nText> getTexts(int objectType, long objectId){
		return i18nTextDao.getTexts(objectType, objectId);
	}
	
	 public List<I18nText> getTexts(int objectType){
		 return i18nTextDao.getTexts(objectType);
	 }
	 
	 public List<I18nText> getTexts(){
		 return i18nTextDao.getTexts();
	 }
	 
	 public I18nText getText(long textID){		 
		 return i18nTextDao.getText(textID);
	 }
	 
	 public void deleteTexts(List<I18nText> texts){
		 i18nTextDao.deleteTexts(texts);
	 }
	 
	 public void saveTexts(List<I18nText> texts){
         List<I18nText> textsToCreate = new ArrayList<I18nText>();
         List<I18nText> textsToUpdate = new ArrayList<I18nText>();
         List<I18nText> textsToDelete = new ArrayList<I18nText>();
         for(I18nText text : texts ){
        	 if(text.getText() == null || text.getText().trim().length() == 0)
             {
                 if(text.getTextId() != -1L)
                     textsToDelete.add(text);
             } else
             if(text.getTextId() == -1L)
                 textsToCreate.add(text);
             else
                 textsToUpdate.add(text);
         }
		 
         i18nTextDao.createTexts(textsToCreate);
         i18nTextDao.updateTexts(textsToUpdate);
         i18nTextDao.deleteTexts(textsToDelete);
         
         reloadResourceBundes();
	 }
	 
	 public void saveTexts(List<I18nText> textList, int objectType, long objectID){
		 for(I18nText text : textList ){
			 if(text.getTextId() == -1L){}
		 }
		 saveTexts(textList);
	 }
	 
	protected void reloadResourceBundes() {
		loadResourceBundles();
	}
	    
	 protected void loadResourceBundles(){		
		 HashMap<Locale, I18nTextResourceBundle> newResourceBundles = new HashMap<Locale, I18nTextResourceBundle>();		 
		 for(I18nLocale locale :  i18nLocaleDao.getAvailableLocales()){   
			 Locale l = locale.toJavaLocale();
			 List<I18nText> textList = i18nTextDao.getTexts(-1, l.toString());			 
			 log.debug( "resource bundle for " + l.toString());			 
			 List<String[]> keyValues = new ArrayList<String[]>();
	         for(I18nText text:textList){
	          	log.debug( text.getResourceBundleKey() + "=" +  text.getText() );
	              keyValues.add(new String[]{text.getResourceBundleKey(), text.getText()});
	          }
	          newResourceBundles.put(l, new I18nTextResourceBundle(l, keyValues ) );
		 }		 
		 //this.resourceBundles = newResourceBundles;
	 }

	public List<I18nLocale> getAvailableLocales() {
		return i18nLocaleDao.getAvailableLocales();
	}

	public I18nLocale getLocaleById(long localeId) {
		return i18nLocaleDao.getLocaleById(localeId);
	}

	public I18nLocale getLocale(Locale locale, boolean createIfNotExist) {
		return i18nLocaleDao.getLocale(locale, createIfNotExist);
	}

	public I18nLocale getLocale(Locale locale) {
		return i18nLocaleDao.getLocale(locale);
	}

	public void deleteLocale(I18nLocale locale) {
		i18nLocaleDao.deleteLocale(locale);		
	}

	public I18nLocale updateLocale(I18nLocale locale) {
		return i18nLocaleDao.updateLocale(locale);
	}

	public ResourceBundle getResourceBundle(String baseName, Locale locale) {
		String bundleName = toBundleName(baseName, locale);
		if(!resourceBundles.containsKey(bundleName)){
			I18nTextResourceBundle bundel = i18nResourceBundleDao.getResourceBundle(baseName, locale);
			resourceBundles.put(bundleName, bundel);
		}
		return resourceBundles.get(bundleName);
	}
	

	/**
	 * Converts the given <code>baseName</code> and <code>locale</code> to the
	 * bundle name. This method is called from the default implementation of the
	 * {@link #newBundle(String, Locale, String, ClassLoader, boolean)
	 * newBundle} and
	 * {@link #needsReload(String, Locale, String, ClassLoader, ResourceBundle, long)
	 * needsReload} methods.
	 * 
	 * <p>
	 * This implementation returns the following value:
	 * 
	 * <pre>
	 * baseName + &quot;_&quot; + language + &quot;_&quot; + country + &quot;_&quot; + variant
	 * </pre>
	 * 
	 * where <code>language</code>, <code>country</code> and
	 * <code>variant</code> are the language, country and variant values of
	 * <code>locale</code>, respectively. Final component values that are empty
	 * Strings are omitted along with the preceding '_'. If all of the values
	 * are empty strings, then <code>baseName</code> is returned.
	 * 
	 * <p>
	 * For example, if <code>baseName</code> is <code>"baseName"</code> and
	 * <code>locale</code> is <code>Locale("ja",&nbsp;"",&nbsp;"XX")</code>,
	 * then <code>"baseName_ja_&thinsp;_XX"</code> is returned. If the given
	 * locale is <code>Locale("en")</code>, then <code>"baseName_en"</code> is
	 * returned.
	 * 
	 * <p>
	 * Overriding this method allows applications to use different conventions
	 * in the organization and packaging of localized resources.
	 * 
	 * @param baseName
	 *            the base name of the resource bundle, a fully qualified class
	 *            name
	 * @param locale
	 *            the locale for which a resource bundle should be loaded
	 * @return the bundle name for the resource bundle
	 * @exception NullPointerException
	 *                if <code>baseName</code> or <code>locale</code> is
	 *                <code>null</code>
	 */
	private String toBundleName(String baseName, Locale locale) {
		
		if (locale == Locale.ROOT) {
			return baseName;
		}

		String language = locale.getLanguage();
		String country = locale.getCountry();
		String variant = locale.getVariant();

		if (language == "" && country == "" && variant == "") {
			return baseName;
		}

		StringBuilder sb = new StringBuilder(baseName);
		sb.append('_');
		if (variant != "") {
			sb.append(language).append('_').append(country).append('_')
					.append(variant);
		} else if (country != "") {
			sb.append(language).append('_').append(country);
		} else {
			sb.append(language);
		}
		return sb.toString();

	}
}