package architecture.common.exception;

import org.apache.commons.lang.exception.NestableRuntimeException;


public class ConfigurationError extends NestableRuntimeException {

	public ConfigurationError() {
		super();
	}

	public ConfigurationError(String msg) {
		super(msg);
	}

	public ConfigurationError(String msg, Throwable cause) {
		super(msg, cause);
	}

	public ConfigurationError(Throwable cause) {
		super(cause);
	}

}
