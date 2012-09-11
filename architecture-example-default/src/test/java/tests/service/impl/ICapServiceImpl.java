package tests.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.transaction.annotation.Transactional;

import tests.service.ICapService;
import architecture.ee.jdbc.sqlquery.SqlQuery;
import architecture.ee.spring.jdbc.support.SqlQueryDaoSupport;

@Transactional(readOnly = true)
public class ICapServiceImpl extends SqlQueryDaoSupport implements ICapService {

	@Override
	protected void initDao() throws Exception {
		super.initDao();		
	}

	public <T> List<T> queryForList(String statement, Class<T> elementType) {				
		SqlQuery query = getSqlQuery();		
		return query.queryForList(statement, elementType);
	}

	public List<Map<String, Object>> queryForList(String statement, Object[] params, int[] paramTypes) {
		SqlQuery query = getSqlQuery();
		return query.queryForList(statement, params, paramTypes );
	}

	public <T> T queryForObject(String statement, Class<T> elementType) {
		SqlQuery query = getSqlQuery();
		return query.queryForObject(statement, elementType);
	}
	
	public List<Map<String, Object>> queryForList(String statement) {
		SqlQuery query = getSqlQuery();
		return query.list(statement);
	}

}