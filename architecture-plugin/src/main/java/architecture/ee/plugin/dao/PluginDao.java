package architecture.ee.plugin.dao;

import java.io.InputStream;
import java.util.List;

public interface PluginDao {

    public abstract PluginEntityObject create(PluginEntityObject entity);

    public abstract void delete(PluginEntityObject entity);

    public abstract void delete(String name);

    public abstract PluginEntityObject getByName(String name);

    public abstract List<PluginEntityObject> getPluginEntityObjects();

    public abstract void setPluginData(PluginEntityObject entity, int contentLength, InputStream content);

    public abstract InputStream getPluginData(PluginEntityObject entity);

    public abstract PluginEntityObject create(PluginEntityObject entity, int contentLength, InputStream content);

    public abstract boolean doesPluginTableExist();
    
}
