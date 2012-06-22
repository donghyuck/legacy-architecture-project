package architecture.ee.jdbc.sqlquery;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.SqlParameterValue;

import architecture.ee.jdbc.sqlquery.factory.impl.SqlQueryImpl;


public class SqlQueryHelper {
	
	
	
	private List<Object> values = new ArrayList<Object>(4);
	
	private LinkedList<Object[]> parameterQueue = new LinkedList<Object[]>();
	
	private Map<String, Object> additionalParameters = new HashMap<String, Object>(4);
	
	public List<Map<String, Object>> list (SqlQuery sqlQuery, String statement){		
		if( sqlQuery instanceof SqlQueryImpl){
			return ((SqlQueryImpl)sqlQuery).queryForListWithAdditionalParameters(statement, values.size() == 0 ? null : values.toArray(), null, additionalParameters );			
		}
		return sqlQuery.queryForList( statement, values.size() == 0 ? null : values.toArray());
	}
	
	public SqlQueryHelper parameter(Object value) {
		values.add(value);
		return this;
	}
	
	public SqlQueryHelper additionalParameter(String name, Object value) {
		additionalParameters.put(name, value);
		return this;
	}

	public SqlQueryHelper additionalParameters(Map<String, Object> args) {
		additionalParameters.putAll(args);
		return this;
	}
	

	public SqlQueryHelper parameter(Object value, int sqlType) {
		SqlParameterValue parameterValue = new SqlParameterValue(sqlType, value);
		values.add(parameterValue);
		return this;
	}
	
	public SqlQueryHelper parameters(Object[] args) {
		for (Object value : args) {
			values.add(value);
		}
		return this;
	}

	public SqlQueryHelper parameters(Object[] args, int [] sqlTypes) {
		for( int i = 0 ; i < args.length ; i ++ ){
			Object value = args[i];
			int sqlType = sqlTypes [i];
			SqlParameterValue parameterValue = new SqlParameterValue(sqlType, value);
			values.add(parameterValue);
		}
		return this;
	}
	
	public SqlQueryHelper inqueue(){
		if (parameterQueue == null)
			this.parameterQueue = new LinkedList<Object[]>();
		this.parameterQueue.add(this.values.toArray());
		values.clear();
		return this;
	}
	
	
	public int executeBatchUpdate(SqlQuery sqlQuery, String statement){
		try {
			return sqlQuery.executeUpdate(statement, parameterQueue);
		} finally {
			values.clear();
			parameterQueue.clear();
		}
	}
	
	
	
/*
	private int startIndex = 0;

	private int maxResults = 0;

	private AbstractSqlQueryClient client;

	

	private List<Object> values = new ArrayList<Object>(4);

	

	private String statement;

	public SqlQueryHelper(SqlQueryClient client) {
		this.client = (AbstractSqlQueryClient)client;
	}
	
	public SqlQueryHelper(AbstractSqlQueryClient client) {
		this.client = client;
	}

	public SqlQueryHelper startIndex(int startIndex) {
		this.startIndex = startIndex;
		return this;
	}

	public SqlQueryHelper maxResults(int maxResults) {
		this.maxResults = maxResults;
		return this;
	}

	public void reset() {
		values.clear();
		this.maxResults = 0;
		this.startIndex = 0;
		additionalParameters.clear();
		if (parameterQueue != null)
			parameterQueue.clear();
	}

	public int update() {
		int count = 0;
		if (parameterQueue != null && parameterQueue.size() > 0) {
			count = client.executeBatchUpdate(statement, parameterQueue);
		} else {
			count = client.executeUpdate(statement, values.toArray());
		}
		reset();
		return count;
	}

	public Map<String, Object> uniqueResult() {
		Map<String, Object> uniqueResult = client.queryForMap(statement, values.toArray());
		reset();
		return uniqueResult;
	}

	public <T> T uniqueResult(Class<T> requiredType) {
		Object uniqueResult = client.queryForObject(statement, requiredType,
				values.toArray());
		reset();
		return (T) uniqueResult;
	}

	public <T> List<T> list(Class<T> elementType) {
		List<T> list;
		if (maxResults > 0) {
			list = client.scroll(statement, elementType, startIndex,
					maxResults, values.toArray());
		} else {
			list = client
					.queryForList(statement, elementType, values.toArray());
		}
		reset();
		return list;
	}

	public List<Map<String, Object>> list() {
		List<Map<String, Object>> list;
		if (maxResults > 0) {
			list = client.scroll(statement, startIndex, maxResults,
					values.toArray());
		} else {
			list = client.queryForList(statement, values.toArray());
		}
		reset();
		return list;
	}

	public long getMaxId(String incrementerName) {
		return client.getMaxLongId(incrementerName);
	}

	public SqlQueryHelper batch() {
		if (parameterQueue == null)
			this.parameterQueue = new LinkedList<Object[]>();

		this.parameterQueue.add(this.values.toArray());
		values.clear();
		return this;
	}

	public SqlQueryHelper statement(String statement) {
		this.statement = statement;
		return this;
	}

	public SqlQueryHelper parameter(Object value) {
		values.add(value);
		return this;
	}

	public SqlQueryHelper parameters(Object[] args) {
		for (Object value : args) {
			values.add(value);
		}
		return this;
	}

	public SqlQueryHelper parameter(int argType, Object value) {
		SqlParameterValue paramValue = new SqlParameterValue(argType, value);
		values.add(paramValue);
		return this;
	}

	public SqlQueryHelper parameters(int[] argTypes, Object[] args) {
		int size = args.length;
		for (int i = 0; i < size; i++) {
			SqlParameterValue paramValue = new SqlParameterValue(argTypes[i],
					args[i]);
			values.add(paramValue);
		}
		return this;
	}

	public SqlQueryHelper additionalParameter(String name, Object value) {
		additionalParameters.put(name, value);
		return this;
	}

	public SqlQueryHelper additionalParameters(Map<String, Object> args) {
		additionalParameters.putAll(args);
		return this;
	}

	protected String getQueryString(String statement) {
		String queryString = client.getBoundSql(statement).getSql();
		return queryString;
	}*/
}
