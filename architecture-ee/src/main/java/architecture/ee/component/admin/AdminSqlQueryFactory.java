package architecture.ee.component.admin;

import architecture.common.scanner.DirectoryListener;
import architecture.ee.jdbc.sqlquery.factory.Configuration;

public interface AdminSqlQueryFactory extends DirectoryListener {

	public abstract Configuration getConfiguration();
	
	public abstract String[] getMappedStatementNames(String namespace);
	
	public abstract String[] getMappedStatementNames();

	
}
