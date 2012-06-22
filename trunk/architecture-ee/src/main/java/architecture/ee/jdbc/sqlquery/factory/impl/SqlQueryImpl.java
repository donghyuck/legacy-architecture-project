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
package architecture.ee.jdbc.sqlquery.factory.impl;

import java.io.StringReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.jdbc.core.ColumnMapRowMapper;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.SingleColumnRowMapper;

import architecture.common.jdbc.ParameterMapping;
import architecture.ee.jdbc.sequencer.incrementer.MaxValueIncrementer;
import architecture.ee.jdbc.sqlquery.SqlQuery;
import architecture.ee.jdbc.sqlquery.factory.Configuration;
import architecture.ee.jdbc.sqlquery.mapping.BoundSql;
import architecture.ee.jdbc.sqlquery.mapping.MappedStatement;
import architecture.ee.spring.jdbc.ExtendedJdbcTemplate;

/**
 * @author   donghyuck
 */
public class SqlQueryImpl implements SqlQuery {
	
	private Log log = LogFactory.getLog(getClass());
	private boolean stopOnError = true;

	private Configuration configuration;

	private ExtendedJdbcTemplate jdbcTemplate;
	private int startIndex = 0;
	private int maxResults = 0;

	private MaxValueIncrementer incrementer ;
	
	private Map<String, Object> additionalParameters = new HashMap<String, Object>(4);
	
	
	public SqlQuery reset() {
		this.startIndex = 0;
		this.maxResults = 0;
		return this;
	}
	
	public MaxValueIncrementer getIncrementer() {
		return incrementer;
	}

	public SqlQuery setAdditionalParameters(Map<String, Object> additionalParameters) {
		this.additionalParameters = additionalParameters;
		return this;
	}
	
	

	// *********************************************
	// Protected Methods
	// ********************************************
	protected SqlQueryImpl(Configuration configuration, MaxValueIncrementer incrementer) {
		this.configuration = configuration;
		this.incrementer = incrementer;
	}

	protected SqlQueryImpl(Configuration configuration, DataSource dataSource, MaxValueIncrementer incrementer) {
		this.configuration = configuration;
		this.incrementer = incrementer;
		setDataSource(dataSource);
	}

	protected SqlQueryImpl(Configuration configuration, ExtendedJdbcTemplate jdbcTemplate, MaxValueIncrementer incrementer) {
		this.configuration = configuration;
		this.incrementer = incrementer;
		this.jdbcTemplate = jdbcTemplate;
	}
	
	protected BoundSql getBoundSql(String statement) {		
		MappedStatement stmt = configuration.getMappedStatement(statement);
		return stmt.getBoundSql(null);
	}
	
	protected BoundSql getBoundSql(String statement, Object parameter) {		
		MappedStatement stmt = configuration.getMappedStatement(statement);	
		BoundSql sql;
		if( additionalParameters.size() > 0)
			sql = stmt.getBoundSql(parameter, additionalParameters);
		else 
			sql = stmt.getBoundSql(parameter);
		
		additionalParameters.clear();
		return sql;
	}
	
	protected BoundSql getBoundSql(String statement, Object parameter, Object additionalParameters ) {		
		MappedStatement stmt = configuration.getMappedStatement(statement);
		BoundSql sql =stmt.getBoundSql(parameter, additionalParameters);
		return sql;
	}
	
	// *********************************************
	// Public Methods from SqlQuery interface
	// ********************************************
	
	public SqlQuery setDataSource(DataSource dataSource) {		
		this.jdbcTemplate = new ExtendedJdbcTemplate(dataSource);
		this.jdbcTemplate.initialize();		
		return this;
	}

	/**
	 * @param startIndex
	 * @return
	 * @uml.property  name="startIndex"
	 */
	public SqlQuery setStartIndex(int startIndex) {
		this.startIndex = startIndex;
		return this;
	}

	/**
	 * @param maxResults
	 * @return
	 * @uml.property  name="maxResults"
	 */
	public SqlQuery setMaxResults(int maxResults) {
		this.maxResults = maxResults;
		return this;
	}
	
	
	// SPRING Style API
	public <T> T queryForObject(String statement, Class<T> elementType) {
		BoundSql sql = getBoundSql(statement);		
		if( elementType == Map.class  ){
			return (T) queryForObject(statement, new ColumnMapRowMapper());
		}else{
			return jdbcTemplate.queryForObject(sql.getSql(), elementType );
		}		
	}
	
