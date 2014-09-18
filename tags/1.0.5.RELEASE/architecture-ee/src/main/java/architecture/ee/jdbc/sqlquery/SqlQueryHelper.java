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
package architecture.ee.jdbc.sqlquery;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.core.SqlParameterValue;
import org.springframework.jdbc.core.support.SqlLobValue;
import org.springframework.jdbc.support.lob.DefaultLobHandler;
import org.springframework.jdbc.support.lob.LobHandler;

import architecture.common.util.io.SharedByteArrayOutputStream;
import architecture.ee.jdbc.sqlquery.factory.impl.SqlQueryImpl;


public class SqlQueryHelper {
		
	static class InputStreamRowMapper implements RowMapper<InputStream> {
		
		public InputStream mapRow(ResultSet rs, int rowNum) throws SQLException {
			InputStream in = null;
			InputStream inputstream;
			try
            {
                in = rs.getBinaryStream(1);
                SharedByteArrayOutputStream out = new SharedByteArrayOutputStream();
                byte buf[] = new byte[1024];
                int len;
                while((len = in.read(buf)) >= 0) 
                    out.write(buf, 0, len);
                out.flush();
                inputstream = out.getInputStream();
            }
            catch(IOException ioe)
            {
                throw new SQLException(ioe.getMessage());
            }
			return inputstream;
		}		
	}
	
	public static RowMapper<InputStream> getInputStreamRowMapper(){
		return new InputStreamRowMapper();
	}
	
	public SqlQueryHelper() {
	
	}
	
	public SqlQueryHelper(LobHandler lobHandler) {
		this.lobHandler = lobHandler;
	}

	private LobHandler lobHandler = new DefaultLobHandler() ;
	
	private List<Object> values = new ArrayList<Object>(4);

	private LinkedList<Object[]> parameterQueue = new LinkedList<Object[]>();
	
	private Map<String, Object> additionalParameters = new HashMap<String, Object>(4);
	
	public List<Map<String, Object>> list (SqlQuery sqlQuery, String statement){		
		if( sqlQuery instanceof SqlQueryImpl){
			return sqlQuery.setAdditionalParameters(additionalParameters).queryForList(statement, values.size() == 0 ? null : values.toArray() );			
		}
		return sqlQuery.queryForList( statement, values.size() == 0 ? null : values.toArray());
	}
	
	public SqlQueryHelper parameter(Object value) {
		values.add(value);
		return this;
	}
	
	public SqlQueryHelper lob(byte[] bytes){
		SqlLobValue value = new SqlLobValue(bytes, lobHandler);		
		values.add(value);
		return this;
	}

	public SqlQueryHelper lob(String content){
		SqlLobValue value = new SqlLobValue(content, lobHandler);		
		values.add(value);
		return this;
	}
	
	public SqlQueryHelper lob(File file) throws IOException {		
		InputStream input = FileUtils.openInputStream(file);
        long size = file.length();        
		return lob(input, (int)size);
	}
		
	public SqlQueryHelper lob(InputStream stream, int length){
		SqlLobValue value = new SqlLobValue(stream, length, lobHandler);		
		values.add(value);
		return this;
	}
	
	public SqlQueryHelper lob(Reader reader, int length){
		SqlLobValue value = new SqlLobValue(reader, length, lobHandler);		
		values.add(value);
		return this;
	}
		
	public SqlQueryHelper additionalParameter(String name, Object value) {
		additionalParameters.put(name, value);
		return this;
	}

	public SqlQueryHelper additionalParameters(Map<String, Object> args) {
		additionalParameters.putAll(args);
		return this;
	}
	
	public SqlQueryHelper parameter(SqlParameter parameter) {
		values.add(parameter);
		return this;
	}
	

	public SqlQueryHelper parameter(Object value, int sqlType) {
		SqlParameterValue parameterValue = new SqlParameterValue(sqlType, value);
		values.add(parameterValue);
		return this;
	}
	
	public SqlQueryHelper parameters(Object[] args) {
		for (Object value : args) {
			values.add(value);
		}
		return this;
	}

	public SqlQueryHelper parameters(Object[] args, int [] sqlTypes) {
		for( int i = 0 ; i < args.length ; i ++ ){
			Object value = args[i];
			int sqlType = sqlTypes [i];
			SqlParameterValue parameterValue = new SqlParameterValue(sqlType, value);
			values.add(parameterValue);
		}
		return this;
	}
	
	public SqlQueryHelper inqueue(){
		if (parameterQueue == null)
			this.parameterQueue = new LinkedList<Object[]>();
		this.parameterQueue.add(this.values.toArray());
		values.clear();
		return this;
	}
		
	public int executeBatchUpdate(SqlQuery sqlQuery, String statement){
		try {
			return sqlQuery.executeUpdate(statement, parameterQueue);
		} finally {
			reset();
		}
	}
		
	public Object[] values() {		
		return values.toArray();	
	}	
	
	public void reset(){
		values.clear();
		parameterQueue.clear();
	}
}
