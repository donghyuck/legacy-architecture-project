package architecture.ee.services;

import java.util.List;
import java.util.Map;

import architecture.common.jdbc.schema.Database;
import architecture.ee.jdbc.sequencer.incrementer.MaxValueIncrementer;

/**
 * 손쉬운 데이터베이스 작업을 위하여 제공되는 SqlQuery 기반의 클라이언트  
 * 
 * @author donghyuck son
 *
 */
public interface SqlQueryClient {
		
	public abstract <T> T uniqueResult(String statement, Class<T> requiredType);
	
	public abstract <T> T uniqueResult(String statement, Object parameter, Class<T> elementType);
	
	public abstract List<Map<String, Object>> list(String statement);
	
	public abstract List<Map<String, Object>> list(String statement, Object parameter);
	
	public abstract <T> List<T> list(String statement, Class<T> elementType);
	
	public abstract <T> List<T> list(String statement, Object parameter, Class<T> elementType);
	
	public abstract int executeUpdate(String statement);
	
	public abstract int executeUpdate(String statement, Object parameter);
	
	public abstract Object executeScript(String statement);
	
	public abstract MaxValueIncrementer getMaxValueIncrementer();
	
	public abstract Database getDatabase(String catalog, String schemaName, String tableName);
			
}