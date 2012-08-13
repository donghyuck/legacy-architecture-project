package architecture.ee.plugin.configurators;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.ehcache.Cache;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;

import architecture.common.lifecycle.ApplicationConstants;
import architecture.ee.plugin.ConfigurationContext;
import architecture.ee.plugin.PluginConfigurationException;
import architecture.ee.plugin.PluginConfigurator;
import architecture.ee.plugin.PluginMetaData;
/**
 * Plugin 에서 캐쉬를 사용하는 경우 캐쉬 설정을 읽어 들여 
 * 캐쉬 객체를 생성하는 파일.
 * 
 * 
 * @author donghyuck
 *
 */
public class PluginCacheConfigurator implements PluginConfigurator {
	
	   	private static final Log log = LogFactory.getLog(PluginCacheConfigurator.class);

	   	private Cache pluginCache;

	    public PluginCacheConfigurator()
	    {
	    }
	    
	    public PluginCacheConfigurator(Cache pluginCache)
	    {
	    	this.pluginCache = pluginCache;
	    }

	    public void configure(ConfigurationContext context)
	    {
	        PluginMetaData metaData = context.getPluginMetaData();
	        File pluginDir = metaData.getPluginDirectory();
	        String pluginName = metaData.getName();
	        configure(pluginDir, pluginName);
	    }

	    public void configure(File pluginDir, String pluginName)
	    {
	        File cacheConfig = new File(pluginDir, "cache-config.xml");
	        if(cacheConfig.exists())
	        {
	            PluginCacheConfigurator configurator = new PluginCacheConfigurator();
	            try
	            {
	                configurator.configure(((InputStream) (new BufferedInputStream(new FileInputStream(cacheConfig)))), pluginName );
	            }
	            catch(Exception e)
	            {
	                log.error(e);
	            }
	        }
	    }

	    public void destroy(ConfigurationContext context)
	    {
	        PluginMetaData metaData = context.getPluginMetaData();
	        
	        pluginCache.remove(metaData.getName());
	    }

	    public void configure(InputStream configDataStream, String pluginName )
	    {
	        try
	        {
	            SAXReader saxReader = new SAXReader();
	            saxReader.setEncoding(ApplicationConstants.DEFAULT_CHAR_ENCODING);
	            Document cacheXml = saxReader.read(configDataStream);
	            List<Node> mappings = cacheXml.selectNodes("/cache-config/cache-mapping");
	            for(Node mapping : mappings){
	            	registerCache(pluginName, mapping);
	            }
	        }
	        catch(DocumentException e)
	        {
	            log.error(e);
	            throw new PluginConfigurationException(e);
	        }
	    }

	    private void registerCache(String pluginName, Node configData)
	    {
	        String cacheName = configData.selectSingleNode("cache-name").getStringValue();
	        String schemeName = configData.selectSingleNode("scheme-name").getStringValue();
	        if(cacheName == null || schemeName == null)
	        {
	            throw new IllegalArgumentException((new StringBuilder()).append("Both cache-name and scheme-name elements are required. Found cache-name: ").append(cacheName).append(" and scheme-name: ").append(schemeName).toString());
	        } else
	        {
	            Map initParams = readInitParams(configData);
	            //pluginCache.put(new net.sf.ehcache.Element());
	            //com.tangosol.net.DefaultConfigurableCacheFactory.CacheInfo info = new com.tangosol.net.DefaultConfigurableCacheFactory.CacheInfo(cacheName, schemeName, initParams);
	            //registry.registerCache(pluginName, info);
	            return;
	        }
	    }

	    private Map<String, String> readInitParams(Node configData)
	    {
	        Map<String, String> paramMap = new HashMap<String, String>();
	        List<Node> params = configData.selectNodes("init-param");
	        for(Node param : params){
	        	String paramName = param.selectSingleNode("param-name").getStringValue();
	        	String paramValue = param.selectSingleNode("param-value").getStringValue();
	        	paramMap.put(paramName, paramValue);
	        }
	        return paramMap;
	    }
}
