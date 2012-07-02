package architecture.ee.audit;

import architecture.ee.exception.SystemException;

public class NotAuditableException extends SystemException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7768631210747297569L;

	public NotAuditableException() {
		super();
	}

	public NotAuditableException(String msg, Throwable cause) {
		super(msg, cause);
	}

	public NotAuditableException(String msg) {
		super(msg);
	}

	public NotAuditableException(Throwable cause) {
		super(cause);
	}

}
