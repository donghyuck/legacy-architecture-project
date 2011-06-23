package architecture.ee.security;

import architecture.ee.exception.SystemException;

public class UnauthorizedException extends SystemException {

	public UnauthorizedException() {
	}

	public UnauthorizedException(String msg, Throwable cause) {
		super(msg, cause);
	}

	public UnauthorizedException(String msg) {
		super(msg);
	}

	public UnauthorizedException(Throwable cause) {
		super(cause);
	}

}
