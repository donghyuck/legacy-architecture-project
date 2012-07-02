package architecture.ee.audit;

import java.lang.reflect.Method;

public interface Auditor {

	public abstract MutableAuditMessage auditMethodCall(MethodCall methodcall)
			throws NotAuditableException;

	public abstract boolean methodIsAuditable(Method method);

	public abstract void setAuditedClass(Class class1) throws NotAuditableException;
}
