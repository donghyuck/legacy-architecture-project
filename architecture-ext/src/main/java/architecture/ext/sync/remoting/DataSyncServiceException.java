package architecture.ext.sync.remoting;

import org.apache.commons.lang.exception.NestableRuntimeException;

public class DataSyncServiceException extends NestableRuntimeException {

	public DataSyncServiceException() {
		super();
	}

	public DataSyncServiceException(String msg, Throwable cause) {
		super(msg, cause);
	}

	public DataSyncServiceException(String msg) {
		super(msg);
	}

	public DataSyncServiceException(Throwable cause) {
		super(cause);
	}

}
