package architecture.common.jdbc.datasource;

import javax.sql.DataSource;

import architecture.common.util.ImplFactory;

/**
 * @author   donghyuck
 */
public class DataSourceFactory {

	public static interface Implementation {	
		
		/**
		 * setup 설정 값 에서 제공하는 프로퍼티 값을  사용하여 데이터소스를 생성한다.
		 * 
		 * @param configService
		 * @return
		 */
		public abstract DataSource getDataSource();
	
	}
	
	/**
	 * @uml.property  name="impl"
	 * @uml.associationEnd  
	 */
	private static Implementation impl = null;
    
    static 
    {
        impl = (Implementation)ImplFactory.loadImplFromKey(DataSourceFactory.Implementation.class);
    }
       
    public static DataSource getDataSource(){
    	return impl.getDataSource();
    }
}