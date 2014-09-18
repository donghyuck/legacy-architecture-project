package architecture.user;

import architecture.ee.exception.ApplicationException;

public class RoleNotFoundException extends ApplicationException {

	public RoleNotFoundException() {
		super();
	}

	public RoleNotFoundException(String msg, Throwable cause) {
		super(msg, cause);
	}

	public RoleNotFoundException(String msg) {
		super(msg);
	}

	public RoleNotFoundException(Throwable cause) {
		super(cause);
	}

}
