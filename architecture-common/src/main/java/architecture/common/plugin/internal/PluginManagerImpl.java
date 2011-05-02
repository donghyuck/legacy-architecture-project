package architecture.common.plugin.internal;

import org.apache.commons.logging.LogFactory;

import architecture.common.plugin.PluginManager;

public class PluginManagerImpl implements PluginManager {

	static final Log log = LogFactory.getLog(PluginManagerImpl.class);
    protected File pluginDirectory;
    protected Map plugins;
    protected Map brokenPlugins;
    protected Map brokenPluginUpgradeExceptions;
    protected Map pluginMeta;
    protected Map classloaders;
    protected Map pluginDirs;
    protected Set devPlugins;
    protected Set pluginListeners;
    protected Map pluginProperties;
    protected Map bundleCache;
    protected List customURLMapperList;
    protected AtomicBoolean initialized;
    
    protected Collection configurators;
    protected TaskEngine taskEngine;

    protected PluginPropertyDAO pluginPropertyDAO;
    protected PluginDAO pluginDAO;
    protected static final PluginManagerImpl instance = new PluginManagerImpl();
    protected EventDispatcher eventDispatcher;
}
