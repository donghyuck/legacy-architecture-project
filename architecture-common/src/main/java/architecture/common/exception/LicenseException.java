package architecture.common.exception;

import org.apache.commons.lang.exception.NestableRuntimeException;

public class LicenseException extends NestableRuntimeException {

	private static final long serialVersionUID = 8541463362674661215L;

	public LicenseException() {
		super();
	}

	public LicenseException(String msg, Throwable cause) {
		super(msg, cause);
	}

	public LicenseException(String msg) {
		super(msg);
	}

	public LicenseException(Throwable cause) {
		super(cause);
	}

}
