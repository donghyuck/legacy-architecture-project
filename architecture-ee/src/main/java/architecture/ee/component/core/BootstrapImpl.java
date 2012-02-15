package architecture.ee.component.core;

import java.lang.ref.WeakReference;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import javax.servlet.ServletContext;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.access.BeanFactoryReference;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.access.ContextSingletonBeanFactoryLocator;

import architecture.common.lifecycle.Repository;
import architecture.common.lifecycle.State;
import architecture.common.lifecycle.bootstrap.Bootstrap;
import architecture.common.lifecycle.service.AdminService;
import architecture.ee.spring.lifecycle.SpringAdminService;

/**
 * @author  donghyuck
 */
public class BootstrapImpl implements Bootstrap.Implementation {

	
	private Log log = LogFactory.getLog(getClass());
	
	private static final String BOOTSTRAP_CONTEXT_KEY = "default-services-context";

	private Map<Class<?>, WeakReference<?>> references = Collections.synchronizedMap(new HashMap<Class<?>, WeakReference<?>>()) ;
	
	private RepositoryImpl repository = new RepositoryImpl();
	
	private final ReentrantLock lock = new ReentrantLock();

	
	public ConfigurableApplicationContext getBootstrapApplicationContext(){
		BeanFactoryReference parentContextRef = ContextSingletonBeanFactoryLocator.getInstance().useBeanFactory(BOOTSTRAP_CONTEXT_KEY);
		return (ConfigurableApplicationContext) parentContextRef.getFactory();	
	}		
	
	@SuppressWarnings("unchecked")
	public <T> T getBootstrapComponent(Class<T> requiredType){
		log.debug( "requiredType:" + requiredType.getName() );
		if( requiredType == Repository.class ){			
			lock.lock();
			try{
			if( repository.getState() != State.INITIALIZED ){				
				repository.initialize();
			}	
			}catch(Exception e){
				
			}finally{
				lock.unlock();
			}
			return (T)repository;
		}
		if( references.get(requiredType) == null){			
			references.put(requiredType, new WeakReference<T>( getBootstrapApplicationContext().getBean(requiredType) ));			
		}
		return (T)references.get(requiredType).get();	
	}
		
	public void boot(ServletContext servletContext){		
		lock.lock();
		try{
			if( repository.getState() != State.INITIALIZED ){
				((RepositoryImpl)repository).setServletContext(servletContext);
			}						
			AdminService adminService = getBootstrapComponent(AdminService.class);
			if(adminService instanceof SpringAdminService ){
				((SpringAdminService)adminService).setServletContext(servletContext);
			}
			adminService.start();
		}finally{
			lock.unlock();
		}		
	}	
		
	public void shutdown(ServletContext servletContext){
		lock.lock();
		try{
			AdminService adminService = getBootstrapComponent(AdminService.class);
			adminService.stop();
			adminService.destroy();
		}finally{
			lock.unlock();
		}
	}
	
}
