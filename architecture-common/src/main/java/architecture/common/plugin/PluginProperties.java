/*
 * Copyright 2010, 2011 INKIUM, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package architecture.common.plugin;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import architecture.common.lifecycle.internal.AbstractApplicationProperties;
import architecture.common.plugin.dao.PluginPropertyDao;

public class PluginProperties extends AbstractApplicationProperties {


    protected final String pluginName;
    private PluginPropertyDao dao;
    protected final Map<String, String> backingMap;
    
    public PluginProperties(String pluginName, Map<String, String> props, PluginPropertyDao dao)
    {
        this.pluginName = pluginName;
        this.dao = dao;
        backingMap = new ConcurrentHashMap<String, String>(props);
    }

    public Collection<String> getChildrenNames(String parentKey)
    {
    	Collection<String> results = new HashSet<String>();
    	String parentKeyDot = (new StringBuilder()).append(parentKey).append(".").toString();
    	for( String key : backingMap.keySet()){
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

    public Collection<String> getPropertyNames()
    {
        return new ArrayList<String>(backingMap.keySet());
    }

    public void clear()
    {
    	throw new UnsupportedOperationException();
    }

    public boolean containsKey(Object key)
    {
        return backingMap.containsKey(key);
    }

    public boolean containsValue(Object value)
    {
        return backingMap.containsKey(value);
    }

    public Set entrySet()
    {
        return backingMap.entrySet();
    }

    public String get(Object key)
    {
        return backingMap.get(key);
    }

    public boolean isEmpty()
    {
        return backingMap.isEmpty();
    }

    public Set keySet()
    {
        return backingMap.keySet();
    }

    public synchronized String put(String key, String value)
    {
        String result;
        result = (String)backingMap.put(key, value);
        if(result == null)
            dao.insertProperty(pluginName, key, value);
        else
            dao.updateProperty(pluginName, key, value);
        
       // CacheFactory.doClusterTask(new PropertyUpdateTask(pluginName, key, value));
        return result;
    }

    public void putAll(Map t)
    {
        java.util.Map.Entry entry;
        for(Iterator iter = t.entrySet().iterator(); iter.hasNext(); put((String)entry.getKey(), (String)entry.getValue()))
            entry = (java.util.Map.Entry)iter.next();
    }

    public synchronized String remove(Object key)
    {
        String result;
        result = (String)backingMap.remove(key);
        if(result != null)
            dao.deleteProperty(pluginName, (String)key);
        
       // CacheFactory.doClusterTask(new PropertyRemoveTask(pluginName, (String)key));
        return result;
    }

    public int size()
    {
        return backingMap.size();
    }

    public Collection<String> values()
    {
        return backingMap.values();
    }

    protected Map<String, String> getBackingMap()
    {
        return backingMap;
    }

}