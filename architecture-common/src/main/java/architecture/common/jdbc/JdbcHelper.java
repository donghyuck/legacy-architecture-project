package architecture.common.jdbc;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.jdbc.support.JdbcUtils;

import architecture.common.jdbc.schema.DatabaseType;

public abstract class JdbcHelper extends JdbcUtils {
	
	private static final Log log = LogFactory.getLog(JdbcUtils.class);
	
	public static DatabaseType getDatabaseType(Connection con){
		
		boolean scrollResultsSupported = false;
		DatabaseType databaseType = DatabaseType.unknown;
		
		try {
			DatabaseMetaData metaData = con.getMetaData();			
			if (metaData != null) {
				String dbName = metaData.getDatabaseProductName().toLowerCase();
				String driverName = metaData.getDriverName().toLowerCase();	
				try {
					scrollResultsSupported = metaData.supportsResultSetType(ResultSet.TYPE_SCROLL_INSENSITIVE);
				} catch (Exception e) {}				
				if (dbName.indexOf("oracle") != -1) {
					databaseType = DatabaseType.oracle;
					scrollResultsSupported = false;
					
				} else if (dbName.indexOf("postgres") != -1) {					
					databaseType = DatabaseType.postgresql;
					scrollResultsSupported = false;
				} else if (dbName.indexOf("interbase") != -1) {
					databaseType = DatabaseType.interbase;
				} else if (dbName.indexOf("sql server") != -1) {
					databaseType = DatabaseType.sqlserver;					
					if (driverName.indexOf("una") != -1) {
					}
					if (driverName.indexOf("jtds") != -1) {
					} else {
						scrollResultsSupported = false;
					}
				} else if (dbName.indexOf("mysql") != -1) {
					databaseType = DatabaseType.mysql;
				} else if (dbName.indexOf("derby") != -1)
					databaseType = DatabaseType.derby;
				else if (dbName.indexOf("db2") != -1)
					databaseType = DatabaseType.db2;
				else if (dbName.indexOf("hsql") != -1)
					databaseType = DatabaseType.hsqldb;
			}
		}
		catch (SQLException ex) {
			log.debug("JDBC driver 'getDatabaseProductName' method threw exception", ex);
		}
		catch (AbstractMethodError err) {
			log.debug("JDBC driver does not support 'getDatabaseProductName' method", err);
		}
		databaseType.scrollResultsSupported = scrollResultsSupported;
		return databaseType;
	}
	
}
