package architecture.ee.component;

import java.util.List;
import java.util.Map;

import architecture.common.jdbc.JdbcUtils;
import architecture.common.jdbc.schema.Database;
import architecture.ee.jdbc.sequencer.incrementer.MaxValueIncrementer;
import architecture.ee.jdbc.sqlquery.SqlQuery;
import architecture.ee.services.SqlQueryClient;
import architecture.ee.spring.jdbc.support.SqlQueryDaoSupport;

public abstract class AbstractSqlQueryClient extends SqlQueryDaoSupport implements SqlQueryClient {
	
	private SqlQuery sqlQuery ;
	
	private Database datebase ;
	
	private MaxValueIncrementer incrementer ;
	
	public MaxValueIncrementer getMaxValueIncrementer() {
		return incrementer;
	}

	public void setMaxValueIncrementer(MaxValueIncrementer incrementer) {
		this.incrementer = incrementer;
	}

	@Override
	protected void initTemplateConfig() {
		super.initTemplateConfig();
		this.sqlQuery = getSqlQuery();
	}
	
	protected SqlQuery getSqlQuery() {
		if( this.sqlQuery == null )
			this.sqlQuery = super.getSqlQuery();
		return this.sqlQuery ;
	}
	
	
	public <T> T uniqueResult(String statement, Class<T> requiredType) {
		return sqlQuery.uniqueResult(statement, requiredType);
	}

	public <T> T uniqueResult(String statement, Object parameter, Class<T> elementType) {
		return sqlQuery.uniqueResult(statement, parameter, elementType);
	}

	public List<Map<String, Object>> list(String statement) {
		return sqlQuery.list(statement);
	}

	public List<Map<String, Object>> list(String statement, Object parameter) {
		return sqlQuery.list(statement, parameter);
	}

	public <T> List<T> list(String statement, Class<T> elementType) {
		return sqlQuery.list(statement, elementType);
	}

	public <T> List<T> list(String statement, Object parameter, Class<T> elementType) {
		return sqlQuery.list(statement, parameter, elementType);
	}

	public int executeUpdate(String statement) {
		return sqlQuery.executeUpdate(statement);
	}

	public int executeUpdate(String statement, Object parameter) {
		return sqlQuery.executeUpdate(statement, parameter);
	}

	public Object executeScript(String statement) {
		return sqlQuery.executeScript(statement);
	}

	public Database getDatabase(String catalog, String schema, String table){
		return JdbcUtils.getDatabase(getConnection(), catalog, schema, table);
	}

}
