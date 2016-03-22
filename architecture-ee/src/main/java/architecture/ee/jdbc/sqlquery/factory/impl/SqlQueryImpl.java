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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.commons.collections.map.FixedSizeMap;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.jdbc.core.CallableStatementCreatorFactory;
import org.springframework.jdbc.core.ColumnMapRowMapper;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.SingleColumnRowMapper;
import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.SqlParameter;

import architecture.common.jdbc.ParameterMapping;
import architecture.common.jdbc.incrementer.MaxValueIncrementer;
import architecture.common.spring.jdbc.core.ExtendedJdbcTemplate;
import architecture.ee.jdbc.sqlquery.SqlQuery;
import architecture.ee.jdbc.sqlquery.factory.Configuration;
import architecture.ee.jdbc.sqlquery.mapping.BoundSql;
import architecture.ee.jdbc.sqlquery.mapping.MappedStatement;
import architecture.ee.spring.jdbc.support.MappedColumnMapRowMapper;
import net.anotheria.moskito.aop.annotation.Monitor;

/**
 * @author donghyuck
 */
@Monitor
public class SqlQueryImpl implements SqlQuery {

    private Log log = LogFactory.getLog(getClass());

    private boolean stopOnError = true;

    private Configuration configuration;

    private ExtendedJdbcTemplate jdbcTemplate;

    private int startIndex = 0;

    private int maxResults = 0;

    private MaxValueIncrementer incrementer;

    private Map<String, Object> additionalParameters = new HashMap<String, Object>(4);

    /**
     * startIndex, maxResult, additionalParameters 값을 초기화 한다.
     * 
     * @return
     */
    public SqlQuery reset() {
	this.startIndex = 0;
	this.maxResults = 0;
	this.additionalParameters.clear();
	return this;
    }

    public MaxValueIncrementer getIncrementer() {
	return incrementer;
    }

    public SqlQuery setAdditionalParameters(Map<String, Object> additionalParameters) {
	this.additionalParameters = FixedSizeMap.decorate(additionalParameters);
	return this;
    }

    // *********************************************
    // Protected Methods
    // ********************************************

    protected SqlQueryImpl(Configuration configuration) {
	this.configuration = configuration;
    }

    protected SqlQueryImpl(Configuration configuration, MaxValueIncrementer incrementer) {
	this.configuration = configuration;
	this.incrementer = incrementer;
    }

    protected SqlQueryImpl(Configuration configuration, DataSource dataSource, MaxValueIncrementer incrementer) {
	this.configuration = configuration;
	this.incrementer = incrementer;
	setDataSource(dataSource);
    }

    protected SqlQueryImpl(Configuration configuration, ExtendedJdbcTemplate jdbcTemplate,
	    MaxValueIncrementer incrementer) {
	this.configuration = configuration;
	this.incrementer = incrementer;
	this.jdbcTemplate = jdbcTemplate;
    }

    protected BoundSql getBoundSql(String statement) {
	MappedStatement stmt = configuration.getMappedStatement(statement);
	BoundSql sql;
	if (additionalParameters.size() > 0)
	    sql = stmt.getBoundSql(null, additionalParameters);
	else
	    sql = stmt.getBoundSql(null);
	// additionalParameters.clear();
	return sql;
    }

    protected BoundSql getBoundSql(String statement, Object parameter) {
	MappedStatement stmt = configuration.getMappedStatement(statement);
	BoundSql sql;
	if (additionalParameters.size() > 0)
	    sql = stmt.getBoundSql(parameter, additionalParameters);
	else
	    sql = stmt.getBoundSql(parameter);
	// additionalParameters.clear();
	return sql;
    }

