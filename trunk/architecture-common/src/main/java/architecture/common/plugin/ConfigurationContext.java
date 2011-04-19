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
import java.util.List;
import java.util.Map;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;


public class ConfigurationContext
{
    private final List results = Lists.newArrayList();
    private final PluginMetaData pluginMetaData;
    private final Map attributes = Maps.newHashMap();
    
    public ConfigurationContext(PluginMetaData pluginMetaData)
    {
        this.pluginMetaData = pluginMetaData;
    }

    public PluginMetaData getPluginMetaData()
    {
        return pluginMetaData;
    }

    public List getResults()
    {
        return results;
    }

    public void addResult(ConfiguratorResult result)
    {
        results.add(result);
    }

    public Map getAttributes()
    {
        return attributes;
    }

}