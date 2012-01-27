package architecture.ee.admin;

import java.util.Locale;

import net.sf.ehcache.Cache;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import architecture.common.lifecycle.AdminService;
import architecture.common.lifecycle.ConfigRoot;
import architecture.common.lifecycle.ConfigService;
import architecture.common.lifecycle.Repository;
import architecture.common.lifecycle.State;
import architecture.common.lifecycle.Version;
import architecture.ee.bootstrap.Bootstrap;
import architecture.ee.i18n.I18nTextManager;
import architecture.ee.security.role.RoleManager;
import architecture.ee.spring.lifecycle.SpringAdminService;
import architecture.ee.user.GroupManager;
import architecture.ee.user.UserManager;
import architecture.ee.util.ApplicatioinConstants;

/**
 * 
 * @author donghyuck son
 *
 */
public final class AdminHelper {

	private static final Log log = LogFactory.getLog(AdminHelper.class);

	public static Repository getRepository(){
		return Bootstrap.getBootstrapComponent(Repository.class);
	}
	
	public static boolean isReady(){		
		return getAdminService().isReady();
	}
	
	public static State getState(){
		return getAdminService().getState();
	}
	
	public static Version getVersion(){
		return getAdminService().getVersion();
	}
	
	public static boolean isSetupComplete(){
		if(isReady()){
			return getAdminService().getConfigService().getApplicationBooleanProperty(ApplicatioinConstants.SETUP_COMPLETE_PROP_NAME, false);
		}else{
			return getAdminService().getConfigService().getLocalProperty(ApplicatioinConstants.SETUP_COMPLETE_PROP_NAME, false) ; 
		}
	}
		
	public static String[] getComponentNames(){
		
		SpringAdminService adminService = (SpringAdminService)getAdminService();
		
		if(isReady()){
			return adminService.getApplicationContext().getBeanDefinitionNames();
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
	
	public static UserManager getUserManager(){
		return Bootstrap.getBootstrapComponent(UserManager.class);
	}

	public static GroupManager getGroupManager(){
		return Bootstrap.getBootstrapComponent(GroupManager.class);
	}

	public static RoleManager getRoleManager(){
		return Bootstrap.getBootstrapComponent(RoleManager.class);
	}
	
	public static net.sf.ehcache.Cache getCache(String name){
	    return 	getCache(name, true);
	}
	
	public static net.sf.ehcache.Cache getCache(String name, boolean createNotExist){			
		
		if(!getCacheManager().cacheExists(name) && createNotExist ){
			
			int maxElementsInMemory = 5000;
            boolean overflowToDisk = false;
            boolean eternal = false;
            long timeToLiveSeconds = 60;
            long timeToIdleSeconds = 30;
            boolean diskPersistent = false;
            long diskExpiryThreadIntervalSeconds = 0;
            
			Cache memoryOnlyCache = new Cache(
				name, 
				maxElementsInMemory,
				overflowToDisk,
				eternal,
				timeToLiveSeconds,
				timeToIdleSeconds,
				diskPersistent,
				diskExpiryThreadIntervalSeconds
			);			
			getCacheManager().addCache(memoryOnlyCache);			
		}else{
			return null;
		}		
		return getCacheManager().getCache(name);		
	}	

	
	
	public static String getMessage(String code, Object[] args, Locale locale){	
		
		Locale localeToUse = locale ;
		if(localeToUse==null)
			localeToUse = getConfigService().getLocale();		
		SpringAdminService adminService = (SpringAdminService)getAdminService();
		if(isReady()){
			return adminService.getApplicationContext().getMessage(code, args, localeToUse);
		}else{ 
		    return Bootstrap.getBootstrapApplicationContext().getMessage(code, args, localeToUse);
		}	
		
	}
}