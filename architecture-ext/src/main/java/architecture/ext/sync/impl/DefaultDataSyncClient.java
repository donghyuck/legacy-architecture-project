package architecture.ext.sync.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import architecture.common.adaptor.Context;
import architecture.common.adaptor.Pipeline;
import architecture.common.adaptor.ReadConnector;
import architecture.common.adaptor.WriteConnector;
import architecture.common.adaptor.processor.ProcessCallback;
import architecture.common.jdbc.TypeAliasRegistry;
import architecture.ext.sync.client.DataSyncClient;

/**
 * @author  donghyuck
 */
public class DefaultDataSyncClient implements DataSyncClient {

	private Log log = LogFactory.getLog(getClass());
	
	public static final String DEFAULT_READ_CONNECTOR_PREFIX = "READ_";
	
	public static final String DEFAULT_WRITE_CONNECTOR_PREFIX = "WRITE_";
	
	/**
	 * @uml.property  name="dEAFULT_TYPE_ALIAS_REGISTRY"
	 * @uml.associationEnd  
	 */
	public static final TypeAliasRegistry DEAFULT_TYPE_ALIAS_REGISTRY =  new TypeAliasRegistry(); 
	
	//public String extraJobSurfix = "_EXT";
	
	/**
	 * @uml.property  name="processMappings"
	 */
	private Map<String, DefaultDataSyncMetaInfo> processMappings = new HashMap<String, DefaultDataSyncMetaInfo>();
	
	/**
	 * @uml.property  name="pipelineMappings"
	 */
	private Map<String, List<String>> pipelineMappings = new HashMap<String, List<String>>();
		
	private Map<String, Object> connectors = new HashMap<String, Object>();
		
	/**
	 * @param connectors
	 * @uml.property  name="connectors"
	 */
	public void setConnectors(Map<String, Object> connectors) {
		this.connectors = connectors;
	}
	
	/**
	 * @return
	 * @uml.property  name="processMappings"
	 */
	public Map<String, DefaultDataSyncMetaInfo> getProcessMappings() {
		return processMappings;
	}
	
	/**
	 * @return
	 * @uml.property  name="pipelineMappings"
	 */
	public Map<String, List<String>> getPipelineMappings() {
		return pipelineMappings;
	}

	/**
	 * @param pipelineMappings
	 * @uml.property  name="pipelineMappings"
	 */
	public void setPipelineMappings(Map<String, List<String>> pipelineMappings) {
		this.pipelineMappings = pipelineMappings;
	}

	/**
	 * @param processMappings
	 * @uml.property  name="processMappings"
	 */
	public void setProcessMappings(Map<String, DefaultDataSyncMetaInfo> processMappings) {		
		this.processMappings = processMappings;
	}
		
	public Object process(String processName) {
		return process( processName, new Object[0] );
	}

	public Object process(String processName, Object[] args) {		
		
		List<DefaultDataSyncMetaInfo> processors = getProcessorMetaInfos(processName);		
		List<Map<String, Object>> input = Collections.EMPTY_LIST;
		for(DefaultDataSyncMetaInfo processor : processors){
			if( hasConnector( processor.connectorName )){
				Context context = getContext(processor);
				if( processor.type == DefaultDataSyncMetaInfo.Type.READ ){
					context.setObject(Context.DATA, args);
					input = (List<Map<String, Object>>) getConnector( processor.connectorName, ReadConnector.class).pull(context);
				} else if( processor.type == DefaultDataSyncMetaInfo.Type.MERGE ){

					// MATCH DATA
					context.setObject(Context.DATA, args);
					String primaryKey = processor.getPrimaryParameterMapping().getProperty();
					
					List<String> list = new ArrayList<String>();
					for ( Map<String, Object> row : (List<Map<String, Object>>) getConnector( processor.connectorName, ReadConnector.class).pull(context) ){
						String value = (String)row.get(primaryKey);
						list.add(value);
					}
					
					
					List<Map<String, Object>> insert = new ArrayList<Map<String, Object>>();
					List<Map<String, Object>> update = new ArrayList<Map<String, Object>>();
					
					
					// READ DATA
					for( Map<String, Object> row : input){
						String value = (String)row.get(primaryKey);
						boolean merge = false;
						for(String no : list )
						{
							if(value.equals(no)){
								merge = true;
				                break;
							}
						}	
						if(merge){
							update.add(row);
						}else{
							insert.add(row);
						}
					}
					
					for(Pipeline p : processor.getPipelineMappings())
					{
						if(processMappings.containsKey(p.getName())){
							DefaultDataSyncMetaInfo subProcessor = processMappings.get(p.getName());
							Context subContext = getContext(subProcessor);
							if( subProcessor.type == DefaultDataSyncMetaInfo.Type.WRITE ){
								if( p.isMatch() ){
									subContext.setObject(Context.DATA, update);
									getConnector( subProcessor.connectorName, WriteConnector.class ).deliver(subContext);
								}else {
									subContext.setObject(Context.DATA, insert);
									getConnector( subProcessor.connectorName, WriteConnector.class ).deliver(subContext);
								}
							}
						}
					}
					
				} else if ( processor.type == DefaultDataSyncMetaInfo.Type.WRITE){
					context.setObject(Context.DATA, input);
					getConnector( processor.connectorName, WriteConnector.class ).deliver(context);
				}
			}
		}
		return input;
	}
		

