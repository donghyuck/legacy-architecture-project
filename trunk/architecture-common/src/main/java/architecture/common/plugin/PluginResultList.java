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

import java.util.AbstractList;
import java.util.List;


public class PluginResultList extends AbstractList
{

    protected final List results;
    private final PluginMetaData pluginMetaData;
    
    public PluginResultList(PluginMetaData metaData, List results)
    {
        pluginMetaData = metaData;
        this.results = results;
    }

    public PluginMetaData getPluginMetaData()
    {
        return pluginMetaData;
    }

    public ConfiguratorResult get(int index)
    {
        return (ConfiguratorResult)results.get(index);
    }

    public int size()
    {
        return results.size();
    }

/*    public volatile Object get(int x0)
    {
        return get(x0);
    }*/

}
