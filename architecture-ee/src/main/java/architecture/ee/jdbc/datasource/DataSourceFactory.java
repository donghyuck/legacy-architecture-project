package architecture.ee.jdbc.datasource;

import javax.sql.DataSource;

import architecture.common.lifecycle.ConfigService;
import architecture.common.util.ImplFactory;

public class DataSourceFactory {

	public static interface Implementation {	
		
		
		public abstract DataSource getDataSource();
	
		/**
		 * ConfigService 에서 제공하는 프로퍼티 값을  사용하여 데이터소스를 생성한다.
		 * 
		 * @param configService
		 * @return
		 */
		public abstract DataSource getDataSource(ConfigService configService);
		
	}
	
	private static Implementation impl = null;
    
    static 
    {
        impl = (Implementation)ImplFactory.loadImplFromKey(DataSourceFactory.Implementation.class);
    }
    
    public static DataSource getDataSource(ConfigService configService){
    	return impl.getDataSource();
    }
    
    public static DataSource getDataSource(){
    	return impl.getDataSource();
    }
}