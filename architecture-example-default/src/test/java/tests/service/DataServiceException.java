package tests.service;

import architecture.ee.exception.ApplicationException;

public class DataServiceException extends ApplicationException {

	public DataServiceException() {
		super();
		// TODO Auto-generated constructor stub
	}

	public DataServiceException(int errorCode, String msg, Throwable cause) {
		super(errorCode, msg, cause);
		// TODO Auto-generated constructor stub
	}

	public DataServiceException(int errorCode, String msg) {
		super(errorCode, msg);
		// TODO Auto-generated constructor stub
	}

	public DataServiceException(int errorCode, Throwable cause) {
		super(errorCode, cause);
		// TODO Auto-generated constructor stub
	}

	public DataServiceException(int errorCode) {
		super(errorCode);
		// TODO Auto-generated constructor stub
	}

	public DataServiceException(String msg, Throwable cause) {
		super(msg, cause);
		// TODO Auto-generated constructor stub
	}

	public DataServiceException(String msg) {
		super(msg);
		// TODO Auto-generated constructor stub
	}

	public DataServiceException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}

}
