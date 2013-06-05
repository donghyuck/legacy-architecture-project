package architecture.ee.web.struts2.interceptor;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import architecture.common.cache.Cache;
import architecture.common.cache.EhcacheWrapper;
import architecture.ee.component.admin.AdminHelper;

import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.PrefixMethodInvocationUtil;

public class ExtendedPrefixMethodInvocationUtil extends PrefixMethodInvocationUtil {

    private static final Log log = LogFactory.getLog(ExtendedPrefixMethodInvocationUtil.class);
       
    private static Cache<String, Boolean> cache = new EhcacheWrapper<String, Boolean>( AdminHelper.getCache("Prefix lookup map"));
    
	public static void invokePrefixMethod(ActionInvocation actionInvocation, String prefixes[]) throws InvocationTargetException, IllegalAccessException {
		
		Object action = actionInvocation.getAction();
		
		String methodName = actionInvocation.getProxy().getMethod();
		if (methodName == null)
			methodName = "execute";
		
		Method method = getPrefixedMethod(prefixes, methodName, action);
		if (method != null)
			method.invoke(action, new Object[0]);
	}

	public static Method getPrefixedMethod(String prefixes[], String methodName, Object action) {
			
		String capitalizedMethodName = capitalizeMethodName(methodName);
		for( String prefix : prefixes){
			if (isCacheMiss(prefix, methodName, action))
				continue;
			
			String prefixedMethodName = (new StringBuilder()).append(prefix).append(capitalizedMethodName).toString();
			try {
				Method method = action.getClass().getMethod(prefixedMethodName, new Class[0]);
				putCache(prefix, methodName, action, true);
				return method;
			} catch (NoSuchMethodException e) {
				putCache(prefix, methodName, action, false);
			}
			if (log.isDebugEnabled())
				log.debug((new StringBuilder()).append("cannot find method [").append(prefixedMethodName).append("] in action [").append(action).append("]").toString());
		}
		return null;
	}
	

    private static boolean isCacheMiss(String prefix, String methodName, Object action)
    {
    	Boolean o = (Boolean)cache.get(getCacheKey(prefix, methodName, action)); 
    	if(o == null){
    		return false;
    	}    	  	   
        return o != null && !o.booleanValue();
    }

    private static String getCacheKey(String prefix, String methodName, Object action)
    {
        return (new StringBuilder()).append(action.getClass().getName()).append('.').append(prefix).append('.').append(methodName).toString();
    }

    private static void putCache(String prefix, String methodName, Object action, boolean exists)
    {
    	cache.put(getCacheKey(prefix, methodName, action), exists);        
    }
}
