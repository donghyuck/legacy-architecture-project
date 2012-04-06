package architecture.common.adaptor.processor;

import architecture.common.adaptor.Context;
import architecture.common.adaptor.DataProcessor;
import architecture.common.adaptor.ReadConnector;
import architecture.common.adaptor.WriteConnector;

/**
 * @author  donghyuck
 */
public class DataSyncProcessor implements DataProcessor {
	
	/**
	 * @uml.property  name="readConnector"
	 * @uml.associationEnd  
	 */
	private ReadConnector readConnector;
	
	/**
	 * @uml.property  name="writeConnector"
	 * @uml.associationEnd  
	 */
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

	/**
	 * @return
	 * @uml.property  name="readConnector"
	 */
	public ReadConnector getReadConnector() {
		return readConnector;
	}

	/**
	 * @param readConnector
	 * @uml.property  name="readConnector"
	 */
	public void setReadConnector(ReadConnector readConnector) {
		this.readConnector = readConnector;
	}

	/**
	 * @return
	 * @uml.property  name="writeConnector"
	 */
	public WriteConnector getWriteConnector() {
		return writeConnector;
	}

	/**
	 * @param writeConnector
	 * @uml.property  name="writeConnector"
	 */
	public void setWriteConnector(WriteConnector writeConnector) {
		this.writeConnector = writeConnector;
	}
	
	

}
