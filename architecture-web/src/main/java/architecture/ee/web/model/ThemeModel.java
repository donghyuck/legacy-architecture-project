package architecture.ee.web.model;

import java.io.IOException;
import java.util.Set;

import architecture.ee.model.ModelObject;
import architecture.ee.web.theme.Theme;

public interface ThemeModel extends ModelObject<Theme> {

    public String getProperty(String name);

    public String getProperty(String name, String defaultValue);

    public Set getPropertyNames();

    public int getIntProperty(String name, int defaultValue);

    public boolean getBooleanProperty(String name);

    public boolean getBooleanProperty(String name, boolean defaultValue);
    
    public boolean validate();
    
    public String getDescription();
    
    public String getName();

    public void setTemplateSource(String templateName, String templateSrc) throws IOException;
    
}
