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

}