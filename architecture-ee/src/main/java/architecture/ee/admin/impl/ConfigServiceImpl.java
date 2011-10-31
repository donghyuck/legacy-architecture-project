package architecture.ee.admin.impl;

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

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.FastDateFormat;
import org.apache.commons.vfs.FileObject;
import org.apache.commons.vfs.FileSystemException;

import architecture.common.lifecycle.ApplicationProperties;
import architecture.common.lifecycle.ComponentImpl;
import architecture.common.lifecycle.ConfigRoot;
import architecture.common.lifecycle.ConfigService;
import architecture.common.lifecycle.Repository;
import architecture.common.lifecycle.internal.EmptyApplicationProperties;
import architecture.common.util.vfs.VFSUtils;
import architecture.ee.bootstrap.Bootstrap;
import architecture.ee.jdbc.datasource.DataSourceFactory;
import architecture.ee.jdbc.query.factory.SqlQueryFactoryBuilder;
import architecture.ee.util.ApplicatioinConstants;
import architecture.ee.util.LocaleUtils;

public class ConfigServiceImpl extends ComponentImpl implements ConfigService {

	private ApplicationProperties setupProperties = null;
	private ApplicationProperties properties = null;	
	private ApplicationProperties localizedProperties = null;	
	
    private Locale locale = null;
    private TimeZone timeZone = null;
    private String characterEncoding = null;
    private FastDateFormat dateFormat = null;
    private FastDateFormat dateTimeFormat = null;
 
    private SqlQueryFactoryBuilder sqlQueryFactoryBuilder = null;   
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
	
	public void reset(){
        this.setupProperties = null;
        this.properties = null;
        this.localizedProperties = null;
        //whiteLabel = false;
        //initWhiteLabel = false;        
		resetL10N();
	}
	
	public void resetL10N(){		
		this.locale = null;
		this.timeZone = null;
		this.characterEncoding = null;
		this.dateFormat = null;
		this.dateTimeFormat = null;
	}	

	public Repository getRepository(){
		return Bootstrap.getBootstrapComponent(Repository.class);
	}
	
	public ConfigRoot getConfigRoot() {
		return getRepository().getConfigRoot();
	}
	
    public String getEffectiveRootPath()
    {	
        if(!StringUtils.isEmpty(effectiveRootPath))
        {
            return effectiveRootPath;
        } else
        {
        	String uri = getConfigRoot().getRootURI();   	
        	try {
        		FileObject obj = VFSUtils.resolveFile(uri);
        		effectiveRootPath = obj.getName().getPath();   		
			} catch (FileSystemException e) {
			}        	
            return effectiveRootPath;
        }
    }
    	
    private ApplicationProperties getLocalizedApplicationProperties()
    {
        if(localizedProperties == null)
        {
            getSetupProperties();
            if(dataSource != null){
            	localizedProperties = newApplicationProperties(true); 
            }
        }
        return localizedProperties == null ? EmptyApplicationProperties.getInstance() : localizedProperties ;
    }
    
	private ApplicationProperties getApplicationProperties () {
		if( properties == null ){			
			getSetupProperties();		
			this.properties = newApplicationProperties(false); 
		}
		return properties == null ? EmptyApplicationProperties.getInstance() : properties;
	}
	
	private ApplicationProperties getSetupProperties(){
		return getRepository().getSetupApplicationProperties();
	}
	
	private ApplicationProperties newApplicationProperties(boolean localized){		
		DataSource dataSourceToUse = this.dataSource;		
		if(dataSourceToUse == null){
			getSetupProperties();
			dataSourceToUse = DataSourceFactory.getDataSource();
		}		
		// 데이터베이스 설정이 완료되지 않았다면 널을 리턴한다.		
		if(dataSourceToUse != null){			
			log.debug("Loading properties from database.");
			JdbcApplicationProperties impl = new JdbcApplicationProperties(localized);
			impl.setEventPublisher(getEventPublisher());
			impl.setConfiguration(getSqlQueryFactoryBuilder().getConfiguration());		
			impl.setDataSource(dataSourceToUse);
			impl.afterPropertiesSet();
			return impl;			
		}
		return null;
	}

	public SqlQueryFactoryBuilder getSqlQueryFactoryBuilder() {
		return sqlQueryFactoryBuilder;
	}

	public void setSqlQueryFactoryBuilder(SqlQueryFactoryBuilder sqlQueryFactoryBuilder) {
		this.sqlQueryFactoryBuilder = sqlQueryFactoryBuilder;
	}

	public Locale getLocale() {
        if(this.locale == null)
        {
            String language = (String)getApplicationProperties().get(ApplicatioinConstants.LOCALE_LANGUAGE_PROP_NAME);
            if(language == null)
                language = "";
            String country = (String)getApplicationProperties().get(ApplicatioinConstants.LOCALE_COUNTRY_PROP_NAME);
            if(country == null)
                country = "";
            if(language.equals("") && country.equals(""))
                locale = Locale.getDefault();
            else
                locale = new Locale(language, country);
        }
        return locale;
	}

	public void setLocale(Locale newLocale) {
		String country = newLocale.getCountry();
		String language = newLocale.getLanguage();
		setApplicationProperty(ApplicatioinConstants.LOCALE_COUNTRY_PROP_NAME, country);
		setApplicationProperty(ApplicatioinConstants.LOCALE_LANGUAGE_PROP_NAME, language);
		resetL10N();
	}

