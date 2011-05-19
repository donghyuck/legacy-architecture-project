package architecture.ee.spring.lifecycle;

import javax.servlet.ServletContext;

import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.web.context.ContextLoader;

import architecture.common.exception.ComponentNotFoundException;
import architecture.common.lifecycle.Application;
import architecture.common.lifecycle.ConfigRoot;

public interface AdminService extends Application {

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
