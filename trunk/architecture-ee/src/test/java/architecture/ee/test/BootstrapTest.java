package architecture.ee.test;

import java.util.Locale;
import java.util.ResourceBundle;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Test;

import architecture.common.lifecycle.Repository;
import architecture.ee.admin.AdminHelper;
import architecture.ee.bootstrap.Bootstrap;
import architecture.ee.i18n.I18nCountry;
import architecture.ee.i18n.I18nLocale;
import architecture.ee.i18n.I18nLocalizer;
import architecture.ee.i18n.dao.I18nCountryDao;
import architecture.ee.jdbc.datasource.DataSourceFactory;
import architecture.ee.model.impl.I18nLocalizerModelImpl;
import architecture.ee.user.dao.UserDao;
import architecture.ee.util.ApplicationHelper;
import architecture.ee.util.I18nTextUtils;

public class BootstrapTest {

	private Log log = LogFactory.getLog(getClass());
	
	
	public void testGetEffectiveRootPath() throws Exception{
		Repository repo = Bootstrap.getBootstrapComponent(Repository.class);
		log.info(" - " + repo.getEffectiveRootPath());
		log.info(" - " + repo.getConfigRoot().getRootURI());
	}	
	

	public void testGetDataSource(){
		log.debug( DataSourceFactory.getDataSource() ) ;
	}
	
	
	public void testGetAdminService(){				
		log.debug( AdminHelper.getAdminService() ) ;
	}
		
	
	public void testIsSetupComplete(){				
		log.debug( "isSetupComplete:" +  AdminHelper.isSetupComplete() ) ;
	}

	public void testGetConfigService(){				
		log.debug( AdminHelper.getConfigService().getApplicationPropertyNames() ) ;
	}	
	
	
	public void testI18nCountryDao(){
		
		I18nCountryDao dao = Bootstrap.getBootstrapComponent(I18nCountryDao.class);		
		int size = dao.allCountryCount();
		
		log.debug( "count:" + size);
    	
		for(I18nCountry country :dao.findAll(0, 50 ) ){
			log.debug( ">>" + country ) ;
		}
		
		for(I18nCountry country :dao.findAll(50, 50 ) ){
			log.debug( ">>>>" + country ) ;
		}
		
	}
	
	
	public void testI18nTextUtils(){
	
		String bundleName = "DefaultFrameworkLocalizer" ;		
		log.debug( ApplicationHelper.getLocale() + ">>>>" + I18nTextUtils.getI18nLocalizer(bundleName, null).getString("main.page.title") ) ;
	}


	public void testGetUserDao(){
		Bootstrap.getBootstrapComponent(UserDao.class).getAllUsers();
		
	}
	

	public void testI18nLocalizer(){
		log.debug( "<===========================================" ) ;
		for(String name : AdminHelper.getComponentNames())
			log.debug( name ) ;
		log.debug( "===========================================>" ) ;

		I18nLocale locale = AdminHelper.getI18nTextManager().getI18nLocale(Locale.KOREA, false);		
		int size = AdminHelper.getI18nTextManager().getAvailableI18nLocalizerCount();
		
		log.debug( "count:" + size);

		if( size == 0 ){
			
			I18nLocalizerModelImpl n = new I18nLocalizerModelImpl();
			n.setDescription("");
			n.setI18nLocale(locale);
			n.setNew(true);
			n.setName("framework");			
			AdminHelper.getI18nTextManager().createI18nLocalizer(n);
			
		}else{
			I18nLocalizer u = AdminHelper.getI18nTextManager().getI18nLocalizerById(1);
			u.getI18nTexts().put("main.page.title", "안녕하세요");
			AdminHelper.getI18nTextManager().updateI18nLocalizer(u);
		}
		
		for(I18nLocalizer localizer : AdminHelper.getI18nTextManager().getAvailableI18nLocalizers() ){
			log.debug( ">>>>" + localizer ) ;
		}
		
	}
	

	public void testGetI18nLocalizer(){
		for(I18nLocalizer localizer : I18nTextUtils.getI18nLocalizers("framework")){
			log.debug( ">>>>" + localizer.getResourceBundle() ) ;
		}		
		log.debug( "=======================================" );
		log.debug( ">>>>" +
		I18nTextUtils.getI18nLocalizer("framework", Locale.CANADA)
		) ;
		log.debug( "=======================================" );
		
		ResourceBundle resourceBundle = I18nTextUtils.getResourceBundle("framework", Locale.CANADA);
		
		
		log.debug( ">>>>" + resourceBundle ) ;		
		
	}
	
	@Test
	public void testGetI18nMessage(){
		String key = "main.page.title";
		//log.debug( ApplicationHelper.getMessage(key, new Object[]{}, null));
	}
	
	
}