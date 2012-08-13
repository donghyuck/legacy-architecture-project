package architecture.ee.plugin;

public interface PluginCacheRegistry
{

    public abstract void registerCache(String s, net.sf.ehcache.config.CacheConfiguration config);

    public abstract void unregisterCaches(String s);

    //public abstract com.tangosol.net.DefaultConfigurableCacheFactory.CacheInfo getCacheInfoForName(String s);
}