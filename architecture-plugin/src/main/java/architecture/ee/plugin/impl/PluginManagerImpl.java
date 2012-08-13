package architecture.ee.plugin.impl;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;

import net.sf.ehcache.Cache;

import org.apache.commons.io.FileUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.Node;

import architecture.common.event.api.EventPublisher;
import architecture.common.event.api.EventSource;
import architecture.common.exception.ConfigurationError;
import architecture.common.exception.ConfigurationWarning;
import architecture.common.license.License;
import architecture.common.lifecycle.service.PluginService;
import architecture.common.task.TaskEngine;
import architecture.ee.event.PluginStateChangeEvent;
import architecture.ee.exception.SystemException;
import architecture.ee.license.FrameworkLicenseManager;
import architecture.ee.plugin.ChainingClassLoader;
import architecture.ee.plugin.ConfigurationContext;
import architecture.ee.plugin.ConfiguratorResult;
import architecture.ee.plugin.DummyPlugin;
import architecture.ee.plugin.Plugin;
import architecture.ee.plugin.PluginClassLoader;
import architecture.ee.plugin.PluginConfigurator;
import architecture.ee.plugin.PluginException;
import architecture.ee.plugin.PluginListener;
import architecture.ee.plugin.PluginManager;
import architecture.ee.plugin.PluginMetaData;
import architecture.ee.plugin.PluginProperties;
import architecture.ee.plugin.PluginRequiresRebuildResult;
import architecture.ee.plugin.PluginResultList;
import architecture.ee.plugin.PluginUtils;
import architecture.ee.plugin.RequireRestartResult;
import architecture.ee.plugin.configurators.PluginCacheConfigurator;
import architecture.ee.plugin.dao.PluginDao;
import architecture.ee.plugin.dao.PluginEntityObject;
import architecture.ee.plugin.dao.PluginPropertyDao;
import architecture.ee.upgrade.UpgradeManager;
import architecture.ee.upgrade.UpgradeTaskException;
import architecture.ee.util.ApplicationHelper;

import com.google.common.collect.Lists;

/**
 * @author donghyuck
 */
public class PluginManagerImpl implements PluginManager, PluginService, EventSource {

	private static final Log log = LogFactory.getLog(PluginManagerImpl.class);

	protected File pluginDirectory;

	protected Map<String, Plugin> plugins;

	protected Map<String, String> brokenPlugins;

	protected Map<String, List<Exception>> brokenPluginUpgradeExceptions;

	protected Map<Object, PluginMetaData> pluginMeta;

	protected Map<Object, PluginClassLoader> classloaders;

	protected Map<Plugin, File> pluginDirs;

	protected Set<String> devPlugins;

	protected Set<PluginListener> pluginListeners;

	protected Map<String, PluginProperties> pluginProperties;

	protected Map<Locale, List<ResourceBundle>> bundleCache;

	protected List customURLMapperList;

	protected AtomicBoolean initialized;

	protected Collection<PluginConfigurator> configurators;

	protected TaskEngine taskEngine;

	protected PluginPropertyDao pluginPropertyDao;

	protected PluginDao pluginDao;

	protected EventPublisher eventDispatcher;

	protected UpgradeManager upgradeManager;
	
	private Cache pluginCache;
	
	protected FrameworkLicenseManager licenseManager;
	
	protected PluginManagerImpl() {
		pluginListeners = new HashSet<PluginListener>();
		pluginProperties = new ConcurrentHashMap<String, PluginProperties>();
		bundleCache = new ConcurrentHashMap<Locale, List<ResourceBundle>>();
		initialized = new AtomicBoolean(false);
		pluginDirectory = ApplicationHelper.getRepository().getFile("plugins");
		plugins = new ConcurrentHashMap<String, Plugin>();
		brokenPlugins = new ConcurrentHashMap<String, String>();
		brokenPluginUpgradeExceptions = new ConcurrentHashMap<String, List<Exception>> ();
		pluginDirs = new ConcurrentHashMap<Plugin, File>();
		classloaders = new ConcurrentHashMap<Object, PluginClassLoader>();
		pluginMeta = new ConcurrentHashMap<Object, PluginMetaData>();
		devPlugins = new HashSet<String>();
		customURLMapperList = Collections.synchronizedList(new ArrayList());
	}
	
	

