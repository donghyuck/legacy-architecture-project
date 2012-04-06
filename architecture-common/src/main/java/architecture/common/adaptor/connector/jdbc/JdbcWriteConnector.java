package architecture.common.adaptor.connector.jdbc;

import java.util.List;
import java.util.Map;

import architecture.common.adaptor.Context;
import architecture.common.adaptor.WriteConnector;
import architecture.common.jdbc.ParameterMapping;
import architecture.common.util.StringUtils;

public class JdbcWriteConnector extends AbstractJdbcConnector implements WriteConnector {

		
	public Object deliver(Context context) {		
		String queryString = context.getObject("queryString", String.class);
		if(StringUtils.isEmpty(queryString)){
			String queryName = context.getObject("queryName", String.class);
			if(!StringUtils.isEmpty(queryName)){
				queryString = getQueryString(queryName);
			}else{
				// 쿼리가 존재하지 않는 경우는 어떻게..
			}
		}		
		List<ParameterMapping> parameterMappings = context.getObject("parameterMappings", List.class);
		
		@SuppressWarnings("unchecked")
				
		
		boolean isBatch = context.getObject("batch", Boolean.class);
		if(isBatch){
			List<Map<String, Object>> data = context.getObject("data", List.class);
			return deliver(queryString, parameterMappings, data);
		}else{
			Object data = context.getObject("data");
			log.debug( "row:" + data);
			if(data instanceof Map)
				return deliver(queryString, parameterMappings, (Map<String, Object>) data);	
			else 
				return deliver(queryString);
		}
	}

	@Override
	protected String getQueryString(String key) {
		return null;
	}

}
