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
package architecture.ee.component.core.lifecycle;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

import javax.sql.DataSource;

import org.apache.commons.lang3.time.FastDateFormat;
import org.apache.commons.vfs2.FileObject;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import architecture.common.jdbc.datasource.DataSourceFactory;
import architecture.common.lifecycle.ApplicationProperties;
import architecture.common.lifecycle.ComponentImpl;
import architecture.common.lifecycle.ConfigService;
import architecture.common.lifecycle.internal.EmptyApplicationProperties;
import architecture.common.util.LocaleUtils;
import architecture.common.util.StringUtils;
import architecture.common.util.vfs.VFSUtils;
import architecture.ee.jdbc.sqlquery.factory.Configuration;
import architecture.ee.util.ApplicationConstants;

/**
 * @author donghyuck
 */

public class ConfigServiceImpl extends ComponentImpl implements ConfigService {

    private ApplicationProperties setupProperties = null;

    private ApplicationProperties properties = null;

    private ApplicationProperties localizedProperties = null;

    private Locale locale = null;

    private TimeZone timeZone = null;

    private String characterEncoding = null;

    private FastDateFormat dateFormat = null;

    private FastDateFormat dateTimeFormat = null;

    private Configuration sqlConfiguration = null;

    private DataSource dataSource = null;

    private String effectiveRootPath = null;

    public ConfigServiceImpl() {
	super();
	setName("ConfigService");
    }

    public void setDataSource(DataSource dataSource) {
	this.dataSource = dataSource;
	reset();
    }

    public void reset() {
	this.setupProperties = null;
	this.properties = null;
	this.localizedProperties = null;
	resetL10N();
    }

    public void resetL10N() {
	this.locale = null;
	this.timeZone = null;
	this.characterEncoding = null;
	this.dateFormat = null;
	this.dateTimeFormat = null;
    }

    /**
     * @return
     */
    public String getEffectiveRootPath() {
	if (!StringUtils.isEmpty(effectiveRootPath)) {
	    return effectiveRootPath;
	} else {
	    String uri = getConfigRoot().getRootURI();
	    FileObject obj = VFSUtils.resolveFile(uri);
	    effectiveRootPath = obj.getName().getPath();
	    return effectiveRootPath;
	}
    }

    private ApplicationProperties getLocalizedApplicationProperties() {
	if (localizedProperties == null) {
	    getSetupProperties();
	    if (dataSource != null) {
		localizedProperties = newApplicationProperties(true);
	    }
	}
	return localizedProperties == null ? EmptyApplicationProperties.getInstance() : localizedProperties;
    }

    private ApplicationProperties getApplicationProperties() {
	if (properties == null) {
	    getSetupProperties();
	    this.properties = newApplicationProperties(false);
	}
	return properties == null ? EmptyApplicationProperties.getInstance() : properties;

    }

    private ApplicationProperties getSetupProperties() {
	if (setupProperties == null)
	    this.setupProperties = getRepository().getSetupApplicationProperties();
	return setupProperties;
    }

    public void setSetupProperties(ApplicationProperties setupProperties) {
	this.setupProperties = setupProperties;
    }

    private ApplicationProperties newApplicationProperties(boolean localized) {
	DataSource dataSourceToUse = this.dataSource;
	if (dataSourceToUse == null) {
	    getSetupProperties();
	    dataSourceToUse = DataSourceFactory.getDataSource();
	}
	// 데이터베이스 설정이 완료되지 않았다면 널을 리턴한다.
	if (dataSourceToUse != null) {
	    log.debug("Loading properties from database.");
	    JdbcApplicationProperties impl = new JdbcApplicationProperties(localized);
	    impl.setEventPublisher(getEventPublisher());
	    impl.setConfiguration(sqlConfiguration);
	    impl.setDataSource(dataSourceToUse);
	    impl.afterPropertiesSet();
	    return impl;
	}
	
	return null;
    }

    public void setSqlConfiguration(Configuration sqlConfiguration) {
	this.sqlConfiguration = sqlConfiguration;
    }

