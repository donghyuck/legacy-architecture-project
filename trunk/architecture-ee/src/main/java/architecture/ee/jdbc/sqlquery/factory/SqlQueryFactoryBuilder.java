package architecture.ee.jdbc.sqlquery.factory;

import architecture.ee.jdbc.sqlquery.factory.impl.SqlQueryFactoryBuilderImpl;


/**
 * @author  donghyuck
 */
public class SqlQueryFactoryBuilder {
	
	public static interface Implementation {
		
		public SqlQueryFactory getSqlQueryFactory();
	
		public SqlQueryFactory getSqlQueryFactory(Configuration configuration);
	}

	/**
	 * @uml.property  name="impl"
	 * @uml.associationEnd  
	 */
	private static Implementation impl = null;
	
	static {
		impl = new SqlQueryFactoryBuilderImpl();
	}

	public static SqlQueryFactory getSqlQueryFactory() {
		return impl.getSqlQueryFactory();
	}
	public static SqlQueryFactory getSqlQueryFactory(Configuration configuration) {
		return impl.getSqlQueryFactory(configuration);
	}
	
}
