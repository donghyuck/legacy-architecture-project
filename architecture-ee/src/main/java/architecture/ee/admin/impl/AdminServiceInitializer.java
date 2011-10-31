package architecture.ee.admin.impl;
import javax.sql.DataSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import architecture.common.lifecycle.ConfigService;
import architecture.ee.admin.AdminHelper;

public class AdminServiceInitializer implements ApplicationContextAware {

	private Log log = LogFactory.getLog(getClass());
	private String dataSourceName = "dataSource" ;
	
	public AdminServiceInitializer() {	
		
	}

	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		
		ConfigService configService = AdminHelper.getConfigService();		
		
		if(configService instanceof ConfigServiceImpl){			
			DataSource dataSource = applicationContext.getBean(dataSourceName, DataSource.class);	
			((ConfigServiceImpl)configService).setDataSource(dataSource);			
		}	
		
		log.info("You are running with FUSE " + AdminHelper.getAdminService().getVersion().getVersionString());
		log.info("Locale:" + configService.getLocale());
		log.info("TimeZone:" + configService.getTimeZone());
		log.info("CharacterEncoding:" + configService.getCharacterEncoding());
		
	}

}
