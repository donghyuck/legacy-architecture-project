package architecture.ee.spring.jdbc.datasource;

import javax.sql.DataSource;

import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.FactoryBean;

import architecture.ee.jdbc.datasource.impl.DataSourceFactoryImpl;

public class DataSourceFactoryBean extends DataSourceFactoryImpl implements FactoryBean<DataSource>, DisposableBean {
	
	private DataSource dataSource;
	
	public void destroy() throws Exception {
		if( dataSource != null && dataSource instanceof org.apache.commons.dbcp.BasicDataSource){
			((org.apache.commons.dbcp.BasicDataSource)dataSource).close();
		}
	}

	public DataSource getObject() throws Exception {
		if( dataSource == null )
			dataSource = getDataSource();		
		return dataSource;
	}

	public Class<DataSource> getObjectType() {
		return DataSource.class;
	}

	public boolean isSingleton() {
		return false;
	}

}
