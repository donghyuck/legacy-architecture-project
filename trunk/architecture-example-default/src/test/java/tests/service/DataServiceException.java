package tests.service;

import architecture.ee.exception.ApplicationException;

public class DataServiceException extends ApplicationException {

	public DataServiceException() {
		super();
	}

	public DataServiceException(int errorCode, String msg, Throwable cause) {
		super(errorCode, msg, cause);
	}

	public DataServiceException(int errorCode, String msg) {
		super(errorCode, msg);
	}

	public DataServiceException(int errorCode, Throwable cause) {
		super(errorCode, cause);
	}

	public DataServiceException(int errorCode) {
		super(errorCode);
	}

	public DataServiceException(String msg, Throwable cause) {
		super(msg, cause);
	}

	public DataServiceException(String msg) {
		super(msg);
	}

	public DataServiceException(Throwable cause) {
		super(cause);
	}

}
