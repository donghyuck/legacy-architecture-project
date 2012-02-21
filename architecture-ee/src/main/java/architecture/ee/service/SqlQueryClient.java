package architecture.ee.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.SqlParameterValue;


/**
 * 손쉬운 데이터베이스 작업을 위하여 제공되는 SqlQuery 기반의 클라이언트  
 * 
 * 
 * @author donghyuck son
 *
 */
public interface SqlQueryClient {
		
	public static class SqlQueryHelper {
		
		private SqlQueryClient client ;
		
		private Map<String, Object> additionalParameters = new HashMap<String, Object>(4);
		
		private List<Object> values = new ArrayList<Object>(4);

		private LinkedList<Object[]> parameterQueue;

		private String statement ;
		
		public SqlQueryHelper(SqlQueryClient client) {
			this.client = client;
		}
		
		public void reset(){
			values.clear();
			additionalParameters.clear();
			if(parameterQueue != null)
				parameterQueue.clear();
		}
				
		public int update(){
			int count = 0 ;
			if(parameterQueue != null && parameterQueue.size() > 0){
				count = client.executeBatchUpdate(statement, parameterQueue);
			}else{
				count = client.executeUpdate(statement, values.toArray());
			}			
			reset();
			return count;
		}
				
		
		public Map<String, Object> uniqueResult(){	
			Map<String, Object> uniqueResult = client.queryForMap(statement, values.toArray());
			reset();
			return uniqueResult;
		}
		
		public <T> T uniqueResult(Class<T> requiredType){			
			Object uniqueResult = client.queryForObject(statement, requiredType, values.toArray());
			reset();
			return (T)uniqueResult;
		}
		
		public <T> List<T> list (Class<T> elementType){			
			List<T> list = client.queryForList(statement, elementType, values.toArray());
			reset();
			return list;
		}
				
		public List<Map<String, Object>> list (){		
			List<Map<String, Object>> list = client.queryForList(statement, values.toArray());
			reset();
			return list;
		}
		
		public long getMaxId(String incrementerName){
			return client.getMaxLongId(incrementerName);
		}
		
		public SqlQueryHelper batch() {
			if (parameterQueue == null)
				this.parameterQueue = new LinkedList<Object[]>();
			
			this.parameterQueue.add(this.values.toArray());
			values.clear();
			return this;
		}
		
		public SqlQueryHelper statement(String statement) {
			this.statement = statement;
			return this;
		}
		
		public SqlQueryHelper parameter(Object value) {
			values.add(value);		
			return this;
		}

		public SqlQueryHelper parameters(Object[] args) {
			for(Object value : args){
				values.add(value);
			}
			return this;
		}
		
		public SqlQueryHelper parameter(int argType, Object value) {
			SqlParameterValue paramValue = new SqlParameterValue(argType, value); 
			values.add(paramValue);			
			return this;
		}

		public SqlQueryHelper parameters(int[] argTypes, Object[] args) {			
			int size = args.length;
			for( int i = 0 ; i < size ; i++){
				SqlParameterValue paramValue = new SqlParameterValue(argTypes[i], args[i]); 
				values.add(paramValue);
			}
			return this;
		}
		
		public SqlQueryHelper additionalParameter(String name, Object value) {
			additionalParameters.put(name, value);
			return this;
		}

		public SqlQueryHelper additionalParameters(Map<String, Object> args) {
			additionalParameters.putAll(args);
			return this;
		}

	}

	public abstract <T> T queryForObject(String statement, Class<T> requiredType, Object[] args);
		
	public abstract Map<String, Object> queryForMap(String sql, Object... args);	
	
	public abstract List<Map<String, Object>> queryForList(String statement, Object... args);	
	
	public abstract <T> List<T> queryForList(String statement, Class<T> elementType); 
	
	public abstract <T> List<T> queryForList(String statement, Class<T> elementType, Object... args);
	
	public abstract int executeUpdate(String sql, Object... args);
	
	public abstract int executeBatchUpdate(String sql, List<Object[]> batchArgs);
	
	
	/**
	 * 테이블을 기반으로 유니크한 값을 리턴한다.
	 * @param incrementerName
	 * @return
	 */
	public abstract long getMaxLongId(String incrementerName);
	
	
	/**
	 * SqlQuery 객체를 리턴한다.
	 * 
	 * @return
	 */
	//public SqlQuery getSqlQuery();
	
	/**
	 * 키(statement)에 해당하는 SqlQuery 객체를 리턴한다.
	 * 
	 * @param statement 키는 [카테고리].[쿼리아이디] 형태로 구성된다.
	 * @return
	 */
	//public SqlQuery getSqlQuery(String statement);

	/**
	 * Springframework 에서 제공하는 JdbcTemplate 의 기능을 확장한 클래스 ExtendedJdbcTemplate 객체를 리턴한다.
	 * @return
	 */
	//public ExtendedJdbcTemplate getExtendedJdbcTemplate();
	
	/**
	 * LOB 데이터를 처리하는 핸들러 객체를 리턴한다.
	 * @return
	 */
	//public LobHandler getLobHandler();
	
	/**
	 * 키에해당하는 SQL 문을 리턴한다. 키는 [카테고리].[쿼리아이디] 형태로 구성된다.
	 * 
	 * @param statement 키는 [카테고리].[쿼리아이디] 형태로 구성된다.
	 * @return
	 */
	//public String getSql(String statement);
	
	/**
	 * 데이터베이스의 테이블 스키마 정보를 리턴한다.
	 * 
	 * @param catalog 카탈로그 명
	 * @param schemaName 스키마 명
	 * @param tableName 테이블 명
	 * @return
	 */
	//public Database getDatabase(String catalog, String schemaName, String tableName);
	
	/**
	 * 지정된 테이블의 데이터를 지정된 경로에 Excel 파일을 생성하여 저장한다.   
	 * 
	 * @param catalog 카탈로그 명
	 * @param schemaName 스키마 명
	 * @param tableName 테이블 명
	 * @param uri Excel 파일 위치
	 */
	//public void exportToExcel(String catalog, String schemaName, String tableName, String uri);
		
	/**
	 * 지정된 경로에 Excel 파일을 읽어드려 지정된 테이블에 저장한다. 오직 (INSERT만을 지원한다.)
	 * 
	 * @param catalog 카탈로그 명
	 * @param schemaName 스키마 명
	 * @param tableName 테이블 명
	 * @param uri Excel 파일 위치
	 */
	//public void importFromExcel(String catalog, String schemaName, String tableName, String uri);
	
}