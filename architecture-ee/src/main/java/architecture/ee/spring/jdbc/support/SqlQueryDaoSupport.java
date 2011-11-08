package architecture.ee.spring.jdbc.support;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import architecture.ee.jdbc.query.SqlQuery;
import architecture.ee.jdbc.query.factory.SqlQueryFactory;

/**
 * @author  Administrator
 */
public class SqlQueryDaoSupport extends ExtendedJdbcDaoSupport {
	
	/**
	 * @uml.property  name="sqlQueryFactory"
	 * @uml.associationEnd  
	 */
	private SqlQueryFactory sqlQueryFactory = null ;
	
	protected Log log = LogFactory.getLog(getClass());

	/**
	 * @uml.property  name="sqlQuery"
	 * @uml.associationEnd  
	 */
	private SqlQuery sqlQuery = null ;
	
	/**
	 * @return
	 * @uml.property  name="sqlQueryFactory"
	 */
	public SqlQueryFactory getSqlQueryFactory() {
		return sqlQueryFactory;
	}

	/**
	 * @param sqlQueryFactory
	 * @uml.property  name="sqlQueryFactory"
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
	 * @uml.property  name="sqlQuery"
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
