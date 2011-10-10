package architecture.ee.jdbc.datasource;

import javax.sql.DataSource;

import architecture.common.util.ImplFactory;

public class DataSourceFactory {

	public static interface Implementation {	
		
		public abstract DataSource getDataSource();
	
	}
	
	private static Implementation impl = null;
    
    static 
    {
        impl = (Implementation)ImplFactory.loadImplFromKey(DataSourceFactory.Implementation.class);
    }
    
    public static DataSource getDataSource(){
    	return impl.getDataSource();
    }
}