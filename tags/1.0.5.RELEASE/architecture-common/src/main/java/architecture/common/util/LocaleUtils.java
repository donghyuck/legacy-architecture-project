/*
 * Copyright 2012 Donghyuck, Son
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package architecture.common.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.StringTokenizer;

public abstract class LocaleUtils extends org.apache.commons.lang3.LocaleUtils  {

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
        Locale locale = null;
        if (localeCode != null) {
            String language = null;
            String country = null;
            String variant = null;
            StringTokenizer tokenizer = new StringTokenizer(localeCode, "_");
            if (tokenizer.hasMoreTokens()) {
                language = tokenizer.nextToken();
                if (tokenizer.hasMoreTokens()) {
                    country = tokenizer.nextToken();
                    if (tokenizer.hasMoreTokens()) {
                        variant = tokenizer.nextToken();
                    }
                }
            }
            locale = new Locale(language,
                    ((country != null) ? country : ""),
                    ((variant != null) ? variant : ""));
        }
        return locale;
    }
    
    public static Locale[] getAvailableLocales()
    {
        Locale locales[] = Locale.getAvailableLocales();
        
        Arrays.sort(locales, new Comparator<Locale>(){
            public int compare(Locale locale1, Locale locale2)
            {
                return locale1.getDisplayName().compareTo(locale2.getDisplayName());
            }});
        return locales;
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
    
    private static List<Locale> getSupportedLocales()
    {    	
        List<Locale> results = new ArrayList<Locale>();
        for(Locale locale : getAvailableLocales() ){
        	results.add(locale);
        }        
        return results;
    }
    
}