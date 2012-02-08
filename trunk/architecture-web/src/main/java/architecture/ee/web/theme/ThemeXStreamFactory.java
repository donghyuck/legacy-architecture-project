package architecture.ee.web.theme;

import architecture.ee.web.theme.impl.ThemeXStreamFactoryImpl;

import com.thoughtworks.xstream.XStream;

public class ThemeXStreamFactory {

	public static interface Implementation {	
		
		public abstract XStream getThemeXStream();
		
	}
	
	private static Implementation impl = null;
    
    static 
    {
        impl = new ThemeXStreamFactoryImpl(); //(Implementation)ImplFactory.loadImplFromKey(DataSourceFactory.Implementation.class);
    }
       
    public static XStream getThemeXStream(){
    	return impl.getThemeXStream();
    }
    
}
