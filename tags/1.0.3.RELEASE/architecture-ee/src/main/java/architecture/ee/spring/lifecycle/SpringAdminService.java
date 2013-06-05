package architecture.ee.spring.lifecycle;

import javax.servlet.ServletContext;

import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.web.context.ContextLoader;

import architecture.common.lifecycle.service.AdminService;

public interface SpringAdminService extends AdminService {

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
		
}
