package architecture.common.jdbc.schema;

public enum DatabaseType {
	
	/**
	 * ORACLE
	 */
	oracle,

	/**
	 * POSTGRESQL
	 */
	postgresql,

	/**
	 * MYSQL
	 */
	mysql,

	/**
	 * HSQLDB
	 */
	hsqldb,

	/**
	 * DB2
	 */
	db2,

	/**
	 * SQLSERVER
	 */
	sqlserver,

	/**
	 * INTERBASE
	 */
	interbase,

	/**
	 * DERBY
	 */
	derby,

	/**
	 * UNKNOWN
	 */
	unknown;
	
	
	public boolean fetchSizeSupported = false;
	
	public boolean scrollResultsSupported = false;
	
}
