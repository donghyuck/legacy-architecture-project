package architecture.user.security.authentication;

import architecture.ee.exception.SystemException;

public class InvalidProviderUserException extends SystemException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4679110380567682282L;

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
