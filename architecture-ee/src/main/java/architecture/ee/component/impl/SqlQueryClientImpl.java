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

package architecture.ee.component.impl;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.ColumnMapRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import architecture.common.jdbc.JdbcUtils;
import architecture.common.jdbc.ParameterMapping;
import architecture.common.jdbc.schema.Database;
import architecture.common.jdbc.schema.Table;
import architecture.ee.exception.ApplicationException;
import architecture.ee.services.SqlQueryCallback;
import architecture.ee.services.SqlQueryClient;
import architecture.ee.spring.jdbc.support.SqlQueryDaoSupport;
import architecture.ee.util.ApplicationHelper;

public class SqlQueryClientImpl extends SqlQueryDaoSupport implements SqlQueryClient {

    private AsyncTaskExecutor taskExecutor = null;

    private DateFormat df = new SimpleDateFormat("yyyyMMddHHmm");

    public void setTaskExecutor(AsyncTaskExecutor taskExecutor) {
	this.taskExecutor = taskExecutor;
    }

    protected String getExportFileName(String tableName) {
	StringBuilder builder = new StringBuilder(tableName);
	builder.append("_");
	builder.append(df.format(new Date()));
	builder.append(".xls");
	return builder.toString();
    }

    protected File getExportFile(String tableName) {
	File base = ApplicationHelper.getRepository().getFile("database");
	File location = new File(base, "export");
	if (!location.exists())
	    location.mkdirs();

	return new File(location, getExportFileName(tableName));
    }

    protected Database getDatabase(Connection connection, String catalogName, String schemaName, String tableName) {
	return JdbcUtils.getDatabase(connection, catalogName, schemaName, tableName);
    }

    public void exportToExcel(String catalogName, String schemaName, String tableName, boolean async) {
	if (async) {
	    taskExecutor.submit(new TableToExcelTask(this, catalogName, schemaName, tableName));
	} else {
	    exportToExcel(catalogName, schemaName, tableName);
	}
    }

    public void exportToExcel(String catalogName, String schemaName, String tableName) {

	boolean hasError = false;

	try {
	    Database database = JdbcUtils.getDatabase(getConnection(), catalogName, schemaName, tableName);
	    Table table = database.getTable(tableName);
	    int idx = 0;
	    int columnSize = table.getColumnNames().length;
	    Map<String, Integer> types = new HashMap<String, Integer>();
	    StringBuilder builder = new StringBuilder();
	    builder.append("SELECT ");
	    for (String columnName : table.getColumnNames()) {
		idx++;
		builder.append(columnName);
		if (idx < columnSize) {
		    builder.append(", ");
		}
		types.put(columnName, table.getColumn(columnName).getType());
	    }

	    builder.append(" FROM " + table.getName());

	    List<Map<String, Object>> list = getExtendedJdbcTemplate().queryForList((builder.toString()));

	    ExcelWriter writer = new ExcelWriter();
	    writer.addSheet(table.getName());
	    writer.getSheetAt(0).setDefaultColumnWidth(15);
	    writer.setHeaderToFirstRow(table);
	    for (Map<String, Object> item : list) {
		writer.setDataToRow(item, table);
	    }

	    File file = getExportFile(tableName);
	    log.debug("now save to file: " + file.getAbsolutePath());
	    if (!file.exists())
		file.createNewFile();
	    writer.write(file);

	} catch (Exception e) {
	    log.error(e);
	    hasError = true;
	}

	// ApplicationHelper.getEventPublisher().publish("");

    }

    private static class TableToExcelTask implements Callable<Boolean> {

	private final SqlQueryClient client;
	private final String catalogName, schemaName, tableName;

	public TableToExcelTask(SqlQueryClient client, String catalogName, String schemaName, String tableName) {
	    this.client = client;
	    this.catalogName = catalogName;
	    this.schemaName = schemaName;
	    this.tableName = tableName;
	}

	public Boolean call() throws Exception {
	    client.call(catalogName, schemaName, tableName);
	    return true;
	}
    }

    private static class ExcelToTableTask implements Callable<Boolean> {
	private final SqlQueryClient client;
	private final String catalogName, schemaName, tableName, uri;

	public ExcelToTableTask(SqlQueryClient client, String catalogName, String schemaName, String tableName,
		String uri) {
	    this.client = client;
	    this.catalogName = catalogName;
	    this.schemaName = schemaName;
	    this.tableName = tableName;
	    this.uri = uri;
	}

	public Boolean call() throws Exception {
	    client.call(catalogName, schemaName, tableName, uri);
	    return true;
	}

    }

