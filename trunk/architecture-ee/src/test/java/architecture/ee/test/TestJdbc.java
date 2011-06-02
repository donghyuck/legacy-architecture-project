package architecture.ee.test;

import static org.junit.Assert.*;

import java.sql.Types;
import java.util.List;
import java.util.Locale;

import org.junit.Test;
import org.springframework.mock.web.MockServletContext;

import architecture.common.lifecycle.ApplicationHelperFactory;
import architecture.common.lifecycle.State;
import architecture.ee.component.AdminService;
import architecture.ee.jdbc.query.SqlQuery;
import architecture.ee.jdbc.query.factory.SqlQueryFactory;

public class TestJdbc {

	public void log(Object obj){
		System.out.println("# " + obj);
	}
	
	@Test
	public void testBoot() {		
		MockServletContext servletContext = new MockServletContext();
		servletContext.addInitParameter(
			"contextConfigLocation", 
			"default-application-context.xml,databaseSubsystemContext.xml"
		);
						
		AdminService admin = ApplicationHelperFactory.getApplicationHelper().getComponent(AdminService.class);
		if(admin.getState() == State.INITIALIZED){
			admin.setServletContext(servletContext);
			log(admin.getState());
			admin.start();
		}	
	}
	
	private SqlQueryFactory getSqlQueryFactory() {
		return ApplicationHelperFactory.getApplicationHelper().getComponent(SqlQueryFactory.class);
	}

	@Test
	public void testGetSqlQueryFacory() throws Exception {
		SqlQueryFactory factory = (SqlQueryFactory) getSqlQueryFactory();
		
		SqlQuery query = getSqlQueryFactory().createSqlQuery();
		List<String> list = query.reset().queryForList("COMMON.SELECT_TABLE_NAMES", String.class);
		System.out.println(list.size());
		
		
	}

	@Test
	public void testSelectAllFromEntApp() throws Exception {
		//SELECT_ALL_FROM_ENT_APP
		
		log( Locale.KOREA.getCountry() ) ;
		log( Locale.KOREA.getLanguage() ) ;
		log( Locale.KOREA.getVariant() ) ;
		log( Locale.KOREA.getISO3Country()) ;
		log( Locale.KOREA.getISO3Language() ) ;
		log( Locale.KOREA.getDisplayName() ) ;
		
		
		SqlQuery query = getSqlQueryFactory().createSqlQuery("COMMON", "SELECT_ALL_ENT_APP" );
		List rows = query.list();
		System.out.println("rows:" + "=" + rows );
	}

	@Test
	public void testBatchUpdate() throws Exception {
		
		//INSERT_ENT_APP_PROPERTY
		SqlQuery query = getSqlQueryFactory().createSqlQuery("COMMON", "DELETE_ALL_ENT_APP_PROPERTY" );		
		int c = query.setString("1").executeUpdate();
		
		log( c + " rows are deleted !!" );
	
		
		query.setStatement("COMMON", "INSERT_ENT_APP_PROPERTY");
		
		for( int i = 0 ; i < 100 ; i ++ ){
			query.setParameters(new Object[]{ "1" , "name-" + i , "value-" + i }, new int []{Types.INTEGER, Types.VARCHAR, Types.VARCHAR});
			query.addToBatch();
		}
		int sum = query.executeUpdate();		
		log( sum + " rows are inserted !!" );

	}
	
	@Test
	public void testQueryForObject() throws Exception {
		
		//COUNT_ENT_APP_PROPERTY
		SqlQuery query = getSqlQueryFactory().createSqlQuery("COMMON", "COUNT_ENT_APP_PROPERTY" );		
		
		Integer count = query.reset().queryForObject("COMMON.COUNT_ENT_APP_PROPERTY", new Integer[]{1}, new int []{Types.INTEGER}, Integer.class);
		
		log("count:" + count);		
		long result = 0;		
	
	}

	//@Test
	public void testQueryScrollableForList() throws Exception {
				
		
		//COUNT_ENT_APP_PROPERTY
		SqlQuery query = getSqlQueryFactory().createSqlQuery("COMMON", "COUNT_ENT_APP_PROPERTY" );
		
				
		Integer count = query.setParameters(new Object[]{1}, new int []{Types.NUMERIC}).uniqueResult(Integer.class);
		log("count:" + count);		
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
