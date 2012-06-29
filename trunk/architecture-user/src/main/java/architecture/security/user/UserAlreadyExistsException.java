package architecture.security.user;

import architecture.ee.exception.ApplicationException;

public class UserAlreadyExistsException extends ApplicationException {

	public UserAlreadyExistsException() {
		super();
	}

	public UserAlreadyExistsException(String msg, Throwable cause) {
		super(msg, cause);
	}

	public UserAlreadyExistsException(String msg) {
		super(msg);
	}

	public UserAlreadyExistsException(Throwable cause) {
		super(cause);
	}

}