	public void setLicenseManager(FrameworkLicenseManager licenseManager) {
		this.licenseManager = licenseManager;
	}

	public void setPluginCache(Cache pluginCache) {
		this.pluginCache = pluginCache;
	}

	public void addPluginListener(PluginListener listener){
		pluginListeners.add(listener);
	}

	public void addUpgradeException(String pluginName, UpgradeTaskException exception){
		List exceptionList = (List)brokenPluginUpgradeExceptions.get(pluginName);
		if(exceptionList == null)
        {
            exceptionList = new ArrayList();
            brokenPluginUpgradeExceptions.put(pluginName, exceptionList);
        }
        exceptionList.add(exception);
	}

	protected File createLocalCache(PluginEntityObject bean) throws IOException {
		File pluginDirectory = ApplicationHelper.getRepository().getFile("plugins");
		InputStream data = pluginDao.getPluginData(bean);
		File jarFile = PluginUtils.outputJarFile(bean.getName(), data, pluginDirectory);
		File pluginDir = PluginUtils.extractPlugin(jarFile, pluginDirectory);
		jarFile.delete();
		return pluginDir;
	}

	protected boolean createPluginCacheDirectories(
			List<PluginEntityObject> pluginBeans) {
		try {
			pluginBeans = pluginDao.getPluginEntityObjects();
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return false;
		}
		try {
			for (PluginEntityObject pluginDbBean : pluginBeans) {
				createLocalCache(pluginDbBean);
			}
		} catch (IOException e) {
			log.error(e.getMessage(), e);
			throw new SystemException(e);
		}
		return true;
	}

	protected ClassLoader createPluginChainingClassloader(){
		ClassLoader parent = Thread.currentThread().getContextClassLoader();
        if(parent == null)
        	parent = ApplicationHelper.class.getClassLoader();
        
        if( ApplicationHelper.isSetupComplete() ){
        	File pluginDir = ApplicationHelper.getRepository().getFile("plugins");
            pluginDir.mkdirs();
            File pluginDirs[] = pluginDir.listFiles(new FileFilter() {
                public boolean accept(File pathname)
                {
                    return pathname.isDirectory();
                }
            });
            
            List pluginClassLoaders = Lists.newArrayList();
            for( File f : pluginDirs){
            	PluginClassLoader pcl = getPluginClassloader(f.getName(), f);
            	 pluginClassLoaders.add(pcl.getClassLoader());
            }
            
            if(Boolean.parseBoolean(System.getProperty("jive.devMode", "false"))){
            	List<String> devPluginPaths = PluginUtils.getDevPluginPaths();
            	for(String path : devPluginPaths){
            		File dir = new File(path);
            		if(dir.exists())
                    {
                        PluginClassLoader pcl = getPluginClassloader(dir.getName(), dir);
                        pluginClassLoaders.add(pcl.getClassLoader());
                    }
            	}            	
            }
            ChainingClassLoader classLoader = new ChainingClassLoader(parent, pluginClassLoaders);
            return classLoader;
        }else{
        	return parent;
        }
	}

	public void deleteBrokenPlugin(String pluginName){
		try
        {
            pluginDao.delete(pluginName);
        }
        catch(Exception e) { }
        try
        {
            File f = new File(pluginDirectory, (new StringBuilder()).append(pluginName).append("_broken").toString());
            FileUtils.forceDelete(f);
        }
        catch(Exception e) { }
        brokenPlugins.remove(pluginName);
        brokenPluginUpgradeExceptions.remove(pluginName);
	}

	public void destroy() {

		for (Plugin plugin : plugins.values()) {
			PluginMetaData metaData = getPluginMetaData(plugin);
			ConfigurationContext ctx = new ConfigurationContext(metaData);
			for (PluginConfigurator configurator : configurators) {
				configurator.destroy(ctx);
			}
			try {
				plugin.destroy();
			} catch (Exception e) {
				log.error(e.getMessage(), e);
			}
		}

		plugins.clear();
		brokenPlugins.clear();
		brokenPluginUpgradeExceptions.clear();
		pluginDirs.clear();
		classloaders.clear();
		initialized.set(false);

	}

