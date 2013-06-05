package architecture.ee.web.struts2.event;

import architecture.common.lifecycle.event.Event;

import com.opensymphony.xwork2.config.ConfigurationProvider;

public class AddConfigurationProviderEvent extends Event {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -5483994383097094160L;
	private final ConfigurationProvider provider;
	
	public AddConfigurationProviderEvent(Object source, ConfigurationProvider provider) {
		super(source);
		this.provider = provider;
	}
	
    public ConfigurationProvider getProvider()
    {
        return provider;
    }

    public int hashCode()
    {
        int prime = 31;
        int result = 1;
        result = prime * result + (provider != null ? provider.hashCode() : 0);
        return result;
    }

    public boolean equals(Object obj)
    {
        if(this == obj)
            return true;
        if(obj == null)
            return false;
        if(!(obj instanceof AddConfigurationProviderEvent))
            return false;
        AddConfigurationProviderEvent other = (AddConfigurationProviderEvent)obj;
        if(provider == null)
        {
            if(other.provider != null)
                return false;
        } else
        if(!provider.equals(other.provider))
            return false;
        return true;
    }

}
