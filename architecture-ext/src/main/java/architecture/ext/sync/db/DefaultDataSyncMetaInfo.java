package architecture.ext.sync.db;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.springframework.util.StringUtils;

import architecture.common.jdbc.ParameterMapping;

public class DefaultDataSyncMetaInfo {

	protected String queryString ;
	
	protected List<ParameterMapping> parameterMappings = new ArrayList<ParameterMapping>();

	public String getQueryString() {
		return queryString;
	}

	public void setQueryString(String queryString) {
		this.queryString = queryString;
	}

	public List<ParameterMapping> getParameterMappings() {
		return parameterMappings;
	}

	public void setParameterMappings(List<String> parameterMappings) {
		for( String mappingString : parameterMappings ){
			
			String[] arr = StringUtils.commaDelimitedListToStringArray(mappingString);
			Properties props = StringUtils.splitArrayElementsIntoProperties(arr, "=");
			String name = props.getProperty("name");
			String javaTypeName = props.getProperty("javaType", null);
			String jdbcTypeName = props.getProperty("jdbcType", null);
			String pattern = props.getProperty("pattern", null);
			ParameterMapping.Builder builder = new ParameterMapping.Builder(name);
			if( jdbcTypeName != null)
				builder.jdbcTypeName(jdbcTypeName);
			if( javaTypeName != null )
				builder.javaType(DefaultDataSyncClient.DEAFULT_TYPE_ALIAS_REGISTRY.resolveAlias(javaTypeName));
			if( pattern != null )
				builder.pattern(pattern);			
			this.parameterMappings.add(builder.build());			
		}
	}
	
	
	
}