    /**
     * @return 로케일 정보를 리턴한다. 기본은 ko_KR 이다.
     */
    public Locale getLocale() {

	if (this.locale == null) {
	    Locale localeToUse = Locale.getDefault();

	    String languageToUse = getLocalProperty(ApplicationConstants.LOCALE_LANGUAGE_PROP_NAME, null);
	    String countryToUse = getLocalProperty(ApplicationConstants.LOCALE_COUNTRY_PROP_NAME, null);
	    if (!StringUtils.isEmpty(languageToUse)) {
		if (StringUtils.isEmpty(countryToUse)) {
		    localeToUse = new Locale(languageToUse, "", "");
		} else {
		    localeToUse = new Locale(languageToUse, countryToUse, "");
		}
	    }

	    languageToUse = (String) getApplicationProperties().get(ApplicationConstants.LOCALE_LANGUAGE_PROP_NAME);
	    countryToUse = (String) getApplicationProperties().get(ApplicationConstants.LOCALE_COUNTRY_PROP_NAME);

	    if (!StringUtils.isEmpty(languageToUse)) {
		if (StringUtils.isEmpty(countryToUse)) {
		    localeToUse = new Locale(languageToUse, null, null);
		} else {
		    localeToUse = new Locale(languageToUse, countryToUse, null);
		}
	    }
	    this.locale = localeToUse;
	}
	return locale;
    }

    /**
     * 로케일을 지정한다.
     * 
     * @param newLocale
     */
    public void setLocale(Locale newLocale) {
	String country = newLocale.getCountry();
	String language = newLocale.getLanguage();
	setApplicationProperty(ApplicationConstants.LOCALE_COUNTRY_PROP_NAME, country);
	setApplicationProperty(ApplicationConstants.LOCALE_LANGUAGE_PROP_NAME, language);
	resetL10N();
    }

    /**
     * @return 문자 인코딩을 리턴한다.
     */
    public String getCharacterEncoding() {
	if (characterEncoding == null) {
	    String encoding = getLocalProperty(ApplicationConstants.LOCALE_CHARACTER_ENCODING_PROP_NAME);
	    if (encoding != null)
		characterEncoding = encoding;
	    String charEncoding = getApplicationProperty(ApplicationConstants.LOCALE_CHARACTER_ENCODING_PROP_NAME);
	    if (charEncoding != null)
		characterEncoding = charEncoding;
	    else if (characterEncoding == null)
		characterEncoding = ApplicationConstants.DEFAULT_CHAR_ENCODING;
	}
	return characterEncoding;
    }

    /**
     * @param characterEncoding
     * @throws UnsupportedEncodingException
     */
    public void setCharacterEncoding(String characterEncoding) throws UnsupportedEncodingException {
	if (!LocaleUtils.isValidCharacterEncoding(characterEncoding)) {
	    throw new UnsupportedEncodingException(
		    (new StringBuilder()).append("Invalid character encoding: ").append(characterEncoding).toString());
	} else {
	    setApplicationProperty(ApplicationConstants.LOCALE_CHARACTER_ENCODING_PROP_NAME, characterEncoding);
	    resetL10N();
	    return;
	}
    }

    /**
     * @return
     */
    public TimeZone getTimeZone() {
	if (timeZone == null)
	    if (properties != null) {
		String timeZoneID = (String) properties.get(ApplicationConstants.LOCALE_TIMEZONE_PROP_NAME);
		if (timeZoneID == null)
		    timeZone = TimeZone.getDefault();
		else
		    timeZone = TimeZone.getTimeZone(timeZoneID);
	    } else {
		return TimeZone.getDefault();
	    }
	return timeZone;
    }

    /**
     * @param newTimeZone
     */
    public void setTimeZone(TimeZone newTimeZone) {
	String timeZoneId = newTimeZone.getID();
	setApplicationProperty(ApplicationConstants.LOCALE_TIMEZONE_PROP_NAME, timeZoneId);
	resetL10N();
    }

    public String formatDate(Date date) {
	if (dateFormat == null) {
	    if (properties != null) {
		dateFormat = FastDateFormat.getDateInstance(2, getTimeZone(), getLocale());
	    } else {
		FastDateFormat instance = FastDateFormat.getDateInstance(2, getTimeZone(), getLocale());
		return instance.format(date);
	    }
	}
	return dateFormat.format(date);
    }

    public String formatDateTime(Date date) {
	if (dateTimeFormat == null) {
	    if (properties != null) {
		dateTimeFormat = FastDateFormat.getDateTimeInstance(2, 2, getTimeZone(), getLocale());
	    } else {
		FastDateFormat instance = FastDateFormat.getDateTimeInstance(2, 2, getTimeZone(), getLocale());
		return instance.format(date);
	    }
	}
	return dateTimeFormat.format(date);
    }

    public String getLocalProperty(String name) {
	return (String) getSetupProperties().get(name);
    }

    public int getLocalProperty(String name, int defaultValue) {
	String value = getLocalProperty(name);
	if (value != null) {
	    try {
		return Integer.parseInt(value);
	    } catch (NumberFormatException nfe) {
	    }
	}
	return defaultValue;
    }

