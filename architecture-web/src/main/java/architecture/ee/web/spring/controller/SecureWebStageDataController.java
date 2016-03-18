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
package architecture.ee.web.spring.controller;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import javax.inject.Inject;
import javax.sql.DataSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.task.TaskExecutor;
import org.springframework.jdbc.support.JdbcUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.NativeWebRequest;

import architecture.common.jdbc.schema.Column;
import architecture.common.jdbc.schema.Database;
import architecture.common.jdbc.schema.Table;
import architecture.common.lifecycle.DatabaseInfo;
import architecture.common.lifecycle.DiskUsage;
import architecture.common.lifecycle.MemoryInfo;
import architecture.common.lifecycle.SystemInfo;
import architecture.common.lifecycle.SystemInformationService;
import architecture.common.user.CompanyManager;
import architecture.ee.component.admin.AdminHelper;
import architecture.ee.exception.NotFoundException;
import architecture.ee.util.ApplicationHelper;
import architecture.ee.web.attachment.AttachmentManager;
import architecture.ee.web.attachment.ImageManager;
import architecture.ee.web.logo.LogoManager;
import architecture.ee.web.navigator.MenuRepository;
import architecture.ee.web.site.WebSiteManager;
import architecture.ee.web.ws.Property;
import net.anotheria.moskito.core.producers.IStatsProducer;
import net.anotheria.moskito.core.registry.IProducerRegistry;
import net.anotheria.moskito.core.registry.ProducerReference;
import net.anotheria.moskito.core.registry.ProducerRegistryFactory;
import net.sf.ehcache.Cache;

@Controller ("secure-web-stage-data-controller")
@RequestMapping("/secure/data")
public class SecureWebStageDataController {

	private static final Log log = LogFactory.getLog(SecureWebStageDataController.class);
	
	@Inject
	@Qualifier("systemInformationService")	
	private SystemInformationService systemInformationService ;	
	
	@Inject
	@Qualifier("imageManager")
	private ImageManager imageManager ;
	
	@Inject
	@Qualifier("attachmentManager")
	private AttachmentManager attachmentManager;

	@Inject
	@Qualifier("menuRepository")
	private MenuRepository menuRepository ;

	@Inject
	@Qualifier("webSiteManager")
	private WebSiteManager webSiteManager;
	
	@Inject
	@Qualifier("companyManager")
	private CompanyManager companyManager;
	
	@Inject
	@Qualifier("logoManager")
	private LogoManager logoManager ;
	
	@Autowired
	private ResourceLoader resourceLoader;

	@Inject
	@Qualifier("taskExecutor")
	private TaskExecutor taskExecutor;
	
	public SecureWebStageDataController() {
	}

	/**
	 * Stage 
	 */
	@RequestMapping(value="/stage/os/get.json",method={RequestMethod.POST, RequestMethod.GET} )
	@ResponseBody
	public SystemInfo  getSystemInfo(NativeWebRequest request) throws NotFoundException {				
		IProducerRegistry ipr = ProducerRegistryFactory.getProducerRegistryInstance();		
		for (ProducerReference ref : ipr.getProducerReferences() ){
			IStatsProducer producer  = ref.get();		
		}
		
		
		return systemInformationService.getSystemInfo();
	}

	@RequestMapping(value="/stage/memory/get.json",method={RequestMethod.POST, RequestMethod.GET} )
	@ResponseBody
	public MemoryInfo  getMemoryInfo(NativeWebRequest request) throws NotFoundException {				
		return systemInformationService.getMemoryInfo();
	}
	
	@RequestMapping(value="/stage/disk/list.json",method={RequestMethod.POST, RequestMethod.GET} )
	@ResponseBody
	public List<DiskUsage>  getDiskUsages(NativeWebRequest request) throws NotFoundException {				
		return systemInformationService.getDiskUsages();
	}	

	@RequestMapping(value="/mgmt/database/list_connection.json",method={RequestMethod.POST, RequestMethod.GET} )
	@ResponseBody
	public List<DatabaseInfo>  getDatabaseConnections(NativeWebRequest request) throws NotFoundException {		
		return systemInformationService.getDatabaseInfos();
	}
	
	@RequestMapping(value="/stage/jdbc/list.json",method={RequestMethod.POST, RequestMethod.GET} )
	@ResponseBody
	public List<DatabaseInfo>  getDatabaseInfos(NativeWebRequest request) throws NotFoundException {				
		return systemInformationService.getDatabaseInfos();
	}
	
	
	private static final SchemaBrowserTask schemaBrowserTask = new SchemaBrowserTask();
	