	private void firePluginCreatedEvent(String name, Plugin plugin)
    {
		 for(PluginListener listener : pluginListeners){
			 listener.pluginCreated(name, plugin);
		 }
    }

	private void firePluginDestroyedEvent(String name, Plugin plugin)
    {
        for(PluginListener listener : pluginListeners){
        	listener.pluginDestroyed(name, plugin);
        }
    }

	public Map<String, String> getBrokenPlugins(){
		return brokenPlugins;
	}

	public Collection<ClassLoader> getClassLoaders() {
		ArrayList<ClassLoader> list = new ArrayList<ClassLoader>();
		for (PluginClassLoader loader : classloaders.values()) {
			list.add(loader.getClassLoader());
		}
		return Collections.unmodifiableCollection(list);
	}

	protected Set<String> getDevPlugins()
    {
        return devPlugins;
    }

	public Plugin getPlugin(String name) {
		return plugins.get(name);
	}

	protected List<PluginEntityObject> getPluginBeans()
    {
        if(!pluginDao.doesPluginTableExist())
            return Collections.emptyList();
        try
        {
            return pluginDao.getPluginEntityObjects();
        }
        catch(Exception e)
        {
            log.warn("Could not initialize plugin dao, this is probably due to an upgrade task having not yet been completed", e);
        }
        return Collections.emptyList();
    }

	public PluginClassLoader getPluginClassloader(Plugin plugin){
		PluginClassLoader pluginclassloader;		 
		PluginMetaData md = getPluginMetaData(plugin);
        if(md != null)
        {
            pluginclassloader = (PluginClassLoader)classloaders.get(md.getName());
        } else
        {
            log.warn((new StringBuilder()).append("No plugin meta data object was found for ").append(plugin).append(" perhaps, the plugin has been uninstalled.").toString());
            pluginclassloader = null;
        }
        return  pluginclassloader ;
	}

	protected PluginClassLoader getPluginClassloader(String pluginName, File pluginDir)
    {
        PluginClassLoader cl = (PluginClassLoader)classloaders.get(pluginName);
        if(cl == null)
        {
            cl = new PluginClassLoader();
            cl.addDirectory(pluginDir);
            cl.initialize();
            classloaders.put(pluginName, cl);
        }
        return cl;
    }

	private int getPluginDatabaseVersion(PluginMetaDataImpl plugin)
    {
        return 0 ; //upgradeManager.getVersionNumber(plugin.getDatabaseKey());
    }
	
	protected File getPluginDirectory()
    {
        return pluginDirectory;
    }
	
	
    protected File getPluginDirectory(Plugin plugin) {
		if (plugin == null)
			return null;
		else
			return (File) pluginDirs.get(plugin);
	}
    
    protected Map getPluginMap()
    {
        return plugins;
    }
    
    public PluginMetaData getPluginMetaData(Plugin plugin) {
		return pluginMeta.get(plugin);
	}
    
    public PluginMetaData getPluginMetaData(String name) {
		return pluginMeta.get(name);
	}
    
    protected synchronized Map<String, String> getPluginProperties(
			String pluginName) {
		PluginProperties props = pluginProperties.get(pluginName);
		if (props == null) {
			props = new PluginProperties(pluginName,
					pluginPropertyDao.getProperties(pluginName),
					pluginPropertyDao);
			pluginProperties.put(pluginName, props);
		}
		return props;
	}
    
    public ResourceBundle getPluginResourceBundle(Plugin plugin, Locale locale){
    	
    	ResourceBundle bundle = null;
    	PluginClassLoader loader = getPluginClassloader(plugin);
        try
        {
            bundle = ResourceBundle.getBundle("plugin_i18n", locale, loader.getClassLoader());
        }
        catch(MissingResourceException e)
        {
        	bundle = null;
        }
        catch(Exception e)
        {
            log.error(e.getMessage(), e);
        }
        return bundle;
    	
    }
    
	public List<ResourceBundle> getPluginResourceBundles(Locale l) {
		List<ResourceBundle> resources = bundleCache.get(l);
		if (resources == null) {
			LinkedList<ResourceBundle> list = new LinkedList<ResourceBundle>();
			for (Plugin p : plugins.values()) {
				try {
					ResourceBundle b = getPluginResourceBundle(p, l);
					if (b != null)
						list.addFirst(b);
				} catch (Exception e) {
					log.error(e.getMessage(), e);
				}
			}
			resources = list;
			bundleCache.put(l, resources);
		}
		return resources;
	}
	
