package architecture.user.profile;

import architecture.ee.exception.ApplicationException;

public interface TypeConverter extends ExternalMapper {

    public static class ConversionException extends ApplicationException {

	public ConversionException() {
	    super();
	}

	public ConversionException(String msg, Throwable cause) {
	    super(msg, cause);
	}

	public ConversionException(String msg) {
	    super(msg);
	}

	public ConversionException(Throwable cause) {
	    super(cause);
	}

    }

    public abstract Object convertFromString(String s) throws ConversionException;

    public abstract String convertToString(Object obj) throws ConversionException;

    public abstract boolean objectIsConvertable(Object obj);

    public abstract boolean stringIsConvertable(String s);

    public abstract boolean objectIsInitialized(Object obj);

    public abstract String getValidationKey();

}
