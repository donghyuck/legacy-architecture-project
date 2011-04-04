/*
 * Copyright 2010, 2011 INKIUM, Inc.
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
package architecture.ee.jdbc.query.factory.impl;

import java.io.StringReader;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.commons.collections.primitives.ArrayIntList;
import org.apache.commons.collections.primitives.IntList;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.SqlTypeValue;
import org.springframework.jdbc.core.StatementCreatorUtils;

import architecture.ee.jdbc.query.SqlQuery;
import architecture.ee.jdbc.query.factory.Configuration;
import architecture.ee.jdbc.query.mapping.BoundSql;
import architecture.ee.jdbc.query.mapping.MappedStatement;
import architecture.ee.jdbc.recordset.Recordset;
import architecture.ee.jdbc.recordset.RecordsetException;
import architecture.ee.jdbc.recordset.RecordsetField;
import architecture.ee.spring.jdbc.support.ExtendedJdbcTemplate;

public class SqlQueryImpl<T> implements SqlQuery {
	
	private int startIndex = 0 ;	
	
	private int maxResults = 0;
	
	private ExtendedJdbcTemplate jdbcTemplate;
	
	private Configuration configuration;
	
	private Map<String, Object> additionalParameters;
	
    private List<Object> values;
    
    private Class<?> returnType = DEFAULT_RETURN_TYPE;
    
    private IntList types;
    
	private LinkedList<Object[]> parameterQueue;	
	
	private String statement;

	private Log log = LogFactory.getLog(getClass());
			
	private boolean stopOnError = true;
	
		
	//*********************************************
	// Public Methods 
	// ********************************************

	
	public void setStopOnError(boolean stopOnError){
		this.stopOnError = stopOnError;
	}
	
	public SqlQuery reset(){					
		this.startIndex = 0 ;
		this.maxResults = 0;	
		this.additionalParameters.clear();		
		this.types.clear();
		this.values.clear();
		this.returnType = DEFAULT_RETURN_TYPE;
		if(parameterQueue != null && parameterQueue.size() > 0)
			this.parameterQueue.clear();
		return this;
	}	
	
	public int getStartIndex() {
		return this.startIndex;
	}
	
	public int getMaxResults() {
		return this.maxResults;
	}
	
	public SqlQuery setStartIndex(int startIndex) {
		this.startIndex = startIndex;
		return this;
	}

	public SqlQuery setMaxResults(int maxResults) {
		this.maxResults = maxResults;
		return this;
	}
	
	public SqlQuery setDataSource(DataSource dataSource){
		this.jdbcTemplate = new ExtendedJdbcTemplate(dataSource);
		return this;
	}
	

	public SqlQuery setAdditionalParameter(String key, Object value) {
		additionalParameters.put(key, value);
		return this;
	}
	
    public Object getAdditionalParameter(String key) {
		return additionalParameters.get(key);
	}
		
	public String getStatement() {
		return statement;
	}

	public SqlQuery setStatement(String statement) {
		this.statement = statement;
		return reset();
	}
	
	public Class<?> getReturnType() {
		return returnType;
	}

	public SqlQuery setReturnType(Class<?> returnType) {
		this.returnType = returnType;
		return this;
	}

	/**
	 * 
	 * @param value
	 * @return
	 */
	public SqlQuery setString(String value) {		
		return setParameter(value, Types.VARCHAR);
	}		
	
	/**
	 * 
	 * @param value
	 * @param type
	 * @return
	 */
	public SqlQuery setParameter(Object value, int type) {
		Object valueToUse = value;
		if( valueToUse == null )
			valueToUse = " ";		
		this.values.add(valueToUse);
		this.types.add(type);
		return this;
	}
	
	/**
	 * 
	 * @param values
	 * @return
	 */
	public SqlQuery setParameters(Object[] values) {
		Object[] valuesToUse = values;
		this.values = Arrays.asList(valuesToUse);
		for(int i = 0; i < valuesToUse.length; i++)
            this.types.add(SqlTypeValue.TYPE_UNKNOWN);			
		return this;
	}

	/**
	 * 
	 * @param values
	 * @param types
	 * @return
	 */
	public SqlQuery setParameters(Object[] values, int[] types) {
		Object[] valuesToUse = values;
		int [] typesToUse = types;		
		this.values = Arrays.asList(valuesToUse);
		for(int i = 0; i < typesToUse.length; i++)
            this.types.add(typesToUse[i]);
		return this;		
	}	
	
	
	/**
	 * 
	 * @param statement 
	 */
	public List<Map<String, Object>> queryForList(String statement) {
		return queryForList(statement, null, null);
	}

	/**
	 * 
	 * @param statement
	 * @param elementType
	 * @param params
	 * @param paramTypes
	 * @return
	 */
	public List<Map<String, Object>> queryForList(String statement, Object[] params, int[] paramTypes){		
		BoundSql sql = getBoundSql(statement, params);
		if( maxResults > 0 ){
			return jdbcTemplate.queryScrollable(sql.getSql(), startIndex, maxResults, params, paramTypes);
		}else{
			return jdbcTemplate.queryForList(sql.getSql(), params, paramTypes );
		}		
	}	
	
	
	/**
	 * 
	 * @param statement
	 * @param elementType 
	 */
	public <T> List<T> queryForList(String statement, Class<T> elementType){		
		return queryForList(statement, null, null, elementType);
	}

	/**
	 * 
	 * @param statement
	 * @param elementType
	 * @param params
	 * @param types
	 * @return
	 */
	public <T> List<T> queryForList(String statement, Object[] params, int[] types, Class<T> elementType){		
		BoundSql sql = getBoundSql(statement, params);		
		if( maxResults > 0 ){
			return jdbcTemplate.queryScrollable(sql.getSql(), startIndex, maxResults, elementType, params, types);
		}else{
			return jdbcTemplate.queryForList(sql.getSql(), params, types, elementType);
		}		
	}			

	/**
	 * 
	 * 
	 * @param statement
	 * @param elementType 
	 */
	public <T> T queryForObject (String statement, Class<T> elementType) {
		BoundSql sql = getBoundSql(statement, null);	
		return jdbcTemplate.queryForObject(sql.getSql(), elementType );
	}

	/**
	 * 
	 * 
	 * @param statement
	 */
	public Map<String, Object> queryForMap (String statement) {
		BoundSql sql = getBoundSql(statement, null);	
		return jdbcTemplate.queryForMap(sql.getSql());
	}

	/**
	 * 
	 * @param statement
	 * @param params
	 * @param paramTypes
	 */
	public Map<String, Object> queryForMap(String statement, Object[] params, int[] paramTypes) {
		BoundSql sql = getBoundSql(statement, params);	
		return jdbcTemplate.queryForMap(sql.getSql(), params, paramTypes);
	}

	/**
	 * 
	 * @param statement
	 * @param elementType
	 * @param params
	 * @param paramTypes
	 */
	public <T> T queryForObject(String statement, Class<T> elementType, Object[] params, int[] paramTypes) {
		BoundSql sql = getBoundSql(statement, params);	
		return jdbcTemplate.queryForObject(sql.getSql(), elementType, params, paramTypes );
	}
		
	public Object uniqueResult() {		
		if( getReturnType().isAssignableFrom(Map.class) ){		
			if(values.size()>0)
				return queryForMap(getStatement(), values.toArray(), types.toArray() );		
			else 
				return queryForMap(getStatement());		
		}		
		if(values.size()>0)
			return queryForObject(getStatement(), getReturnType(), values.toArray(), types.toArray());
		else 
			return queryForObject(getStatement(), getReturnType());
	}
	
	public List list() {
		if( getReturnType().isAssignableFrom(Map.class) ){
			
			if(values.size()>0)
				return queryForList(getStatement(), values.toArray(), types.toArray());
			else 
				return queryForList(getStatement());
		}
		
		if(values.size()>0)
			return queryForList(getStatement(), values.toArray(), types.toArray(), getReturnType());
		else 
			return queryForList(getStatement(), getReturnType());
	}

	
	//*********************************************
	// Recordset Methods 
	//********************************************


	public Object executeScript(){			
		BoundSql sql = getBoundSql(getStatement(), null);		
		final StringReader reader = new StringReader(sql.getSql());		
		return jdbcTemplate.executeScript(stopOnError, reader);			
	}
	
    public SqlQuery addToBatch() {
		if( parameterQueue == null)
			this.parameterQueue =  new LinkedList<Object[]>();				
    	this.parameterQueue.add(this.values.toArray());
    	values.clear();
        return this;
    }
	    
	public int executeUpdate() {
		BoundSql sql = getBoundSql(getStatement(), null);	
		return jdbcTemplate.update(sql.getSql(), values.toArray(), types.toArray());   	
	}
	
	/**
	 * 
	 * 
	 * @param statement
	 * @param recordset
	 * @param params
	 */
    public int executeUpdate(String statement, Recordset recordset, String[] params){  
    	if ( params.length == 0 || params.length > recordset.getFieldCount()){
    		// 잘못된 파마메터 정보이다.
    	}    	
    	
    	return 0;
    }
	
	public int[] executeBatchUpdate() {
		
        if (this.parameterQueue == null) {
            return new int[0];
        }		
        if (this.parameterQueue.isEmpty()) {
            return new int[0];
        }
        BoundSql sql = getBoundSql(getStatement(), null);		
        return jdbcTemplate.batchUpdate(
                sql.getSql(),
                new BatchPreparedStatementSetter() {            
                    public int getBatchSize() {
                        return parameterQueue.size();
                    }           
                    public void setValues(PreparedStatement ps, int index) throws SQLException {                        
                        int sqlColIndx = 1;
                        Object[] params = (Object[]) parameterQueue.removeFirst();                                              
                        List<Object> parameters = Arrays.asList(params);
                        for (int i = 0; i < parameters.size(); i++) {
                            Object in = parameters.get(i);
                            StatementCreatorUtils.setParameterValue(ps, sqlColIndx++, SqlTypeValue.TYPE_UNKNOWN, in);
                        }
                    }                   
                });
        
	}

	public int[] executeBatchUpdate(String statement, Recordset recordset, String[] params) {
		
		BoundSql sql = getBoundSql(getStatement(), null);
		
		class RecordsetBatchPreparedStatementSetter implements BatchPreparedStatementSetter {
			
			private Recordset recordset;
			private String[] params;			
			
			protected RecordsetBatchPreparedStatementSetter(Recordset recordset, String[] params) {
				this.recordset = recordset;
				this.params = params;
			}

			public void setValues(PreparedStatement ps, int i) throws SQLException {
				
				HashMap<String, RecordsetField> flds = recordset.getFields();
				try {
					recordset.setRecordNumber(i);
				} catch (RecordsetException e) {
				}
				
				for (int x = 0 ; i<params.length; x++)
				{
					RecordsetField f = (RecordsetField)flds.get(params[x]);
					Object value = null;
					try {
						value = recordset.getValue(params[i]);
					} catch (Throwable e) {
					}
					if (value == null)
					{
						ps.setNull(x+1, f.getType());	
					}
					else
					{
						//PATCH 2005-04-15 - support for SQLDATE instead of DATE
						if (f.getType()==java.sql.Types.DATE)
						{
							java.util.Date d = (java.util.Date)value;
							value = new Date(d.getTime());
						}						
						ps.setObject(x+1, value, f.getType());
					}
				}
			}

			public int getBatchSize() {
				return recordset.getRecordCount();
			}
		
		}		
		return jdbcTemplate.batchUpdate(sql.getSql(), new RecordsetBatchPreparedStatementSetter( recordset, params));
	}
	
	//*********************************************
	// Protected Methods 
	// ********************************************
	
	protected SqlQueryImpl(Configuration configuration) {		
		this.configuration = configuration;		
		this.additionalParameters = new HashMap<String, Object>(4);
		this.values = new ArrayList<Object>(4);
		this.types = new ArrayIntList(4);
	}

	protected SqlQueryImpl(Configuration configuration, DataSource dataSource) {
		this.configuration = configuration;
		this.additionalParameters = new HashMap<String, Object>(4);
		this.values = new ArrayList<Object>(4);
		this.types = new ArrayIntList(4);
		setDataSource(dataSource);
	}	
	
	protected SqlQueryImpl(Configuration configuration, ExtendedJdbcTemplate jdbcTemplate) {
		this.configuration = configuration;
		this.additionalParameters = new HashMap<String, Object>(4);
		this.values = new ArrayList<Object>(4);
		this.types = new ArrayIntList(4);
		this.jdbcTemplate = jdbcTemplate;
	}
		
	/**
	 * 
	 * @param statement
	 * @return
	 */
	protected BoundSql getBoundSql(String statement, Object params ){
		MappedStatement stmt = configuration.getMappedStatement(statement);
		BoundSql sql ;
		if(additionalParameters.size() > 0)
			sql = stmt.getBoundSql(params, this.additionalParameters);	
		else
			sql = stmt.getBoundSql(params);	
		return sql;
	}

}