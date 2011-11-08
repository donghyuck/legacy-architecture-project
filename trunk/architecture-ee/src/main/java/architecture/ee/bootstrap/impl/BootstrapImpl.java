package architecture.ee.bootstrap.impl;

import java.lang.ref.WeakReference;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletContext;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.access.BeanFactoryReference;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.access.ContextSingletonBeanFactoryLocator;

import architecture.common.lifecycle.AdminService;
import architecture.common.lifecycle.Repository;
import architecture.common.lifecycle.State;
import architecture.ee.spring.lifecycle.SpringAdminService;
import architecture.ee.bootstrap.Bootstrap;

/**
 * @author  donghyuck
 */
public class BootstrapImpl implements Bootstrap.Implementation {

	private Log log = LogFactory.getLog(getClass());
	
	private static final String BOOTSTRAP_CONTEXT_KEY = "default-services-context";

	private Map<Class<?>, WeakReference<?>> references = Collections.synchronizedMap(new HashMap<Class<?>, WeakReference<?>>()) ;
	
	/**
	 * @uml.property  name="repository"
	 * @uml.associationEnd  
	 */
	private RepositoryImpl repository = new RepositoryImpl();
	
	public ConfigurableApplicationContext getBootstrapApplicationContext(){
		BeanFactoryReference parentContextRef = ContextSingletonBeanFactoryLocator.getInstance().useBeanFactory(BOOTSTRAP_CONTEXT_KEY);
		return (ConfigurableApplicationContext) parentContextRef.getFactory();	
	}
		
	
	@SuppressWarnings("unchecked")
	public <T> T getBootstrapComponent(Class<T> requiredType){	
		
		log.debug( "BootstrapComponent requiredType = " + requiredType.getName() );
		
		if( requiredType == Repository.class ){
			
			if( repository.getState() != State.INITIALIZED ){
				try {
					repository.initialize();
				} catch (Throwable e) {}
			}			
			return (T)repository;		
		}
		if( references.get(requiredType) == null){
			/*for ( String n : getBootstrapApplicationContext().getBeanDefinitionNames())
				log.debug(n);*/
			
			references.put(requiredType, new WeakReference<T>( getBootstrapApplicationContext().getBean(requiredType) ));			
		}
		return (T)references.get(requiredType).get();	
	}
	
	public void boot(ServletContext servletContext){			
		if( repository.getState() != State.INITIALIZED ){
			((RepositoryImpl)repository).setServletContext(servletContext);
		}						
		AdminService adminService = getBootstrapComponent(AdminService.class);
		if(adminService instanceof SpringAdminService ){
			((SpringAdminService)adminService).setServletContext(servletContext);
		}
		adminService.start();
	}	
	
	public void shutdown(ServletContext servletContext){
		AdminService adminService = getBootstrapComponent(AdminService.class);
		adminService.stop();
		adminService.destroy();
	}
	
}
