package architecture.ee.web.contact;

import architecture.ee.exception.ApplicationException;

public class ContactGroupNotFoundException extends ApplicationException {

	public ContactGroupNotFoundException() {
		super();
	}
	
	public ContactGroupNotFoundException(int errorCode, String msg, Throwable cause) {
		super(errorCode, msg, cause);
	}

	/**
	 * @param errorCode
	 * @param msg
	 */
	public ContactGroupNotFoundException(int errorCode, String msg) {
		super(errorCode, msg);
	}

	/**
	 * @param errorCode
	 * @param cause
	 */
	public ContactGroupNotFoundException(int errorCode, Throwable cause) {
		super(errorCode, cause);
	}

	/**
	 * @param errorCode
	 */
	public ContactGroupNotFoundException(int errorCode) {
		super(errorCode);
	}

	/**
	 * @param msg
	 * @param cause
	 */
	public ContactGroupNotFoundException(String msg, Throwable cause) {
		super(msg, cause);
	}

	/**
	 * @param msg
	 */
	public ContactGroupNotFoundException(String msg) {
		super(msg);
	}

	/**
	 * @param cause
	 */
	public ContactGroupNotFoundException(Throwable cause) {
		super(cause);
	}
	
	
	
}
