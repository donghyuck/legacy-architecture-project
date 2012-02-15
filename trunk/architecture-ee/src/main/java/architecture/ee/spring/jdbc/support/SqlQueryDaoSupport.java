package architecture.ee.spring.jdbc.support;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import architecture.ee.jdbc.sqlquery.SqlQuery;
import architecture.ee.jdbc.sqlquery.factory.SqlQueryFactory;

/**
 * @author  Administrator
 */
public class SqlQueryDaoSupport extends ExtendedJdbcDaoSupport {
	
	/**
	 */
	private SqlQueryFactory sqlQueryFactory = null ;
	
	protected Log log = LogFactory.getLog(getClass());

	/**
	 */
	private SqlQuery sqlQuery = null ;
	
	/**
	 * @return
	 */
	public SqlQueryFactory getSqlQueryFactory() {
		return sqlQueryFactory;
	}

	/**
	 * @param sqlQueryFactory
	 */
	public void setSqlQueryFactory(SqlQueryFactory sqlQueryFactory) {
		this.sqlQueryFactory = sqlQueryFactory;
	}

	public boolean isSetDataSource(){
		if( getDataSource() != null)
			return true;
		return false;		
	}

	public boolean isSetSqlQueryFactory(){
		if( getSqlQueryFactory() != null)
			return true;
		return false;
		
	}
	
	/**
	 * @return
	 */
	public SqlQuery getSqlQuery(){
		if(sqlQuery == null)
		{
			sqlQuery = sqlQueryFactory.createSqlQuery(getExtendedJdbcTemplate());	
		}
		return sqlQuery;
	}
	
	public SqlQuery getSqlQuery(String catelogy, String name){	
		SqlQuery query = getSqlQuery().setStatement(catelogy, name);		
		return query;
	}
	
	public SqlQuery getSqlQuery(String statement){	
		SqlQuery query = getSqlQuery().setStatement(statement);
		return query;
	}
	
}
