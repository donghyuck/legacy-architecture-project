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
   
package architecture.ee.web.util;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import architecture.common.user.User;
import architecture.ee.util.ApplicationConstants;
import architecture.ee.util.ApplicationHelper;

public class LocaleUtils extends architecture.common.util.LocaleUtils {

    public static Locale getUserLocale(HttpServletRequest request, User user)
    {
        return getUserLocale(request, user, true);
    }
    
    public static Locale getUserLocale(HttpServletRequest request, User user, boolean useDefault)
    {
        Locale locale = null;        
        if(ApplicationHelper.getApplicationBooleanProperty(ApplicationConstants.SKIN_USERS_CHOOSE_LOCALE_PROP_NAME, true))
            if(user!= null && user.getProperties().get(ApplicationConstants.USER_LOCALE_PROP_NAME) != null)
                locale = localeCodeToLocale((String)user.getProperties().get(ApplicationConstants.USER_LOCALE_PROP_NAME));
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
     

    public static String getLocalizedString(String key)
    {    	
        Locale locale = ApplicationHelper.getLocale();
        String value = ApplicationHelper.getLocalizedApplicationProperty(key, locale);
        if(value == null)
        	value = getLocalizedString(key, locale, null);
        return value;
    }

    public static String getLocalizedString(String key, Locale locale)
    {
        String value = ApplicationHelper.getLocalizedApplicationProperty(key, locale);
        if(value == null)
        	value = getLocalizedString(key, locale, null);
        return value;
    }

    public static String getLocalizedString(String key, List<?> arguments)
    {
        return getLocalizedString(key, ApplicationHelper.getLocale(), arguments);
    }

    
    public static String getLocalizedString(String key, Locale locale, List<?> arguments)
    {
    	Locale localeToUse = locale;
    	String keyToUse = key;
    	
        if(keyToUse == null)
            throw new NullPointerException("i18n key cannot be null");
        if(localeToUse == null)
        	localeToUse = ApplicationHelper.getLocale();
        
        String value = ApplicationHelper.getLocalizedMessage(keyToUse, arguments.toArray(), localeToUse);
        if(value == null)
            value = "";
        return value;
    }


    public static String getLocalizedNumber(long number)
    {
        return NumberFormat.getInstance().format(number);
    }

    public static String getLocalizedNumber(long number, Locale locale)
    {
        return NumberFormat.getInstance(locale).format(number);
    }

    public static String getLocalizedNumber(double number)
    {
        return NumberFormat.getInstance().format(number);
    }

    public static String getLocalizedNumber(double number, Locale locale)
    {
        return NumberFormat.getInstance(locale).format(number);
    }
    
/*    public static String getLocalizedString(String key, String pluginName)
    {
        return getLocalizedString(key, pluginName, null);
    }

    public static String getLocalizedString(String key, String pluginName, List<?> arguments)
    {
        return key;
    }*/
}
