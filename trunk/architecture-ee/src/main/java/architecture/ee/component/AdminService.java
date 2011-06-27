package architecture.ee.component;

import javax.servlet.ServletContext;

import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.web.context.ContextLoader;

import architecture.common.exception.ComponentNotFoundException;
import architecture.common.lifecycle.Application;
import architecture.common.lifecycle.ConfigRoot;

public interface AdminService extends Application {

	public static final String DEFAULT_STARTUP_FILENAME = "startup-config.xml";
	
	/**
	 * 
	 * @return
	 */
	public abstract ContextLoader getContextLoader();
	
	/**
	 * 
	 * @param servletContext
	 */
	public abstract void setServletContext(ServletContext servletContext);
	
	/**
	 * 
	 * @return
	 */
	public abstract ConfigurableApplicationContext getApplicationContext();
	
	/**
	 * 
	 * @param <T>
	 * @param requiredType
	 * @return
	 * @throws ComponentNotFoundException
	 */
	public abstract <T> T getComponent(Class<T> requiredType) throws ComponentNotFoundException;
		
	/**
	 * 
	 */
	public abstract void refresh();
	
	/**
	 * 
	 * @return
	 */
	public abstract boolean isSetContextLoader();
	
	/**
	 * 
	 * @return
	 */
	public abstract boolean isSetServletContext();	
	
	/**
	 * 
	 * @return
	 */
	public abstract boolean isSetApplicationContext();
	
	public abstract String getEffectiveRootPath();
	
	public abstract ConfigRoot getConfigRoot();
		
}
