package architecture.ee.component.data;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

import org.springframework.core.task.AsyncTaskExecutor;

import architecture.common.jdbc.JdbcUtils;
import architecture.common.jdbc.schema.Database;
import architecture.common.jdbc.schema.Table;
import architecture.ee.component.ExcelWriter;
import architecture.ee.component.admin.AdminHelper;
import architecture.ee.services.SqlQueryClient;
import architecture.ee.spring.jdbc.support.SqlQueryDaoSupport;

public class SqlQueryClientImpl extends SqlQueryDaoSupport implements SqlQueryClient {

	private AsyncTaskExecutor taskExecutor = null ;
	
	private DateFormat df = new SimpleDateFormat("yyyyMMddHHmm");
	
	public void setTaskExecutor(AsyncTaskExecutor taskExecutor) {
		this.taskExecutor = taskExecutor;
	}

	protected String getExportFileName(String tableName){
		StringBuilder builder = new StringBuilder(tableName);
		builder.append("_");
		builder.append( df.format(new Date()) );
		builder.append(".xls");
		return builder.toString();
	}
	
	protected File getExportFile(String tableName){
		File base = AdminHelper.getRepository().getFile("database");
		File location = new File(base, "export");
		if( ! location.exists() )
			location.mkdirs();
		
		return new File ( location, getExportFileName(tableName));
	}
	
	protected Database getDatabase(Connection connection, String catalogName, String schemaName, String tableName){
		return JdbcUtils.getDatabase( connection, catalogName, schemaName, tableName);
	}
		
	public void exportToExcel(String catalogName, String schemaName, String tableName, boolean  async) 
	{
		if(async)
			taskExecutor.submit(new TableToExcelTask(this, catalogName, schemaName, tableName ));
		else
			exportToExcel(catalogName, schemaName, tableName);
	}
	
	
	public void exportToExcel(String catalogName, String schemaName, String tableName) {
		Database database = JdbcUtils.getDatabase(getConnection(), catalogName, schemaName, tableName);
		Table table = database.getTable(tableName);
		int idx = 0;
		int columnSize = table.getColumnNames().length;
		Map<String, Integer> types = new HashMap<String, Integer>();
		StringBuilder builder = new StringBuilder();
		builder.append("SELECT ");
		for (String columnName : table.getColumnNames()) {
			idx++;
			builder.append(columnName);
			if (idx < columnSize) {
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
		for (Map<String, Object> item : list) {
			writer.setDataToRow(item, table);
		}
		
		try {
			File file = getExportFile(tableName);
			
			log.debug("now save to file: " + file.getAbsolutePath() );
			
			if (!file.exists())
				file.createNewFile();
			
			writer.write(file);
		} catch (IOException e) {
			log.error(e);
		}		
	}

	
	private static class  TableToExcelTask implements Callable<Boolean> {

		private final SqlQueryClient client ;
		private final String catalogName, schemaName, tableName ;
		
		public TableToExcelTask(SqlQueryClient client, String catalogName, String schemaName, String tableName) {
			this.client = client;
			this.catalogName = catalogName;
			this.schemaName  = schemaName;
            this.tableName   = tableName;
		}

		public Boolean call() throws Exception {
			client.exportToExcel(catalogName, schemaName, tableName);			
			return true;
		}
	}


}
