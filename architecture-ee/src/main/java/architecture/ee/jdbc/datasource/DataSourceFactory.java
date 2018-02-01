package architecture.ee.jdbc.datasource;

import javax.sql.DataSource;

/**
 * @author   donghyuck
 */
public interface DataSourceFactory {
	
	public DataSource getDataSource();

}