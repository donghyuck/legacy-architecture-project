package architecture.ext.sync.db;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import architecture.common.adaptor.Context;
import architecture.common.adaptor.ReadConnector;
import architecture.common.adaptor.WriteConnector;
import architecture.common.jdbc.TypeAliasRegistry;
import architecture.ext.sync.client.DataSyncClient;

public class DefaultDataSyncClient implements DataSyncClient {

	public static final String DEFAULT_READ_CONNECTOR_PREFIX = "READ_";
	
	public static final String DEFAULT_WRITE_CONNECTOR_PREFIX = "WRITE_";
	
	public static final TypeAliasRegistry DEAFULT_TYPE_ALIAS_REGISTRY =  new TypeAliasRegistry(); 
	
	private Map<String, DefaultDataSyncMetaInfo> processMappings = new HashMap<String, DefaultDataSyncMetaInfo>();
	
	private ReadConnector readConnector;
	
	private WriteConnector writeConnector;
	
	public Object process(String processName) {
		return process(processName, new Object[]{} );
	}

	public Object process(String processName, Object[] args) {
		
		Context readContext = getReadConetxt(processName);
		readContext.setObject("data", args);	
		
		List<Map<String, Object>> output = (List<Map<String, Object>>) getReadConnector().pull(readContext);
		
		Context writeContext = getWriteConetxt(processName);
		writeContext.setObject("data", output);		
		return getWriteConnector().deliver(writeContext);
	}
	
	protected Context getReadConetxt(String processName){
		
		DefaultDataSyncMetaInfo metaInfo = processMappings.get(DEFAULT_READ_CONNECTOR_PREFIX + processName);		
		DeaultContext context = new DeaultContext();
		context.setObject("parameterMappings", metaInfo.getParameterMappings());
		context.setObject("queryString", metaInfo.getClass());		

		return context;
		
	}

	protected Context getWriteConetxt(String processName){
		
		DefaultDataSyncMetaInfo metaInfo = processMappings.get(DEFAULT_WRITE_CONNECTOR_PREFIX + processName);
		DeaultContext context = new DeaultContext();
		context.setObject("parameterMappings", metaInfo.getParameterMappings());
		context.setObject("queryString", metaInfo.getClass());		

		return context;
	}
	
	public ReadConnector getReadConnector() {
		return readConnector;
	}

	public void setReadConnector(ReadConnector readConnector) {
		this.readConnector = readConnector;
	}

	public WriteConnector getWriteConnector() {
		return writeConnector;
	}

	public void setWriteConnector(WriteConnector writeConnector) {
		this.writeConnector = writeConnector;
	}
	
}
