package architecture.ee.test;

import java.sql.Types;
import java.util.List;
import java.util.Locale;

import org.apache.commons.vfs.FileObject;
import org.junit.Test;
import org.springframework.mock.web.MockServletContext;

import architecture.common.lifecycle.ApplicationHelperFactory;
import architecture.common.lifecycle.State;
import architecture.common.vfs.VFSUtils;
import architecture.ee.component.AdminService;
import architecture.ee.component.GlobalizationService;
import architecture.ee.component.QueryService;
import architecture.ee.data.impexp.excel.ExcelReader;
import architecture.ee.g11n.Country;
import architecture.ee.jdbc.query.SqlQuery;
import architecture.ee.util.LocaleUtils;

public class TestG11N {

	public void log(Object obj){
		System.out.println("# " + obj);
	}
	
	@Test
	public void testGetBootstrapApplicationContext(){		
		
		MockServletContext servletContext = new MockServletContext();
		servletContext.addInitParameter(
			"contextConfigLocation", 
			"default-application-context.xml, databaseSubsystemContext.xml, daoSubsystemContext.xml"
		);
		
		servletContext.addInitParameter("RUNTIME_SERVER_HOME", "C:/TOOLS/workspace/opensource/architecture_v2/architecture-ee/profile/default");
		
		AdminService admin = ApplicationHelperFactory.getApplicationHelper().getComponent(AdminService.class);
		if(admin.getState() == State.INITIALIZED){
			admin.setServletContext(servletContext);
			admin.start();
		}	
		
		log( Locale.KOREA );
	}
	
	//@Test
	public void testGetAllCountry() {
		Locale lo = Locale.KOREA ;
		GlobalizationService g11n = ApplicationHelperFactory.getApplicationHelper().getComponent(GlobalizationService.class);
		List<Country> list = g11n.getCountries();
		for( Country c : list){
			log( c );
			Locale locale = LocaleUtils.toLocale( c.getA2().toLowerCase() );
			log( locale.getDisplayCountry() );
		}
	}

	//@Test
	public void testSaveUpdateLocale() {
		Locale locale = Locale.KOREA ;
		Locale locale2 = Locale.US ;
		Locale locale3 = Locale.JAPAN ;
		GlobalizationService g11n = ApplicationHelperFactory.getApplicationHelper().getComponent(GlobalizationService.class);
		log( g11n.getLocale(locale, true ) );
		log( g11n.getLocale(locale2, true ) );
		log( g11n.getLocale(locale3, true ) );
	}
	
	@Test
	public void testImportFromExcel(){
		try {
			
			FileObject fo = VFSUtils.resolveFile("file:///C:/TOOLS/workspace/opensource/architecture_v2/architecture-ee/profile/default/database/COUNTRY.xls");
			ExcelReader reader = new ExcelReader(fo.getContent().getInputStream());
			log("getNumberOfSheets:" + reader.getNumberOfSheets());
			log("getSheetName:" + reader.getSheetName());
			log("getPhysicalNumberOfRows:" + reader.getPhysicalNumberOfRows());
			log("getFirstRowNum:" + reader.getFirstRowNum());
			log("getLastRowNum:" + reader.getLastRowNum());
			log("getHeaderFromFirstRow:" + reader.getHeaderFromFirstRow());
			log("getDataFromRow:" + reader.getDataFromRow(1));
			log("getDataFromRow:" + reader.getDataFromRow(reader.getLastRowNum()));
			
			QueryService service = ApplicationHelperFactory.getApplicationHelper().getComponent(QueryService.class);
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