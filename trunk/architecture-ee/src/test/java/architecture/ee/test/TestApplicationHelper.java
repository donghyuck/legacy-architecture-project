package architecture.ee.test;

import org.junit.Test;

import architecture.common.lifecycle.ApplicationHelperFactory;
import architecture.common.lifecycle.Server;
import architecture.common.lifecycle.State;
import architecture.ee.bootstrap.Bootstrap;
import architecture.ee.spring.lifecycle.AdminService;

public class TestApplicationHelper {
	
	public void log(Object obj){
		System.out.println("# " + obj);
	}
	
	@Test
	public void testGetBootstrapApplicationContext(){		
		log(Bootstrap.getBootstrapApplicationContext().getDisplayName());
		for(String n : Bootstrap.getBootstrapApplicationContext().getBeanDefinitionNames())
			log(n);		
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
		
		Server app = ApplicationHelperFactory.getApplicationHelper().getServer();
		
		log( app.getHomePath( ) ) ;
		
	}	
}
