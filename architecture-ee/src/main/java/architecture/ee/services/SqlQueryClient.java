package architecture.ee.services;

import java.util.List;
import java.util.Map;

public interface SqlQueryClient {

	public void exportToExcel(String catalogName, String schemaName, String tableName);	
	
	public void exportToExcel(String catalogName, String schemaName, String tableName, boolean asyncMode);	

	public void importFromExcel(String catalogName, String schemaName, String tableName, String uri);	
	
	public void importFromExcel(String catalogName, String schemaName, String tableName, String uri, boolean asyncMode);	
	
	public List<Map<String, Object>> list(String statement);
	
	public List<Map<String, Object>> list(String statement, Object parameters);
	
}
