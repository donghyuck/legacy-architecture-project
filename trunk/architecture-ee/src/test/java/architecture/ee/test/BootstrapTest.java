package architecture.ee.test;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Test;

import architecture.common.lifecycle.Repository;
import architecture.ee.bootstrap.Bootstrap;


public class BootstrapTest {

	private Log log = LogFactory.getLog(getClass());
	
	@Test
	public void testGetEffectiveRootPath(){
		Repository repo = Bootstrap.getBootstrapComponent(Repository.class);
		log.info(" - " + repo.getEffectiveRootPath());
		log.info(" - " + repo.getConfigRoot().getRootURI());
	}
}
