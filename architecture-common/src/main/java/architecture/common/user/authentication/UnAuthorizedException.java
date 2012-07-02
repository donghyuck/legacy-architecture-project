package architecture.common.user.authentication;

import org.apache.commons.lang.exception.NestableRuntimeException;

public class UnAuthorizedException extends NestableRuntimeException {

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
