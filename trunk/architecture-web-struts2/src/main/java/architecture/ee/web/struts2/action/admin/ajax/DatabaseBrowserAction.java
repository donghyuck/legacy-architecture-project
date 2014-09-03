/*
 * Copyright 2012, 2013 Donghyuck, Son
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package architecture.ee.web.struts2.action.admin.ajax;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.concurrent.atomic.AtomicInteger;

import javax.sql.DataSource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.core.task.TaskExecutor;
import org.springframework.jdbc.support.JdbcUtils;

import architecture.common.jdbc.schema.Column;
import architecture.common.jdbc.schema.Database;
import architecture.common.jdbc.schema.DatabaseFactory;
import architecture.common.jdbc.schema.Table;
import architecture.ee.web.struts2.action.support.FrameworkActionSupport;

public class DatabaseBrowserAction extends FrameworkActionSupport {

	private TaskExecutor taskExecutor;
	private static final DatabaseBrowserTask task = new DatabaseBrowserTask();
	private Database database;
	private String targetTableName;
	
	public DatabaseBrowserAction() {
		
	}

	/**
	 * @return taskExecutor
	 */
	public TaskExecutor getTaskExecutor() {
		return taskExecutor;
	}

	/**
	 * @param taskExecutor 설정할 taskExecutor
	 */
	public void setTaskExecutor(TaskExecutor taskExecutor) {
		this.taskExecutor = taskExecutor;
	}
	
	public int getTaskStatusCode(){
		return this.task.getStatusCode();
	}
	
	/**
	 * @return database
	 */
	public Database getDatabase() {
		return database;
	}
	
	public String[] getTableNames(){
		
		if( this.getTaskStatusCode()== 2 ){
			
			for(String t : database.getTableNames())
				log.debug(t);
			
			return this.database.getTableNames();
		}else{
			return new String[]{};		
		}
	}

	public String getCatalog(){
		if(this.getTaskStatusCode()== 2 )
			return this.database.getCatalog();		
		else
			return null;		
	} 
	
	public String getSchema(){
		if(this.getTaskStatusCode()==2  )
			return this.database.getSchema();
		else
			return null;		
	} 	
		
	/**
	 * @return targetTableName
	 */
	public String getTargetTableName() {
		return targetTableName;
	}

	/**
	 * @param targetTableName 설정할 targetTableName
	 */
	public void setTargetTableName(String targetTableName) {
		this.targetTableName = targetTableName;
	}

	public architecture.common.jdbc.schema.Table getTargetTable(){
		if(this.getTaskStatusCode() == 2 && StringUtils.isNotEmpty(targetTableName)) 
			return this.database.getTable(targetTableName);		
		return null;		
	}
		
	/**
	 * @param database 설정할 database
	 */
	public void setDatabase(Database database) {
		this.database = database;
	}

	/**
	 * @return catalogFilter
	 */
	public String getCatalogFilter() {
		String catalog =  getApplicationProperty("performance-monitoring.database.catalog", null);
		if( StringUtils.isEmpty(catalog))
			return null;
		return catalog;
	}

	/**
	 * @return schemaFilter
	 */
	public String getSchemaFilter() {
		String schema =  getApplicationProperty("performance-monitoring.database.schema", null);
		if( StringUtils.isEmpty(schema))
			return null;
		return schema;
		
	}


	public String execute() throws Exception {
		
		if( task.status.get() == 0 ){
			task.action = this;
			taskExecutor.execute(task);
		}
		return success();
	}
	
	
	protected DataSource getDataSource(String dataSourceName){
		return getComponent(dataSourceName, DataSource.class);
	}	
	
	public static class DatabaseBrowserTask implements Runnable {
		
		private AtomicInteger status = new AtomicInteger(0); // none : 0 , running : 1 , done : 2 ;
		
		private DatabaseBrowserAction action ;
		
		public DatabaseBrowserTask() {
		}

		public int getStatusCode(){
			return status.get();
		}

		/**
 *  <OL>
     *	<LI><B>TABLE_CAT</B> String => table catalog (may be <code>null</code>)
     *	<LI><B>TABLE_SCHEM</B> String => table schema (may be <code>null</code>)
     *	<LI><B>TABLE_NAME</B> String => table name
     *	<LI><B>COLUMN_NAME</B> String => column name
     *	<LI><B>DATA_TYPE</B> int => SQL type from java.sql.Types
     *	<LI><B>TYPE_NAME</B> String => Data source dependent type name,
     *  for a UDT the type name is fully qualified
     *	<LI><B>COLUMN_SIZE</B> int => column size.  
     *	<LI><B>BUFFER_LENGTH</B> is not used.
     *	<LI><B>DECIMAL_DIGITS</B> int => the number of fractional digits. Null is returned for data types where  
     * DECIMAL_DIGITS is not applicable.
     *	<LI><B>NUM_PREC_RADIX</B> int => Radix (typically either 10 or 2)
     *	<LI><B>NULLABLE</B> int => is NULL allowed.
     *      <UL>
     *      <LI> columnNoNulls - might not allow <code>NULL</code> values
     *      <LI> columnNullable - definitely allows <code>NULL</code> values
     *      <LI> columnNullableUnknown - nullability unknown
     *      </UL>
     *	<LI><B>REMARKS</B> String => comment describing column (may be <code>null</code>)
     * 	<LI><B>COLUMN_DEF</B> String => default value for the column, which should be interpreted as a string when the value is enclosed in single quotes (may be <code>null</code>)
     *	<LI><B>SQL_DATA_TYPE</B> int => unused
     *	<LI><B>SQL_DATETIME_SUB</B> int => unused
     *	<LI><B>CHAR_OCTET_LENGTH</B> int => for char types the 
     *       maximum number of bytes in the column
     *	<LI><B>ORDINAL_POSITION</B> int	=> index of column in table 
     *      (starting at 1)
     *	<LI><B>IS_NULLABLE</B> String  => ISO rules are used to determine the nullability for a column.
     *       <UL>
     *       <LI> YES           --- if the parameter can include NULLs
     *       <LI> NO            --- if the parameter cannot include NULLs
     *       <LI> empty string  --- if the nullability for the 
     * parameter is unknown
     *       </UL>
     *  <LI><B>SCOPE_CATLOG</B> String => catalog of table that is the scope
     *      of a reference attribute (<code>null</code> if DATA_TYPE isn't REF)
     *  <LI><B>SCOPE_SCHEMA</B> String => schema of table that is the scope
     *      of a reference attribute (<code>null</code> if the DATA_TYPE isn't REF)
     *  <LI><B>SCOPE_TABLE</B> String => table name that this the scope
     *      of a reference attribure (<code>null</code> if the DATA_TYPE isn't REF)
     *  <LI><B>SOURCE_DATA_TYPE</B> short => source type of a distinct type or user-generated
     *      Ref type, SQL type from java.sql.Types (<code>null</code> if DATA_TYPE 
     *      isn't DISTINCT or user-generated REF)
     *   <LI><B>IS_AUTOINCREMENT</B> String  => Indicates whether this column is auto incremented
     *       <UL>
     *       <LI> YES           --- if the column is auto incremented
     *       <LI> NO            --- if the column is not auto incremented
     *       <LI> empty string  --- if it cannot be determined whether the column is auto incremented
     * parameter is unknown
     *       </UL>
     *  </OL>
     *  
		 */
		public void run() {
			if( status.get() == 0){	
				status.set(1);
				Connection conn = null;
				ResultSet rs = null;
				Database databaseToUse = new Database(action.getCatalogFilter(), action.getSchemaFilter());		
				try {	
					conn = action.getDataSource("dataSource").getConnection();
					DatabaseMetaData dbmd = conn.getMetaData();
					
					try{
						rs = dbmd.getTables(action.getCatalogFilter(), action.getSchemaFilter() , null, new String[]{"TABLE"});					
						while (rs.next()) {
							String catalogName = rs.getString("TABLE_CAT");
							String schemaName = rs.getString("TABLE_SCHEM");
							String tableName = rs.getString("TABLE_NAME");							
							Table table = databaseToUse.getTable(tableName);
							if (table == null) {
								table = new Table(tableName);
								table.setCatalog(catalogName);
								table.setSchema(schemaName);
								databaseToUse.addTable(table);
							}
							//action.log.debug(table);
						}
					} finally {
						JdbcUtils.closeResultSet(rs);
					}
					
					for( String tableName : databaseToUse.getTableNames()){
						try{
							Table table = databaseToUse.getTable(tableName);						
							rs = dbmd.getColumns(action.getCatalogFilter(), action.getSchemaFilter(), table.getName(), null);
							while (rs.next()) {
								String columnName = rs.getString("COLUMN_NAME");
								int dataType = Integer.parseInt(rs.getString("DATA_TYPE"));
								table.addColumn(new Column(columnName, dataType));							
							}							
						} finally {
							JdbcUtils.closeResultSet(rs);
						}
					}
					
					for( String tableName : databaseToUse.getTableNames()){
						try{
							Table table = databaseToUse.getTable(tableName);						
							rs = dbmd.getPrimaryKeys(action.getCatalogFilter(), action.getSchemaFilter(), table.getName());
							while (rs.next()) {
								String columnName = rs.getString("COLUMN_NAME");
								//KEY_SEQ
								table.setPrimaryKey(table.getColumn(columnName));					
							}
						} finally {
							JdbcUtils.closeResultSet(rs);
						}
					}					
					action.setDatabase(databaseToUse);					
					status.set(2);
				} catch (SQLException e) {
					// ignore...
					action.log.error(e);
					status.set(0);
				} finally {
					JdbcUtils.closeConnection(conn);
				}				
			}
		}
	}
}
