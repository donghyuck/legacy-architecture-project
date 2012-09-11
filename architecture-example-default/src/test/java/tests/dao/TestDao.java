package tests.dao;

import java.util.List;
import java.util.Map;

public interface TestDao {

	public <T> List<T> queryForList(String statement,  Class<T> elementType);	

	public List<Map<String, Object>> queryForList(String statement);
	
	public List<Map<String, Object>> queryForList(String statement, Object[] params, int[] paramTypes);
	
	public <T> T queryForObject(String statement,  Class<T> elementType);
		
}
