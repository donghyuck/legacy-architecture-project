package architecture.ee.test;

import java.sql.Types;
import java.util.List;
import java.util.Locale;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.vfs.FileObject;
import org.junit.Test;
import org.springframework.mock.web.MockServletContext;

import architecture.common.lifecycle.ApplicationHelperFactory;
import architecture.common.lifecycle.State;
import architecture.common.vfs.VFSUtils;
import architecture.ee.component.AdminService;
import architecture.ee.component.GlobalizationService;
import architecture.ee.component.SqlQueryClient;
import architecture.ee.data.impexp.excel.ExcelReader;
import architecture.ee.g11n.Country;
import architecture.ee.jdbc.query.SqlQuery;
import architecture.ee.util.LocaleUtils;

public class TestG11N {

	private Log log = LogFactory.getLog(getClass());
	
	@Test
	public void testGetBootstrapApplicationContext(){		
		
		MockServletContext servletContext = new MockServletContext();
		servletContext.addInitParameter(
			"contextConfigLocation", 
			"default-application-context.xml, databaseSubsystemContext.xml, daoSubsystemContext.xml"
		);
		
		servletContext.addInitParameter("RUNTIME_APPLICATION_HOME", "C:/TOOLS/workspace/architecture-ee/profile/default");
		
		AdminService admin = ApplicationHelperFactory.getApplicationHelper().getComponent(AdminService.class);
		if(admin.getState() == State.INITIALIZED){
			admin.setServletContext(servletContext);
			admin.start();
		}	
		
		log.debug( Locale.KOREA );
	}
	
	@Test
	public void testGetAllCountry() {
		Locale lo = Locale.KOREA ;
		GlobalizationService g11n = ApplicationHelperFactory.getApplicationHelper().getComponent(GlobalizationService.class);
		List<Country> list = g11n.getCountries();
		for( Country c : list){
			log.debug( c );
			Locale locale = LocaleUtils.toLocale( c.getA2().toLowerCase() );
			log.debug( locale.getDisplayCountry() );
		}
	}

    @Test
	public void testSaveUpdateLocale() {
		Locale locale = Locale.KOREA ;
		Locale locale2 = Locale.US ;
		Locale locale3 = Locale.JAPAN ;
		GlobalizationService g11n = ApplicationHelperFactory.getApplicationHelper().getComponent(GlobalizationService.class);
		log.debug( g11n.getLocale(locale, true ) );
		log.debug( g11n.getLocale(locale2, true ) );
	    log.debug( g11n.getLocale(locale3, true ) );
	}
	
	//@Test
	public void testImportFromExcel(){
		try {
			
			FileObject fo = VFSUtils.resolveFile("file:///C:/TOOLS/workspace/architecture_v2/architecture-ee/profile/default/database/COUNTRY.xls");
			ExcelReader reader = new ExcelReader(fo.getContent().getInputStream());
			log.debug("getNumberOfSheets:" + reader.getNumberOfSheets());
			log.debug("getSheetName:" + reader.getSheetName());
			log.debug("getPhysicalNumberOfRows:" + reader.getPhysicalNumberOfRows());
			log.debug("getFirstRowNum:" + reader.getFirstRowNum());
			log.debug("getLastRowNum:" + reader.getLastRowNum());
			log.debug("getHeaderFromFirstRow:" + reader.getHeaderFromFirstRow());
			log.debug("getDataFromRow:" + reader.getDataFromRow(1));
			log.debug("getDataFromRow:" + reader.getDataFromRow(reader.getLastRowNum()));
			
			SqlQueryClient service = ApplicationHelperFactory.getApplicationHelper().getComponent(SqlQueryClient.class);
			
			SqlQuery query = service.getSqlQuery();
			Integer count = query.setStatement("FRAMEWORK_V2.COUNT_ALL_COUNTRY").uniqueResult(Integer.class);
			if( count == 0){
				query.setStatement("FRAMEWORK_V2.INSERT_COUNTRY");
				for( int i = 1 ; i < reader.getPhysicalNumberOfRows() ; i ++){
					 List<String> data = reader.getDataFromRow(i);
					 query.setParameters(data.toArray(), new int[]{Types.INTEGER, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.INTEGER });
					 query.addToBatch();
				}
				query.executeBatchUpdate();
			}
		} catch (Exception e) {
			e.printStackTrace();
		
		}
		
	}
}