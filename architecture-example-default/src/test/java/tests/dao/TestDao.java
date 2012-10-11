package tests.dao;

import java.util.List;
import java.util.Map;

public interface TestDao {

	public List<Map<String, Object>> queryForList(String statement );	
	
	public List<Map<String, Object>> queryForList(String statement , Object[] params, int[] jdbcTypes);	
	
	public List<String> queryForStringList(String statement );	
		
	public List<String> queryForStringList(String statement , Object[] params, int[] jdbcTypes);	
	
	public Map<String, Object> queryForMap(String statement );	
	
	public Map<String, Object> queryForMap(String statement, Object[] params, int[] jdbcTypes);	
	
}
