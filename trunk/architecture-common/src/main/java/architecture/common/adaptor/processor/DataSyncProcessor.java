package architecture.common.adaptor.processor;

import architecture.common.adaptor.Context;
import architecture.common.adaptor.DataProcessor;
import architecture.common.adaptor.ReadConnector;
import architecture.common.adaptor.WriteConnector;

public class DataSyncProcessor implements DataProcessor {
	
	private ReadConnector readConnector;
	
	private WriteConnector writeConnector;
		
	/**
	 * 
	 */
	public Object process(Object... args) {

		Context context = getContext();		
		
		Object data = getReadConnector().pull(context);		
		
		context.setObject("data",  data);		
		
		return getWriteConnector().deliver(context);
	}

	public Context getContext() {
		return null;
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
