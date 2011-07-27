package architecture.ext.sms.client;

import org.apache.commons.lang.exception.NestableRuntimeException;

public class SMSException extends NestableRuntimeException {

	public SMSException() {
		super();
	}

	public SMSException(String msg, Throwable cause) {
		super(msg, cause);
	}

	public SMSException(String msg) {
		super(msg);
	}

	public SMSException(Throwable cause) {
		super(cause);
	}

}