    protected BoundSql getBoundSql(String statement, Object parameter, Object additionalParameters) {
	MappedStatement stmt = configuration.getMappedStatement(statement);
	BoundSql sql = stmt.getBoundSql(parameter, additionalParameters);
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
     * @uml.property name="startIndex"
     */
    public SqlQuery setStartIndex(int startIndex) {
	this.startIndex = startIndex;
	return this;
    }

    /**
     * @param maxResults
     * @return
     * @uml.property name="maxResults"
     */
    public SqlQuery setMaxResults(int maxResults) {
	this.maxResults = maxResults;
	return this;
    }

    // SPRING Style API
    public <T> T queryForObject(String statement, Class<T> elementType) {
	BoundSql sql = getBoundSql(statement);
	if (elementType == Map.class) {
	    return (T) queryForObject(statement, new ColumnMapRowMapper());
	} else {
	    return jdbcTemplate.queryForObject(sql.getSql(), elementType);
	}
    }

    public <T> T queryForObject(String statement, RowMapper<T> rowMapper) {
	BoundSql sql = getBoundSql(statement);
	return jdbcTemplate.queryForObject(sql.getSql(), rowMapper);
    }

    public <T> T queryForObject(String statement, Object[] params, int[] paramTypes, Class<T> elementType) {
	BoundSql sql = getBoundSql(statement, params);

	if (elementType == Map.class) {
	    return (T) jdbcTemplate.queryForObject(sql.getSql(), params, paramTypes, new ColumnMapRowMapper());
	} else {
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

    public Map<String, Object> queryForMap(String statement, Object[] params, int[] paramTypes,
	    Object additionalParameters) {
	BoundSql sql = getBoundSql(statement, params, additionalParameters);
	return jdbcTemplate.queryForMap(sql.getSql(), params, paramTypes);
    }

    public <T> List<T> queryForList(String statement, Class<T> elementType) {
	return queryForList(statement, new Object[0], new int[0], elementType);
    }

    public <T> List<T> queryForList(String statement, Object[] params, int[] paramTypes, Class<T> elementType) {
	BoundSql sql = getBoundSql(statement, params);
	if (this.maxResults > 0) {
	    return jdbcTemplate.queryScrollable(sql.getSql(), startIndex, maxResults, params, paramTypes, elementType);
	} else {
	    return jdbcTemplate.queryForList(sql.getSql(), params, paramTypes, elementType);
	}
    }

    public <T> List<T> queryForList(String statement, RowMapper<T> rowMapper) {
	return queryForList(statement, new Object[0], new int[0], rowMapper);
    }

    public <T> List<T> queryForList(String statement, Object[] params, int[] paramTypes, RowMapper<T> rowMapper) {
	BoundSql sql = getBoundSql(statement, params);
	if (this.maxResults > 0) {
	    return jdbcTemplate.queryScrollable(sql.getSql(), startIndex, maxResults, params, paramTypes, rowMapper);
	} else {
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
	BoundSql sql = getBoundSql(statement, params);
	if (this.maxResults > 0) {

	    return jdbcTemplate.queryScrollable(sql.getSql(), startIndex, maxResults, params, paramTypes,
		    new ColumnMapRowMapper());
	} else {
	    if (params == null || params.length == 0)
		return jdbcTemplate.query(sql.getSql(), new ColumnMapRowMapper());
	    else
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
	if (elementType == Map.class)
	    return (T) uniqueResult(statement, parameter, new ColumnMapRowMapper());
	return uniqueResult(statement, parameter, new SingleColumnRowMapper<T>(elementType));
    }

    public <T> T uniqueResult(String statement, Object parameter, RowMapper<T> rowMapper) {
	BoundSql sql = getBoundSql(statement, parameter);
	List<ParameterMapping> parameterMappings = sql.getParameterMappings();

	if (parameter instanceof Map)
	    return jdbcTemplate.queryForObject(sql.getSql(), parameterMappings, (Map<String, Object>) parameter,
		    rowMapper);
	else if (parameter instanceof Object[])
	    return jdbcTemplate.queryForObject(sql.getSql(), (Object[]) parameter, rowMapper);
	else
	    return jdbcTemplate.queryForObject(sql.getSql(),
		    parameter == null ? new Object[0] : new Object[] { parameter }, rowMapper);

    }

    public <T> List<T> list(String statement, Class<T> elementType) {
	BoundSql sql = getBoundSql(statement, null);
	return list(sql, new SingleColumnRowMapper<T>(elementType));
    }

    public <T> List<T> list(String statement, Class<T> elementType, Object... parameters) {
	BoundSql sql = getBoundSql(statement, parameters);
	if (this.maxResults > 0) {
	    return jdbcTemplate.queryScrollable(sql.getSql(), startIndex, maxResults, new Object[0], new int[0],
		    new SingleColumnRowMapper<T>(elementType));
	} else {
	    return jdbcTemplate.query(sql.getSql(), new SingleColumnRowMapper<T>(elementType));
	}
    }

    public <T> List<T> list(String statement, Class<T> elementType, Map<String, Object> parameters) {
	BoundSql sql = getBoundSql(statement, parameters);
	if (this.maxResults > 0) {
	    return jdbcTemplate.queryScrollable(sql.getSql(), startIndex, maxResults, new Object[0], new int[0],
		    new SingleColumnRowMapper<T>(elementType));
	} else {
	    return jdbcTemplate.query(sql.getSql(), new SingleColumnRowMapper<T>(elementType));
	}
    }

    public List<Map<String, Object>> list(String statement) {
	BoundSql sql = getBoundSql(statement, null);
	return list(sql, new MappedColumnMapRowMapper(sql.getResultMappings()));
    }

    public List<Map<String, Object>> list(String statement, Object... parameters) {
	BoundSql sql = getBoundSql(statement, parameters);
	if (this.maxResults > 0) {
	    return jdbcTemplate.queryScrollable(sql.getSql(), startIndex, maxResults, parameters, null,
		    new MappedColumnMapRowMapper(sql.getResultMappings()));
	} else {
	    return jdbcTemplate.query(sql.getSql(), sql.getParameterMappings(), (Object[]) parameters,
		    new MappedColumnMapRowMapper(sql.getResultMappings()));
	}
    }

    public List<Map<String, Object>> list(String statement, Map<String, Object> parameters) {
	BoundSql sql = getBoundSql(statement, parameters);
	if (this.maxResults > 0) {
	    return jdbcTemplate.queryScrollable(sql.getSql(), sql.getParameterMappings(), startIndex, maxResults,
		    parameters, new MappedColumnMapRowMapper(sql.getResultMappings()));
	} else {
	    return jdbcTemplate.queryForList(sql.getSql(), sql.getParameterMappings(), parameters,
		    new MappedColumnMapRowMapper(sql.getResultMappings()));
	}
    }

    protected <T> List<T> list(BoundSql sql, RowMapper<T> rowMapper) {
	if (this.maxResults > 0) {
	    return jdbcTemplate.queryScrollable(sql.getSql(), startIndex, maxResults, new Object[0], new int[0],
		    rowMapper);
	} else {
	    return jdbcTemplate.query(sql.getSql(), rowMapper);
	}
    }

    public int update(String statement, Object[] values, int[] types) {
	BoundSql sql = getBoundSql(statement, values);
	return jdbcTemplate.update(sql.getSql(), values, types);
    }

    public int update(String statement) {
	return executeUpdate(statement);
    }

    public int[] batchUpdate(String statement, List<Object[]> parameters) {
	BoundSql sql = getBoundSql(statement, parameters);
	List<ParameterMapping> parameterMappings = sql.getParameterMappings();

	return jdbcTemplate.batchUpdate(sql.getSql(), (List<Object[]>) parameters);
    }

    public int update(String statement, Object... values) {
	BoundSql sql = getBoundSql(statement, values);
	List<ParameterMapping> parameterMappings = sql.getParameterMappings();
	return jdbcTemplate.update(sql.getSql(), values);
    }

    public int executeUpdate(String statement) {
	BoundSql sql = getBoundSql(statement);
	return jdbcTemplate.update(sql.getSql());
    }

    public int executeUpdate(String statement, Object parameters) {

	BoundSql sql = getBoundSql(statement, parameters);

	List<ParameterMapping> parameterMappings = sql.getParameterMappings();

	if (parameters instanceof Map) {
	    return jdbcTemplate.update(sql.getSql(), parameterMappings, (Map<String, Object>) parameters);
	} else if (parameters instanceof List) {
	    // Batch Job Processing :
	    List list = (List) parameters;
	    if (list.size() > 0) {
		Object obj = list.get(0);
		if (obj instanceof Object[]) {
		    jdbcTemplate.batchUpdate(sql.getSql(), (List<Object[]>) parameters);
		} else if (obj instanceof Map) {
		    jdbcTemplate.batchUpdate(sql.getSql(), parameterMappings, (List<Map<String, Object>>) parameters);
		}
	    }
	    return ((List) parameters).size();
	} else if (parameters instanceof Object[]) {
	    return jdbcTemplate.update(sql.getSql(), (Object[]) parameters);
	}
	return 0;
    }

    public Object executeScript(String statement) {
	BoundSql sql = getBoundSql(statement);
	return jdbcTemplate.executeScript(stopOnError, new StringReader(sql.getSql()));
    }

    public Object call(String statement, Object... parameters) {
	BoundSql sql = getBoundSql(statement);

	List<SqlParameter> declaredParameters = new ArrayList<SqlParameter>();
	Map<String, Object> paramsToUse = new HashMap<String, Object>();

	// 메핑 파라메터에 따라 INPUT 과 OUTPU 을 설정한다.

	for (ParameterMapping mapping : sql.getParameterMappings()) {

	    mapping.getProperty();
	    mapping.getJdbcType();
	    mapping.getMode();

	    if (mapping.getMode() == ParameterMapping.Mode.IN) {

		SqlParameter input = new SqlParameter(mapping.getProperty(), mapping.getJdbcType().ordinal());
		declaredParameters.add(input);
		paramsToUse.put(mapping.getProperty(), parameters[mapping.getIndex() - 1]);

	    } else if (mapping.getMode() == ParameterMapping.Mode.OUT) {
		SqlOutParameter output = new SqlOutParameter(mapping.getProperty(), mapping.getJdbcType().ordinal());
		declaredParameters.add(output);
	    }
	}

	CallableStatementCreatorFactory callableStatementFactory = new CallableStatementCreatorFactory(sql.getSql(),
		declaredParameters);
	return jdbcTemplate.call(callableStatementFactory.newCallableStatementCreator(paramsToUse), declaredParameters);

    }

}