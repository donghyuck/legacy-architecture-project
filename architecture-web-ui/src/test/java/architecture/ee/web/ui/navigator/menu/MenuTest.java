package architecture.ee.web.ui.navigator.menu;

import org.junit.Test;

public class MenuTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		org.apache.commons.dbcp.BasicDataSource dataSource = new org.apache.commons.dbcp.BasicDataSource();
		/*dataSource.setDriverClassName("oracle.jdbc.driver.OracleDriver");
		dataSource.setUrl("jdbc:oracle:thin:@//222.122.63.138:1521/INKIUMDB");
		dataSource.setUsername("INKIUM_FOREST1");
		dataSource.setPassword("inkium");
		JdbcMenuDao dao = new JdbcMenuDao();
		dao.setQueryString("SELECT MENU_ID, MENU_NAME, DESCRIPTION, MENU_DATA, CREATION_DATE, MODIFIED_DATE FROM V1_MENU");
		dao.setDataSource(dataSource);
		
		MenuRepositoryImpl repo = new MenuRepositoryImpl();
		repo.setMenuDao(dao);
		try {
			repo.load();
			System.out.println( repo.getMenuNames() ) ;
			for(MenuComponent m : repo.getMenu("INKIUM_USER_MENU").getMenuComponents()){
				
				System.out.println( m.toString() );
				
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}*/
		
	}
	
	@Test
	public void testGetMenuList(){
		
	}

}
