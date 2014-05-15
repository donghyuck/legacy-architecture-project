package architecture.ee.web.community.forum;

import architecture.ee.exception.ApplicationException;

public class TopicNotFoundException extends ApplicationException {
	
	public TopicNotFoundException() {
		super();
	}
	
	public TopicNotFoundException(int errorCode, String msg, Throwable cause) {
		super(errorCode, msg, cause);
	}

	/**
	 * @param errorCode
	 * @param msg
	 */
	public TopicNotFoundException(int errorCode, String msg) {
		super(errorCode, msg);
	}

	/**
	 * @param errorCode
	 * @param cause
	 */
	public TopicNotFoundException(int errorCode, Throwable cause) {
		super(errorCode, cause);
	}

	/**
	 * @param errorCode
	 */
	public TopicNotFoundException(int errorCode) {
		super(errorCode);
	}

	/**
	 * @param msg
	 * @param cause
	 */
	public TopicNotFoundException(String msg, Throwable cause) {
		super(msg, cause);
	}

	/**
	 * @param msg
	 */
	public TopicNotFoundException(String msg) {
		super(msg);
	}

	/**
	 * @param cause
	 */
	public TopicNotFoundException(Throwable cause) {
		super(cause);
	}
	
}
