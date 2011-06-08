package architecture.ee.bootstrap;

import javax.servlet.ServletContext;

import org.springframework.beans.factory.access.BeanFactoryReference;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.access.ContextSingletonBeanFactoryLocator;

import architecture.ee.component.AdminService;

public class Bootstrap {

	public static final String BOOTSTRAP_CONTEXT_KEY = "default-services-context";
	
	public static final AdminService getAdminService(){
		return getBootstrapApplicationContext().getBean(AdminService.class);
	}
	
	public static final ConfigurableApplicationContext getBootstrapApplicationContext(){
		//default-server-context
		// 향후에 프로퍼티에 읽어 올수 있도록 수정.		
		BeanFactoryReference parentContextRef = ContextSingletonBeanFactoryLocator.getInstance().useBeanFactory(BOOTSTRAP_CONTEXT_KEY);
		return (ConfigurableApplicationContext) parentContextRef.getFactory();
	}

	public static final void boot(ServletContext servletContext){
		//default-server-context
		// 향후에 프로퍼티에 읽어 올수 있도록 수정.		
		getAdminService().getContextLoader().initWebApplicationContext(servletContext);
		//ContextLoader contextLoader = ApplicationHelperFactory.getApplicationHelper().getComponent(ContextLoader.class);
		//contextLoader.initWebApplicationContext(servletContext);
		
	}
	
	public static final void shutdown(ServletContext servletContext){
		getAdminService().stop();
		getAdminService().destroy();
		//AdminService admin = ApplicationHelperFactory.getApplicationHelper().getComponent(AdminService.class);		
		//admin.destroy();
	}
}
