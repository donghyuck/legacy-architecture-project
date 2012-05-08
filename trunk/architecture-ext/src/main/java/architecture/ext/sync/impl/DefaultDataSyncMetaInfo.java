package architecture.ext.sync.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.springframework.util.StringUtils;

import architecture.common.adaptor.Pipeline;
import architecture.common.jdbc.ParameterMapping;

/**
 * @author  donghyuck
 */
public class DefaultDataSyncMetaInfo {

		
	/**
	 * @author   donghyuck
	 */
	public enum Type {
		/**
		 * @uml.property  name="rEAD"
		 * @uml.associationEnd  
		 */
		READ, 
		/**
		 * @uml.property  name="wRITE"
		 * @uml.associationEnd  
		 */
		WRITE,
		/**
		 * @uml.property  name="fILTER"
		 * @uml.associationEnd  
		 */
		FILTER,
		/**
		 * @uml.property  name="mERGE"
		 * @uml.associationEnd  
		 */
		MERGE;
	};	
	
	/**
	 * @uml.property  name="type"
	 * @uml.associationEnd  
	 */
	protected Type type = Type.READ ;
	
	/**
	 * @return
	 * @uml.property  name="type"
	 */
	public Type getType() {
		return type;
	}

	protected String name;
	
	protected String queryString = null ;
	
	protected String queryName = null;
	
	protected String connectorName = null;
	
	/**
	 * @uml.property  name="isBatch"
	 */
	protected boolean isBatch = true;
	
	/**
	 * @uml.property  name="pipelineMappings"
	 */
	private List<Pipeline> pipelineMappings = new ArrayList<Pipeline>();

	/**
	 * @uml.property  name="parameterMappings"
	 */
	protected List<ParameterMapping> parameterMappings = new ArrayList<ParameterMapping>();
		
	
	/**
	 * @return
	 * @uml.property  name="pipelineMappings"
	 */
	public List<Pipeline> getPipelineMappings() {
		return pipelineMappings;
	}

	public void setPipelineMappings(List<String> pipelineMappings) {
		for( String pipelineMapping : pipelineMappings ){
			String[] arr = StringUtils.commaDelimitedListToStringArray(pipelineMapping);
			Properties props = StringUtils.splitArrayElementsIntoProperties(arr, "=");
			
			String name = props.getProperty("name", null);
			String match = props.getProperty("", "false");
			if( StringUtils.hasText(name)){
				DefaultPipeline pipeline = new DefaultPipeline();
				pipeline.name = name;
				pipeline.match = Boolean.parseBoolean(match);
				this.pipelineMappings.add(pipeline);
			}
		}
	}

	/**
	 * @return
	 * @uml.property  name="isBatch"
	 */
	public boolean isBatch() {
		return isBatch;
	}

	public void setTypeString(String typeString) {
		this.type = Type.valueOf(typeString.toUpperCase());	
	}

	/**
	 * @param isBatch
	 * @uml.property  name="isBatch"
	 */
	public void setBatch(boolean isBatch) {
		this.isBatch = isBatch;
	}

	/**
	 * @param connectorName
	 * @uml.property  name="connectorName"
	 */
	public void setConnectorName(String connectorName) {
		this.connectorName = connectorName;
	}

	/**
	 * @param queryString
	 * @uml.property  name="queryString"
	 */
	public void setQueryString(String queryString) {
		this.queryString = queryString;
	}

	/**
	 * @param queryName
	 * @uml.property  name="queryName"
	 */
	public void setQueryName(String queryName) {
		this.queryName = queryName;
	}

	/**
	 * @return
	 * @uml.property  name="parameterMappings"
	 */
	public List<ParameterMapping> getParameterMappings() {
		return parameterMappings;
	}

	public void setParameterMappings(List<String> parameterMappings) {
		for( String mappingString : parameterMappings ){
			
			String[] arr = StringUtils.commaDelimitedListToStringArray(mappingString);
			Properties props = StringUtils.splitArrayElementsIntoProperties(arr, "=");
			
			String name = props.getProperty("name");
			
			String index = props.getProperty("index", null);
			
			String javaTypeName = props.getProperty("javaType", null);
			
			String jdbcTypeName = props.getProperty("jdbcType", null);
			
			String pattern = props.getProperty("pattern", null);
			
			String encoding = props.getProperty("encoding", null);
			
			String primary = props.getProperty("primary", "false");
			
			String cipher = props.getProperty("cipher", null);

			String cipherKey = props.getProperty("cipherKey", null);
			
			String cipherKeyAlg = props.getProperty("cipherKeyAlg", null);
			
			ParameterMapping.Builder builder = new ParameterMapping.Builder(name);
			if( index != null )
			    builder.index(Integer.parseInt(index));
			if( encoding != null)
				builder.encoding(encoding);
			if( jdbcTypeName != null)
				builder.jdbcTypeName(jdbcTypeName);
			if( javaTypeName != null )
				builder.javaType(DefaultDataSyncClient.DEAFULT_TYPE_ALIAS_REGISTRY.resolveAlias(javaTypeName));
			if( pattern != null )
				builder.pattern(pattern);	
			if( primary != null )
				builder.primary(Boolean.parseBoolean(primary));	
			
			if( cipher != null )
				builder.cipher(cipher);
			if( cipherKey != null )
				builder.cipherKey(cipherKey);
			if( cipherKeyAlg != null )
				builder.cipherKeyAlg(cipherKeyAlg);
			
			this.parameterMappings.add(builder.build());			
		}
	}
	
	public boolean hasPrimary(){
		for(ParameterMapping m : parameterMappings)
			if( m.isPrimary() ) 
				return true;
		
		return false;
	}
	
	public ParameterMapping getPrimaryParameterMapping(){
		for(ParameterMapping m : parameterMappings)
			if( m.isPrimary() ) 
				return m;		
		return null;
	} 
	
}
