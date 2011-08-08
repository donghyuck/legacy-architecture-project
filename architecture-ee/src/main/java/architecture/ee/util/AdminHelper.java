package architecture.ee.util;

import java.lang.ref.WeakReference;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import architecture.common.lifecycle.ConfigRoot;
import architecture.common.lifecycle.State;
import architecture.ee.bootstrap.Bootstrap;
import architecture.ee.component.AdminService;

public class AdminHelper extends ApplicationHelper {

	private static final Map<Class<?>, WeakReference<?>> references = Collections.synchronizedMap(new HashMap<Class<?>, WeakReference<?>>()) ;
	
	public static <T> T getBootstrapComponent(Class<T> requiredType){
		if( AdminHelper.references.get(requiredType) == null){
			AdminHelper.references.put(requiredType, new WeakReference<T>( Bootstrap.getBootstrapApplicationContext().getBean(requiredType) ));			
		}
		return (T)AdminHelper.references.get(requiredType).get();
	}
	
	
	public static AdminService getAdminService() {	
		return getBootstrapComponent(AdminService.class);
	}	
		
	/**
	 * 
	 * @return
	 */
	public static boolean isReady(){
		return getAdminService().isReady();
	}
	
	public static State getState(){
		return getAdminService().getState();
	}
	
	public static boolean isSetupComplete(){
		if(isReady()){
			return getAdmin().getApplicationBooleanProperty(ApplicatioinConstants.SETUP_COMPLETE_PROP_NAME, false);
		}else{
			return getAdminService().getApplicationProperties().getBooleanProperty(ApplicatioinConstants.SETUP_COMPLETE_PROP_NAME, false);
		}
	}	
		
	public static ConfigRoot getConfigRoot(){
		return getAdminService().getConfigRoot();
	}
	
	public static String getEffectiveRootPath(){
		return getAdminService().getEffectiveRootPath();
	}
	
	
	public static net.sf.ehcache.CacheManager getCacheManager(){
		return getBootstrapComponent(net.sf.ehcache.CacheManager.class);
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