	public Collection<Plugin> getPlugins() {
		return Collections.unmodifiableCollection(plugins.values());
	}
	

	protected PluginDao getSpringUnManagedDao()
    {
    	return null;
    }
	
	public List getUpgradeExceptions(String pluginName){		
		return brokenPluginUpgradeExceptions.get(pluginName);
	}
	
    protected int handleUpgradeTasks(File pluginDir, Plugin p){
    	
    	File upgradeTaskFile = new File(pluginDir, "upgrade.xml");
        if(upgradeTaskFile.exists())
            try
            {
                //return upgradeManager.addUpgradeConfig(new BufferedReader(new FileReader(upgradeTaskFile)));
            	
            }
            catch(Exception e)
            {
                log.error((new StringBuilder()).append("Unable to add upgrade tasks for plugin ").append(p).toString(), e);
            }
        return 0;
    	
    }
    
    /**
     * 데이터베이스에 등록된 플러그인들을 로드한다.
     */
    public void start() {
    	
    	log.debug("PLUGINS ACTIVATED:" + initialized.get());
    	
		if (!initialized.get()) {
			if (!pluginDao.doesPluginTableExist()) {
				log.warn("V2_PLUGIN table does not exist, this is probably do to an upgrade task not being completed yet.");
			} else {
				final List<PluginEntityObject> entities = pluginDao.getPluginEntityObjects();
				taskEngine.submit(new Runnable() {
					public void run() {						
						if (ApplicationHelper.isReady()) {
							for (PluginEntityObject entity : entities) {
								File pluginDir = new File(pluginDirectory, entity.getName());
								try {
									log.debug("ACTIVATING PLUGIN:" + entity.getName() + " (" + pluginDir + ")");
									loadPlugin(pluginDir, entity);
								} catch (PluginException e) {
									PluginManagerImpl.log.error(e.getMessage(),e);
								}
							}
						}

					}
				});
			}
		}
	}
    
    public PluginResultList installPlugin(File file) throws PluginException {
		
		File pluginDir;
		String pluginName;
		PluginEntityObject dbBean;
		PluginMetaDataImpl metaData;
		InputStream in = null;

		try {
			pluginDir = PluginUtils.extractPlugin(file, pluginDirectory);
			pluginName = pluginDir.getName();
			
			if (pluginName == null || pluginName.contains("installtask")) {
				String msg = "Could not determine plugin name";
				brokenPlugins.put(file.getName(), msg);
				log.error(msg);
				throw new PluginException(msg);
			}

			dbBean = new PluginEntityObject();
			dbBean.setName(pluginName);
			
			Document pluginXML = PluginUtils.getPluginConfiguration(pluginDir);

			if (pluginXML == null) {
				String msg = String.format( "Plugin %s does not contain a plugin.xml", new Object[] { pluginName });
				brokenPlugins.put(pluginName, msg);
				log.error(msg);
				throw new PluginException(msg);
			}

			metaData = null;
			Plugin plugin = getPlugin(pluginName);
			
			// 기존에 플러그인이 설치되어 있지 않았다.
			if (plugin == null) {
				plugin = new DummyPlugin(pluginName);
				metaData = new PluginMetaDataImpl(this, pluginXML, pluginDir);
				metaData.setInstalled(true);
				registerPlugin(plugin, pluginDir);
				pluginMeta.put(pluginName, metaData);
				pluginMeta.put(plugin, metaData);
			}
			
			if (pluginDao.getByName(pluginName) != null) {
				log.info(String.format("Plugin %s is already installed, uninstalling then reinstalling.", new Object[] { pluginName }));
				Plugin plugin1 = getPlugin(pluginName);
				uninstallPlugin(plugin1);
				pluginDir = PluginUtils.extractPlugin(file, pluginDirectory);
			}

			try {
				in = new BufferedInputStream(new FileInputStream(file));
				int contentLength = (int) file.length();
				pluginDao.create(dbBean, contentLength, in);
			} finally {
				if (in != null)
					try {
						in.close();
					} catch (IOException e) {
					    log.error(e);
					}
			}

			if(eventDispatcher != null)
			{
				PluginStateChangeEvent event = new PluginStateChangeEvent(pluginName, PluginStateChangeEvent.State.INSTALLED);
				eventDispatcher.publish(event);
			}
			
			PluginResultList pluginresultlist = new PluginResultList(metaData, Lists.newArrayList(new ConfiguratorResult[] {
			    //RequireRestartResult.getRequireRestartResult()
			}));
			
			return pluginresultlist ;
		} catch (Exception e) {
			brokenPlugins.put(file.getName(), (new StringBuilder()).append("Unknown error: ").append(e.getMessage()).toString());
	        log.error(e.getMessage(), e);
	        throw new PluginException(e);
		}
        
	}
    
