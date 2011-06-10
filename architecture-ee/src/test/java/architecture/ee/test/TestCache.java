package architecture.ee.test;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;

import org.junit.Test;
import org.springframework.mock.web.MockServletContext;

import architecture.common.lifecycle.ApplicationHelperFactory;
import architecture.common.lifecycle.State;
import architecture.ee.component.AdminService;

public class TestCache {


	public void log(Object obj){
		System.out.println("# " + obj);
	}
	
	@Test
	public void testBoot() {		
		MockServletContext servletContext = new MockServletContext();
		servletContext.addInitParameter(
			"contextConfigLocation", 
			"default-application-context.xml,databaseSubsystemContext.xml,daoSubsystemContext.xml"
		);
			
		servletContext.addInitParameter("RUNTIME_APPLICATION_HOME", "C:/TOOLS/workspace/architecture_v2/architecture-ee/profile/default");
		
		
		AdminService admin = ApplicationHelperFactory.getApplicationHelper().getComponent(AdminService.class);
		if(admin.getState() == State.INITIALIZED){
			admin.setServletContext(servletContext);
			log(admin.getState());
			admin.start();
		}	
	}

	@Test
	public void newCache(){
		CacheManager manager = ApplicationHelperFactory.getApplicationHelper().getComponent(CacheManager.class);
		log(manager.getClusterUUID());
		Cache memoryOnlyCache = new Cache("testCache", 5000, false, false, 5, 2);
		manager.addCache(memoryOnlyCache);
		Cache cache = manager.getCache("testCache");
		cache.put(new Element("a", "aaa"));
	}
	
	@Test
	public void updateCache(){
		CacheManager manager = ApplicationHelperFactory.getApplicationHelper().getComponent(CacheManager.class);
		log(manager.getClusterUUID());
		Cache cache = manager.getCache("testCache");
		log( cache.get("a").getObjectValue() );
		
	}
}
