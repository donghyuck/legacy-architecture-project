/*
 * Copyright 2012 Donghyuck, Son
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package architecture.common.adaptor.connector.jdbc;

import java.util.Collections;
import java.util.List;

import architecture.common.adaptor.Context;
import architecture.common.adaptor.ReadConnector;
import architecture.common.jdbc.ParameterMapping;
import architecture.common.util.StringUtils;

/**
 * 
 * @author donghyuck
 *
 */
public class JdbcReadConnector extends AbstractJdbcConnector implements ReadConnector {

	
	@SuppressWarnings("unchecked")
	public Object pull(Context context) {			
				
		log.debug("pull : " + context );
		
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
		Object[] data = context.getObject("data", Object[].class);		
		
		if( parameterMappings == null ){
			parameterMappings = Collections.EMPTY_LIST;
		}
		
		if( data == null)
		{
			data = new Object[0];
		}
		
		//Object output;		
		
		return pull(queryString, parameterMappings, data);

	}

	@Override
	protected String getQueryString(String key) {
		return null;
	}
}