    public boolean isInitialized() {
		return initialized.get();
	}
    
	public boolean isPluginBroken(String plugin){
		return brokenPlugins.containsKey(plugin);
	}
    

	public boolean isPluginDownloaded(String pluginFilename) {
		return (new File((new StringBuilder()).append(pluginDirectory)
				.append(File.separator).append(pluginFilename).toString()))
				.exists();
	}
	
	protected void isValidVersion(String pluginName, Document pluginXML, File pluginDir) throws PluginException{

		architecture.common.license.License.Version version = licenseManager.getLicense().getVersion();
		
	    Element maxServerVersionElement = (Element)pluginXML.selectSingleNode("/plugin/maxServerVersion");
	    if(maxServerVersionElement != null){
	    	String maxServerVersionString = maxServerVersionElement.getText();
	    	if(maxServerVersionString != null)
	        {
	            maxServerVersionString = maxServerVersionString.trim();
	          License.Version maxServerVersion = License.Version.parseVersion(maxServerVersionString);
	            if(version.compareTo(maxServerVersion) == 1)
	            {
	                String msg = String.format("Plugin %s requires a version less than or equal to %s", new Object[] {
	                    pluginName, maxServerVersionString
	                });
	                log.error(msg);
	                throw new PluginException(msg);
	            }
	        }
	    }
	    
	    Element minServerVersionElement = (Element)pluginXML.selectSingleNode("/plugin/minServerVersion");
	    if(minServerVersionElement != null)
	    {

	        String minServerVersionString = minServerVersionElement.getText();
	        if(minServerVersionString != null)
	        {
	            minServerVersionString = minServerVersionString.trim();
	            architecture.common.license.License.Version minServerVersion = architecture.common.license.License.Version.parseVersion(minServerVersionString);
	            if(version.compareTo(minServerVersion) == -1)
	            {
	                String msg = String.format("Plugins %s requires a version of at least %s", new Object[] {
	                    pluginName, minServerVersionString
	                });
	                log.error(msg);
	                throw new PluginException(msg);
	            }
	        } else
	        {
	            log.warn(String.format("Empty minServerVersion element has no value in plugin %s", new Object[] {
	                pluginName
	            }));
	        }
	    }
	}
	
	public Class loadClass(Plugin plugin, String className) throws ClassNotFoundException, IllegalAccessException,
			InstantiationException {		
		PluginClassLoader loader = (PluginClassLoader)classloaders.get(plugin);
		return loader.loadClass(className);		
	}
	
	protected void loadDevPlugins()
    {
        List<String> paths = PluginUtils.getDevPluginPaths();
        if(!paths.isEmpty())
        {
        	for(String dirString : paths){
        		try
                {
                    File pluginDir = new File(dirString);
                    File dir;
                    if(pluginDir.isAbsolute())
                    {
                        dir = pluginDir;
                    } else
                    {
                        String workingDir = System.getProperty("user.dir");
                        dir = new File(workingDir, dirString);
                    }
                    if(!getDevPlugins().contains(dir.toString()))
                    {
                    	PluginEntityObject bean = new PluginEntityObject();
                        bean.setName(dir.getName());
                        loadPlugin(dir, bean);
                        getDevPlugins().add(dir.toString());
                    }
                }
                catch(Exception e)
                {
                    log.error(e.getMessage(), e);
                }
        	}            
        }
    }
	
