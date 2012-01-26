package architecture.common.exception;

import org.apache.commons.lang.exception.NestableRuntimeException;


public class ConfigurationError extends NestableRuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2524476352484039261L;

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
