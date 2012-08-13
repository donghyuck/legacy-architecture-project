package architecture.ee.plugin;

import java.io.File;
import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;


public interface PluginManager
{

    public abstract void addPluginListener(PluginListener pluginlistener);

    public abstract void deleteBrokenPlugin(String s);

    public abstract Map getBrokenPlugins();

    public abstract Collection getClassLoaders();

    public abstract Plugin getPlugin(String s);

    public abstract PluginClassLoader getPluginClassloader(Plugin plugin);

    public abstract PluginMetaData getPluginMetaData(Plugin plugin);

    public abstract PluginMetaData getPluginMetaData(String s);

    public abstract ResourceBundle getPluginResourceBundle(Plugin plugin, Locale locale);

    public abstract List getPluginResourceBundles(Locale locale);

    public abstract Collection getPlugins();

    public abstract List getUpgradeExceptions(String s);

    public abstract PluginResultList installPlugin(File file) throws PluginException;

    public abstract boolean isInitialized();

    public abstract boolean isPluginBroken(String s);

    public abstract void removePluginListener(PluginListener pluginlistener);

    public abstract PluginResultList uninstallPlugin(Plugin plugin);

    //public abstract void addUpgradeException(String s, UpgradeTaskException upgradetaskexception);
}

