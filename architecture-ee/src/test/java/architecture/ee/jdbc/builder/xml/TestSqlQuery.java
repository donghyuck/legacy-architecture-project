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
package architecture.ee.jdbc.builder.xml;

import java.sql.Types;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.commons.lang.time.StopWatch;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import architecture.ee.jdbc.query.SqlQuery;
import architecture.ee.jdbc.query.factory.SqlQueryFactory;
import architecture.ee.jdbc.query.mapping.StatementType;

public class TestSqlQuery {

	private static ApplicationContext context = null;

	@Before
	public void setUp() throws Exception {
		String resource = "classpath*:databaseSubsystemContext.xml";
		if (context == null) {
/*			System.out.println("setup ========================1");*/
			context = new ClassPathXmlApplicationContext(resource);
		}
/*
		if (context == null) {
			System.out.println("setup ========================2");
			context = new ClassPathXmlApplicationContext("/" + resource);
		}*/
	
	}

	private SqlQueryFactory getSqlQueryFactory() {
		return (SqlQueryFactory) context.getBean("sqlQueryFactory");
	}

	@Test
	public void testGetDatasource() throws Exception {
		DataSource dataSource = (DataSource) context.getBean("dataSource");
		System.out.println("dataSource" + "=" + dataSource);
	}

	@Test
	public void testGetSqlQueryFactory() throws Exception {
		// for ( int i = 0 ; i < 100 ; i ++){
		System.out.println("sqlQueryFactory" + "=" + getSqlQueryFactory());
		// }
	}

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
	}
}