package architecture.ee.component;

import java.util.List;
import java.util.Map;

import architecture.ee.service.SqlQueryClient;
import architecture.ee.spring.jdbc.support.ExtendedJdbcDaoSupport;

public class SqlQueryClientImpl extends ExtendedJdbcDaoSupport implements SqlQueryClient {
	
	public <T> T queryForObject(String statement, Class<T> requiredType, Object[] args){
		String queryString = getBoundSql(statement).getSql();		
		return getExtendedJdbcTemplate().queryForObject(queryString, requiredType, args);
	}	
	
	public Map<String, Object> queryForMap(String statement, Object... args){
		String queryString = getBoundSql(statement).getSql();		
		return getExtendedJdbcTemplate().queryForMap(queryString, args);		
	}
	
	public List<Map<String, Object>> queryForList(String statement, Object... args){		
		String queryString = getBoundSql(statement).getSql();		
		return getExtendedJdbcTemplate().queryForList(queryString, args);
	}

	public <T> List<T> queryForList(String statement, Class<T> elementType) {		
		String queryString = getBoundSql(statement).getSql();
		return getExtendedJdbcTemplate().queryForList(queryString, elementType, new Object[]{});
	}
	
	public <T> List<T> queryForList(String statement, Class<T> elementType, Object... args) {		
		String queryString = getBoundSql(statement).getSql();
		return getExtendedJdbcTemplate().queryForList(queryString, elementType, args);
	}

	public int executeUpdate(String statement, Object... args) {
		String queryString = getBoundSql(statement).getSql();
		return getExtendedJdbcTemplate().update(queryString, args);
	}
		
	public int executeBatchUpdate(String statement, final List<Object[]> batchArgs) {		
		String queryString = getBoundSql(statement).getSql();		
		int[] cnt = getExtendedJdbcTemplate().batchUpdate( queryString, batchArgs);
		int sum = 0 ;
		for( int c : cnt){
			sum = sum + c ;
		}		
		return sum;
	}
	
	public long getMaxLongId(String incrementerName){
		return getNextId(incrementerName);
	}
	
/*
	public String getSql(String statement) {
		return this.getBoundSql(statement).getSql();
	}
	
	public Database getDatabase(String catalog, String schemaName, String tableName){
		
		Connection conn = getConnection();
		try {
			return DatabaseFactory.newDatabase(conn, catalog, schemaName, tableName);		
		} catch (SQLException e) {
			return null;
		} finally {
			if(conn != null)
				JdbcUtils.closeConnection(conn);
		}
		
	}
	
	
	@Override
	protected void initDao() throws Exception {
		super.initDao();
	}

	public void importFromExcel(String catalog, String schemaName, String tableName, String uri){
		
		try {
			
			Database database = getDatabase(catalog,schemaName,tableName);	
			final Table table = database.getTable(tableName);
			
			final ExcelReader reader = new ExcelReader(uri);						
			List<Map<String, String>> list = reader.getDataAsList();			
			SqlParameterSource[] batchArgs = new SqlParameterSource[list.size()];
			

			int i = 0;
			for (Map<String, String> values : list ) {
				MapSqlParameterSource source = new MapSqlParameterSource();
				for (Map.Entry<String, String> entry : values.entrySet()) {
					source.addValue( entry.getKey(), entry.getValue(), table.getColumn(entry.getKey()).getType() );
				}				
				batchArgs[i] = source;
				i++;
			}						
			StringBuilder builder = new StringBuilder();
			builder.append("INSERT INTO ").append(table.getName()).append(" (");
			int j = 0 ;
			int columns = table.getColumnNames().length;
			for( String columnName : table.getColumnNames()){
				j ++ ;
				builder.append( columnName );
				if( j < columns ){
					builder.append(", ");
				}
			}			
			builder.append(") VALUES ( ");
			j = 0 ;
			for( String columnName : table.getColumnNames()){
				j ++ ;
				builder.append( ":"+ columnName );
				if( j < columns ){
					builder.append(", ");
				}
			}
			builder.append(") ");
			
			getExtendedJdbcTemplate().batchUpdate(builder.toString(), batchArgs);
												
		} catch (IOException e) {

		}
		
	}
	
	
	public void exportToExcel(String catalog, String schemaName, String tableName, String uri) {
		
		Database database = getDatabase(catalog,schemaName,tableName);		
		Table table = database.getTable(tableName);					
		int idx = 0 ;
		int columnSize = table.getColumnNames().length;		
		Map<String, Integer> types = new HashMap<String, Integer>();
		
		StringBuilder builder = new StringBuilder();
		builder.append("SELECT ");		
		for( String columnName : table.getColumnNames()){
			idx ++ ;
			builder.append( columnName );
			if( idx < columnSize ){
				builder.append(", ");
			}
			types.put(columnName, table.getColumn(columnName).getType());			
		}		
		builder.append(" FROM " + table.getName());	
		List<Map<String, Object>> list = getExtendedJdbcTemplate().queryForList((builder.toString()));	
		ExcelWriter writer = new ExcelWriter();
		writer.addSheet(table.getName());
		writer.getSheetAt(0).setDefaultColumnWidth(15);		
		writer.setHeaderToFirstRow(table);
		for( Map<String, Object> item : list ){		
		    writer.setDataToRow(item, table);
		}		
		try {
			FileObject fo = VFSUtils.resolveFile(uri);
			if(!fo.exists())
				fo.createFile();
			
			writer.write(fo.getContent().getOutputStream());
		} catch (FileSystemException e) {
		}
	}*/

	
}
