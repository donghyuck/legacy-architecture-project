package architecture.common.exception;

import java.lang.reflect.Constructor;

import architecture.common.i18n.Localizer;
import architecture.common.util.L10NUtils;

public class CodeableException extends Exception implements Codeable {

    private int errorCode = 0;

    public CodeableException() {
	super();
    }

    public CodeableException(String msg, Throwable cause) {
	super(msg, cause);
    }

    public CodeableException(String msg) {
	super(msg);
    }

    public CodeableException(Throwable cause) {
	super(cause);
    }

    public int getErrorCode() {
	return errorCode;
    }

    public String getErrorCodeString() {
	return Localizer.codeToString(errorCode);
    }

    protected void setErrorCode(int errorCode) {
	this.errorCode = errorCode;
    }

    public static <T> T newException(Class<T> requiredType, int errorCode, Object... args) {
	String messageToUse = getMessage(errorCode, args, null);
	try {
	    Constructor constructor = requiredType.getConstructor(new Class[] { String.class });
	    Object obj = constructor.newInstance(messageToUse);
	    ((CodeableException) obj).setErrorCode(errorCode);
	    return (T) obj;
	} catch (Throwable ex) {

	    throw new RuntimeError(messageToUse, ex);
	}
    }

    /**
     * 
     * @param requiredType
     * @param e
     * @param errorCode
     * @param args
     * @return
     */
    public static <T> T newException(Class<T> requiredType, Throwable e, int errorCode, Object... args) {
	String messageToUse = null;
	if (e != null)
	    messageToUse = e.getMessage();
	messageToUse = getMessage(errorCode, args, messageToUse);
	try {
	    Constructor constructor = requiredType.getConstructor(new Class[] { String.class, Throwable.class });
	    Object obj = constructor.newInstance(messageToUse, e);
	    ((CodeableRuntimeException) obj).setErrorCode(errorCode);
	    return (T) obj;
	} catch (Throwable ex) {
	    throw new RuntimeError(messageToUse, e);
	}
    }

    protected static String getMessage(int errorCode, Object[] args, String msg) {
	String messageToUse = msg;
	try {
	    String errorCodeToUse = Localizer.codeToString(errorCode);
	    if (args != null) {
		messageToUse = L10NUtils.format(errorCodeToUse, args);
	    } else {
		messageToUse = L10NUtils.getMessage(errorCodeToUse);
	    }
	} catch (Exception ignore) {
	}
	return messageToUse;
    }

}