package architecture.ee.spring.util;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.Lifecycle;

import architecture.common.scanner.URLDirectoryScanner;

public class URLDirectoryScannerFactory implements FactoryBean<URLDirectoryScanner>, InitializingBean, Lifecycle {

    private URLDirectoryScanner scanner;
    
    private boolean autoStartup;

	public URLDirectoryScannerFactory()
    {
        scanner = new URLDirectoryScanner();
        autoStartup = false;
    }

    public void afterPropertiesSet()
        throws Exception
    {   
    	scanner.create();
        if (autoStartup)
        	scanner.start();
        
    }

	@SuppressWarnings("unchecked")
	public Class getObjectType()
    {
        return URLDirectoryScanner.class;
    }

    public boolean isSingleton()
    {
        return true;
    }


    public URLDirectoryScanner getObject()
        throws Exception
    {
        return scanner;
    }
    
	public boolean isRunning() {
		return scanner.isStarted();
	}
    
    public boolean isAutoStartup() {
		return autoStartup;
	}

	public void setAutoStartup(boolean autoStartup) {
		this.autoStartup = autoStartup;
	}
	
	public void start() {	
		try {
			if(!isRunning())
			    scanner.start();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void stop() {
		try {
			if(isRunning())
				scanner.doStop();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	

    public void destroy()
        throws Exception
    {
		if(isRunning())
			scanner.doStop();    	
        scanner.destroy();
    }
}
