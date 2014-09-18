package architecture.ee.test;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Test;
import org.springframework.context.ConfigurableApplicationContext;

import architecture.common.jdbc.datasource.DataSourceFactory;
import architecture.common.lifecycle.Repository;
import architecture.common.lifecycle.bootstrap.Bootstrap;
import architecture.ee.component.admin.AdminHelper;

public class BootstrapTest {

	private Log log = LogFactory.getLog(getClass());
	
	
	@Test
	public void testGetEffectiveRootPath() throws Exception{
		Repository repo = Bootstrap.getBootstrapComponent(Repository.class);
		log.info(" -1- " + repo.getEffectiveRootPath());
		log.info(" -1- " + repo.getState());
		log.info(" -2- " + repo.getFile("andang"));
		log.info(" -2- " + repo.getSetupApplicationProperties());
	}	
	

	//@Test
	public void testGetDataSource(){
		log.debug( DataSourceFactory.getDataSource() ) ;
	}
	
	//@Test
	public void testContext(){
		
		ConfigurableApplicationContext context = Bootstrap.getBootstrapApplicationContext();
		
		for( String name : context.getBeanDefinitionNames() ){
			log.debug(name);
		}
	}
	
	
	//@Test
	public void testGetAdminService(){				
		log.debug( AdminHelper.getAdminService() ) ;
	}
		
	//@Test
	public void testIsSetupComplete(){				
		log.debug( "isSetupComplete:" +  AdminHelper.isSetupComplete() ) ;
	}

	//@Test
	public void testGetConfigService(){				
		log.debug( AdminHelper.getConfigService().getApplicationPropertyNames() ) ;
	}	
	
	//@Test
	/*public void testI18nCountryDao(){
		
		I18nCountryDao dao = Bootstrap.getBootstrapComponent(I18nCountryDao.class);		
		int size = dao.allCountryCount();
		
		log.debug( "count:" + size);
    	
		for(I18nCountry country :dao.findAll(0, 50 ) ){
			log.debug( ">>" + country ) ;
		}
		
		for(I18nCountry country :dao.findAll(50, 50 ) ){
			log.debug( ">>>>" + country ) ;
		}
		
	}*/
	
	
	//@Test
	public void testGetI18nMessage(){
		//String key = "main.page.title";
		//log.debug( ApplicationHelper.getMessage(key, new Object[]{}, null));
	}
	
	
}