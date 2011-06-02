package architecture.common.plugin;

public interface PluginManager {
		
	public abstract boolean isInitialized();
	
	/**
	public abstract PluginResultList installPlugin(File file) throws PluginException;

	public abstract Collection getPlugins();

	public abstract Collection getClassLoaders();

	public abstract Plugin getPlugin(String s);

	public abstract PluginMetaData getPluginMetaData(String s);

	public abstract List getPluginResourceBundles(Locale locale);

	public abstract ResourceBundle getPluginResourceBundle(Plugin plugin, Locale locale);

	public abstract void addPluginListener(PluginListener pluginlistener);

	public abstract void removePluginListener(PluginListener pluginlistener);

	public abstract PluginResultList uninstallPlugin(Plugin plugin);

	public abstract PluginMetaData getPluginMetaData(Plugin plugin);

	public abstract PluginClassLoader getPluginClassloader(Plugin plugin);

	public abstract boolean isInitialized();

	public abstract boolean isPluginBroken(String s);

	public abstract Map getBrokenPlugins();

	public abstract void deleteBrokenPlugin(String s);

	public abstract List getUpgradeExceptions(String s);

	public abstract void addUpgradeException(String s, UpgradeTaskException upgradetaskexception);
    **/
}
