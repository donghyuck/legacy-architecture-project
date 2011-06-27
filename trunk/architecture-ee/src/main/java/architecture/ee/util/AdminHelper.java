package architecture.ee.util;

import java.lang.ref.WeakReference;
import java.util.Collections;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.apache.poi.ss.formula.functions.T;

import architecture.common.exception.ComponentNotFoundException;
import architecture.common.lifecycle.ApplicationHelper;
import architecture.common.lifecycle.ApplicationHelperFactory;
import architecture.common.lifecycle.ConfigRoot;
import architecture.common.lifecycle.State;
import architecture.ee.bootstrap.Bootstrap;
import architecture.ee.component.Admin;
import architecture.ee.component.AdminService;

public class AdminHelper {

	private static final Map<Class<?>, WeakReference<?>> references = Collections.synchronizedMap(new HashMap<Class<?>, WeakReference<?>>()) ;
	
	public static ApplicationHelper getApplicationHelper(){
		return ApplicationHelperFactory.getApplicationHelper();
	}
	
	public static State getState(){
		return getAdminService().getState();
	}
		
	public static boolean isReady(){
		return getAdminService().isReady();
	}
	
	public static boolean isSetupComplete(){
	    return getAdmin().getApplicationBooleanProperty("setup.complete", false);
	}	
	
	public static <T> T getComponent(Class<T> requiredType) throws ComponentNotFoundException
	{
		return getApplicationHelper().getComponent(requiredType);		
	}
	
	public static Admin getAdmin() {		
		if( AdminHelper.references.get(Admin.class) == null){
			AdminHelper.references.put(Admin.class, new WeakReference<Admin>(getApplicationHelper().getComponent(Admin.class)));
		}			
		return (Admin)AdminHelper.references.get(Admin.class).get();
	}

	public static AdminService getAdminService() {	
		if( AdminHelper.references.get(AdminService.class) == null){
			AdminHelper.references.put(AdminService.class, new WeakReference<AdminService>( Bootstrap.getAdminService() ));
		}		
		return (AdminService)AdminHelper.references.get(AdminService.class).get();
	}	
	
	public static Locale getLocale(){
		return getAdmin().getLocale();
	}
	
	public static String getEffectiveRootPath(){
		return getAdmin().getEffectiveRootPath();
	}
	
	public static ConfigRoot getConfigRoot(){
		return getAdmin().getConfigRoot();
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

}