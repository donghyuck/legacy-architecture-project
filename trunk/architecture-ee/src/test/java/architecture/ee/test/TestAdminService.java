package architecture.ee.test;

import java.io.UnsupportedEncodingException;

import org.junit.Test;
import org.springframework.mock.web.MockServletContext;

import architecture.common.lifecycle.ApplicationHelperFactory;
import architecture.common.lifecycle.State;
import architecture.ee.component.Admin;
import architecture.ee.component.AdminService;
import architecture.ee.util.AdminHelper;

public class TestAdminService {
	
	public void log(Object obj){
		System.out.println("# " + obj);
	}
	
	@Test
	public void test(){
				
		
	}
	
    //@Test
	public void testGetBootstrapApplicationContext(){		
		
		MockServletContext servletContext = new MockServletContext();
		servletContext.addInitParameter(
			"contextConfigLocation", 
			"classpath:default-application-context.xml, classpath:databaseSubsystemContext.xml, classpath:daoSubsystemContext.xml"
		);
		
		servletContext.addInitParameter("RUNTIME_APPLICATION_HOME", "C:/TOOLS/workspace/architecture_v2/architecture-webapp/target/architecture-webapp/WEB-INF/profile/default");
		
		AdminService admin = AdminHelper.getAdminService();
		if(admin.getState() == State.INITIALIZED){
			admin.setServletContext(servletContext);
			admin.start();
		}	
	}

	//@Test
	public void testGetApplicationState(){		
		
		AdminService admin = AdminHelper.getAdminService();
		log("getState:" + admin.getState());
		log("isReady:" + admin.isReady());
		
	}
	
	
	//@Test
	public void testGetApplicationHome(){		
		
		Admin admin = AdminHelper.getAdmin();
		log( "isReady:" + admin.isReady()  ) ;		
		log( "getInstallRootPath:" + admin.getEffectiveRootPath() ) ;
		log( "getConfigRootPath:" + admin.getConfigRoot().getConfigRootPath()) ;
		log( "getApplicationProperties:" + admin.getApplicationProperties( ) );
	}	
	
	//@Test
	public void testApplicationProperty(){			
		
		Admin admin = AdminHelper.getAdmin();
		log(admin.getLocalProperty("setup.complete"));
	
	}
	
	//@Test
	public void testLocaleAndEncodingProperty(){			
		//LocaleUtils.
		Admin admin = AdminHelper.getAdmin();
		
		try {
			admin.setCharacterEncoding("UTF-8");
		} catch (UnsupportedEncodingException e) {
		}
		
		log( admin.getLocale() );
		log( admin.getTimeZone() );
		log( admin.getCharacterEncoding() );
		
		
	}
	
	//@Test
	public void testStopAdminService(){
		AdminService adminservice = AdminHelper.getAdminService();
		adminservice.stop();
		if(adminservice.isReady()){
			Admin admin = ApplicationHelperFactory.getApplicationHelper().getComponent(Admin.class);
		}
	}
	
	//@Test
	public void testStartAdminService(){
		
		AdminService adminservice = AdminHelper.getAdminService();
		log(adminservice.getState());
		
		adminservice.start();
		
		if(adminservice.isReady()){
			Admin admin = AdminHelper.getAdmin();
			log( admin.getLocale() );
			log( admin.getTimeZone() );
			log( admin.getCharacterEncoding() );
		}
		
	}
}