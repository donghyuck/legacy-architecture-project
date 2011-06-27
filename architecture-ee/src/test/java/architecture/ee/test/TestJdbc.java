package architecture.ee.test;

import java.sql.Types;
import java.util.List;
import java.util.Locale;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Test;
import org.springframework.mock.web.MockServletContext;

import architecture.common.lifecycle.ApplicationHelperFactory;
import architecture.common.lifecycle.State;
import architecture.ee.component.AdminService;
import architecture.ee.component.SqlQueryClient;
import architecture.ee.jdbc.query.SqlQuery;
import architecture.ee.jdbc.query.factory.SqlQueryFactory;
import architecture.ee.jdbc.schema.Table;

public class TestJdbc {

	private Log log = LogFactory.getLog(getClass());
	@Test
	public void test(){
		
		
		
	}
	
	//@Test
	public void testBoot() {		
		
		
		MockServletContext servletContext = new MockServletContext();
		servletContext.addInitParameter(
			"contextConfigLocation", 
			"default-application-context.xml,databaseSubsystemContext.xml,daoSubsystemContext.xml"
		);
				
		servletContext.addInitParameter("RUNTIME_APPLICATION_HOME", "C:/TOOLS/workspace/architecture_v2/architecture-ee/profile/default");
		
		
		AdminService admin = ApplicationHelperFactory.getApplicationHelper().getComponent(AdminService.class);
		if(admin.getState() == State.INITIALIZED){
			admin.setServletContext(servletContext);
			log.debug(admin.getState());
			admin.start();
		}	
		
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	private SqlQueryFactory getSqlQueryFactory() {
		return ApplicationHelperFactory.getApplicationHelper().getComponent(SqlQueryFactory.class);
	}

	//@Test
	public void testQueryForList() throws Exception {		
		SqlQuery query = getSqlQueryFactory().createSqlQuery();
		List<String> list = query.reset().queryForList("COMMON.SELECT_TABLE_NAMES", String.class);
		System.out.println("Table Count:" + list.size());
	}

	//@Test
	public void testQueryForUniqueResult() throws Exception {
		SqlQuery query = getSqlQueryFactory().createSqlQuery().setStatement("COMMON.COUNT_TABLE_NAMES" );				
		Integer count = query.uniqueResult(Integer.class);
		log.debug("Table Count:" + count);		
	}
	
	
	//@Test
	public void testQueryForDynamic() throws Exception {
		SqlQuery query = getSqlQueryFactory().createSqlQuery().setStatement("COMMON.SELECT_TABLE_NAMES" ).setReturnType(String.class);
		
		for(String name : (List<String>)query.list()){
			query.setStatement("COMMON.SELECT_ALL_FROM_TABLE").setMaxResults(10).setAdditionalParameter("TABLE_NAME", name);
			List rows = query.list();			
			log.debug("TABLE :" + name);
			int i = 1 ;
			for( Object obj : rows){
				log.debug("<"+ (i++) +">" + obj);
			}
		}
	}
	
	
	//@Test
	public void testSqlQueryClientForExport(){	
		SqlQueryClient client = ApplicationHelperFactory.getApplicationHelper().getComponent(SqlQueryClient.class);		
		client.exportToExcel(null, null, "I18N_COUNTRY", "file:///C:/TOOLS/workspace/architecture_v2/architecture-ee/profile/default/database/export/V2_I18N_COUNTRY.xls");
		client.exportToExcel(null, null, "I18N_REGION", "file:///C:/TOOLS/workspace/architecture_v2/architecture-ee/profile/default/database/export/V2_I18N_REGION.xls");
		client.exportToExcel(null, null, "V2_ZIPCODE", "file:///C:/TOOLS/workspace/architecture_v2/architecture-ee/profile/default/database/export/V2_ZIPCODE.xls");
	}
	
		
	//@Test
	public void testSqlQueryClientForImport1(){	
		SqlQueryClient client = ApplicationHelperFactory.getApplicationHelper().getComponent(SqlQueryClient.class);		
		
		Table table = client.getDatabase(null, null, "V2_I18N_REGION").getTable("V2_I18N_REGION");
		Integer max = client.getExtendedJdbcTemplate().queryForInt("select count (*) from " + table.getName() );
		if( max == 0 )
			client.importFromExcel(null, null, "V2_I18N_REGION", "file:///C:/TOOLS/workspace/architecture_v2/architecture-ee/profile/default/database/export/V2_I18N_REGION.xls");
		
	}
	
	//@Test
	public void testSqlQueryClientForImport2(){	
		SqlQueryClient client = ApplicationHelperFactory.getApplicationHelper().getComponent(SqlQueryClient.class);		
		
		Table table = client.getDatabase(null, null, "V2_I18N_COUNTRY").getTable("V2_I18N_COUNTRY");
		Integer max = client.getExtendedJdbcTemplate().queryForInt("select count (*) from " + table.getName() );
		if( max == 0 )
			client.importFromExcel(null, null, "V2_I18N_COUNTRY", "file:///C:/TOOLS/workspace/architecture_v2/architecture-ee/profile/default/database/export/V2_I18N_COUNTRY.xls");
		
	}

	//@Test
	public void testSqlQueryClientForImport3(){	
		SqlQueryClient client = ApplicationHelperFactory.getApplicationHelper().getComponent(SqlQueryClient.class);		
		
		Table table = client.getDatabase(null, null, "V2_ZIPCODE").getTable("V2_ZIPCODE");
		Integer max = client.getExtendedJdbcTemplate().queryForInt("select count (*) from " + table.getName() );
		if( max == 0 )
			client.importFromExcel(null, null, "V2_ZIPCODE", "file:///C:/TOOLS/workspace/architecture_v2/architecture-ee/profile/default/database/export/V2_ZIPCODE.xls");
		
	}
	
	//@Test
	public void testDyanmic1()throws Exception {
		SqlQueryClient client = ApplicationHelperFactory.getApplicationHelper().getComponent(SqlQueryClient.class);		
		List<Integer> list = client.getSqlQuery("COMMON.SELECT_ALL_USER_ID_BY_STATUS").setReturnType(Integer.class).setAdditionalParameter( "parameters", new Object[]{0,1,2,3}).list();
	    
		
		log.debug(list);
	}
	//@Test
	public void testSelectAllFromEntApp() throws Exception {
		//SELECT_ALL_FROM_ENT_APP
		
		log.debug( Locale.KOREA.getCountry() ) ;
		log.debug( Locale.KOREA.getLanguage() ) ;
		log.debug( Locale.KOREA.getVariant() ) ;
		log.debug( Locale.KOREA.getISO3Country()) ;
		log.debug( Locale.KOREA.getISO3Language() ) ;
		log.debug( Locale.KOREA.getDisplayName() ) ;
		
		
		SqlQuery query = getSqlQueryFactory().createSqlQuery("COMMON", "SELECT_ALL_ENT_APP" );
		List rows = query.list();
		System.out.println("rows:" + "=" + rows );
	}

	//@Test
	public void testBatchUpdate() throws Exception {
		
		//INSERT_ENT_APP_PROPERTY
		SqlQuery query = getSqlQueryFactory().createSqlQuery("COMMON", "DELETE_ALL_ENT_APP_PROPERTY" );		
		int c = query.setString("1").executeUpdate();
		
		log.debug( c + " rows are deleted !!" );
	
		
		query.setStatement("COMMON", "INSERT_ENT_APP_PROPERTY");
		
		for( int i = 0 ; i < 100 ; i ++ ){
			query.setParameters(new Object[]{ "1" , "name-" + i , "value-" + i }, new int []{Types.INTEGER, Types.VARCHAR, Types.VARCHAR});
			query.addToBatch();
		}
		int sum = query.executeUpdate();		
		log.debug( sum + " rows are inserted !!" );

	}
	
	//@Test
	public void testQueryForObject() throws Exception {
		
		//COUNT_ENT_APP_PROPERTY
		SqlQuery query = getSqlQueryFactory().createSqlQuery("COMMON", "COUNT_ENT_APP_PROPERTY" );		
		
		Integer count = query.reset().queryForObject("COMMON.COUNT_ENT_APP_PROPERTY", new Integer[]{1}, new int []{Types.INTEGER}, Integer.class);
		
		log.debug("count:" + count);		
		long result = 0;		
	
	}
	
	/*
	@Test
	public void testQueryForList() throws Exception {

		long result = 0;
		long startMemoryUse = getMemoryUse();

		SqlQuery query = getSqlQueryFactory().createSqlQuery();

		long endMemoryUse = getMemoryUse();
		float approximateSize = (endMemoryUse - startMemoryUse) / 100f;
		result = Math.round(approximateSize);

		System.out.println("SqlQuery Size=" + result / 1024 + " kbytes");

		List<String> list = query.reset().queryForList(
				"COMMON.SELECT_TABLE_NAMES", String.class);
		System.out.println(list.size());
	}

	@Test
	public void testQueryScrollableForList() throws Exception {
		long result = 0;
		long startMemoryUse = getMemoryUse();
		SqlQuery query = getSqlQueryFactory().createSqlQuery();

		long endMemoryUse = getMemoryUse();
		float approximateSize = (endMemoryUse - startMemoryUse) / 100f;
		result = Math.round(approximateSize);

		System.out.println("SqlQuery Size=" + result / 1024 + " kbytes");

		List<String> list = query.reset().setMaxResults(10)
				.queryForList("COMMON.SELECT_TABLE_NAMES", String.class);
		for (String tablename : list) {
			Integer count = query
					.reset()
					.setAdditionalParameter("TABLE_NAME", tablename)
					.queryForObject("COMMON.SELECT_COUNT_FROM_TABLE",
							Integer.class);
			System.out.println(tablename + "=" + count);
		}
	}

	@Test
	public void testQueryForObject() throws Exception {
		long result = 0;
		long startMemoryUse = getMemoryUse();

		SqlQuery query = getSqlQueryFactory().createSqlQuery();

		long endMemoryUse = getMemoryUse();
		float approximateSize = (endMemoryUse - startMemoryUse) / 100f;
		result = Math.round(approximateSize);

		System.out.println("SqlQuery Size=" + result / 1024 + " kbytes");

		List<String> list = query.reset().queryForList(
				"COMMON.SELECT_TABLE_NAMES", String.class);
		for (String tablename : list) {
			Integer count = query
					.reset()
					.setAdditionalParameter("TABLE_NAME", tablename)
					.queryForObject("COMMON.SELECT_COUNT_FROM_TABLE",
							Integer.class);
			if (count > 0) {
				Map row = query.reset()
						.setAdditionalParameter("TABLE_NAME", tablename)
						.queryForMap("COMMON.SELECT_ALL_FROM_TABLE");
				System.out.println(tablename + "=" + row.size());
			}
		}

		for (String tablename : list) {
			Integer count = (Integer) query
					.setStatement("COMMON.SELECT_COUNT_FROM_TABLE")
					.setAdditionalParameter("TABLE_NAME", tablename)
					.setReturnType(Integer.class).uniqueResult();
			if (count > 0) {
				Map row = (Map) query
						.setStatement("COMMON.SELECT_ALL_FROM_TABLE")
						.setAdditionalParameter("TABLE_NAME", tablename)
						.setReturnType(Map.class).uniqueResult();
				System.out.println(tablename + "=" + row.size());
			}
		}

	}

	@Test
	public void testExecuteScript() throws Exception {
		SqlQuery query = getSqlQueryFactory().createSqlQuery();
		Object ret = query.setStatement("COMMON.SELECT_SCRIPT").executeScript();
		List list = (List) ret;
		for (Object o : list)
			System.out.println(o);
	}

	// "SELECT_TABLE_NAME_BY"

	public void testQueryForListWithParams() throws Exception {

		SqlQuery query = getSqlQueryFactory().createSqlQuery();

		StopWatch w = new StopWatch();
		w.start();
		List<String> list = query.reset().queryForList(
				"COMMON.SELECT_TZ_TUTORMONITOR_BY_YEAR",
				new String[] { "2010" }, new int[] { Types.VARCHAR },
				String.class);
		w.stop();
		System.out.println(list.size() + " time:" + w.getTime());
		list.clear();
		w.reset();
		w.start();
		list = query.setStatement("COMMON.SELECT_TZ_TUTORMONITOR_BY_YEAR")
				.setReturnType(String.class).setString("2010").list();
		w.stop();
		System.out.println(list.size() + " time:" + w.getTime());

		for (String content : list) {
			System.out.println(content);
		}
	}

	public void testList() throws Exception {

		SqlQuery query = getSqlQueryFactory().createSqlQuery();
		StopWatch w = new StopWatch();
		w.start();
		List<String> list2 = query.queryForList("COMMON.SELECT_TABLE_NAMES",
				String.class);
		for (String tablename : list2) {
			query.setAdditionalParameter("TABLE_NAME", tablename).queryForList(
					"COMMON.SELECT_ALL_FROM_TABLE");

		}

		w.stop();
		System.out.println(list2.size() + " time:" + w.getTime());
		w.reset();
		w.start();
		List<String> list = query.setStatement("COMMON.SELECT_TABLE_NAMES")
				.setReturnType(String.class).list();
		for (String tablename : list) {
			List<Map<String, Object>> row = query
					.setStatement("COMMON.SELECT_ALL_FROM_TABLE")
					.setReturnType(Map.class)
					.setAdditionalParameter("TABLE_NAME", tablename).list();
		}
		w.stop();
		System.out.println(list.size() + " time:" + w.getTime());
		System.out.println(StatementType.PREPARED.toString());

	}

	// PRIVATE //
	private static int fSAMPLE_SIZE = 100;
	private static long fSLEEP_INTERVAL = 100;

	private static long getMemoryUse() {
		putOutTheGarbage();
		long totalMemory = Runtime.getRuntime().totalMemory();

		putOutTheGarbage();
		long freeMemory = Runtime.getRuntime().freeMemory();

		return (totalMemory - freeMemory);
	}

	private static void putOutTheGarbage() {
		collectGarbage();
		collectGarbage();
	}

	private static void collectGarbage() {
		try {
			System.gc();
			Thread.currentThread().sleep(fSLEEP_INTERVAL);
			System.runFinalization();
			Thread.currentThread().sleep(fSLEEP_INTERVAL);
		} catch (InterruptedException ex) {
			ex.printStackTrace();
		}
	}*/

}
