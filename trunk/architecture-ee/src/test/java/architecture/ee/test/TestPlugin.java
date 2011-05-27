package architecture.ee.test;

import org.junit.Test;
import org.springframework.mock.web.MockServletContext;

import architecture.common.lifecycle.ApplicationHelperFactory;
import architecture.common.lifecycle.State;
import architecture.ee.component.AdminService;

public class TestPlugin {


	public void log(Object obj){
		System.out.println("# " + obj);
	}
	
	@Test
	public void testBoot() {		
		MockServletContext servletContext = new MockServletContext();
		servletContext.addInitParameter(
			"contextConfigLocation", 
			"default-application-context.xml,databaseSubsystemContext.xml"
		);
						
		AdminService admin = ApplicationHelperFactory.getApplicationHelper().getComponent(AdminService.class);
		
		if(admin.getState() == State.INITIALIZED){
			admin.setServletContext(servletContext);
			log(admin.getState());
			admin.start();
		}
	}

}
