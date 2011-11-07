package architecture.common.plugin.impl;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.config.CacheConfiguration;
import net.sf.ehcache.store.MemoryStoreEvictionPolicy;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import architecture.common.plugin.CacheInfo;
import architecture.common.plugin.PluginCacheRegistry;

/**
 * A simple registry of cache configuration data for plugins.
 */
public class PluginCacheRegistryImpl implements PluginCacheRegistry {
		
	private static final Log Log = LogFactory.getLog(PluginCacheRegistryImpl.class);

    //private static final PluginCacheRegistryImpl instance = new PluginCacheRegistryImpl();

    private Map<String, CacheInfo> extraCacheMappings = new HashMap<String, CacheInfo>();
    
    private Map<String, List<CacheInfo>> pluginCaches = new HashMap<String, List<CacheInfo>>();

    private CacheManager cacheManager;
    
    /*public static PluginCacheRegistry getInstance() {
        return instance;
    }*/

    public PluginCacheRegistryImpl(CacheManager manager) {
    	this.cacheManager = manager;
    }

    /**
     * Registers cache configuration data for a give cache and plugin.
     *
     * @param pluginName the name of the plugin which will use the cache.
     * @param info the cache configuration data.
     */
    public void registerCache(String pluginName, CacheInfo info) {
        
    	extraCacheMappings.put(info.getCacheName(), info);
        List<CacheInfo> caches = pluginCaches.get(pluginName);

        if (caches == null) {
            caches = new ArrayList<CacheInfo>();
            pluginCaches.put(pluginName, caches);
        }
        
        caches.add(info);
        //CacheManager manager = CacheManager.create();       
        Cache cache = new Cache(
        	     new CacheConfiguration(info.getCacheName(), getMaxSizeFromProperty(info))
        	       .memoryStoreEvictionPolicy(MemoryStoreEvictionPolicy.LFU)
        	       .overflowToDisk(true)
        	       .eternal(false)
        	       .timeToLiveSeconds(getMaxLifetimeFromProperty(info))
        	       .timeToIdleSeconds(30)
        	       .diskPersistent(false)
        	       .diskExpiryThreadIntervalSeconds(0));
        cacheManager.addCache(cache);        	   
    }

    /**
     * Unregisters all caches for the given plugin.
     *
     * @param pluginName the name of the plugin whose caches will be unregistered.
     */
    public void unregisterCaches(String pluginName) {
        List<CacheInfo> caches = pluginCaches.remove(pluginName);
        if (caches != null) {
            for (CacheInfo info : caches) {
                extraCacheMappings.remove(info.getCacheName());

                // Destroy cache if we are the last node hosting this plugin
                try {
                	cacheManager.removeCache(info.getCacheName());
                }
                catch (Exception e) {
                    Log.warn(e.getMessage(), e);
                }
            }
        }
    }

    public CacheInfo getCacheInfo(String name) {
        return extraCacheMappings.get(name);
    }

    private int getMaxSizeFromProperty(CacheInfo cacheInfo) {
        String sizeProp = cacheInfo.getParams().get("back-size-high");
        if (sizeProp != null) {
            if ("0".equals(sizeProp)) {
                return -1;
            }
            try {
                return Integer.parseInt(sizeProp);
            }
            catch (NumberFormatException nfe) {
                Log.warn("Unable to parse back-size-high for cache: " + cacheInfo.getCacheName());
            }
        }
        // Return default
        return 10000;//DEFAULT_MAX_CACHE_SIZE;
    }

    private static long getMaxLifetimeFromProperty(CacheInfo cacheInfo) {
/*        String lifetimeProp = cacheInfo.getParams().get("back-expiry");
        if (lifetimeProp != null) {
            if ("0".equals(lifetimeProp)) {
                return -1l;
            }
            long factor = 1;
            if (lifetimeProp.endsWith("m")) {
                factor = JiveConstants.MINUTE;
            }
            else if (lifetimeProp.endsWith("h")) {
                factor = JiveConstants.HOUR;
            }
            else if (lifetimeProp.endsWith("d")) {
                factor = JiveConstants.DAY;
            }
            try {
                return Long.parseLong(lifetimeProp.substring(0, lifetimeProp.length()-1)) * factor;
            }
            catch (NumberFormatException nfe) {
                Log.warn("Unable to parse back-expiry for cache: " + cacheInfo.getCacheName());
            }
        }*/
        // Return default
        return 0 ; // CacheFactory.DEFAULT_MAX_CACHE_LIFETIME;
    }

    private long getMinSizeFromProperty(CacheInfo cacheInfo) {
        String sizeProp = cacheInfo.getParams().get("back-size-low");
        if (sizeProp != null) {
            if ("0".equals(sizeProp)) {
                return -1l;
            }
            try {
                return Integer.parseInt(sizeProp);
            }
            catch (NumberFormatException nfe) {
                Log.warn("Unable to parse back-size-low for cache: " + cacheInfo.getCacheName());
            }
        }
        // Return default
        return 0 ;//CacheFactory.DEFAULT_MAX_CACHE_SIZE;
    }
}