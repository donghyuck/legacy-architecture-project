package architecture.ee.jdbc.datasource.impl;

import javax.sql.DataSource;

import org.apache.commons.dbcp.BasicDataSource;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.jndi.JndiTemplate;

import architecture.common.lifecycle.ConfigService;
import architecture.ee.jdbc.datasource.DataSourceFactory;
import architecture.ee.util.ApplicationHelper;

public class DataSourceFactoryImpl implements DataSourceFactory.Implementation {

	private Log log = LogFactory.getLog(getClass());
	
	private JndiTemplate jndiTemplate = new JndiTemplate();
	
	public DataSource getDataSource() {
		
		DataSource dataSource = null;
		
		ConfigService configService = ApplicationHelper.getConfigService();
		
		String jndiName = configService.getLocalProperty("jdbc-data-source.jndi-data-source.jndi-name");
		
		if(StringUtils.isNotEmpty(jndiName)){
			try{
				log.debug(jndiName);
			    dataSource = jndiTemplate.lookup(jndiName, DataSource.class);
			}catch(Exception e){
				dataSource = null;
			}
		}
		
		if( dataSource == null ){	
			
			String driverClassName = configService.getLocalProperty("jdbc-data-source.basic-dbcp-data-source.driver-class-name");
			String url = configService.getLocalProperty("jdbc-data-source.basic-dbcp-data-source.url");
			String username = configService.getLocalProperty("jdbc-data-source.basic-dbcp-data-source.username");
			String password = configService.getLocalProperty("jdbc-data-source.basic-dbcp-data-source.password");
			
			if(StringUtils.isNotEmpty(url)&StringUtils.isNotEmpty(driverClassName)&StringUtils.isNotEmpty(username)&StringUtils.isNotEmpty(password)){								
				try{
					BasicDataSource basecDataSource = new BasicDataSource();
					basecDataSource.setDriverClassName(driverClassName);
					basecDataSource.setUrl(url);
					basecDataSource.setUsername(username);
					basecDataSource.setPassword(password);
					basecDataSource.setPoolPreparedStatements(true);
					basecDataSource.setInitialSize(3);				
					dataSource = basecDataSource;
				}catch(Exception e){
					dataSource = null;
				}
			}			
		}				
		return dataSource;
	}	
}
