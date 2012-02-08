package architecture.ee.web.struts2.view.freemarker;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Locale;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.ServletActionContext;

import architecture.ee.web.theme.Theme;
import architecture.ee.web.theme.ThemeManager;
import freemarker.template.Configuration;
import freemarker.template.Template;

public class ThemeConfiguration extends Configuration {

	private static final Log log = LogFactory.getLog(ThemeConfiguration.class);
	private ThemeManager themeManager;
	
	protected ThemeConfiguration(ThemeManager themeManager) {
		super();
		this.themeManager = themeManager;
	}	

    public Template getTemplate(String name, Locale locale, String encoding, boolean parse)
        throws IOException
    {
    	//log.debug(" theme template : name = " + name  + ", encoding = " + encoding );
    	
        javax.servlet.http.HttpServletRequest request;
        com.opensymphony.xwork2.ActionContext ac;
        try
        {
            request = ServletActionContext.getRequest();
            ac = ServletActionContext.getActionContext(request);
        }
        catch(Throwable e)
        {
            log.debug("Unable to obtain request or actioncontext in order to determine appropriate themes for request. This is common when rendering Remote Widgets.");
            request = null;
            ac = null;
        }
        
        Template template = null;
        if(request != null && ac != null && name != null && !name.contains(":"))
        {
            List<Theme> themes = themeManager.determineThemes(ac, request);                       
            for(Theme theme : themes){
            	try
                {
            		String path = (new StringBuilder()).append(theme.getName()).append(":").append(name).toString();
                    template = super.getTemplate(path, locale, encoding, parse);
                    if(template != null)
                        break;
                }
                catch(FileNotFoundException e) { }
            }            
        }
        
        if(template == null)
            template = super.getTemplate(name, locale, encoding, parse);
        
        return template;
    }
}