	public <T> T queryForObject(String statement, RowMapper<T> rowMapper) {
		BoundSql sql = getBoundSql(statement);
		return jdbcTemplate.queryForObject(sql.getSql(), rowMapper);
	}

	
	public <T> T queryForObject(String statement, Object[] params, int[] paramTypes, Class<T> elementType) {
		BoundSql sql = getBoundSql(statement, params);		
		
		if( elementType == Map.class  ){
			return (T)jdbcTemplate.queryForObject(sql.getSql(), params, paramTypes, new ColumnMapRowMapper());	
		}else{
			return jdbcTemplate.queryForObject(sql.getSql(), params, paramTypes, elementType);	
		}
	}
		
	public <T> T queryForObject(String statement, Object[] params, int[] paramTypes, RowMapper<T> rowMapper) {		
		BoundSql sql = getBoundSql(statement, params);		
		return jdbcTemplate.queryForObject(sql.getSql(), params, paramTypes, rowMapper);		
	}
	
	public Map<String, Object> queryForMap(String statement) {
		BoundSql sql = getBoundSql(statement);		
		return jdbcTemplate.queryForMap(sql.getSql());
	}

	public Map<String, Object> queryForMap(String statement, Object[] params, int[] paramTypes) {
		BoundSql sql = getBoundSql(statement, params);		
		return jdbcTemplate.queryForMap(sql.getSql(), params, paramTypes);
	}

	public Map<String, Object> queryForMap(String statement, Object[] params, int[] paramTypes, Object additionalParameters) {
		BoundSql sql = getBoundSql(statement, params, additionalParameters);		
		return jdbcTemplate.queryForMap(sql.getSql(), params, paramTypes);
	}
	
	
	
	public <T> List<T> queryForList(String statement, Class<T> elementType) {
		return queryForList(statement, new Object[0], new int[0], elementType );
	}

	public <T> List<T> queryForList(String statement, Object[] params, int[] paramTypes, Class<T> elementType) {
		BoundSql sql = getBoundSql(statement, params);		
		if( this.maxResults > 0 ){
				return jdbcTemplate.queryScrollable(sql.getSql(), startIndex, maxResults, params, paramTypes, elementType);
		}else{
			return jdbcTemplate.queryForList(sql.getSql(), params, paramTypes, elementType);	
		}
	}
	
	public <T> List<T> queryForList(String statement, RowMapper<T> rowMapper) {
		return queryForList(statement, new Object[0], new int[0], rowMapper);
	}

	public <T> List<T> queryForList(String statement, Object[] params, int[] paramTypes, RowMapper<T> rowMapper) {
		BoundSql sql = getBoundSql(statement, params);		
		if( this.maxResults > 0 ){
			return jdbcTemplate.queryScrollable(sql.getSql(), startIndex, maxResults, params, paramTypes, rowMapper);			
		}else{
			return jdbcTemplate.query(sql.getSql(), params, paramTypes, rowMapper);	
		}
	}	
	
	public List<Map<String, Object>> queryForList(String statement) {
		return queryForList(statement, new Object[0], null);
	}

	public List<Map<String, Object>> queryForList(String statement, Object[] params) {
		return queryForList(statement, params, null);
	}

	public List<Map<String, Object>> queryForList(String statement, Object[] params, int[] paramTypes) {
		BoundSql sql = getBoundSql( statement, params );		
		if( this.maxResults > 0 ){
			return jdbcTemplate.queryScrollable(sql.getSql(), startIndex, maxResults, params, paramTypes, new ColumnMapRowMapper());			
		}else{
			return jdbcTemplate.query(sql.getSql(), params, paramTypes, new ColumnMapRowMapper());	
		}
	}

	// New Style API : idea from hibernate 
	public <T> T uniqueResult(String statement, Class<T> elementType) {
		return queryForObject(statement, elementType);
	}

	public <T> T uniqueResult(String statement, RowMapper<T> rowMapper) {
		return queryForObject(statement, rowMapper);
	}

	public <T> T uniqueResult(String statement, Object parameter, Class<T> elementType) {
		if(elementType == Map.class )
			return (T)uniqueResult(statement, parameter,  new ColumnMapRowMapper());
		return uniqueResult(statement, parameter, new SingleColumnRowMapper<T>(elementType));
	}

