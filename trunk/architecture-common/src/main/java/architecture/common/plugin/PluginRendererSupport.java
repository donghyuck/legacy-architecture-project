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



public class PluginRendererSupport
    //implements RenderFilter
{

    protected final Class pluginRendererClass;
    protected final PluginMetaData pluginMetaData;
    
    public PluginRendererSupport(Class pluginRendererClass, PluginMetaData pluginMetaData)
    {
        this.pluginRendererClass = pluginRendererClass;
        this.pluginMetaData = pluginMetaData;
    }

    public PluginMetaData getPluginMetaData()
    {
        return pluginMetaData;
    }

    public String getName()
    {
        return pluginRendererClass.getName();
    }

    public Class getPluginRendererClass()
    {
        return pluginRendererClass;
    }

   /* public RenderFilter getInstance()
    {
    	ApplicationHelperFactory.getApplicationHelper().getInstance(obj);
        return null;//(RenderFilter)AutowiringBeanFactory.getInstance().buildBean(pluginRendererClass.getName());
    }

    public void execute(Document document, RenderContext renderContext)
    {
        try
        {
            RenderFilter renderFilter = getInstance();
            renderFilter.execute(document, renderContext);
        }
        catch(Exception e)
        {
            //Log.error(e);
        }
    }

    public String[] getUserDocumentation(String language)
    {
        return getInstance().getUserDocumentation(language);
    }

    public boolean isDisplayable()
    {
        return getInstance().isDisplayable();
    }

    public int getOrder()
    {
        return getInstance().getOrder();
    }

    public Map getParameters()
    {
        return getInstance().getParameters();
    }

    public void setParameters(Map parameters)
    {
        getInstance().setParameters(parameters);
    }

    public boolean isEnabled()
    {
        return true;
    }

    public void setEnabled(boolean enabled)
    {
        getInstance().setEnabled(enabled);
    }*/

}

