package architecture.ee.spring.lifecycle.internal;

import java.io.File;

import javax.servlet.ServletContext;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.vfs.FileObject;
import org.apache.commons.vfs.FileSystemException;
import org.springframework.jndi.JndiTemplate;
import org.springframework.web.context.support.ServletContextResource;

import architecture.common.lifecycle.ConfigRoot;
import architecture.common.lifecycle.ConfigRootHelper;
import architecture.common.vfs.VFSUtils;

public class ConfigRootHelperImpl implements ConfigRootHelper{
	
    public static final String APPLICATION_HOME_KEY = "runtime.application.home";

    public static final String APPLICATION_CONFIG_ROOT_KEY = "runtime.application.config.root";
    
    public static final String APPLICATION_HOME_ENV_KEY = "RUNTIME_APPLICATION_HOME";
   
    public static final String APPLICATION_CONFIG_ROOT_ENV_KEY = "RUNTIME_APPLICATION_CONFIG_ROOT";
    
	private Log log = LogFactory.getLog(getClass());
	
	private JndiTemplate jndiTemplate = new JndiTemplate();
	
	private String effectiveRootPath;

    private String rootURI = getRootURI();               
        
    public ConfigRoot getConfigRoot(){    	
		try {			
			FileObject obj = VFSUtils.resolveFile(getRootURI());
			FileObject child = obj.resolveFile("config");      
			if(!child.exists()){
                child.createFolder();	
            }
			return new ConfigRootImpl(child);			
		} catch (Exception e) {
		}
		return null;
    }
    
	public String getRootURI() {
		 if(StringUtils.isEmpty(rootURI)){
			 String uri = getEnvironmentRootPath();
			try {
				FileObject obj = VFSUtils.resolveFile(uri);
				rootURI = obj.getName().getURI();
			} catch (FileSystemException e) {
				e.printStackTrace();
			}  
		 }				 
		return rootURI;
	}
       
    public String getEffectiveRootPath()
    {	
        if(!StringUtils.isEmpty(effectiveRootPath))
        {
            return effectiveRootPath;
        } else
        {
        	String uri = getRootURI();   	
        	try {
        		FileObject obj = VFSUtils.resolveFile(uri);
        		effectiveRootPath = obj.getName().getPath();   		
			} catch (FileSystemException e) {
			}        	
            return effectiveRootPath;
        }
    }
        
    public void setServletContext(ServletContext servletContext){
    	
    	String value = servletContext.getInitParameter(APPLICATION_HOME_ENV_KEY);    
    	if(!StringUtils.isEmpty(value)){
    		try {        		
    			ServletContextResource resource = new ServletContextResource(servletContext, value);          		
        		File file = resource.getFile();
        		log.debug( "HOME PATH         :" + file.getAbsolutePath());        		        		
				rootURI = file.toURI().toString();				
			} catch (Throwable e) {
				rootURI = null;
			}
    	}
   	
    	if(StringUtils.isEmpty(rootURI)){
    		FileObject obj;
			try {
				obj = VFSUtils.resolveFile(value);
				log.debug( "HOME PATH         :" +  obj.getName().getURI()); 
				rootURI = obj.getName().getURI();  
			} catch (FileSystemException e) {
				e.printStackTrace();
			}    		
    	}    	
    }    
    
    public String getEnvironmentRootPath()
    {
        String envRootPath = System.getProperty(APPLICATION_HOME_ENV_KEY);
        
        if(envRootPath == null || "".equals(envRootPath))
            try
            {
            	envRootPath = jndiTemplate.lookup("java:comp/env/" + APPLICATION_HOME_ENV_KEY.toLowerCase(), String.class);                
            }
            catch(Exception e) { }
        if(envRootPath == null || "".equals(envRootPath))
        {
            envRootPath = System.getenv(APPLICATION_HOME_ENV_KEY);
            if(envRootPath != null && !"".equals(envRootPath))
                log.info((new StringBuilder()).append("Jive root set from system property to '").append(envRootPath).append("'.").toString());
        }
        if(envRootPath == null || "".equals(envRootPath))
        {
            StringBuilder buffer = new StringBuilder();
            if(System.getProperty("os.name", "Linux").indexOf("Windows") == -1)
                buffer.append(File.separator).append("usr").append(File.separator).append("local").append(File.separator).append("fuse");
            else
                buffer.append("c:").append(File.separator).append("fuse");
            envRootPath = buffer.toString();
            log.warn((new StringBuilder()).append("No explicit configuration of Jive root path. Using system default of: '").append(envRootPath).append("'.").toString());
        }        
        return envRootPath;
    }

}