	public <T> T uniqueResult(String statement, Object parameter, RowMapper<T> rowMapper) {		
		BoundSql sql = getBoundSql(statement, parameter);		
		List<ParameterMapping> parameterMappings = sql.getParameterMappings();		
		if( parameter instanceof Map )
			return jdbcTemplate.queryForObject(sql.getSql(), parameterMappings, (Map<String, Object>) parameter, rowMapper );
		else if (parameter instanceof Object[])
			return jdbcTemplate.queryForObject(sql.getSql(), (Object[])parameter, rowMapper);
		else 
			return jdbcTemplate.queryForObject(sql.getSql(), parameter == null ? new Object[0]: new Object[]{ parameter }, rowMapper);		
	}
	
	public List<Map<String, Object>> list(String statement) {
		return list(statement,  null, new ColumnMapRowMapper());
	}

	public List<Map<String, Object>> list(String statement, Object parameter) {
		return list(statement, parameter, new ColumnMapRowMapper());
	}
	
	public <T> List<T> list(String statement, Class<T> elementType) {
		return list( statement, new SingleColumnRowMapper<T>(elementType));
	}
	
	public <T> List<T> list(String statement, RowMapper<T> rowMapper) {
		BoundSql sql = getBoundSql(statement);		
		if( this.maxResults > 0 ){
			return jdbcTemplate.queryScrollable(sql.getSql(), startIndex, maxResults, new Object[0], new int[0], rowMapper);			
		}else{
			return jdbcTemplate.query(sql.getSql(), rowMapper);	
		}
	}
	
	public <T> List<T> list(String statement, Object parameter, Class<T> elementType) {
		return list(statement, parameter, new SingleColumnRowMapper<T>(elementType));
	}

	public <T> List<T> list(String statement, Object parameter, RowMapper<T> rowMapper) {
		
		BoundSql sql = getBoundSql(statement, parameter);
		
		List<ParameterMapping> parameterMappings = sql.getParameterMappings();

		if( this.maxResults > 0 ){
			if( parameter instanceof Map ){
				return jdbcTemplate.queryScrollable(sql.getSql(), parameterMappings, startIndex, maxResults, (Map<String, Object>)parameter, rowMapper);
			}else if (parameter instanceof Object[]){
				return jdbcTemplate.queryScrollable(sql.getSql(), startIndex, maxResults, (Object[])parameter, null, rowMapper);
			}else{				
				return jdbcTemplate.queryScrollable(sql.getSql(), startIndex, maxResults, parameter == null ? new Object[0] : new Object[]{ parameter }, null, rowMapper);	
			}
		}else{
			if( parameter instanceof Map )
				return jdbcTemplate.queryForList(sql.getSql(), parameterMappings, (Map<String, Object>)parameter, rowMapper );
			else if (parameter instanceof Object[])
				return jdbcTemplate.query(sql.getSql(), (Object[]) parameter, rowMapper);
			else 
				return jdbcTemplate.query(sql.getSql(), parameter == null ? new Object[0]: new Object[]{ parameter }, rowMapper);	
		}		
	}

	
	public int update (String statement, Object[] values, int[] types ){
		BoundSql sql = getBoundSql(statement, values);
		return jdbcTemplate.update(sql.getSql(), values, types) ;
	}
	
	public int executeUpdate(String statement){
		BoundSql sql = getBoundSql(statement);
		return jdbcTemplate.update(sql.getSql());
	}
	
	public int executeUpdate(String statement, Object parameter) {
		BoundSql sql = getBoundSql(statement, parameter);
		List<ParameterMapping> parameterMappings = sql.getParameterMappings();		
		if( parameter instanceof Map ){
			return jdbcTemplate.update(sql.getSql(), parameterMappings, (Map<String, Object>)parameter );
		} else if (parameter instanceof List) {	
			// Batch Job Processing :
			List list = (List)parameter;
			if( list.size() > 0 ){
				Object obj = list.get(0);
				if( obj instanceof Object[])
				{
					jdbcTemplate.batchUpdate(sql.getSql(), (List<Object[]>) parameter );
				}else if ( obj instanceof Map ){
					jdbcTemplate.batchUpdate(sql.getSql(), parameterMappings, (List<Map<String, Object>>) parameter);
				}
			}			
			return ((List)parameter).size();			
		} else if (parameter instanceof Object[]) {
			return jdbcTemplate.update(sql.getSql(), (Object[])parameter );			
		}		
		return 0;
	}
	

