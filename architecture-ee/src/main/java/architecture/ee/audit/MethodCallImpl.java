package architecture.ee.audit;

import java.lang.reflect.Method;

import architecture.common.user.User;


public class MethodCallImpl
    implements MethodCall
{

    private User caller;

    private Method method;

    private Object params[];

    private Object target;

    public MethodCallImpl()
    {
    }

    public MethodCallImpl(User caller, Object target, Method method, Object params[])
    {
        this.caller = caller;
        this.target = target;
        this.method = method;
        this.params = params;
    }

    public User getCaller()
    {
        return caller;
    }

    public Method getMethod()
    {
        return method;
    }

    public Object[] getParameterValues()
    {
        return params;
    }

    public Class getTargetClass()
    {
        if(target != null)
            return target.getClass();
        else
            return method.getDeclaringClass();
    }

    public Object getTargetObject()
    {
        return target;
    }

    public void setCaller(User caller)
    {
        this.caller = caller;
    }
    public void setMethod(Method method)
    {
        this.method = method;
    }
    
    public  void setParameterValues(Object params[])
    {
        this.params = params;
    }
    public void setTargetObject(Object target)
    {
        this.target = target;
    }
}
