package architecture.ee.audit;

import java.lang.reflect.Method;

import architecture.common.user.User;


public interface MethodCall {
	
    public abstract User getCaller();

    public abstract Object getTargetObject();

    public abstract Method getMethod();

    public abstract Class getTargetClass();

    public abstract Object[] getParameterValues();
    
}