    public boolean getLocalProperty(String name, boolean defaultValue) {
	String value = getLocalProperty(name);
	if (value != null) {
	    try {
		return Boolean.parseBoolean(value);
	    } catch (NumberFormatException nfe) {
	    }
	}
	return defaultValue;
    }

    public String getLocalProperty(String name, String defaultValue) {
	String value = getLocalProperty(name);
	if (value != null)
	    return value;
	else
	    return defaultValue;
    }

    public List<String> getLocalProperties(String parent) {
	List<String> values = new ArrayList<String>();
	Collection<String> propNames = getSetupProperties().getChildrenNames(parent);
	for (String propName : propNames) {
	    String value = getApplicationProperty(
		    (new StringBuilder()).append(parent).append(".").append(propName).toString());
	    if (value != null)
		values.add(value);
	}
	return values;
    }

    public void setLocalProperty(String name, String value) {
	getSetupProperties().put(name, value);
    }

    public void setLocalProperties(Map<String, String> map) {
	getSetupProperties().putAll(map);
    }

    public void deleteLocalProperty(String name) {
	getSetupProperties().remove(name);
    }

    public String getApplicationProperty(String name) {
	return getApplicationProperties().get(name);
    }

    public String getApplicationProperty(String name, String defaultValue) {
	getApplicationProperties();
	String value = (String) getApplicationProperties().get(name);
	if (value != null)
	    return value;
	else
	    return defaultValue;
    }

    public List<String> getApplicationPropertyNames() {
	return new ArrayList<String>(getApplicationProperties().getPropertyNames());
    }

    public List<String> getApplicationPropertyNames(String parent) {
	getApplicationProperties();
	return new ArrayList<String>(getApplicationProperties().getChildrenNames(parent));
    }

    public List<String> getApplicationProperties(String parent) {
	getApplicationProperties();
	Collection<String> propertyNames = getApplicationProperties().getChildrenNames(parent);
	List<String> values = new ArrayList<String>();
	for (String propertyName : propertyNames) {
	    String value = getApplicationProperty(propertyName);
	    if (value != null)
		values.add(value);
	}
	return values;
    }

    public int getApplicationIntProperty(String name, int defaultValue) {
	String value = getApplicationProperty(name);
	if (value != null) {
	    try {
		return Integer.parseInt(value);
	    } catch (NumberFormatException nfe) {
	    }
	}
	return defaultValue;
    }

    public boolean getApplicationBooleanProperty(String name) {
	return Boolean.valueOf(getApplicationProperty(name)).booleanValue();
    }

    public boolean getApplicationBooleanProperty(String name, boolean defaultValue) {
	String value = getApplicationProperty(name);
	if (value != null)
	    return Boolean.valueOf(value).booleanValue();
	else
	    return defaultValue;
    }

    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    public void setApplicationProperty(String name, String value) {
	getApplicationProperties().put(name, value);
    }

    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    public void setApplicationProperties(Map<String, String> map) {
	getApplicationProperties().putAll(map);
    }

    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    public void deleteApplicationProperty(String name) {
	getApplicationProperties().remove(name);
    }

    public String getLocalizedApplicationProperty(String name, Locale locale) {
	return (String) getLocalizedApplicationProperties()
		.get((new StringBuilder()).append(name).append(".").append(locale).toString());
    }

    public List<Locale> getLocalizedApplicationPropertyLocales(String name) {
	if (getLocalizedApplicationProperties() instanceof JdbcApplicationProperties)
	    return ((JdbcApplicationProperties) getLocalizedApplicationProperties()).getLocalesForProperty(name);
	else
	    return Collections.emptyList();
    }

    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    public void setLocalizedApplicationProperty(String name, String value, Locale locale) {
	getLocalizedApplicationProperties().put((new StringBuilder()).append(name).append(".").append(locale.toString()).toString(), value);
    }

    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    public void deleteLocalizedApplicationProperty(String name, Locale locale) {
	getLocalizedApplicationProperties().remove((new StringBuilder()).append(name).append(".").append(locale.toString()).toString());
    }

    @Override
    public String toString() {
	return "ConfigServiceImpl [setupProperties=" + setupProperties + ", properties=" + properties
		+ ", localizedProperties=" + localizedProperties + ", locale=" + locale + ", timeZone=" + timeZone
		+ ", characterEncoding=" + characterEncoding + ", dateFormat=" + dateFormat + ", dateTimeFormat="
		+ dateTimeFormat + ", sqlConfiguration=" + sqlConfiguration + ", dataSource=" + dataSource
		+ ", effectiveRootPath=" + effectiveRootPath + "]";
    }
    
    
    
}