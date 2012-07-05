package architecture.ee.services;

public interface SqlQueryClient {

	public void exportToExcel(String catalogName, String schemaName, String tableName);	
	
	public void exportToExcel(String catalogName, String schemaName, String tableName, boolean asyncMode);	

	public void importFromExcel(String catalogName, String schemaName, String tableName, String uri);	
	
	public void importFromExcel(String catalogName, String schemaName, String tableName, String uri, boolean asyncMode);	
	
	
}
