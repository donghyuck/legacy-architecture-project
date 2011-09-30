package architecture.ee.spring.lifecycle.internal;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.TimeZone;

import javax.sql.DataSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.core.task.TaskExecutor;

import architecture.common.event.api.EventPublisher;
import architecture.common.event.api.EventSource;
import architecture.common.lifecycle.AdminService;
import architecture.common.lifecycle.ApplicationConstants;
import architecture.common.lifecycle.ApplicationProperties;
import architecture.common.lifecycle.ConfigRoot;
import architecture.common.lifecycle.State;
import architecture.common.lifecycle.internal.EmptyApplicationProperties;
import architecture.ee.component.ApplicationService;
import architecture.ee.component.GlobalizationService;
import architecture.ee.jdbc.query.factory.Configuration;
import architecture.ee.util.ApplicatioinConstants;
import architecture.ee.util.LocaleUtils;

public class ApplicationServiceImpl implements ApplicationService, EventSource {
	
	private Log log = LogFactory.getLog(getClass());	
	
	private AdminServiceImpl adminService = null;	
	private ApplicationProperties properties = null;	
	private ApplicationProperties localizedProperties = null;	
	private DataSource dataSource = null;	
	private EventPublisher eventPublisher = null;	
	private PluginManagerImpl pluginManager = null;
	private TaskExecutor taskExecutor = null;
	private GlobalizationService globalizationService = null;
	
	
    private Locale locale = null;
    private TimeZone timeZone = null;
    private String characterEncoding = null;
    
	public ApplicationServiceImpl(AdminService adminService) {
		
		this.adminService = (AdminServiceImpl)adminService;
		this.eventPublisher = this.adminService.getBootstrapComponent(EventPublisher.class);
		this.pluginManager = this.adminService.getBootstrapComponent(PluginManagerImpl.class);
		this.taskExecutor = this.adminService.getBootstrapComponent(TaskExecutor.class);
	
	}
	
	public boolean isSetDataSource(){
		if(this.dataSource == null )
			return false;
		return true;
	}

	public void setGlobalizationService(GlobalizationService globalizationService) {
		this.globalizationService = globalizationService;
	}

	public boolean isSetTaskExecutor(){
		if(taskExecutor == null)
			return false;
		return true;
	}
	
	public void executeTask(Runnable task){
		taskExecutor.execute(task);
	}


	public State getState() {
		return adminService.getState();
	}
	
	public ConfigRoot getConfigRoot(){
		return adminService.getConfigRoot();
	}
	