	public String getCharacterEncoding() {
        if(characterEncoding == null)
        {
            String encoding = getLocalProperty(ApplicatioinConstants.LOCALE_CHARACTER_ENCODING_PROP_NAME);
            if(encoding != null)
                characterEncoding = encoding;
            String charEncoding = getApplicationProperty(ApplicatioinConstants.LOCALE_CHARACTER_ENCODING_PROP_NAME);
            if(charEncoding != null)
                characterEncoding = charEncoding;
            else
            if(characterEncoding == null)
                characterEncoding = ApplicatioinConstants.DEFAULT_CHAR_ENCODING ;
        }
        return characterEncoding;
	}

	public void setCharacterEncoding(String characterEncoding)
			throws UnsupportedEncodingException {
		if(!LocaleUtils.isValidCharacterEncoding(characterEncoding)){
			throw new UnsupportedEncodingException((new StringBuilder()).append("Invalid character encoding: ").append(characterEncoding).toString());			
		}else{
            setApplicationProperty(ApplicatioinConstants.LOCALE_CHARACTER_ENCODING_PROP_NAME, characterEncoding);
            resetL10N();
            return;
		}
	}

	public TimeZone getTimeZone() {
        if(timeZone == null)
            if(properties != null)
            {
                String timeZoneID = (String)properties.get(ApplicatioinConstants.LOCALE_TIMEZONE_PROP_NAME);
                if(timeZoneID == null)
                    timeZone = TimeZone.getDefault();
                else
                    timeZone = TimeZone.getTimeZone(timeZoneID);
            } else
            {
                return TimeZone.getDefault();
            }
        return timeZone;
	}

	public void setTimeZone(TimeZone newTimeZone) {
		String timeZoneId = newTimeZone.getID();
		setApplicationProperty(ApplicatioinConstants.LOCALE_TIMEZONE_PROP_NAME, timeZoneId);
		resetL10N();
	}

	public String formatDate(Date date) {
        if(dateFormat == null){
            if(properties != null)
            {
                dateFormat = FastDateFormat.getDateInstance(2, getTimeZone(), getLocale());
            } else
            {
                FastDateFormat instance = FastDateFormat.getDateInstance(2, getTimeZone(), getLocale());
                return instance.format(date);
            }
        }
        return dateFormat.format(date);
	}

	public String formatDateTime(Date date) {
	    if(dateTimeFormat == null){
            if(properties != null)
            {
                dateTimeFormat = FastDateFormat.getDateTimeInstance(2, 2, getTimeZone(), getLocale());
            } else
            {
                FastDateFormat instance = FastDateFormat.getDateTimeInstance(2, 2, getTimeZone(), getLocale());
                return instance.format(date);
            }
	    }
        return dateTimeFormat.format(date);
	}

	
	public String getLocalProperty(String name) {
		return (String)getSetupProperties().get(name);
	}

	public int getLocalProperty(String name, int defaultValue) {
        String value = getLocalProperty(name);
        if(value != null){
            try
            {
                return Integer.parseInt(value);
            }
            catch(NumberFormatException nfe) { }
        }
        return defaultValue;
	}

	public boolean getLocalProperty(String name, boolean defaultValue) {
        String value = getLocalProperty(name);
        if(value != null){
            try
            {
                return Boolean.parseBoolean(value);
            }
            catch(NumberFormatException nfe) { }
        }
        return defaultValue;
	}
	
	public String getLocalProperty(String name, String defaultValue) {
        String value = getLocalProperty(name);
        if(value != null)
            return value;
        else
            return defaultValue;
	}

	public List<String> getLocalProperties(String parent) {
		List<String> values = new ArrayList<String>();
        Collection<String> propNames = getSetupProperties().getChildrenNames(parent);
        for(String propName : propNames ){
        	String value = getApplicationProperty((new StringBuilder()).append(parent).append(".").append(propName).toString());
            if(value != null)
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
		String value = (String)getApplicationProperties().get(name);
        if(value != null)
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
        for(String propertyName : propertyNames){
        	String value = getApplicationProperty(propertyName);
            if(value != null)
                values.add(value);
        }
        return values;
	}

	public int getApplicationIntProperty(String name, int defaultValue) {
		String value = getApplicationProperty(name);
        if(value != null){
            try
            {
                return Integer.parseInt(value);
            }
            catch(NumberFormatException nfe) { }
        }
        return defaultValue;
	}

	public boolean getApplicationBooleanProperty(String name) {
		return Boolean.valueOf(getApplicationProperty(name)).booleanValue();
	}

	public boolean getApplicationBooleanProperty(String name,
			boolean defaultValue) {
		String value = getApplicationProperty(name);
        if(value != null)
            return Boolean.valueOf(value).booleanValue();
        else
            return defaultValue;
	}

	public void setApplicationProperty(String name, String value) {
		getApplicationProperties().put(name, value);		
	}

	public void setApplicationProperties(Map<String, String> map) {
		getApplicationProperties().putAll(map);
	}

	public void deleteApplicationProperty(String name) {
		getApplicationProperties().remove(name);
	}

	public String getLocalizedApplicationProperty(String name, Locale locale) {
		return (String)getLocalizedApplicationProperties().get((new StringBuilder()).append(name).append(".").append(locale).toString());
	}

	public List<Locale> getLocalizedApplicationPropertyLocales(String name) {
		if( getLocalizedApplicationProperties() instanceof JdbcApplicationProperties )
            return ((JdbcApplicationProperties)getLocalizedApplicationProperties()).getLocalesForProperty(name);
        else
            return Collections.emptyList();
	}

	public void setLocalizedApplicationProperty(String name, String value, Locale locale) {		
		getLocalizedApplicationProperties().put((new StringBuilder()).append(name).append(".").append(locale.toString()).toString(), value);		
	}
	
	public void deleteLocalizedApplicationProperty(String name, Locale locale) {
		getLocalizedApplicationProperties().remove((new StringBuilder()).append(name).append(".").append(locale.toString()).toString());		
	}
}