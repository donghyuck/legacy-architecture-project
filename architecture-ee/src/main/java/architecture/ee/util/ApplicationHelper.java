package architecture.ee.util;

import java.lang.ref.WeakReference;
import java.util.Collections;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

import architecture.common.exception.ComponentNotFoundException;
import architecture.common.lifecycle.ApplicationHelperFactory;
import architecture.ee.component.Admin;

public class ApplicationHelper {

	private static final Map<Class<?>, WeakReference<?>> references = Collections.synchronizedMap(new HashMap<Class<?>, WeakReference<?>>()) ;
	
	public static architecture.common.lifecycle.ApplicationHelper getApplicationHelper(){
		return ApplicationHelperFactory.getApplicationHelper();
	}
	
	public static <T> T getComponent(Class<T> requiredType) throws ComponentNotFoundException
	{
		if( ApplicationHelper.references.get(requiredType) == null){
			ApplicationHelper.references.put(requiredType, new WeakReference<T>( getApplicationHelper().getComponent(requiredType) ));			
		}
		return (T)ApplicationHelper.references.get(requiredType).get();
	}
	
	public static Admin getAdmin() {		
		return getComponent(Admin.class);
	}

	public static Locale getLocale(){
		return getAdmin().getLocale();
	}
	
	public static String getCharacterEncoding(){
		return getAdmin().getCharacterEncoding();
	}
	
	public static String getApplicationProperty(String name, String defaultValue){
		return getAdmin().getApplicationProperty(name, defaultValue);
	}
	
	public static int getApplicationIntProperty(String name, int defaultValue){
		return getAdmin().getApplicationIntProperty(name, defaultValue);
	}
	
	public static boolean getApplicationBooleanProperty(String name, boolean defaultValue){
		return getAdmin().getApplicationBooleanProperty(name, defaultValue);
	}
	
	public static TimeZone getTimeZone(){
		return getAdmin().getTimeZone();
	}
	
	public static String getLocalizedApplicationProperty(String name, Locale locale){
		return getAdmin().getLocalizedApplicationProperty(name, locale);
	}
}