	@RequestMapping(value="/stage/jdbc/schema/list.json",method={RequestMethod.POST, RequestMethod.GET} )
	@ResponseBody
	public SchemaInfo  getDatabaseSchema(NativeWebRequest request) throws NotFoundException {		
		
		if(schemaBrowserTask.getStatusCode() == 0 ){
			schemaBrowserTask.catalogFilter = ApplicationHelper.getApplicationProperty("performance-monitoring.database.catalog", null);
			schemaBrowserTask.schemaFilter = ApplicationHelper.getApplicationProperty("performance-monitoring.database.schema", null);		
			schemaBrowserTask.dataSource = ApplicationHelper.getComponent("dataSource",  DataSource.class);
			taskExecutor.execute(schemaBrowserTask);
		}		
		SchemaInfo schemaInfo = new SchemaInfo();
		schemaInfo.status = schemaBrowserTask.getStatusCode();
		if( schemaInfo.status == 2 ){
			schemaInfo.catalog = schemaBrowserTask.database.getCatalog();
			schemaInfo.schema = schemaBrowserTask.database.getSchema();
			for( String tableName : schemaBrowserTask.database.getTableNames() )
				schemaInfo.addTable(tableName);
		}		
		return schemaInfo;
	}	

	@RequestMapping(value="/stage/jdbc/schema/get.json",method={RequestMethod.POST, RequestMethod.GET} )
	@ResponseBody
	public architecture.common.jdbc.schema.Table  getDatabaseTable(
			@RequestParam(value="table", required=true ) String table,
		NativeWebRequest request) throws NotFoundException {			
		if( schemaBrowserTask.getStatusCode() == 2 ){
			return schemaBrowserTask.database.getTable(table);		
		}		
		return null;
	}	
	
	
	
	@RequestMapping(value="/stage/cache/list.json",method={RequestMethod.POST, RequestMethod.GET} )
	@ResponseBody
	public List<CacheStats>  listCacheStats(NativeWebRequest request) throws NotFoundException {				
		String[] names = AdminHelper.getCacheManager().getCacheNames();
		List<CacheStats> infos = new ArrayList<CacheStats>(names.length);
		for(String name : names){
			infos.add(new CacheStats(AdminHelper.getCacheManager().getCache(name)));			
		}
		return infos;
	}

	@RequestMapping(value="/stage/cache/get.json",method={RequestMethod.POST, RequestMethod.GET} )
	@ResponseBody
	public CacheStats  getCacheStats(@RequestParam(value="name", required=true ) String name, NativeWebRequest request) throws NotFoundException {		
		Cache cache = AdminHelper.getCacheManager().getCache(name);
		return new CacheStats(cache);
	}
	
	


	public static class SchemaInfo{
		private String schema;
		private String catalog;
		private List<Property> tables;
		private int status;		
		
		
		/**
		 * 
		 */
		public SchemaInfo() {
			status = 0 ;
			catalog = null;
			schema = null;
			tables = new ArrayList<Property>();
		}
		/**
		 * @return schema
		 */
		public String getSchema() {
			return schema;
		}
		/**
		 * @param schema 설정할 schema
		 */
		public void setSchema(String schema) {
			this.schema = schema;
		}
		/**
		 * @return catalog
		 */
		public String getCatalog() {
			return catalog;
		}
		/**
		 * @param catalog 설정할 catalog
		 */
		public void setCatalog(String catalog) {
			this.catalog = catalog;
		}
		/**
		 * @return tables
		 */
		public List<Property> getTables() {
			return tables;
		}
		/**
		 * @param tables 설정할 tables
		 */
		public void setTables(List<Property> tables) {
			this.tables = tables;
		}
		
		public void addTable(String name){
			this.tables.add(new Property("name", name));
		}
		/**
		 * @return status
		 */
		public int getStatus() {
			return status;
		}
		/**
		 * @param status 설정할 status
		 */
		public void setStatus(int status) {
			this.status = status;
		}
	}
	
    
	public static class SchemaBrowserTask implements Runnable {
		
		private AtomicInteger status = new AtomicInteger(0); // none : 0 , running : 1 , done : 2 ;
				
		private String catalogFilter;
		
		private String schemaFilter;
		
		private DataSource dataSource;
		
		private Database database;
		