	public List loadPlugin(File pluginDir, PluginEntityObject pluginDbBean)
			throws PluginException {
		
		if (!ApplicationHelper.isSetupComplete()) {
			return Collections.emptyList();
		}		

		log.debug((new StringBuilder()).append("Loading action from: ").append(pluginDir.getName()).toString());
		Document pluginXML;
		try {
			pluginXML = PluginUtils.getPluginConfiguration(pluginDir);			
		} catch (DocumentException e) {
			pluginXML = null;
		}
		
		if (pluginXML == null) {
			String msg = (new StringBuilder()).append("Plugin ").append(pluginDir.getName()).append(" could not be loaded: no plugin.xml file found").toString();
			log.error(msg);
			brokenPlugins.put(pluginDir.getName(), "No plugin.xml found.");
			throw new PluginException(msg);
		}

		ArrayList results = Lists.newArrayList();		
		
		String pluginName;
		PluginClassLoader pluginLoader;
				
		Node pluginNameNode = pluginXML.selectSingleNode("/plugin/name");
		
		pluginName = pluginNameNode.getText();
		
		isValidVersion(pluginName, pluginXML, pluginDir);
		
		pluginLoader = getPluginClassloader(pluginName, pluginDir);		
		if (pluginLoader == null) {
			return Collections.emptyList();
		}
		pluginLoader.initialize();
		log.debug( "Plugin classloader urls:" + pluginLoader.getURLS() );
						
		Plugin plugin;
		PluginMetaDataImpl metaData;
		ConfigurationContext context;
		Thread currentThread;
		ClassLoader oldLoader;
				
		Node classNode = pluginXML.selectSingleNode("/plugin/class");
		if (classNode != null) {
			String className = classNode.getText();
			try {
				log.debug("Plugin class:" + className);
				plugin = (Plugin) pluginLoader.loadClass(className).newInstance();
				log.debug("Plugin object:" + plugin);
				log.debug("******************************** ");
			} catch (Throwable e) {
	            brokenPlugins.put(pluginDir.getName(), "Failed to configure class loader.");
	            log.debug(e);
	            throw new PluginException(e);
			}
		} else {
			plugin = new DummyPlugin(pluginName);
		}
		log.debug("===============================1============" );
		metaData = new PluginMetaDataImpl(plugin, pluginLoader, this, pluginXML, pluginDir);
		log.debug("=========================2==================" );
		metaData.setPluginDbBean(pluginDbBean);
		log.debug("=======================3====================" );
		registerPlugin(plugin, pluginDir);
		log.debug("=======================4====================" );
		pluginMeta.put(pluginName, metaData);
		log.debug("======================5=====================" );
		pluginMeta.put(plugin, metaData);
		log.debug("====================6=======================" );
		context = new ConfigurationContext(metaData);
		log.debug("=======================7====================" );
		currentThread = Thread.currentThread();
		oldLoader = currentThread.getContextClassLoader();
		
		log.debug("===========================================" );
		try {
			currentThread.setContextClassLoader(pluginLoader.getClassLoader());
			
			log.debug("Plugin configures:" + configurators.size() );
			
			for (PluginConfigurator configurator : configurators) {
				log.debug("Plugin configure:" + configurator.getClass().getName());
				configurator.configure(context);
			}
		} catch (Exception e) {
			brokenPlugins.put(pluginDir.getName(), "Failed to configure class loader.");
			throw new PluginException(e);
		} finally {
			if (oldLoader != null)
				currentThread.setContextClassLoader(oldLoader);
		}
		
		log.debug("===========================================" );
		
		int pluginDbVersion = getPluginDatabaseVersion(metaData);

		boolean init = true;
		if (pluginDbVersion > 0 && metaData.getDatabaseVersion() != pluginDbVersion) {
			brokenPlugins.put(pluginDir.getName(),
							(new StringBuilder())
									.append("Database version mismatches plugin version. Current: ")
									.append(pluginDbVersion)
									.append(", Required: ")
									.append(metaData.getDatabaseVersion())
									.toString());
			init = false;
		}
		if (init) {
			try {
				plugin.init();
				firePluginCreatedEvent(pluginDir.getName(), plugin);
			} catch (IncompatibleClassChangeError e) {
				log.error((new StringBuilder()).append("Unable to initialize plugin, plugin ").append(pluginName).append(" binds to an old class version needs to be required.").toString());
				results.add(PluginRequiresRebuildResult.getPluginRequiresRebuildResult());
				brokenPlugins.put(pluginDir.getName(), "Failed to initialize.");
			}
			results.addAll(context.getResults());
			ChainingClassLoader.clearCache();
		}
		return results;
	}
	
