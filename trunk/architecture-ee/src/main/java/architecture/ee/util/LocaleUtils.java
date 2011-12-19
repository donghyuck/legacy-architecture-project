package architecture.ee.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import architecture.ee.i18n.I18nLocalizer;
import architecture.ee.user.User;

public class LocaleUtils extends org.apache.commons.lang.LocaleUtils {

	private static Map<Locale, List<Locale>> cachedCandidateLocales = new HashMap<Locale, List<Locale>>();
	
	public static final List<Locale> SUPPORTED_LOCALES = Collections.unmodifiableList(getSupportedLocales());
	
    /**
     * Converts a locale string like "en", "en_US" or "en_US_win" to a Java
     * locale object. If the conversion fails, null is returned.
     *
     * @param localeCode the locale code for a Java locale. See the {@link java.util.Locale}
     *                   class for more details.
     * @return The Java Locale that matches the locale code, or <tt>null</tt>.                  
     */
    public static Locale localeCodeToLocale(String localeCode) {
    	return toLocale(localeCode);
    }
    
    public static boolean isValidCharacterEncoding(String encoding)
    {    
        boolean valid = true;
        try
        {
            "".getBytes(encoding);
        }
        catch(Exception e)
        {
            valid = false;
        }        
        return valid;
    }
    
    public static Locale getUserLocale(HttpServletRequest request, User user)
    {
        return getUserLocale(request, user, true);
    }
    
    public static Locale getUserLocale(HttpServletRequest request, User user, boolean useDefault)
    {
        Locale locale = null;        
        if(ApplicationHelper.getApplicationBooleanProperty(ApplicatioinConstants.SKIN_USERS_CHOOSE_LOCALE_PROP_NAME, true))
            if(user!= null && user.getProperties().get(ApplicatioinConstants.USER_LOCALE_PROP_NAME) != null)
                locale = localeCodeToLocale((String)user.getProperties().get(ApplicatioinConstants.USER_LOCALE_PROP_NAME));
            else
                locale = getLocaleFromRequestHeader(request);        
        if(locale != null)
            return locale;
        if(useDefault)
            return ApplicationHelper.getLocale();
        else
            return null;
    }
    
    public static Locale getLocaleFromRequestHeader(HttpServletRequest request)
    {
        if(request == null)
            return null;
        
        String header = request.getHeader("Accept-Language");
        
        if(header == null)
            return null;
        
        List<Locale> locales = new ArrayList<Locale>();
        
        String localeCodes[] = header.split(",");
        for(String localeCode:localeCodes){
        	localeCode = localeCode.replace('-', '_').trim();
            if(localeCode.equals("*"))
                continue;
            int qualityIndex = localeCode.indexOf(";");
            if(qualityIndex > -1)
                locales.add(localeCodeToLocale(localeCode.substring(0, qualityIndex)));
            else
                locales.add(localeCodeToLocale(localeCode));
        }

        for(Locale locale:locales){
            for( Locale candidate : getCandidateLocales(locale)){
            	if(SUPPORTED_LOCALES.contains(candidate));
            		return locale;
            }
        }        
        return null;
    }
    
    public static List<Locale> getCandidateLocales(Locale locale)
    {
        if(cachedCandidateLocales.containsKey(locale))
            return cachedCandidateLocales.get(locale);
        
        List<Locale> results = new ArrayList<Locale>();
        results.add(locale);        
        if(locale.getVariant().length() > 0)
            results.add(new Locale(locale.getLanguage(), locale.getCountry()));
        if(locale.getCountry().length() > 0)
            results.add(new Locale(locale.getLanguage()));
        results = Collections.unmodifiableList(results);
        cachedCandidateLocales.put(locale, results);
        return results;
    }    
    
    private static List<Locale> getSupportedLocales(){
    	List<Locale> locales = new ArrayList<Locale>(); 
    	List<I18nLocalizer> list = I18nTextUtils.getI18nLocalizers();
    	for(I18nLocalizer localizer : list){
    		Locale localeToUse = localizer.getI18nLocale().toJavaLocale();
    		locales.add(localeToUse);
    	}
    	return locales;
    }
}
