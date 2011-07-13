package architecture.ee.user;

import architecture.ee.exception.ApplicationException;

public class UserAlreadyExistsException extends ApplicationException {

	public UserAlreadyExistsException() {
		super();
		// TODO Auto-generated constructor stub
	}

	public UserAlreadyExistsException(String msg, Throwable cause) {
		super(msg, cause);
		// TODO Auto-generated constructor stub
	}

	public UserAlreadyExistsException(String msg) {
		super(msg);
		// TODO Auto-generated constructor stub
	}

	public UserAlreadyExistsException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}

}
