package architecture.ext.sync.remoting;

public interface DataSyncService {
	
	public Object process(String jobCode) throws DataSyncServiceException;
	
	public Object read(String jobCode, Object[] args) throws DataSyncServiceException;
	
	public void processBySilentMode(String jobCode)throws DataSyncServiceException;
	
}
