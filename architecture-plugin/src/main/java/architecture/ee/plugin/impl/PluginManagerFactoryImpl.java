package architecture.ee.plugin.impl;

import architecture.ee.plugin.PluginManager;
import architecture.ee.plugin.PluginManagerFactory;

public class PluginManagerFactoryImpl implements
		PluginManagerFactory.Implementation {

	private PluginManagerImpl instance = new PluginManagerImpl();

	public PluginManager getPluginManager() {
		return instance;
	}

}