package architecture.common.exception;

import org.apache.commons.lang.exception.NestableError;

public class RuntimeError extends NestableError {

	public RuntimeError() {
		super();
	}

	public RuntimeError(String msg) {
		super(msg);
	}

	public RuntimeError(String msg, Throwable cause) {
		super(msg, cause);
	}

	public RuntimeError(Throwable cause) {
		super(cause);
	}

}
