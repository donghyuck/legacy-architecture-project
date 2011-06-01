package architecture.ee.plugin.dao;

import java.util.Map;

public interface PluginPropertyDao {

    public abstract Map<String, String> getProperties(String pluginName) ;

    public abstract void insertProperty(String pluginName, String propName, String propValue);

	public abstract void updateProperty(String pluginName, String propName, String propValue);
	
	public abstract void deleteProperty(String pluginName, String propName);
	
}
