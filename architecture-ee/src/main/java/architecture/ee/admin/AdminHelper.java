package architecture.ee.admin;

import architecture.common.lifecycle.AdminService;
import architecture.common.lifecycle.ConfigRoot;
import architecture.common.lifecycle.ConfigService;
import architecture.ee.bootstrap.Bootstrap;
import architecture.ee.util.ApplicationHelper;

public final class AdminHelper {

		
	public static boolean isSetupComplete(){
		return ApplicationHelper.isSetupComplete();
	}	
		
	public static AdminService getAdminService(){
		return ApplicationHelper.getAdminService();
	}
	
	public static ConfigService getConfigService(){
		return ApplicationHelper.getConfigService();
	}
	
	public static ConfigRoot getConfigRoot(){
		return ApplicationHelper.getConfigService().getConfigRoot();
	}
	
	public static String getEffectiveRootPath(){
		return ApplicationHelper.getConfigService().getEffectiveRootPath();
	}
			
	public static net.sf.ehcache.CacheManager getCacheManager(){		
		return Bootstrap.getBootstrapComponent(net.sf.ehcache.CacheManager.class);
	}
	
	public static net.sf.ehcache.Cache getCache(String cacheName){		
		net.sf.ehcache.Cache memoryOnlyCache = getCacheManager().getCache(cacheName);		
		if( memoryOnlyCache == null ){
			getCacheManager().addCache(cacheName);
			memoryOnlyCache = getCacheManager().getCache(cacheName);
		}
		return memoryOnlyCache;
	}	


}