	public String getEffectiveRootPath() {
		return "";
	}
	
	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}
	
	public void setEventPublisher(EventPublisher eventPublisher) {
		this.eventPublisher = eventPublisher;
	}

	public void initialize(){
	
		if(isSetDataSource()){
			this.properties = createPropertyMap(dataSource, false);
			this.localizedProperties = createPropertyMap(dataSource, true);
		}
		if( this.pluginManager.isInitialized() ){
			this.pluginManager.setDataSource(dataSource);
			this.pluginManager.initialize();
		}
	}
	
    public Locale getLocale()
    {
        if(locale == null)
        {
            String language = getPropertyMap().get(ApplicatioinConstants.LOCALE_LANGUAGE_PROP_NAME);
            if(language == null)
                language = "";
            String country = getPropertyMap().get(ApplicatioinConstants.LOCALE_COUNTRY_PROP_NAME);
            if(country == null)
                country = "";
            if(language.equals("") && country.equals(""))
                locale = Locale.getDefault();
            else
                locale = new Locale(language, country);
        }
        return locale;
    }

    public void setLocale(Locale newLocale)
    {
        String value = newLocale.getCountry();
        String name = "locale.country";
        setProperty(name, value);
        
        locale = null;
        timeZone = null;
        characterEncoding = null;
    }

    public String getCharacterEncoding()
    {
        if(characterEncoding == null)
        {
            String encoding = getLocalProperty(ApplicatioinConstants.LOCALE_CHARACTER_ENCODING_PROP_NAME);
            if(encoding != null)
                characterEncoding = encoding;
            String charEncoding = getProperty(ApplicatioinConstants.LOCALE_CHARACTER_ENCODING_PROP_NAME);
            if(charEncoding != null)
                characterEncoding = charEncoding;
            else
            if(characterEncoding == null)
                characterEncoding = ApplicationConstants.DEFAULT_CHAR_ENCODING;
        }
        return characterEncoding;
    }

    public void setCharacterEncoding(String characterEncoding)
        throws UnsupportedEncodingException
    {
        if(!LocaleUtils.isValidCharacterEncoding(characterEncoding))
        {
            throw new UnsupportedEncodingException((new StringBuilder()).append("Invalid character encoding: ").append(characterEncoding).toString());
        } else
        {
            String value = characterEncoding;
            String name = "locale.characterEncoding";
            setProperty(name, value);

            locale = null;
            timeZone = null;
            characterEncoding = null;
            
            return;
        }
    }

    public TimeZone getTimeZone()
    {
        if( timeZone == null)
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

    public void setTimeZone(TimeZone newTimeZone)
    {
        String value = newTimeZone.getID();
        String name = ApplicatioinConstants.LOCALE_TIMEZONE_PROP_NAME;
        setProperty(name, value);        
        locale = null;
        timeZone = null;
        characterEncoding = null;
    }
    
    
    
    // propertymap support !!
	
    public String getLocalProperty(String name)
    {
        return (String)getSetupProperties().get(name);
    }
    
    public int getLocalProperty(String name, int defaultValue)
    {
        String value = getLocalProperty(name);
        if(value != null)
            try
            {
                return Integer.parseInt(value);
            }
            catch(NumberFormatException nfe) {}
        return defaultValue;
    }

    public String getLocalProperty(String name, String defaultValue)
    {
        String value = getLocalProperty(name);
        if(value != null)
            return value;
        else
            return defaultValue;
    }

    public List<String> getLocalProperties(String parent)
    {
        Collection<String> propNames = getSetupProperties().getChildrenNames(parent);        
        List<String> values = new ArrayList<String>();
        for( String propName: propNames){
        	String value = getProperty((new StringBuilder()).append(parent).append(".").append(propName).toString());
        	if(value != null)
                values.add(value);        	
        }
        return values;
    }
    
    public void setLocalProperty(String name, String value)
    {
        getSetupProperties().put(name, value);
    }

    public void setLocalProperties(Map<String, String> map)
    {
        getSetupProperties().putAll(map);
    }

    public void deleteLocalProperty(String name)
    {
        try
        {
            getSetupProperties().remove(name);
        }
        catch(Exception e)
        {
            log.error(e);
        }
    }
    

    public String getProperty(String name)
    {
        return (String)getPropertyMap().get(name);
    }
    
    public String getProperty(String name, String defaultValue)
    {
    	getPropertyMap();
        String value = (String)getPropertyMap().get(name);
        if(value != null)
            return value;
        else
            return defaultValue;
    }    

    public List<String> getPropertyNames()
    {
        return new ArrayList<String>(getPropertyMap().getPropertyNames());
    }

    public List<String> getPropertyNames(String parent)
    {
        getPropertyMap();
        return new ArrayList<String>(getPropertyMap().getChildrenNames(parent));
    }

    public List<String> getProperties(String parent)
    {
        getPropertyMap();
        Collection<String> propertyNames = getPropertyMap().getChildrenNames(parent);
                
        List<String> values = new ArrayList<String>();
        for(String propertyName : propertyNames){
        	 String value = getProperty(propertyName);
             if(value != null)
                 values.add(value);
        }
        return values;
    }

    public int getIntProperty(String name, int defaultValue)
    {
        String value = getProperty(name);
        if(value != null)
            try
            {
                return Integer.parseInt(value);
            }
            catch(NumberFormatException nfe) { }
        return defaultValue;
    }

    public boolean getBooleanProperty(String name)
    {
        return Boolean.valueOf(getProperty(name)).booleanValue();
    }

    public boolean getBooleanProperty(String name, boolean defaultValue)
    {
        String value = getProperty(name);
        if(value != null)
            return Boolean.valueOf(value).booleanValue();
        else
            return defaultValue;
    }

    public void setProperty(String name, String value)
    {
        getPropertyMap().put(name, value);
    }

    public void setProperties(Map<String, String> propertyMap)
    {
        getPropertyMap().putAll(propertyMap);
    }

    public void deleteProperty(String name)
    {
        getPropertyMap().remove(name);
    }
    
    public String getLocalizedProperty(String name, Locale locale){
    	return getLocalizedProperties().get((new StringBuilder()).append(name).append(".").append(locale.toString()).toString());
    }

    public List<Locale> getLocalizedPropertyLocales(String name)
    {
        if(  getLocalizedProperties() instanceof JdbcApplicationProperties )
            return ((JdbcApplicationProperties)getLocalizedProperties()).getLocalesForProperty(name);
        else
            return Collections.emptyList();
    }

    public void setLocalizedProperty(String name, String value, Locale locale)
    {
        getLocalizedProperties().put((new StringBuilder()).append(name).append(".").append(locale.toString()).toString(), value);
    }

    public void deleteLocalizedProperty(String name, Locale locale)
    {
    	getLocalizedProperties().remove((new StringBuilder()).append(name).append(".").append(locale.toString()).toString());
    }
    
	private ApplicationProperties getSetupProperties(){
		return null;
	}
	
	public ApplicationProperties getPropertyMap() {		
		if( properties == null && isSetDataSource()){
			getSetupProperties();
			this.properties = createPropertyMap(dataSource, false); 
		}
		return properties == null ? EmptyApplicationProperties.getInstance() : properties;
	}
	    
	private ApplicationProperties getLocalizedProperties()
    {
        if(localizedProperties == null && isSetDataSource())
        {
        	localizedProperties = createPropertyMap(dataSource, true); 	
        }
        return localizedProperties == null ? EmptyApplicationProperties.getInstance() : localizedProperties;
    }
		
	private ApplicationProperties createPropertyMap(DataSource dataSource, boolean localized){		
		JdbcApplicationProperties impl = new JdbcApplicationProperties(localized);
		impl.setEventPublisher(eventPublisher);
		impl.setConfiguration(adminService.getBootstrapComponent(Configuration.class));
		impl.setDataSource(dataSource);
		impl.afterPropertiesSet();
		return impl;
	}

	public boolean isReady() {
		return adminService.isReady();
	}

	public ResourceBundle getResourceBundle(String baseName) {
		return globalizationService.getResourceBundle(baseName, getLocale());
	}


}
