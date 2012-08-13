package architecture.ee.plugin;

import java.io.File;
import java.io.FilenameFilter;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


public class PluginClassLoader
{

	private static final Log log = LogFactory.getLog(PluginClassLoader.class);
	
    private URLClassLoader classLoader;

    private final List<URL> list = new ArrayList<URL>();

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
                list.add(classesDir.toURI().toURL());
            File databaseDir = new File(directory, "database");
            if(databaseDir.exists())
                list.add(databaseDir.toURI().toURL());
            File i18nDir = new File(directory, "i18n");
            if(i18nDir.exists())
                list.add(i18nDir.toURI().toURL());
            File libDir = new File(directory, "lib");
            File jars[] = libDir.listFiles(new FilenameFilter() {

                public boolean accept(File dir, String name)
                {
                    return name.endsWith(".jar") || name.endsWith(".zip");
                }
            });
            
            
            
            if(jars != null)
            {
            	for(File jar : jars){
            		if(jar != null && jar.isFile())
            			list.add(jar.toURI().toURL());
            	}
            }
        }
        catch(MalformedURLException e)
        {
            log.error(e);
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
        ClassLoader parent =  null;
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

    public Collection<URL> getURLS()
    {
        return list;
    }

    public void initialize()
    {
        URL urlArray[] = new URL[list.size()];
        list.toArray(urlArray);
        if(classLoader != null)
            classLoader = new URLClassLoader(urlArray, classLoader);
        else
            classLoader = new URLClassLoader(urlArray, findParentClassLoader());
    }

    public Class<?> loadClass(String name)
        throws ClassNotFoundException
    {
    	log.debug( toString () );
    	
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