		public SchemaBrowserTask() {
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
				this.database = new Database(catalogFilter, schemaFilter);		
				try {	
					conn = dataSource.getConnection();
					DatabaseMetaData dbmd = conn.getMetaData();					
					try{
						rs = dbmd.getTables(catalogFilter, schemaFilter, null, new String[]{"TABLE"});					
						while (rs.next()) {
							String catalogName = rs.getString("TABLE_CAT");
							String schemaName = rs.getString("TABLE_SCHEM");
							String tableName = rs.getString("TABLE_NAME");							
							Table table = database.getTable(tableName);
							if (table == null) {
								table = new Table(tableName);
								table.setCatalog(catalogName);
								table.setSchema(schemaName);
								database.addTable(table);
							}
						}
					} finally {
						JdbcUtils.closeResultSet(rs);
					}
					
					for( String tableName : database.getTableNames()){
						try{
							Table table = database.getTable(tableName);						
							rs = dbmd.getColumns(catalogFilter, schemaFilter, table.getName(), null);
							while (rs.next()) {								
								String columnName = rs.getString("COLUMN_NAME");
								String dataTypeName = rs.getString("TYPE_NAME");
								int dataType = Integer.parseInt(rs.getString("DATA_TYPE"));
								int columnSize = Integer.parseInt(rs.getString("COLUMN_SIZE"));
								String comment = rs.getString("COLUMN_DEF");		
								int ordinalPosition = Integer.parseInt(rs.getString("ORDINAL_POSITION"));
								String nullable = rs.getString("IS_NULLABLE");								
								table.addColumn(new Column(columnName, dataType, dataTypeName, columnSize, nullable, comment, ordinalPosition));							
							}							
						} finally {
							JdbcUtils.closeResultSet(rs);
						}
					}
					
					for( String tableName : database.getTableNames()){
						try{
							Table table = database.getTable(tableName);						
							rs = dbmd.getPrimaryKeys(catalogFilter, schemaFilter, table.getName());
							while (rs.next()) {
								String columnName = rs.getString("COLUMN_NAME");
								//KEY_SEQ
								table.setPrimaryKey(table.getColumn(columnName));
								table.getColumn(columnName).setPrimaryKey(true);
							}
						} finally {
							JdbcUtils.closeResultSet(rs);
						}
					}
					status.set(2);
				} catch (SQLException e) {
					status.set(0);
				} finally {
					JdbcUtils.closeConnection(conn);
				}				
			}
		}
	}	
	
	public static class CacheStats {

		private final String cacheName;
		private final int statisticsAccuracy;
		private final long cacheHits;
		private final long onDiskHits;
		private final long offHeapHits;
		private final long inMemoryHits;
		private final long misses;
		private final long onDiskMisses;
		private final long offHeapMisses;
		private final long inMemoryMisses;
		private final long size;
		private final long memoryStoreSize;
		private final long offHeapStoreSize;
		private final long diskStoreSize;
		private final float averageGetTime;
		private final long evictionCount;
		private final long searchesPerSecond;
		private final long averageSearchTime;
		private long writerQueueLength;
		private long maxEntriesLocalHeap = 0;
		private long maxEntriesLocalDisk = 0;
		private boolean diskPersistent = false;
		public CacheStats(String cacheName, int statisticsAccuracy,
				long cacheHits, long onDiskHits, long offHeapHits,
				long inMemoryHits, long misses, long onDiskMisses,
				long offHeapMisses, long inMemoryMisses, long size,
				float averageGetTime, long evictionCount, long memoryStoreSize,
				long offHeapStoreSize, long diskStoreSize,
				long searchesPerSecond, long averageSearchTime,
				long writerQueueLength) {
			this.cacheName = cacheName;
			this.statisticsAccuracy = statisticsAccuracy;
			this.cacheHits = cacheHits;
			this.onDiskHits = onDiskHits;
			this.offHeapHits = offHeapHits;
			this.inMemoryHits = inMemoryHits;
			this.misses = misses;
			this.onDiskMisses = onDiskMisses;
			this.offHeapMisses = offHeapMisses;
			this.inMemoryMisses = inMemoryMisses;
			this.size = size;
			this.averageGetTime = averageGetTime;
			this.evictionCount = evictionCount;
			this.memoryStoreSize = memoryStoreSize;
			this.offHeapStoreSize = offHeapStoreSize;
			this.diskStoreSize = diskStoreSize;
			this.searchesPerSecond = searchesPerSecond;
			this.averageSearchTime = averageSearchTime;
			this.writerQueueLength = writerQueueLength;
		}

		public CacheStats(net.sf.ehcache.Cache cache) {
			this.maxEntriesLocalHeap = cache.getCacheConfiguration().getMaxEntriesLocalHeap();			
			this.maxEntriesLocalDisk = cache.getCacheConfiguration().getMaxEntriesLocalDisk();		
			this.diskPersistent = cache.getCacheConfiguration().isDiskPersistent();
			this.cacheName = cache.getName();			
			this.statisticsAccuracy = cache.getLiveCacheStatistics().getStatisticsAccuracy();			
			this.cacheHits = cache.getLiveCacheStatistics().getCacheHitCount();
			this.onDiskHits = cache.getLiveCacheStatistics().getOnDiskHitCount();
			this.offHeapHits = cache.getLiveCacheStatistics().getOffHeapHitCount(); 
			this.inMemoryHits = cache.getLiveCacheStatistics().getInMemoryHitCount();
			this.misses = cache.getLiveCacheStatistics().getCacheMissCount();
			this.onDiskMisses = cache.getLiveCacheStatistics().getOnDiskMissCount(); 
			this.offHeapMisses = cache.getLiveCacheStatistics().getOffHeapMissCount(); 
			this.inMemoryMisses = cache.getLiveCacheStatistics().getInMemoryMissCount(); 
			this.size = cache.getSizeBasedOnAccuracy(cache.getLiveCacheStatistics().getStatisticsAccuracy());
			this.averageGetTime = cache.getAverageGetTime();
			this.evictionCount = cache.getLiveCacheStatistics().getEvictedCount();
			this.memoryStoreSize = cache.getMemoryStoreSize();
			this.offHeapStoreSize = cache.getOffHeapStoreSize();
			this.diskStoreSize = cache.getDiskStoreSize();
			this.searchesPerSecond = cache.getSearchesPerSecond(); 
			this.averageSearchTime = cache.getAverageSearchTime(); 
			this.writerQueueLength = cache.getLiveCacheStatistics().getWriterQueueLength();
		}

		/**
		 * @return diskPersistent
		 */
		public boolean isDiskPersistent() {
			return diskPersistent;
		}

		/**
		 * @param diskPersistent 설정할 diskPersistent
		 */
		public void setDiskPersistent(boolean diskPersistent) {
			this.diskPersistent = diskPersistent;
		}

		/**
		 * @return maxEntriesLocalHeap
		 */
		public long getMaxEntriesLocalHeap() {
			return maxEntriesLocalHeap;
		}

		/**
		 * @param maxEntriesLocalHeap 설정할 maxEntriesLocalHeap
		 */
		public void setMaxEntriesLocalHeap(long maxEntriesLocalHeap) {
			this.maxEntriesLocalHeap = maxEntriesLocalHeap;
		}

		/**
		 * @return maxEntriesLocalDisk
		 */
		public long getMaxEntriesLocalDisk() {
			return maxEntriesLocalDisk;
		}

		/**
		 * @param maxEntriesLocalDisk 설정할 maxEntriesLocalDisk
		 */
		public void setMaxEntriesLocalDisk(long maxEntriesLocalDisk) {
			this.maxEntriesLocalDisk = maxEntriesLocalDisk;
		}

		/**
		 * @return writerQueueLength
		 */
		public long getWriterQueueLength() {
			return writerQueueLength;
		}

		/**
		 * @param writerQueueLength 설정할 writerQueueLength
		 */
		public void setWriterQueueLength(long writerQueueLength) {
			this.writerQueueLength = writerQueueLength;
		}

		/**
		 * @return cacheName
		 */
		public String getCacheName() {
			return cacheName;
		}

		/**
		 * @return statisticsAccuracy
		 */
		public int getStatisticsAccuracy() {
			return statisticsAccuracy;
		}

		/**
		 * @return cacheHits
		 */
		public long getCacheHits() {
			return cacheHits;
		}

		/**
		 * @return onDiskHits
		 */
		public long getOnDiskHits() {
			return onDiskHits;
		}

		/**
		 * @return offHeapHits
		 */
		public long getOffHeapHits() {
			return offHeapHits;
		}

		/**
		 * @return inMemoryHits
		 */
		public long getInMemoryHits() {
			return inMemoryHits;
		}

		/**
		 * @return misses
		 */
		public long getMisses() {
			return misses;
		}

		/**
		 * @return onDiskMisses
		 */
		public long getOnDiskMisses() {
			return onDiskMisses;
		}

		/**
		 * @return offHeapMisses
		 */
		public long getOffHeapMisses() {
			return offHeapMisses;
		}

		/**
		 * @return inMemoryMisses
		 */
		public long getInMemoryMisses() {
			return inMemoryMisses;
		}

		/**
		 * @return size
		 */
		public long getSize() {
			return size;
		}

		/**
		 * @return memoryStoreSize
		 */
		public long getMemoryStoreSize() {
			return memoryStoreSize;
		}

		/**
		 * @return offHeapStoreSize
		 */
		public long getOffHeapStoreSize() {
			return offHeapStoreSize;
		}

		/**
		 * @return diskStoreSize
		 */
		public long getDiskStoreSize() {
			return diskStoreSize;
		}

		/**
		 * @return averageGetTime
		 */
		public float getAverageGetTime() {
			return averageGetTime;
		}

		/**
		 * @return evictionCount
		 */
		public long getEvictionCount() {
			return evictionCount;
		}

		/**
		 * @return searchesPerSecond
		 */
		public long getSearchesPerSecond() {
			return searchesPerSecond;
		}

		/**
		 * @return averageSearchTime
		 */
		public long getAverageSearchTime() {
			return averageSearchTime;
		}	
	}	
}
