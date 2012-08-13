package architecture.ee.plugin;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Collection;
import java.util.Enumeration;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import architecture.common.cache.Cache;
import architecture.common.cache.EhcacheWrapper;
import architecture.ee.util.ApplicationHelper;

public class ChainingClassLoader extends URLClassLoader {

	
    private static Cache<String, Object> cacheLookupCache = new EhcacheWrapper<String, Object>(ApplicationHelper.creatCache("classLoaderLookupCache", 30000L));
    private static final Log log = LogFactory.getLog(ChainingClassLoader.class);
    private static final URL NULL_URL_ARRAY[] = new URL[0];
    
    public static void clearCache()
    {
        cacheLookupCache.clear();
    }
    
	private final Collection<ClassLoader> loaders;
    
    private final ClassLoader parent;

    public ChainingClassLoader(ClassLoader parent, Collection<ClassLoader> loaders)
    {
        super(NULL_URL_ARRAY, parent);
        this.parent = parent;
        this.loaders = loaders;
    }

    public URL findResource(String string)
    {
        if(parent instanceof URLClassLoader)
            try
            {
                URL resource = ((URLClassLoader)parent).findResource(string);
                if(resource != null)
                    return resource;
            }
            catch(Exception e)
            {
                log.fatal(e.getMessage(), e);
            }
        return super.findResource(string);
    }

    public Enumeration<URL> findResources(String string)
        throws IOException
    {
        if(parent instanceof URLClassLoader)
            try
            {
                Enumeration<URL> resource = ((URLClassLoader)parent).findResources(string);
                if(resource != null)
                    return resource;
            }
            catch(Exception e)
            {
                log.fatal(e.getMessage(), e);
            }
        return super.findResources(string);
    }

    public URL getResource(String string)
    {
        URL url = parent.getResource(string);
        if(url == null)
        {
        	for(ClassLoader l : loaders ){
        		url = l.getResource(string);
        		if(url != null)
        		    break;
        	}            
        }
        return url;
    }

    public InputStream getResourceAsStream(String string)
    {
        InputStream stream = parent.getResourceAsStream(string);
        if(stream == null)
        {
        	for(ClassLoader l : loaders){
        		stream = l.getResourceAsStream(string);
        		if( stream != null )
        			break;
        	}            
        }
        return stream;
    }

    public Enumeration<URL> getResources(String string) throws IOException
    {
        Enumeration<URL> enumeration = parent.getResources(string);
        if(enumeration == null || !enumeration.hasMoreElements())
        {
        	for(ClassLoader l : loaders){
        		enumeration = l.getResources(string);
        		if(enumeration != null && enumeration.hasMoreElements()){
        			break;
        		}
        	}
        }
        return enumeration;
    }

    public URL[] getURLs()
    {
        if(parent instanceof URLClassLoader)
            try
            {
                URL resource[] = ((URLClassLoader)parent).getURLs();
                if(resource != null)
                    return resource;
            }
            catch(Exception e)
            {
                log.fatal(e.getMessage(), e);
            }
        return super.getURLs();
    }

    public ClassLoader getWrappedClassLoader()
    {
        return parent;
    }

    public Class<?> loadClass(String string)
        throws ClassNotFoundException
    {
        if(cacheLookupCache.get(string) != null)
        {
            Object clazz = cacheLookupCache.get(string);
            if(clazz instanceof ClassNotFoundException)
                throw (ClassNotFoundException)clazz;
            else
                return (Class<?>)clazz;
        }
        
        ClassNotFoundException ex = null;
        Class<?> clazz = null;
        try
        {
            clazz = parent.loadClass(string);
        }
        catch(ClassNotFoundException e)
        {
            ex = e;
        }
        
        if(clazz == null)
        {
        	for(ClassLoader l : loaders){
        		try
                {
                    clazz = l.loadClass(string);
                    break;
                }
                catch(ClassNotFoundException e) { }
        	}        	
        }
        if(clazz == null)
        {
            cacheLookupCache.put(string, ex);
            throw ex;
        } else
        {
            cacheLookupCache.put(string, clazz);
            return clazz;
        }
    }
    
}
