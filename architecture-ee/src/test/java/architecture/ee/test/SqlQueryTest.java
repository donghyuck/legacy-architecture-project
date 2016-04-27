package architecture.ee.test;

import java.io.InputStream;
import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import architecture.common.lifecycle.bootstrap.Bootstrap;
import architecture.common.spring.jdbc.core.ExtendedJdbcTemplate;
import architecture.common.user.Company;
import architecture.ee.jdbc.sqlquery.SqlQuery;
import architecture.ee.jdbc.sqlquery.SqlQueryHelper;
import architecture.ee.jdbc.sqlquery.builder.xml.XmlSqlBuilder;
import architecture.ee.jdbc.sqlquery.factory.Configuration;
import architecture.ee.jdbc.sqlquery.factory.SqlQueryFactory;
import architecture.ee.jdbc.sqlquery.factory.impl.SqlQueryFactoryImpl;
import architecture.ee.jdbc.sqlquery.mapping.MappedStatement;
import architecture.ee.jdbc.sqlquery.mapping.MapperSource;

public class SqlQueryTest {

    private static Log log = LogFactory.getLog(SqlQueryTest.class);

    private static ClassPathXmlApplicationContext context = null;

    @BeforeClass
    public static void setup() {
	log.debug("setup context..");
	//context = new ClassPathXmlApplicationContext("syncSubsystemContext.xml");
    }

    @Test
    public void testXmlSqlBuilder() {
	InputStream input = getClass().getClassLoader().getResourceAsStream("common-sqlset.xml");
	Configuration config = new Configuration();
	XmlSqlBuilder builder = new XmlSqlBuilder(input, config);
	builder.build();
	
	//config.buildAllStatements();
	log.debug( "%%%%%%%%//" + config.getMapper("COMMON.companyRowMapper") );
	for (String name : config.getMappedStatementNames()) {
	    MappedStatement statement = config.getMappedStatement(name);
	    log.debug("SQL:" + name);
	    //log.debug(statement.getBoundSql(null).getParameterMappings());
	}
	
	
	
	
	for (String name : config.getMapperNames()) {
	    MapperSource factory = config.getMapper(name);
	    log.debug("FACTORY: " + factory.getID());
	    //log.debug(statement.getBoundSql(null).getParameterMappings());
	}
    }
    
    
    
    // @Test
    public void testLoadSqlQueryFactory() {
	SqlQueryFactory factory = context.getBean(SqlQueryFactory.class);

	SqlQuery query = factory.createSqlQuery();
	SqlQueryHelper helper = new SqlQueryHelper();
	helper.additionalParameter("TABLE_NAME", "V2_I18N_LOCALE");
	List<Map<String, Object>> list = helper.list(query, "COMMON.SELECT_TABLE_ROWS");
	for (Map row : list)
	    log.debug(row);

	/*
	 * List<Map<String, Object>> list =
	 * query.list("COMMON.SELECT_TABLE_NAMES"); for(Map row : list)
	 * log.debug(row);
	 */

	/*
	 * Integer intValue = query.uniqueResult("COMMON.SELECT_TABLE_COUNT",
	 * Integer.class); log.debug(intValue);
	 */
	/*
	 * List<Map<String, Object>> list =
	 * query.setStartIndex(19).setMaxResults(6).list(
	 * "COMMON.SELECT_TABLE_NAMES"); for(Map row : list) log.debug(row);
	 */

	/*
	 * List<String> list2 =
	 * query.setStartIndex(0).setMaxResults(3).queryForList(
	 * "COMMON.SELECT_TABLE_NAMES_BY_LIKE", new Object[]{ "V2_USER%" }, new
	 * int[] {Types.VARCHAR}, String.class); for(String row : list2)
	 * log.debug(row);
	 */

    }

    // @Test
    public void testLoadXml() {

	InputStream is = getClass().getClassLoader().getResourceAsStream("common-sqlset.xml");
	Configuration config = new Configuration();
	XmlSqlBuilder builder = new XmlSqlBuilder(is, config);
	builder.build();
	
	//config.buildAllStatements();

	for (String name : config.getMappedStatementNames()) {
	    MappedStatement statement = config.getMappedStatement(name);
	    log.debug("SQL:" + name);
	    log.debug(statement.getDescription());
	}
	
	for (String name : config.getMapperNames()) {
	    MapperSource factory = config.getMapper(name);
	    log.debug("Factory:" + name);
	}
    }

    // @Test
    public void testContext() {
	ConfigurableApplicationContext context = Bootstrap.getBootstrapApplicationContext();
	for (String name : context.getBeanDefinitionNames()) {
	    log.debug(name);
	}
    }

