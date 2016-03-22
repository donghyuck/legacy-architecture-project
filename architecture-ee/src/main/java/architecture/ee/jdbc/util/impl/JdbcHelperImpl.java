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
package architecture.ee.jdbc.util.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.jdbc.support.MetaDataAccessException;
import org.springframework.jdbc.support.nativejdbc.NativeJdbcExtractor;
import org.springframework.jdbc.support.nativejdbc.SimpleNativeJdbcExtractor;

import architecture.ee.jdbc.util.JdbcHelper;

/**
 * @author donghyuck
 */
public class JdbcHelperImpl implements JdbcHelper {

    private NativeJdbcExtractor automaticJdbcExtractor;

    // True if the database support transactions.
    private boolean transactionsSupported;

    private int transactionIsolation;

    // True if the database requires large text fields to be streamed.
    private boolean streamTextRequired;

    // True if the database supports the Statement.setMaxRows() method.
    private boolean streamBlobRequired;

    // True if the database supports the Statement.setFetchSize() method.
    private boolean fetchSizeSupported;

    // True if the database supports correlated subqueries.
    private boolean subqueriesSupported;

    private boolean maxRowsSupported;

    private boolean deleteSubqueriesSupported;

    // True if the database supports scroll-insensitive results.
    private boolean scrollResultsSupported;

    // True if the database supports batch updates.
    private boolean batchUpdatesSupported;

    // databse product name.
    private String databaseProductName;

    // database product version.
    private String databaseProductVersion;

    // database jdbc driver name.
    private String jdbcDriverName;

    // jdbc driver version.
    private String jdbcDriverVersion;

    // default database type.
    private DatabaseType databaseType = DatabaseType.unknown;

    private Log log = LogFactory.getLog(getClass());

    protected JdbcHelperImpl() {
	automaticJdbcExtractor = new SimpleNativeJdbcExtractor();
    }

    protected JdbcHelperImpl(DataSource dataSource) {
	try {
	    setDatabaseMetaData(dataSource);
	    automaticJdbcExtractor = new SimpleNativeJdbcExtractor();
	} catch (MetaDataAccessException e) {
	    e.printStackTrace();
	}
    }

    public boolean isBatchUpdatesSupported() {
	return batchUpdatesSupported;
    }

    public String getDatabaseProductName() {
	return databaseProductName;
    }

    public String getDatabaseProductVersion() {
	return databaseProductVersion;
    }

    public DatabaseType getDatabaseType() {
	return databaseType;
    }

    public boolean isDeleteSubqueriesSupported() {
	return deleteSubqueriesSupported;
    }

    public boolean isFetchSizeSupported() {
	return fetchSizeSupported;
    }

    public String getJdbcDriverName() {
	return jdbcDriverName;
    }

    public String getJdbcDriverVersion() {
	return jdbcDriverVersion;
    }

    public boolean isMaxRowsSupported() {
	return maxRowsSupported;
    }

    public boolean isScrollResultsSupported() {
	return scrollResultsSupported;
    }

    public boolean isStreamBlobRequired() {
	return streamBlobRequired;
    }

    public boolean isStreamTextRequired() {
	return streamTextRequired;
    }

    public boolean isSubqueriesSupported() {
	return subqueriesSupported;
    }

    public int getTransactionIsolation() {
	return transactionIsolation;
    }

    public boolean isTransactionsSupported() {
	return transactionsSupported;
    }

