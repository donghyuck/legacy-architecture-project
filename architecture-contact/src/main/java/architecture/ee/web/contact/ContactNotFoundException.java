package architecture.ee.web.contact;

import architecture.ee.exception.ApplicationException;

public class ContactNotFoundException extends ApplicationException {

	public ContactNotFoundException() {
		super();
	}
	
	public ContactNotFoundException(int errorCode, String msg, Throwable cause) {
		super(errorCode, msg, cause);
	}

	/**
	 * @param errorCode
	 * @param msg
	 */
	public ContactNotFoundException(int errorCode, String msg) {
		super(errorCode, msg);
	}

	/**
	 * @param errorCode
	 * @param cause
	 */
	public ContactNotFoundException(int errorCode, Throwable cause) {
		super(errorCode, cause);
	}

	/**
	 * @param errorCode
	 */
	public ContactNotFoundException(int errorCode) {
		super(errorCode);
	}

	/**
	 * @param msg
	 * @param cause
	 */
	public ContactNotFoundException(String msg, Throwable cause) {
		super(msg, cause);
	}

	/**
	 * @param msg
	 */
	public ContactNotFoundException(String msg) {
		super(msg);
	}

	/**
	 * @param cause
	 */
	public ContactNotFoundException(Throwable cause) {
		super(cause);
	}
}
