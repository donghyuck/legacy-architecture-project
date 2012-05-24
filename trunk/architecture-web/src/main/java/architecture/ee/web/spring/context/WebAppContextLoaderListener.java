package architecture.ee.web.spring.context;

import java.util.Locale;

import javax.servlet.ServletContextEvent;

import org.springframework.web.context.ContextLoaderListener;

public class WebAppContextLoaderListener extends ContextLoaderListener
{

    public WebAppContextLoaderListener()
    {
    }

    public boolean canInitialiseContext()
    {
        return true;
    }

    public void contextInitialized(ServletContextEvent servletcontextevent)
    {
        if(canInitialiseContext())
            super.contextInitialized(servletcontextevent);
        
/*        WebApplicationHelper webapplicationhelper = WebApplicationHelperFactory.getWebApplicationHelper();
        webapplicationhelper.setServletContext(servletcontextevent.getServletContext());
        webapplicationhelper.contextReloaded();
        webapplicationhelper.getLog().info("" + webapplicationhelper.getServletContext().getServerInfo());
        webapplicationhelper.getLog().info("Servlet " + webapplicationhelper.getServletContext().getMajorVersion() + "." + webapplicationhelper.getServletContext().getMinorVersion());
        Locale locale = webapplicationhelper.getLocale();
        webapplicationhelper.getLog().info("Locale " + locale.getDisplayName());
        webapplicationhelper.getLog().info("Encoding " + webapplicationhelper.getCharacterEncoding());*/
    }
}

