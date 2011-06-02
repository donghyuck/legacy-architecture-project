package architecture.common.lifecycle;

import java.util.List;
import java.util.Locale;
import java.util.Map;

public interface ApplicationPropertiesSupport {
	
	public ApplicationProperties getApplicationProperties();
	
	public String getLocalProperty(String name);
	
	public int getLocalProperty(String name, int defaultValue);
	
	public String getLocalProperty(String name, String defaultValue);
	
	public List<String> getLocalProperties(String parent);
	
	public void setLocalProperty(String name, String value);
	
	public void setLocalProperties(Map<String, String> map);
	
	public void deleteLocalProperty(String name);
		
	public String getApplicationProperty(String name);
	
	public String getApplicationProperty(String name, String defaultValue);
	
	public List<String> getApplicationPropertyNames();
	
	public List<String> getApplicationPropertyNames(String parent);
	
	public List<String> getApplicationProperties(String parent);
	
	public int getApplicationIntProperty(String name, int defaultValue);
	
	public boolean getApplicationBooleanProperty(String name);
	
	public boolean getApplicationBooleanProperty(String name, boolean defaultValue);
	
	public void setApplicationProperty(String name, String defaultValue);
	
	public void setApplicationProperties(Map<String, String> map);
	
	public void deleteApplicationProperty(String name);
	
	

    public List<Locale> getLocalizedPropertyLocales(String name);

    public void setLocalizedApplicationProperty(String name, String value, Locale locale);

    public void deleteLocalizedApplicationProperty(String name, Locale locale);	
    
}