	public <T> T process(String processName, Object[] args, ProcessCallback<T> action) {
		
		List<DefaultDataSyncMetaInfo> processors = getProcessorMetaInfos(processName);
		List<Context> contexts = new ArrayList<Context>();		
		//List<Map<String, Object>> input = Collections.EMPTY_LIST;
		for(DefaultDataSyncMetaInfo processor : processors){
			if( hasConnector( processor.connectorName )){
				Context context = getContext(processor);
				if( processor.type == DefaultDataSyncMetaInfo.Type.READ ){
					context.setObject("data", args);
					List<Map<String, Object>> input = (List<Map<String, Object>>) getConnector( processor.connectorName, ReadConnector.class).pull(context);
					context.setObject("output", input);
					contexts.add(context);
				}
			}
		}		
		return action.doInProcess();
		
	}

	@SuppressWarnings("unchecked")
	public List<DefaultDataSyncMetaInfo> getProcessorMetaInfos(String processName){
		if( pipelineMappings.containsKey(processName) ){
			List<DefaultDataSyncMetaInfo> processors = new LinkedList<DefaultDataSyncMetaInfo>();
			for( String name :  pipelineMappings.get(processName)){
				if(processMappings.containsKey(name))
					processors.add(processMappings.get(name));
			}
			return processors;
		}
		else {
			return Collections.EMPTY_LIST;
		}		
	}
	
	
	protected Context getContext(DefaultDataSyncMetaInfo processor){
		
		DeaultContext context = new DeaultContext();
		if( processor != null ){
			context.setObject("parameterMappings", processor.parameterMappings);
			context.setObject("queryString", processor.queryString);
			context.setObject("queryName", processor.queryName);
			context.setObject("batch", processor.isBatch);
			context.setConnectorName(processor.connectorName);
			if( processor.type == DefaultDataSyncMetaInfo.Type.READ || 
				processor.type == DefaultDataSyncMetaInfo.Type.MERGE )
				context.setObject(Context.TYPE, Context.Type.INPUT);
			else 
				context.setObject(Context.TYPE, Context.Type.OUTPUT);
		}
		return context;
	}
	
	protected Context getContext(String processName){
		DefaultDataSyncMetaInfo metaInfo = processMappings.get(processName);			
		return getContext(metaInfo);
	}
	
	protected boolean hasConnector(String name){
		return connectors.containsKey(name);
	}
	
	protected <T> T getConnector(String name, Class<T> requiredType){
		if(connectors.containsKey(name))
			return (T) connectors.get(name);
		return null;
	}	

	public List<Map<String, Object>> read(String processName) {
		return read(processName, new Object[0]);
	}
	
	public List<Map<String, Object>> read(String processName, Object[] args) {
		Context context = getContext(DEFAULT_READ_CONNECTOR_PREFIX + processName);
		context.setObject("data", args);		
		return (List<Map<String, Object>>) getConnector(context.getConnectorName(), ReadConnector.class).pull(context);
	}	
	
	
	public Object write(String processName, List<Map<String, Object>> input) {		
		Context context = getContext(DEFAULT_WRITE_CONNECTOR_PREFIX + processName);
		context.setObject("data", input);		
		return getConnector( context.getConnectorName(), WriteConnector.class ).deliver(context);		
	}
	
	public Object write(String processName, Map<String, Object> input) {
		
		Context context = getContext(DEFAULT_WRITE_CONNECTOR_PREFIX + processName);
		context.setObject("data", input);		
		return getConnector( context.getConnectorName(), WriteConnector.class ).deliver(context);
	
	}
	

	protected boolean hasReadMapping(String processName){		
		return processMappings.containsKey(DEFAULT_READ_CONNECTOR_PREFIX + processName);
	}
	
	protected boolean hasWriterMapping(String processName){
		return processMappings.containsKey(DEFAULT_WRITE_CONNECTOR_PREFIX + processName);
	}
		
	
}
