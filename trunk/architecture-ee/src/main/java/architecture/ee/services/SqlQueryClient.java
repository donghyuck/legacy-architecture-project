package architecture.ee.services;

import java.util.List;
import java.util.Map;

public interface SqlQueryClient {

	
	public void exportToExcel(String catalogName, String schemaName, String tableName);	
	
	public void exportToExcel(String catalogName, String schemaName, String tableName, boolean async);	

	
	public void importFromExcel(String catalogName, String schemaName, String tableName, String uri);	
	
	public void importFromExcel(String catalogName, String schemaName, String tableName, String uri, boolean async);	
	
	
	public List<Map<String, Object>> list(String statement);
	
	public List<Map<String, Object>> list(String statement, Object ... parameters);
	
	
	/**
	 * 단일 컬럼에 대한 데이터를 Map 으로 리턴한다.
	 * @param statement
	 * @param parameter
	 * @return
	 */
	public Map<String, Object> uniqueResult(String statement, Object parameter );
	
	/**
	 * 
	 * @param statement
	 * @param parameters
	 * @return
	 */
	public Object call(String statement, Object... parameters);
	
	public Object update(String statement, Object[] values, int[] types);
	
	
	
}
