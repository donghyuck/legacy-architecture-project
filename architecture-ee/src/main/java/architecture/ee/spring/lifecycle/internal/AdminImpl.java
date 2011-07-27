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
import architecture.common.lifecycle.ApplicationConstants;
import architecture.common.lifecycle.ApplicationProperties;
import architecture.common.lifecycle.ConfigRoot;
import architecture.common.lifecycle.State;
import architecture.common.lifecycle.internal.EmptyApplicationProperties;
import architecture.ee.component.Admin;
import architecture.ee.component.AdminService;
import architecture.ee.component.GlobalizationService;
import architecture.ee.jdbc.query.factory.Configuration;
import architecture.ee.util.ApplicatioinConstants;
import architecture.ee.util.LocaleUtils;

public class AdminImpl implements Admin, EventSource {
	
	private Log log = LogFactory.getLog(getClass());	
	
	private AdminServiceImpl adminService ;	
	private ApplicationProperties properties = null;	
	private ApplicationProperties localizedProperties = null;	
	private DataSource dataSource = null;	
	private EventPublisher eventPublisher = null;	
	private PluginManagerImpl pluginManager;
	private TaskExecutor taskExecutor;
	private GlobalizationService globalizationService;
	
	
    private Locale locale = null;
    private TimeZone timeZone = null;
    private String characterEncoding = null;
    
	public AdminImpl(AdminService adminService) {
		
		this.adminService = (AdminServiceImpl)adminService;
		this.eventPublisher = this.adminService.getBootstrapComponent(EventPublisher.class);
		this.pluginManager = this.adminService.getBootstrapComponent(PluginManagerImpl.class);
		this.taskExecutor = this.adminService.getBootstrapComponent(TaskExecutor.class);
	
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
		return adminService.getEffectiveRootPath();
	}
	
	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}
	
	public void setEventPublisher(EventPublisher eventPublisher) {
		this.eventPublisher = eventPublisher;
	}

	public void initialize(){
		
		this.properties = createApplicationProperties(dataSource, false);
		this.localizedProperties = createApplicationProperties(dataSource, true);
		
		if( this.pluginManager.isInitialized() ){
			this.pluginManager.setDataSource(dataSource);
			this.pluginManager.initialize();
		}
	}
	
    public Locale getLocale()
    {
        if(locale == null)
        {
            String language = getApplicationProperties().get(ApplicatioinConstants.LOCALE_LANGUAGE_PROP_NAME);
            if(language == null)
                language = "";
            String country = getApplicationProperties().get(ApplicatioinConstants.LOCALE_COUNTRY_PROP_NAME);
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
        setApplicationProperty(name, value);
        
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
            String charEncoding = getApplicationProperty(ApplicatioinConstants.LOCALE_CHARACTER_ENCODING_PROP_NAME);
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
            setApplicationProperty(name, value);

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
        setApplicationProperty(name, value);        
        locale = null;
        timeZone = null;
        characterEncoding = null;
    }
	
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
        	String value = getApplicationProperty((new StringBuilder()).append(parent).append(".").append(propName).toString());
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
    

    public String getApplicationProperty(String name)
    {
        return (String)getApplicationProperties().get(name);
    }
    
    public String getApplicationProperty(String name, String defaultValue)
    {
    	getApplicationProperties();
        String value = (String)getApplicationProperties().get(name);
        if(value != null)
            return value;
        else
            return defaultValue;
    }    

    public List<String> getApplicationPropertyNames()
    {
        return new ArrayList<String>(getApplicationProperties().getPropertyNames());
    }

    public List<String> getApplicationPropertyNames(String parent)
    {
        getApplicationProperties();
        return new ArrayList<String>(getApplicationProperties().getChildrenNames(parent));
    }

    public List<String> getApplicationProperties(String parent)
    {
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

    public int getApplicationIntProperty(String name, int defaultValue)
    {
        String value = getApplicationProperty(name);
        if(value != null)
            try
            {
                return Integer.parseInt(value);
            }
            catch(NumberFormatException nfe) { }
        return defaultValue;
    }

    public boolean getApplicationBooleanProperty(String name)
    {
        return Boolean.valueOf(getApplicationProperty(name)).booleanValue();
    }

    public boolean getApplicationBooleanProperty(String name, boolean defaultValue)
    {
        String value = getApplicationProperty(name);
        if(value != null)
            return Boolean.valueOf(value).booleanValue();
        else
            return defaultValue;
    }

    public void setApplicationProperty(String name, String value)
    {
        getApplicationProperties().put(name, value);
    }

    public void setApplicationProperties(Map<String, String> propertyMap)
    {
        getApplicationProperties().putAll(propertyMap);
    }

    public void deleteApplicationProperty(String name)
    {
        getApplicationProperties().remove(name);
    }
    

    public List<Locale> getLocalizedPropertyLocales(String name)
    {
        if(  getLocalizedProperties() instanceof JdbcApplicationPropertiesImpl )
            return ((JdbcApplicationPropertiesImpl)getLocalizedProperties()).getLocalesForProperty(name);
        else
            return Collections.emptyList();
    }

    public void setLocalizedApplicationProperty(String name, String value, Locale locale)
    {
        getLocalizedProperties().put((new StringBuilder()).append(name).append(".").append(locale.toString()).toString(), value);
    }

    public void deleteLocalizedApplicationProperty(String name, Locale locale)
    {
    	getLocalizedProperties().remove((new StringBuilder()).append(name).append(".").append(locale.toString()).toString());
    }
    
	private ApplicationProperties getSetupProperties(){
		return adminService.getApplicationProperties();
	}
	
	public ApplicationProperties getApplicationProperties() {		
		if( properties == null){
			getSetupProperties();
			this.properties = createApplicationProperties(dataSource, false); 
		}
		return properties == null ? EmptyApplicationProperties.getInstance() : properties;
	}
	    
	private ApplicationProperties getLocalizedProperties()
    {
        if(localizedProperties == null)
        {
        	localizedProperties = createApplicationProperties(dataSource, true); 	
        }
        return localizedProperties == null ? EmptyApplicationProperties.getInstance() : localizedProperties;
    }
		
	private ApplicationProperties createApplicationProperties(DataSource dataSource, boolean localized){		
		JdbcApplicationPropertiesImpl impl = new JdbcApplicationPropertiesImpl(localized);
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
