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

import java.io.File;
import java.io.FilenameFilter;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

public class PluginClassLoader 
{

    private URLClassLoader classLoader;

    private final List list = new ArrayList();

    public PluginClassLoader()
        throws SecurityException
    {
    }

    public void addDirectory(File directory)
    {
        try
        {
            File classesDir = new File(directory, "classes");
            if(classesDir.exists())
                list.add(classesDir.toURL());
            File databaseDir = new File(directory, "database");
            if(databaseDir.exists())
                list.add(databaseDir.toURL());
            File i18nDir = new File(directory, "i18n");
            if(i18nDir.exists())
                list.add(i18nDir.toURL());
            File libDir = new File(directory, "lib");
            File jars[] = libDir.listFiles(
            	new FilenameFilter() {            
		            public boolean accept(File dir, String name)
					{
					    return name.endsWith(".jar") || name.endsWith(".zip");
					}});
            
            if(jars != null)
            {
                File arr$[] = jars;
                int len$ = arr$.length;
                for(int i$ = 0; i$ < len$; i$++)
                {
                    File jar = arr$[i$];
                    if(jar != null && jar.isFile())
                        list.add(jar.toURL());
                }

            }
        }
        catch(MalformedURLException mue)
        {
            //Log.error(mue);
        }
    }

    public void addURL(URL url)
    {
        list.add(url);
    }

    public void destroy()
    {
        classLoader = null;
    }

    private ClassLoader findParentClassLoader()
    {
        ClassLoader parent = null; //com/jivesoftware/community/lifecycle/JiveApplication.getClassLoader();
        if(parent == null)
            parent = getClass().getClassLoader();
        if(parent == null)
            parent = ClassLoader.getSystemClassLoader();
        return parent;
    }

    public ClassLoader getClassLoader()
    {
        return classLoader;
    }

    public Collection getURLS()
    {
        return list;
    }

    public void initialize()
    {
        Iterator urls = list.iterator();
        URL urlArray[] = new URL[list.size()];
        for(int i = 0; urls.hasNext(); i++)
            urlArray[i] = (URL)urls.next();

        if(classLoader != null)
            classLoader = new URLClassLoader(urlArray, classLoader);
        else
            classLoader = new URLClassLoader(urlArray, findParentClassLoader());
    }

    public Class loadClass(String name)
        throws ClassNotFoundException
    {
        return classLoader.loadClass(name);
    }
    
    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        sb.append("PluginClassLoader");
        sb.append("{classLoader=").append(classLoader);
        sb.append(", list=").append(list);
        sb.append('}');
        return sb.toString();
    }
}

