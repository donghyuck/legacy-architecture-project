package architecture.ee.spring.jdbc.support;

import javax.sql.DataSource;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;

import architecture.common.jdbc.datasource.DataSourceFactory;
import architecture.common.jdbc.datasource.DataSourceProvider;
import architecture.common.util.StringUtils;

public class DefaultDataSourceProvider implements DataSourceProvider, InitializingBean, FactoryBean<DataSource> {

    private String dataSourceName;
    private DataSource dataSource ;    

	public String getDataSourceName() {
		return dataSourceName;
	}

	public void setDataSourceName(String dataSourceName) {
		this.dataSourceName = dataSourceName;
	}

	
	public DataSource getDataSource() {
		try {
			return getObject();
		} catch (Exception e) {
			
			return null;
		}
	}

	public DataSource getObject() throws Exception {
		return this.dataSource;
	}

	public Class<?> getObjectType() {
		return DataSource.class;
	}

	public boolean isSingleton() {
		return true;
	}

	public void afterPropertiesSet() throws Exception {
		if(StringUtils.isEmpty(dataSourceName))
			dataSourceName = "default";
		
		this.dataSource = DataSourceFactory.getDataSource(dataSourceName);
	}

}
