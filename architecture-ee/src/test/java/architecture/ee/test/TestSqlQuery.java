/*
 * Copyright 2010, 2011 INKIUM, Inc.
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
package architecture.ee.test;

import java.sql.Types;
import java.util.List;

import org.junit.Test;
import org.springframework.mock.web.MockServletContext;

import architecture.common.lifecycle.ApplicationHelperFactory;
import architecture.common.lifecycle.State;
import architecture.ee.jdbc.query.SqlQuery;
import architecture.ee.jdbc.query.factory.SqlQueryFactory;
import architecture.ee.spring.lifecycle.AdminService;

public class TestSqlQuery {
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
	public void testSelectAllFromEntApp() throws Exception {
		//SELECT_ALL_FROM_ENT_APP
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
		

		
		//Integer count = query.queryForObject("COMMON.COUNT_ENT_APP_PROPERTY", new Integer[]{1}, new int []{Types.INTEGER}, new SingleColumnRowMapper(Integer.class));
				
				/*
				new RowMapper<Integer> (){
			
			private Class<Integer> requiredType = Integer.class;
			
			protected Object getColumnValue(ResultSet rs, int index) throws SQLException {
				return JdbcUtils.getResultSetValue(rs, index);
			}
			protected Object getColumnValue(ResultSet rs, int index, Class requiredType) throws SQLException {
				if (requiredType != null) {
					return JdbcUtils.getResultSetValue(rs, index, requiredType);
				}
				else {
					// No required type specified -> perform default extraction.
					return getColumnValue(rs, index);
				}
			}
			protected Object convertValueToRequiredType(Object value, Class requiredType) {
				if (String.class.equals(requiredType)) {
					return value.toString();
				}
				else if (Number.class.isAssignableFrom(requiredType)) {
					if (value instanceof Number) {
						// Convert original Number to target Number class.
						return NumberUtils.convertNumberToTargetClass(((Number) value), requiredType);
					}
					else {
						// Convert stringified value to target Number class.
						return NumberUtils.parseNumber(value.toString(), requiredType);
					}
				}
				else {
					throw new IllegalArgumentException(
							"Value [" + value + "] is of type [" + value.getClass().getName() +
							"] and cannot be converted to required type [" + requiredType.getName() + "]");
				}
			}

			public Integer mapRow(ResultSet rs, int rowNum) throws SQLException {
				// Validate column count.
				ResultSetMetaData rsmd = rs.getMetaData();
				int nrOfColumns = rsmd.getColumnCount();
				if (nrOfColumns != 1) {
					throw new IncorrectResultSetColumnCountException(1, nrOfColumns);
				}

				// Extract column value from JDBC ResultSet.
				Object result = getColumnValue(rs, 1, this.requiredType);
				if (result != null && this.requiredType != null && !this.requiredType.isInstance(result)) {
					// Extracted value does not match already: try to convert it.
					try {
						return (Integer) convertValueToRequiredType(result, this.requiredType);
					}
					catch (IllegalArgumentException ex) {
						throw new TypeMismatchDataAccessException(
								"Type mismatch affecting row number " + rowNum + " and column type '" +
								rsmd.getColumnTypeName(1) + "': " + ex.getMessage());
					}
				}
				return (Integer) result;
			}});*/
		
		log("count:" + count);		
		long result = 0;		
		/*
		SqlQuery query = getSqlQueryFactory().createSqlQuery("COMMON", "SELECT_ALL_ENT_APP" );
		List list = query.reset().setMaxResults(10).list();
		log(list.size());
		**/
		
	}

	//@Test
	public void testQueryScrollableForList() throws Exception {
				
		
		//COUNT_ENT_APP_PROPERTY
		SqlQuery query = getSqlQueryFactory().createSqlQuery("COMMON", "COUNT_ENT_APP_PROPERTY" );
		
				
		Integer count = query.setParameters(new Object[]{1}, new int []{Types.NUMERIC}).uniqueResult(Integer.class);
		log("count:" + count);		
		long result = 0;
		
/*
		SqlQuery query = getSqlQueryFactory().createSqlQuery("COMMON", "SELECT_ALL_ENT_APP" );
		List list = query.reset().setMaxResults(10).list();
		log(list.size());
		**/
		
		
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