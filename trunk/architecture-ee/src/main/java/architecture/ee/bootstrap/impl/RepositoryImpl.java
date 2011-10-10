package architecture.ee.bootstrap.impl;

import java.io.File;

import javax.servlet.ServletContext;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.vfs.FileObject;
import org.apache.commons.vfs.FileSystemException;
import org.springframework.jndi.JndiTemplate;
import org.springframework.web.context.support.ServletContextResource;

import architecture.common.lifecycle.ComponentImpl;
import architecture.common.lifecycle.ConfigRoot;
import architecture.common.lifecycle.Repository;
import architecture.common.lifecycle.State;
import architecture.common.util.vfs.VFSUtils;

import architecture.common.lifecycle.ApplicationConstants;

public class RepositoryImpl extends ComponentImpl implements Repository {
   
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
    	
    	// 서블릿 컨텍스트에 설정된 프로퍼티 값을 검사 : ARCHITECTURE_INSTALL_ROOT_ENV_KEY
    	String value = servletContext.getInitParameter( ApplicationConstants.ARCHITECTURE_INSTALL_ROOT_ENV_KEY );    
    	if(!StringUtils.isEmpty(value)){
    		try {        		
    			ServletContextResource resource = new ServletContextResource(servletContext, value);          		
        		File file = resource.getFile();
        		log.debug( "Setting install root with " + ApplicationConstants.ARCHITECTURE_INSTALL_ROOT_ENV_KEY + ":" + file.getAbsolutePath());  
        		
				this.rootURI = file.toURI().toString();				
			
    		} catch (Throwable e) {
				rootURI = null;
			}
    	}
   	
    	if(StringUtils.isEmpty(rootURI)){
    		FileObject obj;
			try {
				obj = VFSUtils.resolveFile(value);
				log.debug( "Setting install root with " + ApplicationConstants.ARCHITECTURE_INSTALL_ROOT_ENV_KEY + ":" +  obj.getName().getURI()); 
				rootURI = obj.getName().getURI();  
			} catch (FileSystemException e) {
				e.printStackTrace();
			}    		
    	}    	
    	
    	setState(State.INITIALIZED);
    }    
    
    public String getEnvironmentRootPath()
    {
    	// Java System Property 에서 검색: -Darchitecture.install.root=
        String envRootPath = System.getProperty(ApplicationConstants.ARCHITECTURE_INSTALL_ROOT_KEY);        
        if(envRootPath == null || "".equals(envRootPath))
            try
            {
            	// architecture_install_root=
            	envRootPath = jndiTemplate.lookup("java:comp/env/" + ApplicationConstants.ARCHITECTURE_INSTALL_ROOT_ENV_KEY.toLowerCase(), String.class);                
            }
            catch(Exception e) { }
            
        if(envRootPath == null || "".equals(envRootPath))
        {
            envRootPath = System.getenv(ApplicationConstants.ARCHITECTURE_INSTALL_ROOT_KEY);
            if(envRootPath != null && !"".equals(envRootPath))
                log.info((new StringBuilder()).append("Architecture root set from system property to '").append(envRootPath).append("'.").toString());
        }
        if(envRootPath == null || "".equals(envRootPath))
        {
            StringBuilder buffer = new StringBuilder();
            if(System.getProperty("os.name", "Linux").indexOf("Windows") == -1)
                buffer.append(File.separator).append("apps").append(File.separator).append("framework");
            else
                buffer.append("c:").append(File.separator).append("apps").append(File.separator).append("framework");
            envRootPath = buffer.toString();
            log.warn((new StringBuilder()).append("No explicit configuration of Architecture root path. Using system default of: '").append(envRootPath).append("'.").toString());
        }        
        return envRootPath;
    }
    
}
