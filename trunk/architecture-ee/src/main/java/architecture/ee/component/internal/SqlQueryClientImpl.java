package architecture.ee.component.internal;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.vfs.FileObject;
import org.apache.commons.vfs.FileSystemException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.JdbcUtils;

import architecture.common.vfs.VFSUtils;
import architecture.ee.component.SqlQueryClient;
import architecture.ee.data.impexp.excel.ExcelReader;
import architecture.ee.data.impexp.excel.ExcelWriter;
import architecture.ee.jdbc.schema.Database;
import architecture.ee.jdbc.schema.DatabaseFactory;
import architecture.ee.jdbc.schema.Table;
import architecture.ee.spring.jdbc.support.SqlQueryDaoSupport;

public class SqlQueryClientImpl extends SqlQueryDaoSupport implements SqlQueryClient {

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
	}

	
}