	public void preInit() throws PluginException {
		
		if( ApplicationHelper.isSetupComplete() ){			
			log.debug("Pre initializing");			
			if(!pluginDao.doesPluginTableExist())
            {
                log.warn("Plugin table does not exist, this is probably do to an upgrade task not being completed yet.");
            } else {            	
            	File deployDir = ApplicationHelper.getRepository().getFile("plugins");            	
            	if(deployDir.exists()){            		
            		File files[] = deployDir.listFiles(new FilenameFilter() {
            			public boolean accept(File dir, String name)
                        {
                            return name.endsWith(".jar");
                        }            			
            		});            		
            		for( File file : files){
            			try{
	            			installPlugin(file);
	            			try
	                        {
	                            log.debug((new StringBuilder()).append("Deleting plugin jar ").append(file.getName()).toString());
	                            FileUtils.forceDelete(file);
	                        }
	                        catch(IOException e)
	                        {
	                            log.error(e.getMessage(), e);
	                        }                        
            			}catch(Exception e)
                        {
            				log.error(e);
                            log.warn((new StringBuilder()).append("Renaming broken plugin ").append(file.getName()).append(" to ").append(file.getName()).append("_broken.").toString());
                        }            			
            			if(!file.renameTo( new File(deployDir, (new StringBuilder()).append(file.getName()).append("_broken").toString()))){
                            log.error((new StringBuilder()).append("Unable to rename plugin ").append(file.getName()).toString());
            			}            			
            		}
            	}            	
            	pluginDirs.clear();
                plugins.clear();
                pluginMeta.clear();
                
                File pluginDirectory = ApplicationHelper.getRepository().getFile("plugins");                try
                {
                    FileUtils.deleteDirectory(pluginDirectory);
                }
                catch(IOException e)
                {	
                    throw new PluginException(e);
                }
                
                pluginDirectory.mkdirs();
                List<PluginEntityObject> pluginBeans = getPluginBeans();
                createPluginCacheDirectories(pluginBeans);
                
                Thread currentThread = Thread.currentThread();
                currentThread.setContextClassLoader(createPluginChainingClassloader());
                
                PluginCacheConfigurator configurator = new PluginCacheConfigurator(pluginCache);
                for(PluginEntityObject bean : pluginBeans ){                	
                	File currentDir = new File(pluginDirectory, bean.getName());
                	configurator.configure(currentDir, bean.getName());
                }                
                List<String> paths = PluginUtils.getDevPluginPaths();                
                for(String path : paths){
                	File dir = new File(path);
                    if(dir.exists())
                        configurator.configure(dir, dir.getName());
                }
            }
		}
	}
	
	public void processSpringConfigs(List<String> springPaths){
		if( ApplicationHelper.isSetupComplete() ){
			pluginDao = getSpringUnManagedDao();
            if(!pluginDao.doesPluginTableExist())
            {
                log.warn("Plugin table does not exist, this is probably do to an upgrade task not being completed yet.");
            } else
            {
                List<PluginEntityObject> pluginBeans = getPluginBeans();
                for(PluginEntityObject bean : pluginBeans ){
                	File currentDir = new File(pluginDirectory, bean.getName());
                    if(currentDir.exists())
                    {
                        File springFile = new File(currentDir, "spring.xml");
                        if(springFile.exists())
                            try
                            {
                                log.info(String.format("Adding spring.xml for plugin %s to the main spring context.", new Object[] {
                                    currentDir.getName()
                                }));
                                springPaths.add(springFile.toURI().toURL().toString());
                            }
                            catch(MalformedURLException e)
                            {
                                log.error(e.getMessage(), e);
                            }
                        else
                        if(log.isDebugEnabled())
                            log.debug(String.format("Plugin %s is restartable but does not have a spring config", new Object[] {
                                currentDir.getName()
                            }));
                    } else
                    {
                        log.warn(String.format("Could not find the local directory for plugin %s. An additional restart will be needed forthe plugin to load correctly", new Object[] {
                            currentDir.getName()
                        }));
                    }
                }
                
                if(Boolean.parseBoolean(System.getProperty("jive.devMode", "false")))
                {
                    if(log.isDebugEnabled())
                        log.debug("Adding dev plugins spring configurations.");
                    List<String> paths = PluginUtils.getDevPluginPaths();
                    for( String path : paths ){
                    	File dir = new File(path);
                        if(dir.exists())
                        {
                            File springConf = new File(dir, "spring.xml");
                            if(springConf.exists())
                                try
                                {
                                    springPaths.add(springConf.toURI().toURL().toString());
                                }
                                catch(MalformedURLException e)
                                {
                                    log.error(e.getMessage(), e);
                                }
                        }
                    }
                    
                }
            }
		}
	}
	
