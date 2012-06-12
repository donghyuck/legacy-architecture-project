package architecture.user;

import architecture.ee.exception.ApplicationException;

public class UserNotFoundException extends ApplicationException {

	public UserNotFoundException() {
		super();
	}

	public UserNotFoundException(String msg, Throwable cause) {
		super(msg, cause);
	}

	public UserNotFoundException(String msg) {
		super(msg);
	}

	public UserNotFoundException(Throwable cause) {
		super(cause);
	}

}
