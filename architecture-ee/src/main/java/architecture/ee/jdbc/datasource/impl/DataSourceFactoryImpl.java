package architecture.ee.jdbc.datasource.impl;

import java.util.Collection;

import javax.inject.Inject;
import javax.sql.DataSource;

import org.apache.commons.lang3.ClassUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.pool.impl.GenericObjectPool;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jndi.JndiTemplate;
import org.springframework.util.MethodInvoker;

import architecture.common.exception.RuntimeError;
import architecture.common.lifecycle.ApplicationProperties;
import architecture.common.lifecycle.Repository;
import architecture.common.util.L10NUtils;
import architecture.ee.jdbc.datasource.DataSourceFactory;

public class DataSourceFactoryImpl implements DataSourceFactory {

	private Log log = LogFactory.getLog(getClass());
	
	private JndiTemplate jndiTemplate = new JndiTemplate();
	
	private String profileName ;	
	
	@Inject
	@Qualifier("repository")
	private Repository repository;
	
	
	public String getProfileName() {
		return profileName;
	}

	
	public void setProfileName(String profileName) {
		this.profileName = profileName;
	}
	
	public DataSource getDataSource() {
		if(StringUtils.isEmpty(profileName))
		{
			return getDataSource("default");
		}else{
			return getDataSource(profileName);
		}
	}
	
	
	public DataSource getDataSource(String profileName) {
		
		DataSource dataSource = null;
		log.debug("repository:"+ repository == null ? false : true );
	System.out.println("=====================");
		ApplicationProperties setupProperties = repository.getSetupApplicationProperties();
		log.debug("properties:" + setupProperties);
		Collection<String> c = setupProperties.getChildrenNames("database."+ profileName);
		if( c.size() == 0 ){
			// 주어진 이름에 해당하는 데이터베이스 연결 정보가 존재하지 않음 .
			throw new RuntimeError(L10NUtils.format("003057", profileName));
		}
		
		String jndiTag = "database."+ profileName + ".jndiDataSourceProvider";			
		if( setupProperties.getChildrenNames(jndiTag).size() > 0 ){
			String jndiName = setupProperties.get( jndiTag + ".jndiName"); 
			if(StringUtils.isNotEmpty(jndiName)){
				try{
				    dataSource = new DataSourceProxy(  jndiTemplate.lookup(jndiName, DataSource.class) );
				}catch(Exception e){
					log.warn("There is no Jndi Object with name [" + jndiName + "]", e);
					dataSource = null;
				}
			}
		}else{
			log.warn(L10NUtils.format("003055", jndiTag));			
		}		
		
		String providerTag = "database."+ profileName + ".oracleDataSourceProvider";		
		if( dataSource == null && setupProperties.getChildrenNames(providerTag).size() > 0 ){
			String driverClassName = setupProperties.get( providerTag + ".driverClassName"); 
			String url = setupProperties.get( providerTag + ".url"); 
			String username = setupProperties.get( providerTag + ".user"); 
			String password = setupProperties.get( providerTag + ".password"); 
			boolean connectionCachingEnabled = setupProperties.getBooleanProperty(providerTag + ".connectionCachingEnabled", false );			
			try {
				MethodInvoker invoker = new MethodInvoker();
				Class driverClass = ClassUtils.getClass(driverClassName);
				Object driverObject = driverClass.newInstance();			
				invoker.setTargetObject(driverObject);				
				invoker.setTargetMethod("setURL");
				invoker.setArguments(new Object[]{url});	
				invoker.prepare();
				invoker.invoke();
				
				invoker.setTargetMethod("setUser");
				invoker.setArguments(new Object[]{username});
				invoker.prepare();
				invoker.invoke();

				invoker.setTargetMethod("setPassword");
				invoker.setArguments(new Object[]{password});
				invoker.prepare();
				invoker.invoke();
				
				invoker.setTargetMethod("setConnectionCachingEnabled");
				invoker.setArguments(new Object[]{connectionCachingEnabled});
				invoker.prepare();
				invoker.invoke();
				
				dataSource = (DataSource) driverObject;
				
			} catch (Exception e) {
				log.error(e);
			}			
		}
		
		String pooledTag = "database."+ profileName + ".pooledDataSourceProvider";		
		
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
		}else{
			log.warn(L10NUtils.format("003055", pooledTag));	
		}
		
		if( dataSource == null )
			throw new RuntimeError(L10NUtils.format("003056", profileName));
		
		return dataSource;
	}

}
