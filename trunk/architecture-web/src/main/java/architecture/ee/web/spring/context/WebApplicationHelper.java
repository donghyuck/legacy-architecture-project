package architecture.ee.web.spring.context;

import javax.servlet.ServletContext;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationEvent;


public class WebApplicationHelper  {

/*
    private Log log = LogFactory.getLog(WebApplicationHelper.class);
    private ServletContext servletContext;
    private ApplicationContext context;
    private boolean failedLoading;
    private Locale locale;
    private TimeZone timeZone;
    private DateFormat dateFormat;
    private DateFormat dateTimeFormat;
    private DateFormat timeFormat;
    private String characterEncoding;
    
    
    // WeakReference 
    
    protected WebApplicationHelper()
    {
        failedLoading = false;       
        characterEncoding = null;
        log = LogFactory.getLog(architecture.ee.web.context.WebApplicationHelper.class);
        servletContext = null;
        context = null;
        xmlProperties = null;
    }

    protected void setServletContext(ServletContext servletcontext)
    {
        servletContext = servletcontext;
        loadSetupProperties();
        setApplicationContext(WebApplicationContextUtils.getWebApplicationContext(servletcontext));
    }

    public ServletContext getServletContext()
    {
        return servletContext;
    }

    public ApplicationContext getApplicationContext()
    {
        return context;
    }

    protected void setApplicationContext(ApplicationContext applicationcontext)
    {
        context = applicationcontext;
    }

    public Object createComponent(Class class1)
    {
        return context.getAutowireCapableBeanFactory().autowire(class1, 1, false);
    }

    public void autowireComponent(Object obj)
    {
        if(context != null)
            context.getAutowireCapableBeanFactory().autowireBeanProperties(obj, 1, false);
        else
            log.debug("ApplicationContext is null or has not been set. Cannot proceed with autowiring of component: " + obj);
    }

    public Object getComponent(Object obj)
        throws ComponentNotFoundException
    {
        if(context == null)
        {
            log.fatal("Spring Application context has not been set");
            throw new IllegalStateException("Spring Application context has not been set");
        }
        if(obj == null)
        {
            log.error("The component key cannot be null");
            throw new ComponentNotFoundException("The component key cannot be null");
        }
        String as[];
        try
        {
            if(!(obj instanceof Class))
                break MISSING_BLOCK_LABEL_154;
            as = context.getBeanNamesForType((Class)obj);
            if(as == null || as.length == 0)
                throw new ComponentNotFoundException("The container is unable to resolve instance of " + ((Class)obj).getName());
            if(as.length > 1)
            {
                Map map = context.getBeansOfType((Class)obj);
                return new ArrayList(map.values());
            }
        }
        catch(BeansException beansexception)
        {
            throw new ComponentNotFoundException("Failed to find component: " + beansexception.getMessage(), beansexception);
        }
        obj = as[0];
        return context.getBean(obj.toString());
    }

    public synchronized void refresh()
    {
        ContextLoader contextloader = new ContextLoader();
        org.springframework.web.context.WebApplicationContext webapplicationcontext = WebApplicationContextUtils.getWebApplicationContext(servletContext);
        if(webapplicationcontext != null)
            contextloader.closeWebApplicationContext(servletContext);
        contextloader.initWebApplicationContext(servletContext);
        if(context == null)
            setApplicationContext(WebApplicationContextUtils.getWebApplicationContext(servletContext));
        contextReloaded();
    }

    public boolean isSetup()
    {
        return context != null;
    }

    protected void contextReloaded()
    {
        if(context != null)
            context.publishEvent(new ContextLoadedEvent(context));
    }

    public void publishEvent(ApplicationEvent applicationevent)
    {
        context.publishEvent(applicationevent);
    }

    public Log getLog()
    {
        return log;
    }

    private boolean isValidCharacterEncoding(String s)
    {
        boolean flag = true;
        try
        {
            "".getBytes(s);
        }
        catch(Exception exception)
        {
            flag = false;
        }
        return flag;
    }
*/
    
}
