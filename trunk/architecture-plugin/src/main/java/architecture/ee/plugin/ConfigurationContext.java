package architecture.ee.plugin;

import java.util.List;
import java.util.Map;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

public class ConfigurationContext
{

    private final Map attributes = Maps.newHashMap();
    private final PluginMetaData pluginMetaData;
    private final List results = Lists.newArrayList();
    
    public ConfigurationContext(PluginMetaData pluginMetaData)
    {
        this.pluginMetaData = pluginMetaData;
    }

    public void addResult(ConfiguratorResult result)
    {
        results.add(result);
    }

    public Map getAttributes()
    {
        return attributes;
    }

    public PluginMetaData getPluginMetaData()
    {
        return pluginMetaData;
    }

    public List getResults()
    {
        return results;
    }

}