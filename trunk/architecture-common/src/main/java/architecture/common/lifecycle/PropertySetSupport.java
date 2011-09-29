package architecture.common.lifecycle;

import java.util.List;
import java.util.Locale;
import java.util.Map;

public interface PropertySetSupport {

	public ApplicationProperties getPropertyMap();

	public String getLocalProperty(String name);

	public int getLocalProperty(String name, int defaultValue);

	public String getLocalProperty(String name, String defaultValue);

	public List<String> getLocalProperties(String parent);

	public void setLocalProperty(String name, String value);

	public void setLocalProperties(Map<String, String> map);

	public void deleteLocalProperty(String name);

	public String getProperty(String name);

	public String getProperty(String name, String defaultValue);

	public List<String> getPropertyNames();

	public List<String> getPropertyNames(String parent);

	public List<String> getProperties(String parent);

	public int getIntProperty(String name, int defaultValue);

	public boolean getBooleanProperty(String name);

	public boolean getBooleanProperty(String name, boolean defaultValue);

	public void setProperty(String name, String defaultValue);

	public void setProperties(Map<String, String> map);

	public void deleteProperty(String name);

	public String getLocalizedProperty(String name, Locale locale);

	public List<Locale> getLocalizedPropertyLocales(String name);

	public void setLocalizedProperty(String name, String value, Locale locale);

	public void deleteLocalizedProperty(String name, Locale locale);

}
