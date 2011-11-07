package architecture.ee.admin;

import architecture.common.lifecycle.AdminService;
import architecture.common.lifecycle.ConfigRoot;
import architecture.common.lifecycle.ConfigService;
import architecture.common.lifecycle.Repository;
import architecture.common.lifecycle.State;
import architecture.ee.bootstrap.Bootstrap;
import architecture.ee.i18n.I18nTextManager;
import architecture.ee.spring.lifecycle.SpringAdminService;
import architecture.ee.util.ApplicatioinConstants;

public final class AdminHelper {

	public static Repository getRepository(){
		return Bootstrap.getBootstrapComponent(Repository.class);
	}
	
	public static boolean isReady(){		
		return getAdminService().isReady();
	}
	
	public static State getState(){
		return getAdminService().getState();
	}
	
	public static boolean isSetupComplete(){
		if(isReady()){
			return getAdminService().getConfigService().getApplicationBooleanProperty(ApplicatioinConstants.SETUP_COMPLETE_PROP_NAME, false);
		}else{
			return getAdminService().getConfigService().getLocalProperty(ApplicatioinConstants.SETUP_COMPLETE_PROP_NAME, false) ; 
		}
	}
		
	public static String[] getComponentNames(){
		SpringAdminService admin = (SpringAdminService)getAdminService();
		if(isReady()){
			return admin.getApplicationContext().getBeanDefinitionNames();
		}else{ 
		    return Bootstrap.getBootstrapComponentNames();
		}
	}
	
	public static AdminService getAdminService(){
		return Bootstrap.getAdminService();
	}
	
	public static ConfigService getConfigService(){
		return Bootstrap.getConfigService();
	}
	
	public static ConfigRoot getConfigRoot(){
		return getRepository().getConfigRoot();
	}
	
	public static String getEffectiveRootPath(){
		return getRepository().getEffectiveRootPath();
	}
			
	public static I18nTextManager getI18nTextManager(){
		return Bootstrap.getBootstrapComponent(I18nTextManager.class);
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