	protected void registerPlugin(Plugin plugin, File pluginDir)
    {
        plugins.put(pluginDir.getName(), plugin);
        pluginDirs.put(plugin, pluginDir);
    }
	
	public void removePluginListener(PluginListener listener){
		pluginListeners.remove(listener);
	}
	
	public void setConfigurators(Collection<PluginConfigurator> configurators) {
		this.configurators = configurators;
	}

    public void setEventPublisher(EventPublisher eventPublisher) {
		this.eventDispatcher = eventPublisher;
	}

    public void setPluginDao(PluginDao pluginDao){
    	this.pluginDao = pluginDao;
    }
    
    public void setPluginPropertyDao(PluginPropertyDao pluginPropertyDao){
    	this.pluginPropertyDao = pluginPropertyDao;
    }
    
    public void setTaskEngine(TaskEngine taskEngine){
    	this.taskEngine = taskEngine;
    }
    
    public void setUpgradeManager(UpgradeManager upgradeManager){
    	this.upgradeManager = upgradeManager;
    }
    
    public PluginResultList uninstallPlugin(Plugin plugin){
		
		PluginResultList pluginresultlist;
		
		PluginMetaDataImpl metaData = (PluginMetaDataImpl)getPluginMetaData(plugin);
        pluginDao.delete(metaData.getName());
        metaData.setUninstalled(true);
        if(eventDispatcher != null)
        {
        	PluginStateChangeEvent event = new PluginStateChangeEvent(metaData.getName(), PluginStateChangeEvent.State.UNINSTALLED);
        	eventDispatcher.publish(event);
        }
        pluginresultlist = new PluginResultList(metaData, Lists.newArrayList(new ConfiguratorResult[] {
            RequireRestartResult.getRequireRestartResult()
        }));
        
        return pluginresultlist;
	}

	public PluginResultList unloadPlugin(String pluginName)
    {
		PluginResultList pluginresultlist;
		bundleCache.clear();
        log.debug((new StringBuilder()).append("Unloading plugin ").append(pluginName).toString());
        Plugin plugin = (Plugin)plugins.get(pluginName);
        if(plugin == null)
        {
            pluginresultlist = new PluginResultList(null, Collections.emptyList());
        } else
        {
            PluginMetaData meta = (PluginMetaData)pluginMeta.get(pluginName);
            PluginClassLoader classLoader = (PluginClassLoader)classloaders.get(plugin);
            if(classLoader != null)
                classLoader.destroy();
            plugins.remove(pluginName);
            pluginMeta.remove(pluginName);
            pluginMeta.remove(plugin);
            pluginDirs.remove(plugin);
            classloaders.remove(pluginName);
            ConfigurationContext context = new ConfigurationContext(meta);
            for( PluginConfigurator configurator : configurators)
            	configurator.destroy(context);
            
            plugin.destroy();
            ChainingClassLoader.clearCache();
            
            firePluginDestroyedEvent(pluginName, plugin);
            
            PluginStateChangeEvent event = new PluginStateChangeEvent(pluginName, PluginStateChangeEvent.State.UNLOADED);
            eventDispatcher.publish(event);            
            
            List results = context.getResults();
            pluginresultlist = new PluginResultList(meta, results);
        }
        return pluginresultlist;
    }

	public void activate() {
		this.start();		
	}

	public void deactivate() {
		this.destroy();		
	}

	public void prepare() throws ConfigurationWarning, ConfigurationError {
		try {
			this.preInit();
		} catch (PluginException e) {
			throw new ConfigurationError(e);
		}		
	}
}