package architecture.ee.test;

import java.io.UnsupportedEncodingException;

import org.junit.Test;
import org.springframework.mock.web.MockServletContext;

import architecture.common.lifecycle.ApplicationHelperFactory;
import architecture.common.lifecycle.Server;
import architecture.common.lifecycle.State;
import architecture.ee.component.Admin;
import architecture.ee.component.AdminService;

public class TestAdminService {
	
	public void log(Object obj){
		System.out.println("# " + obj);
	}
	
	@Test
	public void testGetBootstrapApplicationContext(){		
		
		MockServletContext servletContext = new MockServletContext();
		servletContext.addInitParameter(
			"contextConfigLocation", 
			"default-application-context.xml, databaseSubsystemContext.xml, daoSubsystemContext.xml"
		);
		
		servletContext.addInitParameter("RUNTIME_SERVER_HOME", "C:/TOOLS/workspace/opensource/architecture_v2/architecture-ee/profile/default");
		
		AdminService admin = ApplicationHelperFactory.getApplicationHelper().getComponent(AdminService.class);
		if(admin.getState() == State.INITIALIZED){
			admin.setServletContext(servletContext);
			admin.start();
		}	
	}
	
	@Test
	public void testGetApplicationHelper(){		
		
		ApplicationHelperFactory.getApplicationHelper();
	}

	@Test
	public void testGetApplicationBeforeStart(){		
		
		Server app = ApplicationHelperFactory.getApplicationHelper().getServer();		
		log(app.getState());
	}
	
	@Test
	public void testApplicationState(){		
		
		AdminService admin = ApplicationHelperFactory.getApplicationHelper().getComponent(AdminService.class);
		
		log(admin.getState());
		
		admin.start();
		
		log(admin.getState());
	}	
	
	@Test
	public void testGetApplicationAfterStart(){		
		
		Server app = ApplicationHelperFactory.getApplicationHelper().getServer();
		
		log(app.getState());
		
		if(app.getState() == State.STARTED || app.getState() == State.RUNNING ){
			//app.stop();
			//app.destroy();
		}
	}	
	
	@Test
	public void testGetServerHome(){				
		Server server = ApplicationHelperFactory.getApplicationHelper().getServer();	
		log( server.isReady()  ) ;		
		log( server.getRootURI() ) ;
		log( server.getInstallRootPath() ) ;
		log( server.getConfigRoot().getConfigRootPath()) ;
		server.getApplicationProperties();
	}	
	
	@Test
	public void testApplicationProperty(){			
		
		Admin admin = ApplicationHelperFactory.getApplicationHelper().getComponent(Admin.class);
		log(admin.getLocalProperty("setup.complete"));
	
	}
	
	@Test
	public void testLocaleAndEncodingProperty(){			
		//LocaleUtils.
		Admin admin = ApplicationHelperFactory.getApplicationHelper().getComponent(Admin.class);
		
		try {
			admin.setCharacterEncoding("UTF-8");
		} catch (UnsupportedEncodingException e) {
		}
		
		log( admin.getLocale() );
		log( admin.getTimeZone() );
		log( admin.getCharacterEncoding() );
		
		
	}
	
	@Test
	public void testStopAdminService(){
		AdminService adminservice = ApplicationHelperFactory.getApplicationHelper().getComponent(AdminService.class);
		adminservice.stop();
		if(adminservice.isReady()){
			Admin admin = ApplicationHelperFactory.getApplicationHelper().getComponent(Admin.class);
		}
	}
	
	@Test
	public void testStartAdminService(){
		
		AdminService adminservice = ApplicationHelperFactory.getApplicationHelper().getComponent(AdminService.class);
		log(adminservice.getState());
		
		adminservice.start();
		
		if(adminservice.isReady()){
			Admin admin = ApplicationHelperFactory.getApplicationHelper().getComponent(Admin.class);	
			log( admin.getLocale() );
			log( admin.getTimeZone() );
			log( admin.getCharacterEncoding() );
		}
		
	}
}
