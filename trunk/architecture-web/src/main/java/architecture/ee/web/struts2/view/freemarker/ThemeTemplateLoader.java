package architecture.ee.web.struts2.view.freemarker;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.ServletActionContext;

import architecture.ee.util.ApplicationHelper;
import architecture.ee.web.theme.Theme;
import architecture.ee.web.theme.ThemeManager;
import architecture.ee.web.util.ApplicatioinConstants;

import com.opensymphony.xwork2.ActionContext;

import freemarker.cache.StatefulTemplateLoader;
import freemarker.cache.TemplateLoader;

public class ThemeTemplateLoader  implements TemplateLoader, StatefulTemplateLoader {

    private Log log = LogFactory.getLog(ThemeTemplateLoader.class);
    
    private static Map<String, Long> missCache = new HashMap<String, Long>();    

    public static void clearCache(String theme, String template)
    {
        String fullName = (new StringBuilder()).append(theme).append(":").append(template).toString();
        if(missCache.containsKey(fullName))
            missCache.remove(fullName);
    }

    public void resetState()
    {
        missCache.clear();
    }

    public Object findTemplateSource(String name)
    {
        // setup 검색..
		log.debug("finding " + name);
		
        String parts[] = name.split(":");
        if(parts.length != 2)
        {
            List<Theme> themes = getThemeManager().determineThemes(ActionContext.getContext(), ServletActionContext.getRequest());
            
            for(Theme t : themes){
            	Object ts = loadTemplateSource(t.getName(), name);
                if(ts != null)
                    return ts;
            }
            return null;
        } else
        {
            String theme = parts[0];
            if(theme.indexOf("/") > -1)
                theme = theme.substring(theme.lastIndexOf("/"));
            String template = parts[1];
            return loadTemplateSource(theme, template);
        }
    }

    protected Object loadTemplateSource(String theme, String template)
    {
        String cacheKey = (new StringBuilder()).append(theme).append(":").append(template).toString();
        long expires = (long)ApplicationHelper.getApplicationIntProperty(ApplicatioinConstants.THTMES_CACHE_TIMEOUT_PROP_NAME, 10) * 60L * 1000L;
        
        if(missCache.containsKey(cacheKey))
        {
            long timestamp = ((Long)missCache.get(cacheKey)).longValue();
            if(System.currentTimeMillis() - timestamp < expires)
            {
                log.debug((new StringBuilder()).append("The following template has been cached as a missing page: ").append(cacheKey).toString());
                return null;
            }
        }
        
        File file = new File((new StringBuilder()).append(getThemeManager().getThemeHome()).append(File.separator).append(theme).append(File.separator).append(template).toString());
       
        log.debug( file.getAbsoluteFile() + "  "+ file.exists() );
        
        if(file.exists())
        {
            missCache.remove(cacheKey);
            return file;
        } else
        {
            missCache.put(cacheKey, Long.valueOf(System.currentTimeMillis()));
            return null;
        }
    }

    private ThemeManager getThemeManager()
    {
        return ApplicationHelper.getComponent(ThemeManager.class);
    }

    public long getLastModified(Object obj)
    {
        return ((File)obj).lastModified();
    }

    public Reader getReader(Object obj, String encoding)
        throws IOException
    {
        File file = (File)obj;
        return new InputStreamReader(new FileInputStream(file), "UTF-8");
    }

    public void closeTemplateSource(Object obj)
        throws IOException
    {
    }

}
