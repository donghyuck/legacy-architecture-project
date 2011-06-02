package architecture.ee.component.internal;

import architecture.ee.component.QueryService;
import architecture.ee.spring.jdbc.support.SqlQueryDaoSupport;

public class QueryServiceImpl extends SqlQueryDaoSupport implements QueryService {

	public String getSql(String statement) {
		return this.getBoundSql(statement).getSql();
	}
	
}