    public void setDatabaseMetaData(DataSource dataSource) throws MetaDataAccessException {

	// DatabaseType type = JdbcUtils.getDatabaseType(dataSource);

	/*
	 * JdbcUtils.extractDatabaseMetaData(dataSource, new
	 * DatabaseMetaDataCallback() {
	 * 
	 * private boolean transactionsSupported; private int
	 * transactionIsolation; // True if the database requires large text
	 * fields to be streamed. private boolean streamTextRequired; // True if
	 * the database supports the Statement.setMaxRows() method. private
	 * boolean streamBlobRequired; // True if the database supports the
	 * Statement.setFetchSize() method. private boolean fetchSizeSupported;
	 * // True if the database supports correlated subqueries. private
	 * boolean subqueriesSupported;
	 * 
	 * private boolean maxRowsSupported;
	 * 
	 * private boolean deleteSubqueriesSupported;
	 * 
	 * // True if the database supports scroll-insensitive results. private
	 * boolean scrollResultsSupported;
	 * 
	 * // True if the database supports batch updates. private boolean
	 * batchUpdatesSupported;
	 * 
	 * // databse product name. private String databaseProductName;
	 * 
	 * // database product version. private String databaseProductVersion;
	 * 
	 * // database jdbc driver name. private String jdbcDriverName;
	 * 
	 * // jdbc driver version. private String jdbcDriverVersion;
	 * 
	 * public Object processMetaData(DatabaseMetaData metaData) throws
	 * SQLException, MetaDataAccessException {
	 * 
	 * // Supports transactions? transactionsSupported =
	 * metaData.supportsTransactions(); transactionIsolation =
	 * metaData.getDefaultTransactionIsolation();
	 * 
	 * // Supports subqueries? subqueriesSupported =
	 * metaData.supportsCorrelatedSubqueries(); // Supports scroll
	 * insensitive result sets? Try/catch // block is a // workaround for
	 * DB2 JDBC driver, which throws an // exception on // the method call.
	 * 
	 * try { scrollResultsSupported =
	 * metaData.supportsResultSetType(ResultSet.TYPE_SCROLL_INSENSITIVE); }
	 * catch (Exception e) { scrollResultsSupported = false; }
	 * 
	 * // Supports batch updates. batchUpdatesSupported =
	 * metaData.supportsBatchUpdates(); // Set defaults for other meta
	 * properties. streamTextRequired = false; maxRowsSupported = true;
	 * fetchSizeSupported = true;
	 * 
	 * // Get the database name so that we can perform meta // data
	 * settings. String dbName =
	 * metaData.getDatabaseProductName().toLowerCase(); String driverName =
	 * metaData.getDriverName().toLowerCase(); String dbVersion =
	 * metaData.getDatabaseProductVersion();
	 * 
	 * databaseProductName = metaData.getDatabaseProductName();
	 * databaseProductVersion = metaData.getDatabaseProductVersion();
	 * jdbcDriverName = metaData.getDriverName(); jdbcDriverVersion =
	 * metaData.getDriverVersion();
	 * 
	 * if (dbName.indexOf("oracle") != -1) { databaseType =
	 * DatabaseType.oracle; streamTextRequired = true;
	 * scrollResultsSupported = false; // The i-net AUGURO JDBC driver if
	 * (driverName.indexOf("auguro") != -1) { streamTextRequired = false;
	 * fetchSizeSupported = true; maxRowsSupported = false; } else if
	 * (driverName.indexOf("Weblogic, Inc. Java-OCI JDBC Driver") != -1)
	 * streamTextRequired = false; } else // Postgres properties if
	 * (dbName.indexOf("postgres") != -1) { databaseType =
	 * DatabaseType.postgresql; scrollResultsSupported = false;
	 * streamBlobRequired = true; fetchSizeSupported = false; } else if
	 * (dbName.indexOf("interbase") != -1) { fetchSizeSupported = false;
	 * maxRowsSupported = false; } else if (dbName.indexOf("sql server") !=
	 * -1) { databaseType = DatabaseType.sqlserver; if
	 * (driverName.indexOf("una") != -1) { fetchSizeSupported = true;
	 * maxRowsSupported = false; } if (driverName.indexOf("jtds") != -1) {
	 * fetchSizeSupported = true; maxRowsSupported = true; } else {
	 * streamBlobRequired = true; fetchSizeSupported = false;
	 * maxRowsSupported = false; scrollResultsSupported = false; } } else if
	 * (dbName.indexOf("mysql") != -1) { if (dbVersion != null &&
	 * dbVersion.startsWith("3.")) databaseType = DatabaseType.mysql; else
	 * databaseType = DatabaseType.mysql;
	 * 
	 * transactionsSupported = false; } else if (dbName.indexOf("derby") !=
	 * -1) databaseType = DatabaseType.derby; else if (dbName.indexOf("db2")
	 * != -1) databaseType = DatabaseType.db2; else if
	 * (dbName.indexOf("hsql") != -1) databaseType = DatabaseType.hsqldb;
	 * return this; } });
	 */
    }

    public PreparedStatement createScrollablePreparedStatement(Connection con, String sql) throws SQLException {
	if (isScrollResultsSupported())
	    return con.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
	else
	    return con.prepareStatement(sql);

    }

    public void setFetchSize(ResultSet rs, int fetchSize) {
	if (isFetchSizeSupported())
	    try {
		rs.setFetchSize(fetchSize);
	    } catch (Throwable t) {
		fetchSizeSupported = false;
	    }
    }

    public void scrollResultSet(ResultSet rs, int rowNumber) throws SQLException {
	if (isScrollResultsSupported()) {
	    if (rowNumber > 0) {
		rs.setFetchDirection(1000);
		rs.absolute(rowNumber);
	    }
	} else {
	    for (int i = 0; i < rowNumber; i++)
		rs.next();
	}
    }

    public NativeJdbcExtractor getNativeJdbcExtractor() {
	return automaticJdbcExtractor;
    }

    public void setNativeJdbcExtractor(NativeJdbcExtractor jdbcExtractor) {
	this.automaticJdbcExtractor = jdbcExtractor;
    }
}
