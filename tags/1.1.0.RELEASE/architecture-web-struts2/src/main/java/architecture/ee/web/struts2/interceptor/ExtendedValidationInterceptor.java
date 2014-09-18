package architecture.ee.web.struts2.interceptor;

import java.lang.reflect.Method;

import architecture.ee.web.struts2.annotation.NoValidation;

import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.validator.ValidationInterceptor;


public class ExtendedValidationInterceptor extends ValidationInterceptor {

	public ExtendedValidationInterceptor() {		
	}
	
    protected boolean applyInterceptor(ActionInvocation invocation)
    {
        boolean applyMethod = true;
        Object action = invocation.getAction();
        String methodName = invocation.getProxy().getMethod();
        
        if(methodName == null)
            methodName = "execute";
        try
        {
            Method method = action.getClass().getMethod(methodName, new Class[0]);
            applyMethod = checkAnnotation(method);
        }
        catch(NoSuchMethodException e)
        {
            try
            {
                methodName = (new StringBuilder()).append("do").append(methodName.substring(0, 1).toUpperCase()).append(methodName.substring(1)).toString();
                Method method = action.getClass().getMethod(methodName, new Class[0]);
                applyMethod = checkAnnotation(method);
            }
            catch(NoSuchMethodException e1) { }
        }
        
        if(applyMethod)
            applyMethod = super.applyInterceptor(invocation);        
        return applyMethod;
    }

    private boolean checkAnnotation(Method method)
    {
        boolean applyMethod = true;
        java.lang.annotation.Annotation annotations[] = method.getAnnotations();
        if(annotations != null)
        {
        	for(java.lang.annotation.Annotation annotation : annotations){
                if(annotation instanceof NoValidation)
                {
                    applyMethod = false;
                    break;
                }
        	}
        }
        return applyMethod;
    }
}