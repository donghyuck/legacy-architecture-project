package architecture.ee.spring.jdbc.support;

import javax.sql.DataSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.jdbc.support.lob.LobHandler;

import architecture.ee.jdbc.sqlquery.SqlQuery;
import architecture.ee.jdbc.sqlquery.factory.SqlQueryFactory;
import architecture.ee.spring.jdbc.ExtendedJdbcTemplate;

/**
 * @author  andang, son
 */
public class SqlQueryDaoSupport extends JdbcDaoSupport {
	
	protected Log log = LogFactory.getLog(getClass());
	
	private SqlQueryFactory sqlQueryFactory = null ;

	public SqlQueryFactory getSqlQueryFactory() {
		return sqlQueryFactory;
	}

	public void setSqlQueryFactory(SqlQueryFactory sqlQueryFactory) {
		this.sqlQueryFactory = sqlQueryFactory;
	}

	protected ExtendedJdbcTemplate createJdbcTemplate(DataSource dataSource) {
		return new ExtendedJdbcTemplate(dataSource);
	}
	
	public ExtendedJdbcTemplate getExtendedJdbcTemplate(){
		return (ExtendedJdbcTemplate) getJdbcTemplate();
	} 
	
	protected void initTemplateConfig() {
	    log.debug("initTemplateConfig");
	    getExtendedJdbcTemplate().initialize();
	}
	
	public LobHandler getLobHandler(){
		return getExtendedJdbcTemplate().getLobHandler();
	} 
	
	public void setLobHandler(LobHandler lobHandler){
		getExtendedJdbcTemplate().setLobHandler(lobHandler);
	} 

	private boolean isSetDataSource(){
		if( getDataSource() != null)
			return true;
		return false;		
	}
	
	private boolean isSetSqlQueryFactory(){
		if( sqlQueryFactory != null )
			return true;
		return false;
		
	}
		
	protected SqlQuery getSqlQuery(){
		if(isSetSqlQueryFactory()){
			if(isSetDataSource())
				return sqlQueryFactory.createSqlQuery(getExtendedJdbcTemplate());	
			else 
				return sqlQueryFactory.createSqlQuery();			
		}
		return null;
	}	
	
	protected SqlQuery getSqlQuery(DataSource dataSource){
		if(isSetSqlQueryFactory())
			return sqlQueryFactory.createSqlQuery(dataSource);
		return null;
	}
}
