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
	public ContextLoader getContextLoader();
	
	/**
	 * 
	 * @param servletContext
	 */
	public void setServletContext(ServletContext servletContext);
	
	/**
	 * 
	 * @return
	 */
	public ConfigurableApplicationContext getApplicationContext();
	
	/**
	 * 
	 * @param <T>
	 * @param requiredType
	 * @return
	 * @throws ComponentNotFoundException
	 */
	public <T> T getComponent(Class<T> requiredType) throws ComponentNotFoundException;
		
	/**
	 * 
	 */
	public void refresh();
	
	/**
	 * 
	 * @return
	 */
	public boolean isSetContextLoader();
	
	/**
	 * 
	 * @return
	 */
	public boolean isSetServletContext();	
	
	/**
	 * 
	 * @return
	 */
	public boolean isSetApplicationContext();
	
	public abstract String getInstallRootPath();
	
	public abstract ConfigRoot getConfigRoot();
		
}
