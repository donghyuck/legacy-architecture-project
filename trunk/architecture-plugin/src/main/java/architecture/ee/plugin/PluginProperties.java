package architecture.ee.plugin;

import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import architecture.common.lifecycle.ApplicationProperties;
import architecture.common.lifecycle.internal.AbstractApplicationProperties;
import architecture.ee.plugin.dao.PluginPropertyDao;

public class PluginProperties extends AbstractApplicationProperties 
implements ApplicationProperties {

    protected final Map<String, String> backingMap;
    
    private PluginPropertyDao dao;
    
    protected final String pluginName;
    
    public PluginProperties(String pluginName, Map<String, String> props, PluginPropertyDao dao)
    {
        this.pluginName = pluginName;
        this.dao = dao;
        backingMap = new ConcurrentHashMap<String, String>(props);
    }
    
	public void clear() {
		throw new UnsupportedOperationException();
	}

	public boolean containsKey(Object key) {
		return backingMap.containsKey(key);
	}

	public boolean containsValue(Object value) {
		return backingMap.containsKey(value);
	}

	public Set<java.util.Map.Entry<String, String>> entrySet() {
		return backingMap.entrySet();
	}

	public String get(Object key) {
		return backingMap.get(key);
	}

	protected Map<String, String> getBackingMap()
    {
        return backingMap;
    }
	
	public Collection<String> getChildrenNames(String parentKey)
    {
		Collection<String> results = new HashSet<String>();
        String parentKeyDot = (new StringBuilder()).append(parentKey).append(".").toString();
        for(String key : backingMap.keySet()){
        	if(key.startsWith(parentKeyDot) && !key.equals(parentKey))
            {
                int dotIndex = key.indexOf(".", parentKey.length() + 1);
                if(dotIndex < 1)
                {
                    if(!results.contains(key))
                        results.add(key);
                } else
                {
                    String name = (new StringBuilder()).append(parentKey).append(key.substring(parentKey.length(), dotIndex)).toString();
                    results.add(name);
                }
            }
        }
        return results;
    }

	public Collection<String> getPropertyNames() {
		return backingMap.keySet();
	}

	public boolean isEmpty() {
		return backingMap.isEmpty();
	}

	public Set<String> keySet() {
		return backingMap.keySet();
	}

	public synchronized String put(String key, String value) {
		
		String result;
        result = backingMap.put(key, value);
        if(result == null)
            dao.insertProperty(pluginName, key, value);
        else
            dao.updateProperty(pluginName, key, value);
        
        return result;
        
	}
    	
    public void putAll(Map m) {
		for( Map.Entry<String, String> entry : (Set<Map.Entry<String, String>>) m.entrySet() ){
			put((String)entry.getKey(), (String)entry.getValue());
		}
		
	}
    
    public synchronized String remove(Object key){
    	
    	String result = (String)backingMap.remove(key);
        if(result != null)
            dao.deleteProperty(pluginName, (String)key);
        return result;
    };
    
    public int size(){
    	return backingMap.size();
    }
    
    public Collection<String> values(){
    	return backingMap.values();
    }

}
