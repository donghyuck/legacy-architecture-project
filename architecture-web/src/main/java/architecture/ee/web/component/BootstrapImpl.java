package architecture.ee.web.component;

import java.util.concurrent.locks.ReentrantLock;

import javax.servlet.ServletContext;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.ConfigurableApplicationContext;

import architecture.common.lifecycle.Repository;
import architecture.common.lifecycle.State;
import architecture.common.lifecycle.bootstrap.Bootstrap;
import architecture.ee.component.core.RepositoryImpl;

public class BootstrapImpl implements Bootstrap.Implementation {
	
	private Log log = LogFactory.getLog(getClass());
	
	private final ReentrantLock lock = new ReentrantLock();
	
	private RepositoryImpl repository = new RepositoryImpl();
	
	public <T> T getBootstrapComponent(Class<T> requiredType) {
		
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
		
		return null;
	}

	public ConfigurableApplicationContext getBootstrapApplicationContext() {
		return null;
	}

	public void boot(ServletContext servletContext) {
		
	}

	public void shutdown(ServletContext servletContext) {
		
	}


}
