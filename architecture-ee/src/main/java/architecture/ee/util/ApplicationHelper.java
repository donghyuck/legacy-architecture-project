package architecture.ee.util;

import java.lang.ref.WeakReference;
import java.util.Collections;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

import net.sf.ehcache.CacheManager;

import architecture.common.event.api.EventPublisher;
import architecture.common.exception.ComponentNotFoundException;
import architecture.common.lifecycle.ApplicationHelperFactory;
import architecture.common.lifecycle.ConfigService;
import architecture.common.lifecycle.Repository;
import architecture.common.lifecycle.State;
import architecture.common.lifecycle.service.AdminService;

import architecture.ee.component.admin.AdminHelper;
import architecture.ee.i18n.I18nTextManager;
import architecture.ee.security.role.RoleManager;
import architecture.ee.user.GroupManager;
import architecture.ee.user.UserManager;

/**
 * 컴포넌트들에 대한 인터페이스를 제공하는 Helper 클래스.
 * 
 * @author donghyuck
 *
 */
public final class ApplicationHelper {

	private static final Map<Class<?>, WeakReference<?>> references = Collections.synchronizedMap(new HashMap<Class<?>, WeakReference<?>>()) ;
	
	@SuppressWarnings("unchecked")
	public static <T> T getComponent(Class<T> requiredType) throws ComponentNotFoundException
	{
		if( ApplicationHelper.references.get(requiredType) == null){
			ApplicationHelper.references.put(requiredType, new WeakReference<T>( ApplicationHelperFactory.getApplicationHelper().getComponent(requiredType) ));			
		}
		return (T)ApplicationHelper.references.get(requiredType).get();
	}
		
	public static void autowireComponent(Object obj){
		ApplicationHelperFactory.getApplicationHelper().autowireComponent(obj);
	}
	
	public static ConfigService getConfigService(){
		return AdminHelper.getConfigService();
	}
		
	public static AdminService getAdminService(){
		return AdminHelper.getAdminService();
	}

	public static I18nTextManager getI18nTextManager(){
		return getComponent(I18nTextManager.class);
	}
	
	public static UserManager getUserManager(){
		return getComponent(UserManager.class);
	}

	public static GroupManager getGroupManager(){
		return getComponent(GroupManager.class);
	}
	
	public static RoleManager getRoleManager(){
		return getComponent(RoleManager.class);
	}
	
	public static boolean isSetupComplete(){
		return AdminHelper.isSetupComplete();
	}
	
	public static boolean isReady(){		
		return AdminHelper.isReady();
	}

	public static EventPublisher getEventPublisher(){		
		return AdminHelper.getEventPublisher();
	}
	
	public static State getState(){
		return AdminHelper.getState();
	}

	public static Repository getRepository(){
		return AdminHelper.getRepository();
	}
	
	public static net.sf.ehcache.Cache creatCache(String name, long lifetime){
		CacheManager cacheManager = AdminHelper.getCacheManager();
		if( ! cacheManager.cacheExists(name) ){
			net.sf.ehcache.config.CacheConfiguration config = new net.sf.ehcache.config.CacheConfiguration(name, 0);
			config.setEternal(false);
			config.setOverflowToDisk(false);
			config.setTimeToLiveSeconds(lifetime);
			net.sf.ehcache.Cache cache = new net.sf.ehcache.Cache(config);
			cacheManager.addCache(cache);
		}
		return cacheManager.getCache(name);
	}
	
	public static Locale getLocale(){
		if (isReady()) {
			return getConfigService().getLocale();
		} else {
			 String language = getConfigService().getLocalProperty(ApplicationConstants.LOCALE_LANGUAGE_PROP_NAME);
	            if(language == null)
	                language = "";
	            String country = getConfigService().getLocalProperty(ApplicationConstants.LOCALE_COUNTRY_PROP_NAME);
	            if(country == null)
	                country = "";
	            if(language.equals("") && country.equals(""))
	                return Locale.getDefault();
	            else
	                return new Locale(language, country);            
		}
	}
	
	public static String getCharacterEncoding(){
		if(isReady()){
			return getConfigService().getCharacterEncoding();
		}else{
			return getConfigService().getLocalProperty(
				ApplicationConstants.LOCALE_CHARACTER_ENCODING_PROP_NAME, 
				ApplicationConstants.LOCALE_CHARACTER_ENCODING_PROP_NAME);
		}
	}
	
	public static String getApplicationProperty(String name, String defaultValue){		
		if(isReady()){
			return getConfigService().getApplicationProperty(name, defaultValue);
		}else{ 
			return getConfigService().getLocalProperty(name, defaultValue);
		}
	}
	
	public static int getApplicationIntProperty(String name, int defaultValue){
		if(isReady())
			return getConfigService().getApplicationIntProperty(name, defaultValue);
		else 
			return getConfigService().getLocalProperty(name, defaultValue);
	}
	
	public static boolean getApplicationBooleanProperty(String name, boolean defaultValue){
		if(isReady())
		    return getConfigService().getApplicationBooleanProperty(name, defaultValue);
		else
			return getConfigService().getLocalProperty(name, defaultValue);
	}
	
	public static TimeZone getTimeZone(){
		if(isReady()){
			return getConfigService().getTimeZone();
		}else{
			String timeZoneID = getConfigService().getLocalProperty(ApplicationConstants.LOCALE_TIMEZONE_PROP_NAME);
			if(timeZoneID == null)
				return TimeZone.getDefault();
			else
				return TimeZone.getTimeZone(timeZoneID);
		}
	}
	
	public static String getLocalizedApplicationProperty(String name, Locale locale){
		if(isReady())
			return getConfigService().getLocalizedApplicationProperty(name, locale);
		else 
			return null;
	}
	
	public static String getMessage(String code, Object[] args, Locale locale){		
		return AdminHelper.getMessage(code, args, locale);
	}
	
}