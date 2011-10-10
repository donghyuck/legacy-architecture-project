package architecture.ee.jdbc.datasource;

import javax.sql.DataSource;

public interface DataSourceProvider {

	public DataSource getDataSource();
	
}
