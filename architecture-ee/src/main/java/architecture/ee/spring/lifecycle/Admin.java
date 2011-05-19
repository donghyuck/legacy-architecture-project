package architecture.ee.spring.lifecycle;

import java.io.UnsupportedEncodingException;
import java.util.Locale;
import java.util.TimeZone;

import architecture.common.lifecycle.ApplicationPropertiesService;
import architecture.common.lifecycle.ConfigRoot;
import architecture.common.lifecycle.State;

public interface Admin extends ApplicationPropertiesService {

	public abstract State getState();
	
	public abstract ConfigRoot getConfigRoot();
	
	public String getInstallRootPath();	

    public Locale getLocale();

    public void setLocale(Locale newLocale);

    public String getCharacterEncoding();

    public void setCharacterEncoding(String characterEncoding) throws UnsupportedEncodingException;

    public TimeZone getTimeZone();

    public void setTimeZone(TimeZone newTimeZone);
	
    public boolean isReady();
	
}
