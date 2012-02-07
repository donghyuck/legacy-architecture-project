package architecture.common.lifecycle.service;

import architecture.common.exception.ConfigurationError;
import architecture.common.exception.ConfigurationWarning;

public interface PluginService {
	
	public abstract void prepare() throws ConfigurationWarning, ConfigurationError ;
	
	public abstract void activate();
	
	public abstract void deactivate();
	
}
