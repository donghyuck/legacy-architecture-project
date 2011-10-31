package architecture.ee.test;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Test;

import architecture.common.lifecycle.Repository;
import architecture.ee.admin.AdminHelper;
import architecture.ee.bootstrap.Bootstrap;
import architecture.ee.jdbc.datasource.DataSourceFactory;


public class BootstrapTest {

	private Log log = LogFactory.getLog(getClass());
	
	@Test
	public void testGetEffectiveRootPath(){
		Repository repo = Bootstrap.getBootstrapComponent(Repository.class);
		log.info(" - " + repo.getEffectiveRootPath());
		log.info(" - " + repo.getConfigRoot().getRootURI());
	}	
	
	@Test
	public void testGetDataSource(){
		log.debug( DataSourceFactory.getDataSource() ) ;
	}
	
	@Test
	public void testGetAdminService(){				
		log.debug( AdminHelper.getAdminService() ) ;
	}
	
	@Test
	public void testGetConfigService(){				
		log.debug( AdminHelper.getConfigService().getApplicationPropertyNames() ) ;
	}
	
}