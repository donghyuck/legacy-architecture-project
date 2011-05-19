package architecture.ee.spring.lifecycle.internal;

import java.io.File;
import java.util.concurrent.atomic.AtomicReference;

import javax.servlet.ServletContext;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.vfs.FileObject;
import org.apache.commons.vfs.FileSystemException;
import org.springframework.jndi.JndiTemplate;

import architecture.common.lifecycle.ConfigRoot;
import architecture.common.lifecycle.ConfigRootHelper;
import architecture.common.vfs.VFSUtils;

public class ConfigRootHelperImpl implements ConfigRootHelper {

	
    public static final String SERVER_ROOT_KEY = "runtime.server.root";

    public static final String SERVER_ROOT_ENV_KEY = "RUNTIME_SERVER_HOME";
    
    private static final AtomicReference<ConfigRoot> instance = new AtomicReference<ConfigRoot>();
    
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
       
    public String getInstallRootPath()
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
    	String uri = servletContext.getInitParameter(SERVER_ROOT_ENV_KEY);    	
    	if(!StringUtils.isEmpty(uri)){    		
    		FileObject obj;
			try {
				obj = VFSUtils.resolveFile(uri);
				rootURI = obj.getName().getURI();  
			} catch (FileSystemException e) {
			}    		
    	}    	
    }    
    
    public String getEnvironmentRootPath()
    {
        String envRootPath = System.getProperty(SERVER_ROOT_ENV_KEY);
        
        if(envRootPath == null || "".equals(envRootPath))
            try
            {
            	envRootPath = jndiTemplate.lookup("java:comp/env/" + SERVER_ROOT_ENV_KEY.toLowerCase(), String.class);                
            }
            catch(Exception e) { }
        if(envRootPath == null || "".equals(envRootPath))
        {
            envRootPath = System.getenv(SERVER_ROOT_ENV_KEY);
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
