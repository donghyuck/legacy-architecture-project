package architecture.common.exception;

import org.apache.commons.lang.exception.NestableRuntimeException;

public class RuntimeWarning extends NestableRuntimeException {

	public RuntimeWarning() {
		super();
	}

	public RuntimeWarning(String msg) {
		super(msg);
	}

	public RuntimeWarning(String msg, Throwable cause) {
		super(msg, cause);
	}

	public RuntimeWarning(Throwable cause) {
		super(cause);
	}

}
