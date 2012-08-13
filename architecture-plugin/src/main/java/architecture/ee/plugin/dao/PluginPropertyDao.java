package architecture.ee.plugin.dao;

import java.util.Map;

public interface PluginPropertyDao {

	public abstract Map<String, String> getProperties(String name);

	public abstract void insertProperty(String s, String s1, String s2);

	public abstract void updateProperty(String s, String s1, String s2);

	public abstract void deleteProperty(String s, String s1);

}
