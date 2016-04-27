package architecture.ee.spring.context;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.context.ContextLoaderListener;

import architecture.common.lifecycle.bootstrap.Bootstrap;

public class WebApplicationContextLoaderListener extends ContextLoaderListener {

    private Log log = LogFactory.getLog(getClass());

    public void contextInitialized(ServletContextEvent event) {

	ServletContext useToServletContext = event.getServletContext();
	try {
	    
	    Bootstrap.boot(useToServletContext);
	    
	} catch (Throwable e) {
	    log.error("Bootstrap.boot", e);
	}

    }

    public void contextDestroyed(ServletContextEvent event) {
	try {
	    
	    ServletContext servletContext = event.getServletContext();
	    Bootstrap.shutdown(servletContext);
	    
	} catch (Exception ex) {
	} finally {
	}

    }

}
