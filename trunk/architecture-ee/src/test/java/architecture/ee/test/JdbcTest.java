package architecture.ee.test;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import architecture.ee.service.SqlQueryClient;
import architecture.ee.service.SqlQueryClient.SqlQueryHelper;

public class JdbcTest {

	private static Log log = LogFactory.getLog(JdbcTest.class);
	
	private static String location = "daoSubsystemContext.xml";
		
	private static ClassPathXmlApplicationContext context = null;
	
	public static ApplicationContext getApplicationContext(){		
		if(context == null)
		  context = new ClassPathXmlApplicationContext(location);
		return context;
	}
	
	@Test
	public void testGetContext(){
		ApplicationContext ctx = getApplicationContext();
	}

	@Test
	public void testGetSqlQueryClient(){
		
		ApplicationContext ctx = getApplicationContext();
		
		
		SqlQueryClient client = ctx.getBean(SqlQueryClient.class);
		SqlQueryHelper helper = new SqlQueryHelper(client);
		
		Date now = new Date();		
	   
	    helper.statement("FRAMEWORK_V2.CREATE_ROLE");
		for( int i = 0 ; i < 10 ; i++){
			long id = helper.getMaxId("ROLE");
			helper.parameter(id)
				.parameter("ROLE_USER_OPTION" + id)
				.parameter("사용자 롤" + id )
				.parameter(now)
				.parameter(now)
				.batch();
		}		
		helper.update();
		
		List<Long> list = helper.statement("FRAMEWORK_V2.SELECT_ALL_ROLE_IDS").list(Long.class);		
		helper.statement("FRAMEWORK_V2.SELECT_ROLE_BY_ID");		
		for(Long id : list){
			Map<String,Object> row = helper.parameter(id).uniqueResult();
			System.out.println( row );
		}		
		
	}
}
