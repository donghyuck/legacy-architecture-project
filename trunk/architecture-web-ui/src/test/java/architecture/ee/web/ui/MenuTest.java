package architecture.ee.web.ui;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import architecture.ee.web.ui.menu.MenuBuilder;
import architecture.ee.web.ui.menu.MenuItem;
import architecture.ee.web.ui.menu.tree.MenuTree;
import architecture.ee.web.util.tree.Node;

public class MenuTest {
	
	private Log log = LogFactory.getLog(getClass());
	private static ApplicationContext context = null ;
	private static ApplicationContext getApplicationContext(){
		if(context == null)
			context = new ClassPathXmlApplicationContext("classpath:/databaseSubsystemContext.xml", "classpath:/daoSubsystemContext.xml");
		
		return context;
	}
	
	@Test
	public void testGetApplicationContext(){
		//getApplicationContext();
	}
	
	@Test
	public void testGetMenu(){
		/*MenuBuilder builder = (MenuBuilder)getApplicationContext().getBean("menuBuilder");
		
		MenuTree menu = builder.getTreeByName("USER");
		System.out.println( "" + menu.getRootElement().getData().getTitle() );
		for( Node<MenuItem> c :  menu.getRootElement().getChildren() ){
			System.out.println( "-" + c.getData().getTitle() );
			for( Node<MenuItem> cc :  c.getChildren() ){
				System.out.println( "  -" + cc.getData().getTitle() );
			}
		}
		*/
	}
}
