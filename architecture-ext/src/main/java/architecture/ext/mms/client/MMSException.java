package architecture.ext.mms.client;

import org.apache.commons.lang.exception.NestableRuntimeException;

public class MMSException extends NestableRuntimeException {

	public MMSException() {
		super();
	}

	public MMSException(String msg, Throwable cause) {
		super(msg, cause);
	}

	public MMSException(String msg) {
		super(msg);
	}

	public MMSException(Throwable cause) {
		super(cause);
	}

}
