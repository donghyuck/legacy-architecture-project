package architecture.ee.plugin;

import architecture.ee.plugin.impl.PluginManagerFactoryImpl;

public class PluginManagerFactory {

	public static interface Implementation {
		public abstract PluginManager getPluginManager();
	}
	
	/**
	 */
	private static Implementation impl = null;
    
    static 
    {
        impl = new PluginManagerFactoryImpl(); // (Implementation)ImplFactory.loadImplFromKey(PluginManagerFactory.Implementation.class);
    }
       
    public static PluginManager getPluginManager(){
    	return impl.getPluginManager();
    }
    
}
