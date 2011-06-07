package architecture.ee.component.internal;

import architecture.ee.component.SqlQeuryClient;
import architecture.ee.spring.jdbc.support.SqlQueryDaoSupport;

public class SqlQeuryClientImpl extends SqlQueryDaoSupport implements SqlQeuryClient {

	public String getSql(String statement) {
		return this.getBoundSql(statement).getSql();
	}
	
}
