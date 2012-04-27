package architecture.ee.jdbc.datasource.impl;

import javax.sql.DataSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.pool.impl.GenericObjectPool;
import org.springframework.jndi.JndiTemplate;

import architecture.common.jdbc.datasource.DataSourceFactory;
import architecture.common.lifecycle.ApplicationProperties;
import architecture.common.util.StringUtils;

import architecture.ee.component.admin.AdminHelper;

public class DataSourceFactoryImpl implements DataSourceFactory.Implementation {

	private Log log = LogFactory.getLog(getClass());
	
	private JndiTemplate jndiTemplate = new JndiTemplate();
	
	public DataSource getDataSource() {
		
		DataSource dataSource = null;		
		
		ApplicationProperties setupProperties = AdminHelper.getRepository().getSetupApplicationProperties();
		String jndiTag = "database.default.pooledDataSourceProvider";
		if( setupProperties.getChildrenNames(jndiTag).size() > 0 ){
			String jndiName = setupProperties.get( jndiTag + ".jndiName"); 
			if(StringUtils.isNotEmpty(jndiName)){
				try{
				    dataSource = jndiTemplate.lookup(jndiName, DataSource.class);
				}catch(Exception e){
					log.error("There is no Jndi Object with name [" + jndiName + "]", e);
					dataSource = null;
				}
			}
		}		
		
		String pooledTag = "database.default.pooledDataSourceProvider";		
		
		if( dataSource == null && setupProperties.getChildrenNames(pooledTag).size() > 0 ){
			String driverClassName = setupProperties.get( pooledTag + ".driverClassName"); 
			String url = setupProperties.get( pooledTag + ".url"); 
			String username = setupProperties.get( pooledTag + ".username"); 
			String password = setupProperties.get( pooledTag + ".password"); 

			int maxIdle = setupProperties.getIntProperty( pooledTag + ".maxIdle", GenericObjectPool.DEFAULT_MAX_IDLE); 
			int maxActive = setupProperties.getIntProperty( pooledTag + ".maxActive", GenericObjectPool.DEFAULT_MAX_ACTIVE); 
			long maxWait = setupProperties.getLongProperty( pooledTag + ".maxWait", GenericObjectPool.DEFAULT_MAX_WAIT); 
			
			String validationQuery = setupProperties.get( pooledTag + ".validationQuery"); 
			
			boolean testOnBorrow = setupProperties.getBooleanProperty( pooledTag + ".testOnBorrow", GenericObjectPool.DEFAULT_TEST_ON_BORROW); 
			boolean testWhileIdle = setupProperties.getBooleanProperty( pooledTag + ".testWhileIdle", GenericObjectPool.DEFAULT_TEST_WHILE_IDLE); 
						
			long timeBetweenEvictionRunsMillis = setupProperties.getLongProperty( pooledTag + ".timeBetweenEvictionRunsMillis", GenericObjectPool.DEFAULT_TIME_BETWEEN_EVICTION_RUNS_MILLIS); 
			long minEvictableIdleTimeMillis = setupProperties.getLongProperty( pooledTag + ".minEvictableIdleTimeMillis", GenericObjectPool.DEFAULT_MIN_EVICTABLE_IDLE_TIME_MILLIS); 
			int numTestsPerEvictionRun = setupProperties.getIntProperty( pooledTag + ".numTestsPerEvictionRun", GenericObjectPool.DEFAULT_NUM_TESTS_PER_EVICTION_RUN); 
			
			org.apache.commons.dbcp.BasicDataSource basic = new org.apache.commons.dbcp.BasicDataSource();
			
			basic.setDriverClassName(driverClassName);
			basic.setUrl(url);
			basic.setUsername(username);
			basic.setPassword(password);
			
			basic.setMaxIdle(maxIdle);
			basic.setMaxActive(maxActive);
			basic.setMaxWait(maxWait);
			
			if(!StringUtils.isEmpty(validationQuery))
				basic.setValidationQuery(validationQuery);
			
			basic.setTestOnBorrow(testOnBorrow);
			basic.setTestWhileIdle(testWhileIdle);
						
			basic.setMinEvictableIdleTimeMillis(minEvictableIdleTimeMillis);
			basic.setTimeBetweenEvictionRunsMillis(timeBetweenEvictionRunsMillis);
			basic.setNumTestsPerEvictionRun(numTestsPerEvictionRun);
			dataSource = basic;
		}
		 				
		return dataSource;
	}

}
