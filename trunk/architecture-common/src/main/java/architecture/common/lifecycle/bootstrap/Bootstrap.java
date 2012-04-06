package architecture.common.lifecycle.bootstrap;

import javax.servlet.ServletContext;

import org.springframework.context.ConfigurableApplicationContext;

import architecture.common.event.api.EventPublisher;
import architecture.common.lifecycle.ConfigService;
import architecture.common.lifecycle.service.AdminService;
import architecture.common.util.ImplFactory;

/**
 * @author   donghyuck
 */
public class Bootstrap {
	
	public static interface Implementation {
		
		public <T> T getBootstrapComponent(Class<T> requiredType);		

		public ConfigurableApplicationContext getBootstrapApplicationContext();
		
		public void boot(ServletContext servletContext);
		
		public void shutdown(ServletContext servletContext);
		
	}
	
	/**
	 * @uml.property  name="impl"
	 * @uml.associationEnd  
	 */
	private static Implementation impl = null;
    
    static 
    {
        impl = (Implementation) ImplFactory.loadImplFromKey(Bootstrap.Implementation.class);
    }
   	
	public static <T> T getBootstrapComponent(Class<T> requiredType){
		return impl.getBootstrapComponent(requiredType);
	}
	
	public static ConfigurableApplicationContext getBootstrapApplicationContext(){
		// 향후에 프로퍼티에 읽어 올수 있도록 수정.	
		return impl.getBootstrapApplicationContext();
	}
	
	public static String[] getBootstrapComponentNames() {
		return impl.getBootstrapApplicationContext().getBeanDefinitionNames();
	}
	
	public static AdminService getAdminService(){
		return impl.getBootstrapComponent(AdminService.class);
	}

	public static EventPublisher getEventPublisher(){
		return impl.getBootstrapComponent(EventPublisher.class);
	}
	
	public static ConfigService getConfigService(){
		return impl.getBootstrapComponent(ConfigService.class);
	}

	public static final void boot(ServletContext servletContext){	
		impl.boot(servletContext);
	}
		
	
	public static final void shutdown(ServletContext servletContext){
		impl.shutdown(servletContext);
	}
}