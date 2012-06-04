package architecture.common.jdbc.schema;

/**
 * @author   donghyuck
 */
public enum DatabaseType {
	
	oracle,
	postgresql,
	mysql,
	hsqldb,
	db2,
	sqlserver,
	interbase,
	derby,
	unknown;
	
	public  boolean transactionsSupported;
	
	public int transactionIsolation;

	// True if the database requires large text fields to be streamed.
	public boolean streamTextRequired;

	// True if the database supports the Statement.setMaxRows() method.
	public boolean streamBlobRequired;

	// True if the database supports the Statement.setFetchSize() method.
	public boolean fetchSizeSupported;

	// True if the database supports correlated subqueries.
	public boolean subqueriesSupported;

	public boolean maxRowsSupported;

	public boolean deleteSubqueriesSupported;

	// True if the database supports scroll-insensitive results.
	public boolean scrollResultsSupported;

	// True if the database supports batch updates.
	public boolean batchUpdatesSupported;

	// databse product name.
	public String databaseProductName;

	// database product version.
	public String databaseProductVersion;

	// database jdbc driver name.
	public String jdbcDriverName;

	// jdbc driver version.
	public String jdbcDriverVersion;
	
}
