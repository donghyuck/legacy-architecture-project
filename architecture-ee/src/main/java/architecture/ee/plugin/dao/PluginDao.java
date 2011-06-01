package architecture.ee.plugin.dao;

import java.io.InputStream;
import java.util.List;

public interface PluginDao {

    public abstract PluginBean create(PluginBean pluginbean);

    public abstract void delete(PluginBean pluginbean);

    public abstract void delete(String pluginName);

    public abstract PluginBean getByName(String pluginName);

    public abstract List<PluginBean> getPluginBeans();

    public abstract void setPluginData(PluginBean pluginbean, int contentLength, InputStream inputstream);

    public abstract InputStream getPluginData(PluginBean pluginbean);

    public abstract PluginBean create(PluginBean pluginbean, int contentLength, InputStream inputstream);

    public abstract boolean doesPluginTableExist();
    
}