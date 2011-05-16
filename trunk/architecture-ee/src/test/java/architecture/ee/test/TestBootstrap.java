package architecture.ee.test;

import org.junit.Test;
import org.springframework.mock.web.MockServletContext;

import architecture.common.lifecycle.Application;
import architecture.common.lifecycle.ApplicationHelperFactory;
import architecture.common.lifecycle.State;
import architecture.ee.spring.lifecycle.AdminService;

public class TestBootstrap {

	public void log(Object obj){
		System.out.println("# " + obj);
	}
	
	@Test
	public void testBoot() {		
		MockServletContext servletContext = new MockServletContext();
		servletContext.addInitParameter(
			"contextConfigLocation", 
			"default-application-context.xml"
		);
						
		AdminService admin = ApplicationHelperFactory.getApplicationHelper().getComponent(AdminService.class);
		
		log("isSetContextLoader:" +admin.isSetContextLoader());
		log("isSetServletContext:" +admin.isSetServletContext());		
		
		if(admin.getState() == State.INITIALIZED){
			admin.setServletContext(servletContext);
			log(admin.getState());
			admin.start();
		}	
	}

	@Test
	public void testShutdown() {	
		AdminService admin = ApplicationHelperFactory.getApplicationHelper().getComponent(AdminService.class);
		if(admin.getState() == State.STARTED){
			admin.stop();
			admin.destroy();
			log(admin.getState());
		}
	}
}
