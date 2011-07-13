package architecture.ee.user;

import architecture.ee.exception.ApplicationException;

public class EmailAlreadyExistsException extends ApplicationException {

	public EmailAlreadyExistsException() {
		super();
		// TODO Auto-generated constructor stub
	}

	public EmailAlreadyExistsException(String msg, Throwable cause) {
		super(msg, cause);
		// TODO Auto-generated constructor stub
	}

	public EmailAlreadyExistsException(String msg) {
		super(msg);
		// TODO Auto-generated constructor stub
	}

	public EmailAlreadyExistsException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}

}