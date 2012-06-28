package architecture.user;

import architecture.ee.exception.ApplicationException;

public class EmailAlreadyExistsException extends ApplicationException {

	public EmailAlreadyExistsException() {
		super();
	}

	public EmailAlreadyExistsException(String msg, Throwable cause) {
		super(msg, cause);
	}

	public EmailAlreadyExistsException(String msg) {
		super(msg);
	}

	public EmailAlreadyExistsException(Throwable cause) {
		super(cause);
	}

	public EmailAlreadyExistsException(int errorCode, String msg,
			Throwable cause) {
		super(errorCode, msg, cause);
	}

	public EmailAlreadyExistsException(int errorCode, String msg) {
		super(errorCode, msg);
	}

	public EmailAlreadyExistsException(int errorCode, Throwable cause) {
		super(errorCode, cause);
	}

	public EmailAlreadyExistsException(int errorCode) {
		super(errorCode);
	}

}