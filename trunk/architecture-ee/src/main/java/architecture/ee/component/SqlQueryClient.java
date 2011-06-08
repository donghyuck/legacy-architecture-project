package architecture.ee.component;

import org.springframework.jdbc.support.lob.LobHandler;

import architecture.ee.jdbc.query.SqlQuery;
import architecture.ee.jdbc.schema.Database;
import architecture.ee.spring.jdbc.ExtendedJdbcTemplate;

public interface SqlQueryClient {

	public SqlQuery getSqlQuery();
	
	public SqlQuery getSqlQuery(String statement);
	
	public ExtendedJdbcTemplate getExtendedJdbcTemplate();
	
	public LobHandler getLobHandler();
	
	public String getSql(String statement);
	
	public Database getDatabase(String catalog, String schemaName, String tableName);
	
	public void exportToExcel(String catalog, String schemaName, String tableName, String path);
		
	public void importFromExcel(String catalog, String schemaName, String tableName, String uri);
	
}
