package architecture.ee.services;

import java.util.List;
import java.util.Map;

public interface SqlQueryClient {

	
	public void exportToExcel(String catalogName, String schemaName, String tableName);	
	
	public void exportToExcel(String catalogName, String schemaName, String tableName, boolean async);	
	
	public void importFromExcel(String catalogName, String schemaName, String tableName, String uri);	
	
	public void importFromExcel(String catalogName, String schemaName, String tableName, String uri, boolean async);	
	
	/**
	 * 하나 이상의 데이터를 List<Map> 으로 리턴한다.
	 * @param statement
	 * @param parameters 
	 * @return
	 */
	public List<Map<String, Object>> list(String statement);
	
	/**
	 * 하나 이상의 데이터를 List<Map> 으로 리턴한다.
	 * @param statement
	 * @param parameters 
	 * @return
	 */
	public List<Map<String, Object>> list(String statement, Object ... parameters);
	
	
	/**
	 * 단일 데이터를 Map 으로 리턴한다.
	 * @param statement
	 * @param parameter
	 * @return 데이터가 하나가 아니면 오류가 발생된다. 
	 */
	public Map<String, Object> uniqueResult(String statement, Object parameter );
	
	/**
	 * 단일 데이터를 Map 으로 리턴한다.
	 * @param statement
	 * @return 데이터가 하나가 아니면 오류가 발생된다. 
	 */
	public Map<String, Object> uniqueResult(String statement);
	
	/**
	 * 
	 * @param statement
	 * @return
	 */
	public Object update(String statement);

	/**
	 * 
	 * @param statement
	 * @param parameters
	 * @return
	 */
	public Object update(String statement, Object...parameters );
	
	/**
	 * 배치 모드로 쿼리를 실행한다. int[] 형태로 결과가 리턴된다. 
	 * 
	 * @param statement
	 * @param parameters
	 * @return
	 */
	public Object batchUpdate(String statement, List<Object[]> parameters );
	
		
	/**
	 * 
	 * @param statement
	 * @param parameters
	 * @return
	 */
	public Object update(String statement, Map<String, Object> parameters );
	

	/**
	 * 
	 * @param statement
	 * @param values
	 * @param types
	 * @return
	 */
	public Object update(String statement, Object[] values, int[] types);
	

	/**
	 * 
	 * @param statement
	 * @param parameters
	 * @return 
	 */
	public Object call(String statement, Object... parameters);	
	
	/**
	 * 
	 * @param action
	 * @return
	 */
	public <T> T execute ( SqlQueryCallback<T> action )  ;
	
	/**
	 * 단일 트랜잭션으로 처리되며 scriptName 에 해당하는 스크립트의 특정 함수를  
	 * 호출하고 그결과를 리턴한다. 
	 * 
	 * @param scriptName
	 * @param methodName
	 * @param parameters
	 * @return
	 */
	public  Object unitOfWork ( String scriptName , String methodName ,  Object... parameters );
	
	
}
