package architecture.user.security.authentication;

import architecture.ee.exception.SystemException;

public class UnAuthorizedException extends SystemException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6339398042209520398L;

	public UnAuthorizedException() {
	}

	public UnAuthorizedException(String msg, Throwable cause) {
		super(msg, cause);
	}

	public UnAuthorizedException(String msg) {
		super(msg);
	}

	public UnAuthorizedException(Throwable cause) {
		super(cause);
	}

}
