package architecture.ee.web.model.impl;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.Collections;
import java.util.Properties;
import java.util.Set;

import org.apache.commons.io.FileUtils;

import architecture.ee.model.impl.BaseModelObject;
import architecture.ee.util.ApplicationHelper;
import architecture.ee.web.model.ThemeModel;
import architecture.ee.web.theme.Theme;
import architecture.ee.web.theme.ThemeManager;

public class ThemeModelImpl extends BaseModelObject<Theme> implements ThemeModel, Theme {

    private String name;
    private String description;
    private Properties properties;

    public ThemeModelImpl(String name, String description, Properties properties)
    {
        this.name = name.toLowerCase();
        this.description = description;
        this.properties = properties;
    }

    public String getProperty(String name)
    {
        return getProperty(name, null);
    }

    public String getProperty(String name, String defaultValue)
    {
        if(properties != null)
            return (String)properties.get(name);
        else
            return defaultValue;
    }

    public Set getPropertyNames()
    {
        if(properties != null)
            return properties.keySet();
        else
            return Collections.emptySet();
    }

    public int getIntProperty(String name, int defaultValue)
    {
        String value = getProperty(name);
        if(value != null)
            try
            {
                return Integer.parseInt(value);
            }
            catch(NumberFormatException nfe) { }
        return defaultValue;
    }

    public boolean getBooleanProperty(String name)
    {
        return getBooleanProperty(name, false);
    }

    public boolean getBooleanProperty(String name, boolean defaultValue)
    {
        String value = getProperty(name);
        if(value != null)
            return Boolean.valueOf(value).booleanValue();
        else
            return defaultValue;
    }
    
    public boolean validate()
    {
        boolean validated = true;
        if(name == null)
            validated = false;
        return validated;
    }
    
    public String getDescription()
    {
        return description;
    }
    
    public String getName(){
    	return name;
    }

    
    
    /*
    public String getTemplateSource(String templateName) throws IOException
    {
        String templateValue;        
       
        
        String templatesHome = (new StringBuilder()).append(getThemeManager().getThemeHome()).append(File.separator).append(getName()).toString();
        
        File templateFile = new File((new StringBuilder()).append(templatesHome).append(templateName).append(".ftl").toString());
        
        InputStream stream = ClassUtils.getResourceAsStream((new StringBuilder()).append(templateName).append(".ftl").toString());
        if(stream == null)
            return null;
        stream.close();
        
        if(templateFile.exists()){
        	templateValue = FileUtils.readFileToString(templateFile, "UTF-8");        	
        }
     
        BufferedReader reader = null;
        try {
			reader = new BufferedReader(new InputStreamReader(ClassUtils.getResourceAsStream((new StringBuilder()).append(templateName).append(".ftl").toString())));			
			StringBuffer buf = new StringBuffer();			
			String line;
			while((line = reader.readLine()) != null) 
			    buf.append(line).append(System.getProperty("line.separator"));
			templateValue = buf.toString();
		} finally {
	        if(reader != null)
	            reader.close();
		}
		return templateValue;        
    }
    */
   
    
    private ThemeManager getThemeManager(){
    	return ApplicationHelper.getComponent(ThemeManager.class);
    }
    
    /**
     * templateSrc 소스 파일을 로컬에 templateName 으로 저장한다.
     * 
     * @param templateName
     * @param templateSrc
     * @throws IOException
     */
    public void setTemplateSource(String templateName, String templateSrc)
    throws IOException
	{
    	setTemplateSource(templateName, templateSrc, true);
	}
    
    private void setTemplateSource(String templateName, String templateSrc, boolean sendClusterTask)
    throws IOException
	{
	    String templatesHome = getThemeManager().getThemeHome(this);
	    File templateFile = new File((new StringBuilder()).append(templatesHome).append(templateName).append(".ftl").toString());
	    
	    boolean exists = templateFile.exists();	    
	    if(!exists)
	        FileUtils.forceMkdir(templateFile.getParentFile());
	    
	    FileUtils.writeStringToFile(templateFile, templateSrc, "UTF-8");
	    
	    /*if(!exists)
	        ThemeTemplateLoader.clearCache(getName(), (new StringBuilder()).append(templateName).append(".ftl").toString());*/	    
	}
    
    
    
    
	public int getObjectType() {
		return 0;
	}

	public int compareTo(Theme o) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Object clone() {
		return null;
	}

	@Override
	public Serializable getPrimaryKeyObject() {
		return null;
	}

}
