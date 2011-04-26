package architecture.ee.lifecycle;

import org.junit.Test;

import architecture.common.lifecycle.Application;
import architecture.common.lifecycle.ApplicationHelperFactory;
import architecture.common.lifecycle.State;
import architecture.ee.bootstrap.Bootstrap;

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
		
		Application app = ApplicationHelperFactory.getApplicationHelper().getApplication();
		
		log(app.getState());
	}
	
	@Test
	public void testApplicationState(){		
		
		Application app = ApplicationHelperFactory.getApplicationHelper().getApplication();
		
		log(app.getState());
		
		//app.start();
		
		log(app.getState());
	}	
	
	@Test
	public void testGetApplicationAfterStart(){		
		
		Application app = ApplicationHelperFactory.getApplicationHelper().getApplication();
		
		log(app.getState());
		
		if(app.getState() == State.STARTED || app.getState() == State.RUNNING ){
			//app.stop();
			//app.destroy();
		}
	}	
	
}
