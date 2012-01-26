package architecture.common.exception;

import org.apache.commons.lang.exception.NestableRuntimeException;

public class ConfigurationWarning extends NestableRuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4891487084047187129L;

	public ConfigurationWarning() {
		super();
	}

	public ConfigurationWarning(String msg) {
		super(msg);
	}

	public ConfigurationWarning(String msg, Throwable cause) {
		super(msg, cause);
	}

	public ConfigurationWarning(Throwable cause) {
		super(cause);
	}

}
