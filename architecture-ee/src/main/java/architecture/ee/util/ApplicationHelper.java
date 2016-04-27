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
package architecture.ee.util;

import java.lang.ref.WeakReference;
import java.util.Collections;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.core.io.Resource;

import architecture.common.event.api.EventPublisher;
import architecture.common.exception.ComponentNotFoundException;
import architecture.common.i18n.I18nTextManager;
import architecture.common.license.License;
import architecture.common.lifecycle.ApplicationHelperFactory;
import architecture.common.lifecycle.ConfigService;
import architecture.common.lifecycle.Repository;
import architecture.common.lifecycle.State;
import architecture.common.lifecycle.bootstrap.Bootstrap;
import architecture.common.util.StringUtils;
import architecture.ee.component.admin.AdminHelper;
import architecture.ee.spring.lifecycle.SpringAdminService;
import groovy.lang.GroovyClassLoader;

/**
 * 컴포넌트들에 대한 인터페이스를 제공하는 Helper 클래스.
 * 
 * @author donghyuck
 *
 */
public final class ApplicationHelper {

    private static final Log LOG = LogFactory.getLog(ApplicationHelper.class);

    private static final Map<Class<?>, WeakReference<?>> references = Collections.synchronizedMap(new HashMap<Class<?>, WeakReference<?>>());

    @SuppressWarnings("unchecked")
    public static <T> T getComponent(Class<T> requiredType) throws ComponentNotFoundException {
	if (ApplicationHelper.references.get(requiredType) == null) {
	    ApplicationHelper.references.put(requiredType, new WeakReference<T>(ApplicationHelperFactory.getApplicationHelper().getComponent(requiredType)));
	}
	return (T) ApplicationHelper.references.get(requiredType).get();
    }

    public static <T> T getComponent(String requiredName, Class<T> requiredType) throws ComponentNotFoundException {
	return ApplicationHelperFactory.getApplicationHelper().getComponent(requiredName, requiredType);
    }

    public static void autowireComponent(Object obj) {
	ApplicationHelperFactory.getApplicationHelper().autowireComponent(obj);
    }

    
    public static Resource getResource(String path) {
	SpringAdminService adminService = (SpringAdminService) AdminHelper.getAdminService();
	return adminService.getApplicationContext().getResource(path);
    }

    public static License getLicense() {
	return AdminHelper.getLicense();
    }

    public static ConfigService getConfigService() {
	return AdminHelper.getConfigService();
    }

    public static I18nTextManager getI18nTextManager() {
	return getComponent(I18nTextManager.class);
    }

    public static boolean isSetupComplete() {
	return AdminHelper.isSetupComplete();
    }

    public static boolean isReady() {
	return AdminHelper.isReady();
    }

    public static EventPublisher getEventPublisher() {
	return AdminHelper.getEventPublisher();
    }

    public static State getState() {
	return AdminHelper.getState();
    }

    public static Repository getRepository() {
	return AdminHelper.getRepository();
    }

    public static Class loadClass(String scriptName, boolean lookupScriptFiles, boolean preferClassOverScript)
	    throws ClassNotFoundException {
	GroovyClassLoader gcl = Bootstrap.getBootstrapComponent(groovy.lang.GroovyClassLoader.class);
	return gcl.loadClass(scriptName, lookupScriptFiles, preferClassOverScript);
    }

    public static Locale getLocale() {
	if (isReady()) {
	    return getConfigService().getLocale();
	} else {
	    String language = getConfigService().getLocalProperty(ApplicationConstants.LOCALE_LANGUAGE_PROP_NAME);
	    if (language == null)
		language = "";
	    String country = getConfigService().getLocalProperty(ApplicationConstants.LOCALE_COUNTRY_PROP_NAME);
	    if (country == null)
		country = "";
	    if (language.equals("") && country.equals(""))
		return Locale.getDefault();
	    else
		return new Locale(language, country);
	}
    }

    public static String getCharacterEncoding() {
	if (isReady()) {
	    return getConfigService().getCharacterEncoding();
	} else {
	    return getConfigService().getLocalProperty(ApplicationConstants.LOCALE_CHARACTER_ENCODING_PROP_NAME,
		    ApplicationConstants.DEFAULT_CHAR_ENCODING);
	}
    }

    public static String getApplicationProperty(String name, String defaultValue) {

	ConfigService config = getConfigService();
	String propValue = config.getLocalProperty(name, null);

	if (isReady()) {
	    String str = config.getApplicationProperty(name, null);
	    if (!StringUtils.isEmpty(str))
		propValue = str;
	}

	if (propValue == null)
	    propValue = defaultValue;
	return propValue;
    }

    public static int getApplicationIntProperty(String name, int defaultValue) {
	String str = getApplicationProperty(name, Integer.toString(defaultValue));
	return Integer.parseInt(str);
    }

    public static long getApplicationLongProperty(String name, long defaultValue) {
	String str = getApplicationProperty(name, Long.toString(defaultValue));
	return Integer.parseInt(str);
    }

    public static boolean getApplicationBooleanProperty(String name, boolean defaultValue) {
	String str = getApplicationProperty(name, Boolean.toString(defaultValue));
	return Boolean.parseBoolean(str);
    }

    public static TimeZone getTimeZone() {
	if (isReady()) {
	    return getConfigService().getTimeZone();
	} else {
	    String timeZoneID = getConfigService().getLocalProperty(ApplicationConstants.LOCALE_TIMEZONE_PROP_NAME);
	    if (timeZoneID == null)
		return TimeZone.getDefault();
	    else
		return TimeZone.getTimeZone(timeZoneID);
	}
    }

    public static String getLocalizedApplicationProperty(String name, Locale locale) {
	if (isReady())
	    return getConfigService().getLocalizedApplicationProperty(name, locale);
	else
	    return null;
    }

    public static String getLocalizedMessage(String code, Object[] args, Locale locale) {
	return AdminHelper.getLocalizedMessage(code, args, locale);
    }

}