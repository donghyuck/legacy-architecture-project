package architecture.ee.component;

import java.io.UnsupportedEncodingException;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.TimeZone;

import architecture.common.lifecycle.ApplicationPropertiesSupport;
import architecture.common.lifecycle.ConfigRoot;
import architecture.common.lifecycle.State;

public interface Admin extends ApplicationPropertiesSupport {

	public abstract void executeTask(Runnable task);
	
	public abstract State getState();
	
	public abstract ConfigRoot getConfigRoot();
	
	public abstract String getEffectiveRootPath();	

    public abstract Locale getLocale();

    public abstract void setLocale(Locale newLocale);

    public abstract String getCharacterEncoding();

    public abstract void setCharacterEncoding(String characterEncoding) throws UnsupportedEncodingException;

    public abstract TimeZone getTimeZone();

    public abstract void setTimeZone(TimeZone newTimeZone);
	
    public abstract boolean isReady();
    
    public abstract ResourceBundle getResourceBundle(String baseName);
	
}