    public void importFromExcel(String catalog, String schemaName, String tableName, String uri) {
	try {
	    Database database = JdbcUtils.getDatabase(getConnection(), catalog, schemaName, tableName);
	    final Table table = database.getTable(tableName);
	    final ExcelReader reader = new ExcelReader(uri);
	    final List<Map<String, String>> list = reader.getDataAsList();
	    SqlParameterSource[] batchArgs = new SqlParameterSource[list.size()];
	    int i = 0;
	    for (Map<String, String> values : list) {
		MapSqlParameterSource source = new MapSqlParameterSource();
		for (Map.Entry<String, String> entry : values.entrySet()) {
		    source.addValue(entry.getKey(), entry.getValue(), table.getColumn(entry.getKey()).getType());
		}
		batchArgs[i] = source;
		i++;
	    }

	    StringBuilder builder = new StringBuilder();
	    int columns = table.getColumnNames().length;
	    List<ParameterMapping> parameterMappings = new ArrayList<ParameterMapping>(columns);
	    builder.append("INSERT INTO ").append(table.getName()).append(" (");
	    int j = 0;
	    for (String columnName : table.getColumnNames()) {
		j++;
		builder.append(columnName);
		if (j < columns) {
		    builder.append(", ");
		}
	    }
	    builder.append(") VALUES ( ");
	    j = 0;
	    for (String columnName : table.getColumnNames()) {
		j++;
		builder.append("?");
		if (j < columns) {
		    builder.append(", ");
		}
	    }
	    builder.append(") ");

	    getExtendedJdbcTemplate().update(builder.toString(), new BatchPreparedStatementSetter() {
		public void setValues(PreparedStatement ps, int i) throws SQLException {
		    Map<String, String> row = list.get(i);
		    int idx = 1;
		    for (String columnName : table.getColumnNames()) {
			Object valueToUse = row.get(columnName);
			int type = table.getColumn(columnName).getType();
			if (valueToUse == null)
			    ps.setNull(idx, type);
			else
			    ps.setObject(idx, valueToUse, type);
			idx++;
		    }
		}

		public int getBatchSize() {
		    return list.size();
		}
	    });

	} catch (Exception e) {
	    log.error(e);
	}
    }

    public void importFromExcel(String catalogName, String schemaName, String tableName, String uri,
	    boolean asyncMode) {
	if (asyncMode) {
	    taskExecutor.submit(new ExcelToTableTask(this, catalogName, schemaName, tableName, uri));
	} else {
	    importFromExcel(catalogName, schemaName, tableName, uri);
	}
    }

    public Map<String, Object> uniqueResult(String statement) {
	return getSqlQuery().uniqueResult(statement, new ColumnMapRowMapper());
    }

    public Map<String, Object> uniqueResult(String statement, Object parameter) {
	return getSqlQuery().uniqueResult(statement, parameter, new ColumnMapRowMapper());
    }

    public List<Map<String, Object>> list(String statement) {
	return getSqlQuery().list(statement);
    }

    public List<Map<String, Object>> list(String statement, Object... parameters) {
	return getSqlQuery().list(statement, parameters);
    }

    public Object update(String statement) {
	return getSqlQuery().update(statement);
    }

    public Object update(String statement, Object[] values, int[] types) {
	return getSqlQuery().update(statement, values, types);
    }

    public Object update(String statement, Object... parameters) {
	return getSqlQuery().update(statement, parameters);
    }

    public Object batchUpdate(String statement, List<Object[]> parameters) {
	return getSqlQuery().batchUpdate(statement, parameters);
    }

    public Object update(String statement, Map<String, Object> parameters) {
	return getSqlQuery().executeUpdate(statement, parameters);
    }

    public Object call(String statement, Object... parameters) {
	return getSqlQuery().call(statement, parameters);
    }

    public <T> T execute(SqlQueryCallback<T> action) {
	return action.doInSqlQuery(getSqlQuery());
    }

    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW, rollbackFor = {
	    ApplicationException.class })
    public Object unitOfWork(String scriptName, String methodName, Object... parameters) {
	return unitOfWorkWithoutTransaction(scriptName, methodName, parameters);
    }

    public Object unitOfWorkWithoutTransaction(String scriptName, String methodName, Object... parameters) {
	return execute(scriptName, methodName, parameters);
    }

    public List<Map<String, Object>> list(String statement, int startIndex, int maxResults) {
	return getSqlQuery().setStartIndex(startIndex).setMaxResults(maxResults).list(statement);
    }

    public <T> T uniqueResult(String statement, Class<T> elementType, Object... parameters) {
	return getSqlQuery().uniqueResult(statement, parameters, elementType);
    }

}