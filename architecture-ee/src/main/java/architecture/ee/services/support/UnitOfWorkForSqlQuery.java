package architecture.ee.services.support;

import architecture.ee.jdbc.sqlquery.SqlQuery;
import architecture.ee.services.UnitOfWork;

public abstract class UnitOfWorkForSqlQuery implements UnitOfWork {

	
	protected SqlQuery sqlQuery;
	
	public String getName() {
		return getClass().getName();
	}

	public SqlQuery getSqlQuery() {
		return sqlQuery;
	}

	public void setSqlQuery(SqlQuery sqlQuery) {
		this.sqlQuery = sqlQuery;
	}
	
}
