package architecture.ee.jdbc.sqlquery.factory;

import javax.sql.DataSource;

import architecture.common.scanner.DirectoryListener;

public interface SqlQueryFactoryBuilder extends DirectoryListener {
	
	public abstract Configuration getConfiguration();
	
	public SqlQueryFactory createSqlQueryFactory() ;
	
	public SqlQueryFactory createSqlQueryFactory(DataSource dataSource);
	
	public boolean isSetDataSource();
	
	
}
