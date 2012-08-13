package architecture.ee.plugin.configurators;

import java.io.File;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import architecture.common.event.api.EventPublisher;
import architecture.common.event.api.EventSource;

import architecture.ee.plugin.ConfigurationContext;
import architecture.ee.plugin.PluginConfigurationProvider;
import architecture.ee.plugin.PluginConfigurator;
import architecture.ee.plugin.PluginMetaData;

import architecture.ee.web.struts2.event.AddConfigurationProviderEvent;
import architecture.ee.web.struts2.event.RemoveConfigurationProviderEvent;

import com.opensymphony.xwork2.config.ConfigurationProvider;

public class Struts2Configurator implements PluginConfigurator, EventSource {

	protected static final Log log = LogFactory.getLog(PluginConfigurationProvider.class); 
    protected Map<String, ConfigurationProvider> pluginProviders;
    protected EventPublisher eventPublisher;

    public Struts2Configurator()
    {
        pluginProviders = new ConcurrentHashMap<String, ConfigurationProvider>();
    }
    
    public void setEventPublisher(EventPublisher eventPublisher)
    {
        this.eventPublisher = eventPublisher;
    }

    public void configure(ConfigurationContext context)
    {
        PluginMetaData metaData = context.getPluginMetaData();
        if(!pluginProviders.containsKey(metaData.getName()))
        {
            File pluginDir = metaData.getPluginDirectory();
            if((new File(pluginDir, "struts.xml")).exists())
                try
                {
                    PluginConfigurationProvider pluginProvider = new PluginConfigurationProvider(metaData);
                    pluginProviders.put(metaData.getName(), pluginProvider);
                    eventPublisher.publish(new AddConfigurationProviderEvent(this, pluginProvider));
                }
                catch(Throwable e)
                {
                    log.error(e.getMessage(), e);
                }
        } else
        {
            log.warn(String.format("StrutsConfigurator: Plugin %s must be destroyed before being plugin config can be reloaded.", new Object[] {
                metaData.getName()
            }));
        }
    }
    
    public void destroy(ConfigurationContext context)
    {
        PluginMetaData metaData = context.getPluginMetaData();
        PluginConfigurationProvider provider = (PluginConfigurationProvider)pluginProviders.remove(metaData.getName());
        if(provider != null)
        	eventPublisher.publish(new RemoveConfigurationProviderEvent(this, provider));
    }
}
