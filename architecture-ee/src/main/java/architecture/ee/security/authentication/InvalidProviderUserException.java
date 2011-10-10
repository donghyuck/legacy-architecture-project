package architecture.ee.security.authentication;

import architecture.ee.exception.SystemException;

public class InvalidProviderUserException extends SystemException {

	public InvalidProviderUserException() {
		super();
	}

	public InvalidProviderUserException(String msg, Throwable cause) {
		super(msg, cause);
	}

	public InvalidProviderUserException(String msg) {
		super(msg);
	}

	public InvalidProviderUserException(Throwable cause) {
		super(cause);
	}

}
