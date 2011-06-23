package architecture.ee.util;

import java.util.Locale;

import architecture.common.exception.ComponentNotFoundException;
import architecture.common.lifecycle.ApplicationHelper;
import architecture.common.lifecycle.ApplicationHelperFactory;
import architecture.common.lifecycle.ConfigRoot;
import architecture.common.lifecycle.State;
import architecture.ee.bootstrap.Bootstrap;
import architecture.ee.component.Admin;
import architecture.ee.component.AdminService;

public class AdminHelper {
	
	public static ApplicationHelper getApplicationHelper(){
		return ApplicationHelperFactory.getApplicationHelper();
	}
	
	public static State getState(){
		return getAdminService().getState();
	}
		
	public static boolean isReady(){
		return getAdminService().isReady();
	}
	
	public static <T> T getComponent(Class<T> requiredType) throws ComponentNotFoundException
	{
		return getApplicationHelper().getComponent(requiredType);		
	}
	
	public static Admin getAdmin() {		
		return getApplicationHelper().getComponent(Admin.class);		
	}

	public static AdminService getAdminService() {			
		return Bootstrap.getAdminService();
	}	
	
	public static Locale getLocale(){
		return getAdmin().getLocale();
	}
	
	public static String getInstallRootPath(){
		return getAdmin().getInstallRootPath();
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
