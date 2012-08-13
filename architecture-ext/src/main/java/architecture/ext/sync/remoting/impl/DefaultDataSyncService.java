package architecture.ext.sync.remoting.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import architecture.ext.sync.client.DataSyncClient;
import architecture.ext.sync.remoting.DataSyncService;
import architecture.ext.sync.remoting.DataSyncServiceException;

public class DefaultDataSyncService implements DataSyncService {

	private Log log = LogFactory.getLog(getClass());
	
	private DataSyncClient dataSyncClient;
	
	
	public DataSyncClient getDataSyncClient() {
		return dataSyncClient;
	}

	public void setDataSyncClient(DataSyncClient dataSyncClient) {
		this.dataSyncClient = dataSyncClient;
	}

	public Object process(String jobCode) throws DataSyncServiceException {
		
		try {
			return dataSyncClient.process(jobCode);
		} catch (Throwable e) {
			throw new DataSyncServiceException(e);
		}
	
	}

	public Object read(String jobCode, Object[] args) throws DataSyncServiceException {
		try {
			return dataSyncClient.read(jobCode, args);
		} catch (Throwable e) {
			throw new DataSyncServiceException(e);
		}
	}

	public void processBySilentMode(String jobCode)
			throws DataSyncServiceException {
		System.out.println("RUN " + jobCode );
		try {
			log.debug("processBySilentMode " + jobCode );
			Object obj = dataSyncClient.process(jobCode);
			log.debug("RESULT");
		} catch (Throwable e) {
			throw new DataSyncServiceException(e);
		}
		
	}

}