    // @Test
    public void testExtJdbcTemplateWithBootstrap() {

	ConfigurableApplicationContext context = Bootstrap.getBootstrapApplicationContext();

	DataSource dataSource = (DataSource) context.getBean("dataSource");
	ExtendedJdbcTemplate template = new ExtendedJdbcTemplate(dataSource);
	List list = template.queryForList("select table_name from tabs");
	for (Object obj : list)
	    log.debug(obj);
    }

    // @Test
    public void testSqlQueryClient() {
	/*
	 * ConfigurableApplicationContext context =
	 * Bootstrap.getBootstrapApplicationContext(); SqlQueryClient client =
	 * (SqlQueryClient)context.getBean("sqlQueryClient"); for( Map row :
	 * client.list("FRAMEWORK_V2.SELECT_ALL_PROPERTY")){ log.debug(row); }
	 */
    }

    // @Test
    public void testSqlQueryWithSpringStyle() {

	DataSource dataSource = (DataSource) context.getBean("dataSource");
	InputStream is = getClass().getClassLoader().getResourceAsStream("common-sqlset.xml");
	Configuration config = new Configuration();
	XmlSqlBuilder builder = new XmlSqlBuilder(is, config);
	builder.build();
	config.buildAllStatements();
	SqlQueryFactoryImpl factory = new SqlQueryFactoryImpl(config);
	factory.setDataSource(dataSource);
	SqlQuery query = factory.createSqlQuery();
	Map row = query.queryForMap("COMMON.SELECT_TABLE_COUNT");
	log.debug(row);

	Integer value = query.queryForObject("COMMON.SELECT_TABLE_COUNT", Integer.class);
	log.debug(value);

	String v = query.queryForObject("COMMON.SELECT_TABLE_NAME", new Object[] { "I18N_COUNTRY" },
		new int[] { Types.VARCHAR }, String.class);
	log.debug(v);

	Map v2 = query.queryForMap("COMMON.SELECT_TABLE_NAME", new Object[] { "I18N_COUNTRY" },
		new int[] { Types.VARCHAR });
	log.debug(v2);

	List v3 = query.queryForList("COMMON.SELECT_TABLE_NAMES", String.class);
	log.debug(v3);

	List v4 = query.setStartIndex(0).setMaxResults(3).queryForList("COMMON.SELECT_TABLE_NAMES_BY_LIKE",
		new Object[] { "V2_USER%" }, new int[] { Types.VARCHAR }, String.class);
	log.debug(v4);

    }

    // @Test
    public void testSqlQueryWithNewStyle() {

	SqlQuery query = context.getBean(SqlQuery.class);
	log.debug(" test 1 start -------------------------------------------");
	log.debug(query.uniqueResult("COMMON.SELECT_TABLE_NAME", "I18N_COUNTRY", String.class));
	log.debug(" test 1 end -------------------------------------------");
	log.debug(" test 2 start -------------------------------------------");
	log.debug(query.setMaxResults(5).list("COMMON.SELECT_TABLE_NAMES_BY_LIKE", "%%", String.class));
	log.debug(" test 2 end -------------------------------------------");

	List list = new ArrayList();
	Map input = new HashMap();
	input.put("TABLE_NAME", "%T%");
	list.add(input);
	log.debug(" test 3 start -------------------------------------------");
	log.debug(query.setMaxResults(5).list("COMMON.SELECT_TABLE_NAMES_BY_LIKE", input));
	log.debug(" test 3 end -------------------------------------------");
	log.debug(" test 4 start -------------------------------------------");

	SqlQueryHelper helper = new SqlQueryHelper();

	helper.parameters(new Object[] { "1", "name1", "value1" },
		new int[] { Types.INTEGER, Types.VARCHAR, Types.VARCHAR }).inqueue();
	helper.parameters(new Object[] { "2", "name2", "value2" },
		new int[] { Types.INTEGER, Types.VARCHAR, Types.VARCHAR }).inqueue();

	helper.executeBatchUpdate(query, "COMMON.INSERT_ENT_APP_PROPERTY");

	helper.parameters(new Object[] { "1", "name1", "value1" },
		new int[] { Types.INTEGER, Types.VARCHAR, Types.VARCHAR });
	helper.additionalParameter("state", 3);
	helper.list(query, "COMMON.INSERT_ENT_APP_PROPERTY");

	// query.executeUpdate("COMMON.INSERT_GLOBAL_PROPERTY", new
	// Object[]{"hello", "hello kkk"});
	for (Map row : query.setMaxResults(15).list("COMMON.SELECT_ALL_GLOBAL_PROPERTY")) {
	    log.debug(row);
	}

	log.debug(" test 4 end -------------------------------------------");
    }
}
