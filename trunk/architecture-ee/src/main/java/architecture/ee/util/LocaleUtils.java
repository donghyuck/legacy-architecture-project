package architecture.ee.util;

import java.util.Locale;

public class LocaleUtils extends org.apache.commons.lang.LocaleUtils {

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
    
    
}
