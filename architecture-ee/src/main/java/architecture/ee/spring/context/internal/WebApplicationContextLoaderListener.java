package architecture.ee.spring.context.internal;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.ContextLoaderListener;

import architecture.ee.bootstrap.Bootstrap;

public class WebApplicationContextLoaderListener  extends ContextLoaderListener {
	
	private Log log = LogFactory.getLog(getClass());
	

	public void contextInitialized(ServletContextEvent event) {
		
		ServletContext useToServletContext = event.getServletContext();
		/*if(log.isInfoEnabled())
			log.info(MessageFormatter.format("016002"));*/
		
		try {
            Bootstrap.boot(useToServletContext);
		} catch (Throwable e) {
			e.printStackTrace();
			log.error(e);
		}
		
	}

	public void contextDestroyed(ServletContextEvent event)
	{
		
		/*if(log.isInfoEnabled())
			log.info(MessageFormatter.format("016013"));*/
		
		try{
			ServletContext servletContext = event.getServletContext();
			Bootstrap.shutdown(servletContext);
		}catch(Exception ex){
			//log.warn(MessageFormatter.format("016012"), ex);
		}
		super.contextDestroyed(event);
	}
	
    protected ContextLoader createContextLoader()
    {
        return Bootstrap.getBootstrapApplicationContext().getBean(ContextLoader.class);
    }

/*    public ContextLoader getContextLoader()
    {
        return loader;
    }*/
}
