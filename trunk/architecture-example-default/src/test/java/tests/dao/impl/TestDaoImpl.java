package tests.dao.impl;

import java.io.File;
import java.io.IOException;
import java.sql.Types;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import tests.dao.TestDao;
import architecture.ee.jdbc.sqlquery.SqlQueryHelper;
import architecture.ee.spring.jdbc.support.SqlQueryDaoSupport;

public class TestDaoImpl extends SqlQueryDaoSupport implements TestDao {

	@Override
	protected void initDao() throws Exception {
		// DAO 객체를 초기화할 때 함께 작업할 내용이 있다면 여기에 코드를 삽입한다.
		super.initDao();
	}

	public List<Map<String, Object>> queryForList(String statement) {
		return getSqlQuery().queryForList(statement);
	}

	public List<Map<String, Object>> queryForList(String statement, Object[] params, int[] jdbcTypes) {
		return getSqlQuery().queryForList(statement, params, jdbcTypes);
	}

	public List<String> queryForStringList(String statement) {
		return getSqlQuery().queryForList(statement, String.class);
	}

	public List<String> queryForStringList(String statement, Object[] params, int[] jdbcTypes) {
		return getSqlQuery().queryForList(statement, params, jdbcTypes, String.class);
	}

	public Map<String, Object> queryForMap(String statement) {
		return getSqlQuery().queryForMap(statement);
	}

	public Map<String, Object> queryForMap(String statement, Object[] params,
			int[] jdbcTypes) {
		return getSqlQuery().queryForMap(statement, params, jdbcTypes);
	}
	
	public Object batchUpdate(String statement, List<Object[]> parameters) {
		return  getSqlQuery().batchUpdate(statement, parameters);
	}	

	public Object batchUpdate(String statement, List<Object[]> parameters, int[] jdbcTypes) {
		
		SqlQueryHelper helper = new SqlQueryHelper();
		for( Object[] params : parameters ){
			helper.parameters(params, jdbcTypes).inqueue();
		}		
		return  helper.executeBatchUpdate( getSqlQuery() , statement );			
	}	
	
	public String save(String statement, File file) throws IOException {
		
		UUID uuid = UUID.randomUUID();
	
		SqlQueryHelper helper = new SqlQueryHelper(getLobHandler());
		helper.parameter(uuid.toString());
	
		helper.lob(file);
		getSqlQuery().update( statement,  helper.values(),  new int[] {Types.VARCHAR, Types.BLOB});
		
		return uuid.toString() ;
		
	}
	
	public List<String> queryForStringList(String statement, int startIndex, int maxResults) {
		return getSqlQuery().setStartIndex(startIndex).setMaxResults(maxResults).queryForList(statement, String.class);
	} 

}