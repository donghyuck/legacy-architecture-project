package architecture.ee.services;

import java.util.List;
import java.util.Map;

/**
 * 손쉬운 데이터베이스 작업을 위하여 제공되는 SqlQuery 기반의 클라이언트  
 * 
 * @author donghyuck son
 *
 */
public interface SqlQueryClient {
		
	/**
	 * @param statement
	 * @param elementType
	 */
	public <T> T uniqueResult(String statement, Class<T> elementType) ;

	/**
	 * 
	 * @param statement
	 * @param elementType
	 * @param additionalParameters
	 * @return
	 */
	public <T> T uniqueResult(String statement, Class<T> elementType, Map<String, Object> additionalParameters) ;
	/**
	 * 
	 * @param statement
	 * @param elementType
	 * @param parameter
	 * @return
	 */
	public <T> T uniqueResult(String statement, Class<T> elementType, Object parameter) ;

	/**
	 * 
	 * @param statement
	 * @param elementType
	 * @param parameter
	 * @param additionalParameters
	 * @return
	 */
	public <T> T uniqueResult(String statement, Class<T> elementType, Object parameter, Map<String, Object> additionalParameters);

	/**
	 * 
	 * @param statement
	 */
	public List<Map<String, Object>> list(String statement);

	/**
	 * 
	 * @param statement
	 * @param additionalParameters
	 * @return
	 */
	public List<Map<String, Object>> list(String statement, Map<String, Object> additionalParameters);

	/**
	 * 
	 * @param statement
	 * @param parameters
	 * @return
	 */
	public List<Map<String, Object>> list(String statement, Object... parameters) ;
	/**
	 * 
	 * @param statement
	 * @param additionalParameters
	 * @param parameters
	 * @return
	 */
	public List<Map<String, Object>> list(String statement, Map<String, Object> additionalParameters, Object... parameters);

	/**
	 * 
	 * @param statement
	 * @param elementType
	 */
	public <T> List<T> list(String statement, Class<T> elementType) ;
	/**
	 * 
	 * @param statement
	 * @param elementType
	 * @param additionalParameters
	 * @return
	 */
	public <T> List<T> list(String statement, Class<T> elementType, Map<String, Object> additionalParameters);

	/**
	 * 
	 * @param statement
	 * @param elementType
	 * @param parameters
	 * @return
	 */
	public <T> List<T> list(String statement, Class<T> elementType, Object... parameters) ;

	/**
	 * 
	 * @param statement
	 * @param elementType
	 * @param additionalParameters
	 * @param parameters
	 * @return
	 */
	public <T> List<T> list(String statement, Class<T> elementType, Map<String, Object> additionalParameters, Object... parameters);
	
	
	
	/*
	public abstract int executeUpdate(String statement);
	
	public abstract int executeUpdate(String statement, Object parameter);
	
	public abstract Object executeScript(String statement);
	
	public abstract long getNextId(String name);*/
				
}