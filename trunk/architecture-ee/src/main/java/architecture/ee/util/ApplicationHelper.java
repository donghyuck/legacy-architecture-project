package architecture.ee.util;

import java.lang.ref.WeakReference;
import java.util.Collections;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

import architecture.common.exception.ComponentNotFoundException;
import architecture.common.lifecycle.AdminService;
import architecture.common.lifecycle.ApplicationHelperFactory;
import architecture.common.lifecycle.ConfigService;
import architecture.common.lifecycle.State;
import architecture.ee.bootstrap.Bootstrap;

public class ApplicationHelper {

	private static final Map<Class<?>, WeakReference<?>> references = Collections.synchronizedMap(new HashMap<Class<?>, WeakReference<?>>()) ;
	
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
		return Bootstrap.getConfigService();
	}
		
	public static AdminService getAdminService(){
		return Bootstrap.getAdminService();
	}
	
	public static boolean isSetupComplete(){
		if(isReady()){
			return getAdminService().getConfigService().getApplicationBooleanProperty(ApplicatioinConstants.SETUP_COMPLETE_PROP_NAME, false);
		}else{
			return getAdminService().getConfigService().getLocalProperty(ApplicatioinConstants.SETUP_COMPLETE_PROP_NAME, false) ; 
		}
	}
	
	public static boolean isReady(){
		
		return getAdminService().isReady();
	}
	
	public static State getState(){
		return getAdminService().getState();
	}
	
	public static Locale getLocale(){
		if (isReady()) {
			return getAdminService().getConfigService().getLocale();
		} else {
			 String language = getAdminService().getConfigService().getLocalProperty(ApplicatioinConstants.LOCALE_LANGUAGE_PROP_NAME);
	            if(language == null)
	                language = "";
	            String country = getAdminService().getConfigService().getLocalProperty(ApplicatioinConstants.LOCALE_COUNTRY_PROP_NAME);
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
			return getAdminService().getConfigService().getCharacterEncoding();
		}else{
			return getAdminService().getConfigService().getLocalProperty(
				ApplicatioinConstants.LOCALE_CHARACTER_ENCODING_PROP_NAME, 
				ApplicatioinConstants.LOCALE_CHARACTER_ENCODING_PROP_NAME);
		}
	}
	
	public static String getApplicationProperty(String name, String defaultValue){		
		if(isReady()){
			return getAdminService().getConfigService().getApplicationProperty(name, defaultValue);
		}else{ 
			return getAdminService().getConfigService().getLocalProperty(name, defaultValue);
		}
	}
	
	public static int getApplicationIntProperty(String name, int defaultValue){
		if(isReady())
			return getAdminService().getConfigService().getApplicationIntProperty(name, defaultValue);
		else 
			return getAdminService().getConfigService().getLocalProperty(name, defaultValue);
	}
	
	public static boolean getApplicationBooleanProperty(String name, boolean defaultValue){
		if(isReady())
		    return getAdminService().getConfigService().getApplicationBooleanProperty(name, defaultValue);
		else
			return getAdminService().getConfigService().getLocalProperty(name, defaultValue);
	}
	
	public static TimeZone getTimeZone(){
		if(isReady()){
			return getAdminService().getConfigService().getTimeZone();
		}else{
			String timeZoneID = getAdminService().getConfigService().getLocalProperty(ApplicatioinConstants.LOCALE_TIMEZONE_PROP_NAME);
			if(timeZoneID == null)
				return TimeZone.getDefault();
			else
				return TimeZone.getTimeZone(timeZoneID);
		}
	}
	
	public static String getLocalizedApplicationProperty(String name, Locale locale){
		if(isReady())
			return getAdminService().getConfigService().getLocalizedApplicationProperty(name, locale);
		else 
			return null;
	}
	
}