	public Object executeScript(String statement) {
		BoundSql sql = getBoundSql(statement);
		return jdbcTemplate.executeScript(stopOnError, new StringReader(sql.getSql()));
	}
	

	
	
	/*
	*//**
	 *//*
	private int startIndex = 0;

	*//**
	 *//*
	private int maxResults = 0;

	*//**
	 *//*
	private ExtendedJdbcTemplate jdbcTemplate;

	*//**
	 *//*
	private Configuration configuration;

	private Map<String, Object> additionalParameters;

	private List<Object> values;

	*//**
	 *//*
	private Class<?> returnType = DEFAULT_RETURN_TYPE;

	private IntList types;

	private LinkedList<Object[]> parameterQueue;

	*//**
	 *//*
	private String statement;

	private Log log = LogFactory.getLog(getClass());

	private boolean stopOnError = true;

	// *********************************************
	// Public Methods
	// ********************************************

	*//**
	 * @param stopOnError
	 *//*
	public void setStopOnError(boolean stopOnError) {
		this.stopOnError = stopOnError;
	}

	public SqlQuery reset() {
		this.startIndex = 0;
		this.maxResults = 0;
		this.additionalParameters.clear();
		this.types.clear();
		this.values.clear();
		this.returnType = DEFAULT_RETURN_TYPE;
		if (parameterQueue != null && parameterQueue.size() > 0)
			this.parameterQueue.clear();
		return this;
	}

	*//**
	 * @return
	 *//*
	public int getStartIndex() {
		return this.startIndex;
	}

	*//**
	 * @return
	 *//*
	public int getMaxResults() {
		return this.maxResults;
	}

	*//**
	 * @param startIndex
	 * @return
	 *//*
	public SqlQuery setStartIndex(int startIndex) {
		this.startIndex = startIndex;
		return this;
	}

	*//**
	 * @param maxResults
	 * @return
	 *//*
	public SqlQuery setMaxResults(int maxResults) {
		this.maxResults = maxResults;
		return this;
	}

	public SqlQuery setDataSource(DataSource dataSource) {		
		this.jdbcTemplate = new ExtendedJdbcTemplate(dataSource);
		this.jdbcTemplate.initialize();		
		return this;
	}

	public SqlQuery setAdditionalParameter(String key, Object value) {
		additionalParameters.put(key, value);
		return this;
	}

	public Object getAdditionalParameter(String key) {
		return additionalParameters.get(key);
	}

	*//**
	 * @return
	 *//*
	public String getStatement() {
		return statement;
	}

	*//**
	 * @param statement
	 * @return
	 *//*
	public SqlQuery setStatement(String statement) {
		this.statement = statement;
		return reset();
	}
	
	public SqlQuery setStatement(String catelogy, String key) {
		this.statement = catelogy + "." + key;
		return reset();
	}	

	*//**
	 * @return
	 *//*
	public Class<?> getReturnType() {
		return returnType;
	}

	*//**
	 * @param returnType
	 * @return
	 *//*
	public SqlQuery setReturnType(Class<?> returnType) {
		this.returnType = returnType;
		return this;
	}

	*//**
	 * 
	 * @param value
	 * @return
	 *//*
	public SqlQuery setString(String value) {
		return setParameter(value, Types.VARCHAR);
	}

	*//**
	 * 
	 * @param value
	 * @param type
	 * @return
	 *//*
	public SqlQuery setParameter(Object value, int type) {
		Object valueToUse = value;
		if (valueToUse == null)
			valueToUse = " ";
		this.values.add(valueToUse);
		this.types.add(type);
		return this;
	}

	*//**
	 * 
	 * @param values
	 * @return
	 *//*
	public SqlQuery setParameters(Object[] values) {
		
		Object[] valuesToUse = values;
		//this.values = Arrays.asList(valuesToUse);
				
		if(!this.values.isEmpty())
			this.values.clear();				
		for ( Object value : valuesToUse){
			log.debug("value=" + value);
			this.values.add(value);
		}		
		for (int i = 0; i < valuesToUse.length; i++)
			this.types.add(SqlTypeValue.TYPE_UNKNOWN);	
		return this;
	}

	*//**
	 * 
	 * @param values
	 * @param types
	 * @return
	 *//*
	public SqlQuery setParameters(Object[] values, int[] types) {
		//Object[] valuesToUse = values;		
		int[] typesToUse = types;		
		if(!this.values.isEmpty())
			this.values.clear();		
		
		for ( Object value : values){
			//log.debug("value=" + value);
			this.values.add(value);
		}
		
		for (int i = 0; i < typesToUse.length; i++){
			this.types.add(typesToUse[i]);
		}	
		
		return this;
	}

	*//**
	 * 
	 * @param statement
	 *//*
	public List<Map<String, Object>> queryForList(String statement) {
		return queryForList(statement, null, null);
	}

	*//**
	 * 
	 * @param statement
	 * @param elementType
	 * @param params
	 * @param paramTypes
	 * @return
	 *//*
	public List<Map<String, Object>> queryForList(String statement, Object[] params, int[] paramTypes) {
		BoundSql sql = getBoundSql(statement, params);
		if (maxResults > 0) {
			return jdbcTemplate.queryScrollable(sql.getSql(), startIndex, maxResults, params, paramTypes);
		} else {
			return jdbcTemplate.queryForList(sql.getSql(), params, paramTypes);
		}
	}

	*//**
	 * 
	 * @param statement
	 * @param elementType
	 *//*
	public <T> List<T> queryForList(String statement, Class<T> elementType) {
		return queryForList(statement, null, null, elementType);
	}

	*//**
	 * 
	 * @param statement
	 * @param elementType
	 * @param params
	 * @param types
	 * @return
	 *//*
	public <T> List<T> queryForList(String statement, Object[] params, int[] types, Class<T> elementType) {
		BoundSql sql = getBoundSql(statement, params);
		if (maxResults > 0) {
			return jdbcTemplate.queryScrollable(sql.getSql(), startIndex, maxResults, elementType, params, types);
		} else {
			return jdbcTemplate.queryForList(sql.getSql(), params, types, elementType);
		}
	}

	*//**
	 * 
	 * 
	 * @param statement
	 * @param elementType
	 *//*
	public <T> T queryForObject(String statement, Class<T> elementType) {
		BoundSql sql = getBoundSql(statement, null);
		return jdbcTemplate.queryForObject(sql.getSql(), elementType);
	}

	*//**
	 * 
	 * 
	 * @param statement
	 *//*
	public Map<String, Object> queryForMap(String statement) {
		BoundSql sql = getBoundSql(statement, null);
		return jdbcTemplate.queryForMap(sql.getSql());
	}

	*//**
	 * 
	 * @param statement
	 * @param params
	 * @param paramTypes
	 *//*
	public Map<String, Object> queryForMap(String statement, Object[] params, int[] paramTypes) {
		BoundSql sql = getBoundSql(statement, params);
		return jdbcTemplate.queryForMap(sql.getSql(), params, paramTypes);
	}

	*//**
	 * 
	 * @param statement
	 * @param elementType
	 * @param params
	 * @param paramTypes
	 *//*
	public <T> T queryForObject(String statement, Object[] params, int[] paramTypes, Class<T> elementType) {
		BoundSql sql = getBoundSql(statement, params);
		return jdbcTemplate.queryForObject(sql.getSql(), params, paramTypes, elementType);
	}

	
	*//**
	 * 
	 * 
	 * @param statement
	 * @param params
	 * @param paramTypes
	 * @param rowMapper
	 * @return
	 *//*
	public <T> T queryForObject(String statement, Object[] params, int[] paramTypes, RowMapper<T> rowMapper ){
		BoundSql sql = getBoundSql(statement, params);		
		return jdbcTemplate.queryForObject(sql.getSql(), params, paramTypes, rowMapper);
	}
	
	public <T> T queryForObject(String statement, Object[] params, int[] paramTypes, ResultSetExtractor<T> extractor ){
		BoundSql sql = getBoundSql(statement, params);		
		return jdbcTemplate.query(sql.getSql(), params, paramTypes, extractor);
	}

	public <T> T uniqueResult(RowMapper<T> rowMapper) {
		return queryForObject(getStatement(), values.toArray(), types.toArray(), rowMapper);
	}
	
	public Object uniqueResult() {		
		if (getReturnType().isAssignableFrom(Map.class)) {		
			if (values.size() > 0)
				return queryForMap(getStatement(), values.toArray(), types.toArray());
			else
				return queryForMap(getStatement());		
		}else{		
			if (values.size() > 0)
				return queryForObject(getStatement(), values.toArray(), types.toArray(), getReturnType());
			else
				return queryForObject(getStatement(), getReturnType());
		}
	}
	
	public <T> T uniqueResult(Class<T> elementType) {	
		if (elementType.isAssignableFrom(Map.class)) {		
			if (values.size() > 0)
				return (T) queryForMap(getStatement(), values.toArray(), types.toArray());
			else
				return (T)queryForMap(getStatement());		
		}else{		
			if (values.size() > 0)
				return queryForObject(getStatement(), values.toArray(), types.toArray(), elementType);
			else
				return queryForObject(getStatement(), elementType);
		}
	}
	
	
	public List list() {
		if (getReturnType().isAssignableFrom(Map.class)) {
			if (values.size() > 0)
				return queryForList(getStatement(), values.toArray(), types.toArray());
			else
				return queryForList(getStatement());
		}

		if (values.size() > 0)
			return queryForList(getStatement(), values.toArray(), types.toArray(), getReturnType());
		else
			return queryForList(getStatement(), getReturnType());
	}

	// *********************************************
	// Recordset Methods
	// ********************************************

	public Object executeScript() {
		BoundSql sql = getBoundSql(getStatement(), null);
		final StringReader reader = new StringReader(sql.getSql());
		return jdbcTemplate.executeScript(stopOnError, reader);
	}

	public SqlQuery addToBatch() {
		if (parameterQueue == null)
			this.parameterQueue = new LinkedList<Object[]>();
		
		this.parameterQueue.add(this.values.toArray());
		values.clear();
		return this;
	}

	public int executeUpdate() {		
		if( parameterQueue != null && parameterQueue.size() > 0 )
		{
			int [] cnt =  executeBatchUpdate();		
			int sum = 0 ;
			for( int c : cnt){
				sum = sum + c ;
			}
			return sum;
		}
		else {
			BoundSql sql = getBoundSql(getStatement(), null);
			return jdbcTemplate.update( sql.getSql(), values.toArray(), types.toArray());
		}
	}

	*//**
	 * 
	 * 
	 * @param statement
	 * @param recordset
	 * @param params
	 *//*
	public int executeUpdate(String statement, Recordset recordset, String[] params) {
		if (params.length == 0 || params.length > recordset.getFieldCount()) {
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
		
		return jdbcTemplate.batchUpdate(sql.getSql(),
				new BatchPreparedStatementSetter() {
			
					public int getBatchSize() {
						return parameterQueue.size();
					}

					public void setValues(PreparedStatement ps, int index)
							throws SQLException {
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
		
		class RecordsetBatchPreparedStatementSetter implements
				BatchPreparedStatementSetter {

			private Recordset recordset;
			private String[] params;

			protected RecordsetBatchPreparedStatementSetter(
					Recordset recordset, String[] params) {
				this.recordset = recordset;
				this.params = params;
			}

			public void setValues(PreparedStatement ps, int i)
					throws SQLException {

				HashMap<String, RecordsetField> flds = recordset.getFields();
				try {
					recordset.setRecordNumber(i);
				} catch (RecordsetException e) {
				}

				for (int x = 0; i < params.length; x++) {
					RecordsetField f = (RecordsetField) flds.get(params[x]);
					Object value = null;
					try {
						value = recordset.getValue(params[i]);
					} catch (Throwable e) {
					}
					if (value == null) {
						ps.setNull(x + 1, f.getType());
					} else {
						// PATCH 2005-04-15 - support for SQLDATE instead of
						// DATE
						if (f.getType() == java.sql.Types.DATE) {
							java.util.Date d = (java.util.Date) value;
							value = new Date(d.getTime());
						}
						ps.setObject(x + 1, value, f.getType());
					}
				}
			}

			public int getBatchSize() {
				return recordset.getRecordCount();
			}

		}
		return jdbcTemplate.batchUpdate(sql.getSql(), new RecordsetBatchPreparedStatementSetter(recordset, params));
	}

	// *********************************************
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

	*//**
	 * 
	 * @param statement
	 * @return
	 *//*
	protected BoundSql getBoundSql(String statement, Object params) {		
		MappedStatement stmt = configuration.getMappedStatement(statement);
		BoundSql sql;
		if (additionalParameters.size() > 0)
			sql = stmt.getBoundSql(params, this.additionalParameters);
		else
			sql = stmt.getBoundSql(params);
		return sql;
	}*/
	
}