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
package architecture.common.jdbc;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.springframework.jdbc.support.DatabaseMetaDataCallback;
import org.springframework.jdbc.support.MetaDataAccessException;

import architecture.common.jdbc.schema.Database;
import architecture.common.jdbc.schema.DatabaseFactory;
import architecture.common.jdbc.schema.DatabaseType;

public abstract class JdbcUtils extends org.springframework.jdbc.support.JdbcUtils {
	
//	/private static final Log log = LogFactory.getLog(JdbcUtils.class);
		
	public static DatabaseType getDatabaseType( DatabaseMetaData dbmd ) throws SQLException{
		
		DatabaseType databaseType = DatabaseType.unknown;
		
		boolean transactionsSupported = dbmd.supportsTransactions();
		int transactionIsolation      = dbmd.getDefaultTransactionIsolation();
		boolean subqueriesSupported   = dbmd.supportsCorrelatedSubqueries();
		boolean scrollResultsSupported;
		
		try {
			scrollResultsSupported = dbmd.supportsResultSetType(ResultSet.TYPE_SCROLL_INSENSITIVE);
		} catch (Exception e) {
			scrollResultsSupported = false;
		}

		// Supports batch updates.
		boolean batchUpdatesSupported = dbmd.supportsBatchUpdates();
		// Set defaults for other meta properties.
		boolean streamTextRequired = false;
		boolean maxRowsSupported = true;
		boolean fetchSizeSupported = true;
		boolean streamBlobRequired = false;

		// Get the database name so that we can perform meta
		// data settings.
		String dbName = dbmd.getDatabaseProductName().toLowerCase();
		String driverName = dbmd.getDriverName().toLowerCase();
		String dbVersion = dbmd.getDatabaseProductVersion();

		String databaseProductName = dbmd.getDatabaseProductName();
		String databaseProductVersion = dbmd.getDatabaseProductVersion();
		String jdbcDriverName = dbmd.getDriverName();
		String jdbcDriverVersion = dbmd.getDriverVersion();
		
		if (dbName.indexOf("oracle") != -1) {
			databaseType = DatabaseType.oracle;
			streamTextRequired = true;
			scrollResultsSupported = false;
			// The i-net AUGURO JDBC driver
			if (driverName.indexOf("auguro") != -1) {
				streamTextRequired = false;
				fetchSizeSupported = true;
				maxRowsSupported = false;
			} else if (driverName.indexOf("Weblogic, Inc. Java-OCI JDBC Driver") != -1)
				streamTextRequired = false;
		} else
		// Postgres properties
		if (dbName.indexOf("postgres") != -1) {
			databaseType = DatabaseType.postgresql;
			scrollResultsSupported = false;
			streamBlobRequired = true;
			fetchSizeSupported = false;
		} else if (dbName.indexOf("interbase") != -1) {
			fetchSizeSupported = false;
			maxRowsSupported = false;
		} else if (dbName.indexOf("sql server") != -1) {
			databaseType = DatabaseType.sqlserver;
			if (driverName.indexOf("una") != -1) {
				fetchSizeSupported = true;
				maxRowsSupported = false;
			}
			if (driverName.indexOf("jtds") != -1) {
				fetchSizeSupported = true;
				maxRowsSupported = true;
			} else {
				streamBlobRequired = true;
				fetchSizeSupported = false;
				maxRowsSupported = false;
				scrollResultsSupported = false;
			}
		} else if (dbName.indexOf("mysql") != -1) {
			if (dbVersion != null && dbVersion.startsWith("3."))
				databaseType = DatabaseType.mysql;
			else
				databaseType = DatabaseType.mysql;

			transactionsSupported = false;
		} else if (dbName.indexOf("derby") != -1)
			databaseType = DatabaseType.derby;
		else if (dbName.indexOf("db2") != -1)
			databaseType = DatabaseType.db2;
		else if (dbName.indexOf("hsql") != -1)
			databaseType = DatabaseType.hsqldb;
		
		databaseType.transactionsSupported = transactionsSupported;
		databaseType.batchUpdatesSupported = batchUpdatesSupported;
		databaseType.transactionIsolation = transactionIsolation;
		databaseType.streamTextRequired = streamTextRequired;
		databaseType.streamBlobRequired = streamBlobRequired;
		databaseType.fetchSizeSupported = fetchSizeSupported;
		databaseType.subqueriesSupported = subqueriesSupported;
		databaseType.maxRowsSupported = maxRowsSupported;
		databaseType.scrollResultsSupported = scrollResultsSupported;
		databaseType.databaseProductName = databaseProductName;
		databaseType.databaseProductVersion = databaseProductVersion;
		databaseType.jdbcDriverVersion = jdbcDriverVersion;
		databaseType.jdbcDriverName = jdbcDriverName;
							
		return databaseType;
		
	}
	
	public static DatabaseType getDatabaseType(DataSource dataSource){
		try {
			return (DatabaseType) extractDatabaseMetaData(dataSource, new DatabaseMetaDataCallback(){				
				public Object processMetaData(DatabaseMetaData dbmd)
						throws SQLException, MetaDataAccessException {
					return getDatabaseType(dbmd);
				}});
			
		} catch (MetaDataAccessException e) {
			return DatabaseType.unknown;
		}
	}
	
	
	
	public static DatabaseType getDatabaseType(Connection conn){
		try {
			DatabaseMetaData metaData = conn.getMetaData();
			return getDatabaseType(metaData);
		} catch (SQLException e) {
			return DatabaseType.unknown;
		} finally {
			if(conn != null)
				JdbcUtils.closeConnection(conn);
		}
	}
		
	public static PreparedStatement createScrollablePreparedStatement(DatabaseType type, Connection con, String sql) throws SQLException {
		if (type.scrollResultsSupported)
			return con.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
		else
			return con.prepareStatement(sql);

	}

	public static void setFetchSize(DatabaseType type, ResultSet rs, int fetchSize) {
		if (type.fetchSizeSupported)
			try {
				rs.setFetchSize(fetchSize);
			} catch (Throwable t) {
			}
	}

	public static void scrollResultSet(DatabaseType type, ResultSet rs, int rowNumber)
			throws SQLException {
		if (type.scrollResultsSupported) {
			if (rowNumber > 0) {
				rs.setFetchDirection(1000);
				rs.absolute(rowNumber);
			}
		} else {
			for (int i = 0; i < rowNumber; i++)
				rs.next();
		}
	}
	
	
	public static Database getDatabase(Connection conn, String catalog, String schemaName, String tableName) {
		try {
			return DatabaseFactory.newDatabase(conn, catalog, schemaName, tableName);		
		} catch (SQLException e) {
			return null;
		} finally {
			if(conn != null)
				JdbcUtils.closeConnection(conn);
		}
	}
	
	
	public static String lookupColumnName(ResultSetMetaData resultSetMetaData, int columnIndex) throws SQLException {
		String name = resultSetMetaData.getColumnLabel(columnIndex);
		if (name == null || name.length() < 1) {
			name = resultSetMetaData.getColumnName(columnIndex);
		}
		return name;
	}
	
}
