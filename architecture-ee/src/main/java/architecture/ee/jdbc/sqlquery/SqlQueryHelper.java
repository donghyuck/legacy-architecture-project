package architecture.ee.jdbc.sqlquery;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.core.SqlParameterValue;

import architecture.ee.jdbc.sqlquery.factory.impl.SqlQueryImpl;


public class SqlQueryHelper {
		
	//private CallableStatementCreatorFactory callableStatementFactory;
	
	private List<Object> values = new ArrayList<Object>(4);
	
	private LinkedList<Object[]> parameterQueue = new LinkedList<Object[]>();
	
	private Map<String, Object> additionalParameters = new HashMap<String, Object>(4);
	
	//private final List<SqlParameter> declaredParameters = new ArrayList<SqlParameter>();
	
	public List<Map<String, Object>> list (SqlQuery sqlQuery, String statement){		
		if( sqlQuery instanceof SqlQueryImpl){
			return sqlQuery.setAdditionalParameters(additionalParameters).queryForList(statement, values.size() == 0 ? null : values.toArray(), null );			
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
	
	public SqlQueryHelper parameter(SqlParameter parameter) {
		values.add(parameter);
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
	
	

}
