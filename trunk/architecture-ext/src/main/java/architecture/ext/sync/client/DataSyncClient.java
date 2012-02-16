package architecture.ext.sync.client;


public interface DataSyncClient {
	
	public abstract Object process (String processName);
    
	public abstract Object process (String processName, Object[] args);
	
}
