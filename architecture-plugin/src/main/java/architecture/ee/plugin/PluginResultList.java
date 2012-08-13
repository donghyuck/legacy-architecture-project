package architecture.ee.plugin;

import java.util.AbstractList;
import java.util.List;


public class PluginResultList extends AbstractList
{

    private final PluginMetaData pluginMetaData;

    protected final List results;

    public PluginResultList(PluginMetaData metaData, List results)
    {
        pluginMetaData = metaData;
        this.results = results;
    }

    public ConfiguratorResult get(int index)
    {
        return (ConfiguratorResult)results.get(index);
    }

    public PluginMetaData getPluginMetaData()
    {
        return pluginMetaData;
    }
    
    public int size()
    {
        return results.size();
    }
    
}

