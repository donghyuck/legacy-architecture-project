package architecture.ee.component.core;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;

import javax.servlet.ServletContext;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.vfs.FileObject;
import org.apache.commons.vfs.FileSystemException;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;
import org.springframework.jndi.JndiTemplate;
import org.springframework.web.context.support.ServletContextResource;

import architecture.common.exception.ComponentDisabledException;
import architecture.common.exception.ConfigurationError;
import architecture.common.exception.ConfigurationWarning;
import architecture.common.lifecycle.ApplicationConstants;
import architecture.common.lifecycle.ApplicationProperties;
import architecture.common.lifecycle.ComponentImpl;
import architecture.common.lifecycle.ConfigRoot;
import architecture.common.lifecycle.Repository;
import architecture.common.lifecycle.State;
import architecture.common.lifecycle.internal.EmptyApplicationProperties;
import architecture.common.lifecycle.internal.XmlApplicationProperties;
import architecture.common.util.vfs.VFSUtils;
import architecture.common.xml.XmlProperties;


/**
 * @author  donghyuck
 */
public class RepositoryImpl extends ComponentImpl implements Repository {
   
	private Log log = LogFactory.getLog(getClass());
	
	private JndiTemplate jndiTemplate = new JndiTemplate();
	
	/**
	 */
	private String effectiveRootPath;

    public RepositoryImpl() {
		super();
	}

    /**
	 */
    private FileObject rootFileObject = getRootFileObject() ;
        
    /**
	 */
    private ApplicationProperties setupProperties = null;
        
	public String getRootURI() {		
		 if( rootFileObject == null ){
			 String uri = getEnvironmentRootPath();
			try {
				FileObject obj = VFSUtils.resolveFile(uri);
				rootFileObject = obj;
				return obj.getName().getURI();
			} catch (Exception e) {		
				return null;
			}  
		 }				 
		return rootFileObject.getName().getURI();
	}
	
    public ConfigRoot getConfigRoot(){    	
		try {			
			FileObject child = getRootFileObject().resolveFile("config");      
			if(!child.exists()){
                child.createFolder();	
            }
			return new ConfigRootImpl(child);			
		} catch (Exception e) {
		}
		return null;
    }
    

       
	/**
	 * @return
	 */
	private FileObject getRootFileObject(){
		
		if( getState() != State.INITIALIZED || getState() != State.INITIALIZING ){			
			
			ClassLoader classloader = Thread.currentThread().getContextClassLoader();
			
			/*try {
				Enumeration<URL> enumeration = classloader.getResources("application-init.xml");
				do {
					if (!enumeration.hasMoreElements())
						break;
					URL url = (URL) enumeration.nextElement();					
					InputStream input = url.openStream();
					XmlProperties prop = new XmlProperties(input);
					String home = prop.getProperty("home");					
					System.out.println( "application:" + home + "(" + url + "");
				} while (true);
			} catch (IOException e) {
				
			}	*/	
			
			try {		
				InputStream input = classloader.getResourceAsStream("application-init.xml");			
				XmlProperties prop = new XmlProperties(input);
				String envRootPath = prop.getProperty("home");
				if(!StringUtils.isEmpty(envRootPath)){
					FileObject obj = VFSUtils.resolveFile(envRootPath);
					log.debug( "Setting application home from " + "application-init.xml" + ":" +  obj.getName().getURI()); 
					this.rootFileObject = obj;
					setState(State.INITIALIZED);
				}
			} catch (Throwable e) {}
		}		
		return rootFileObject;
	}
	
	public void initialize() throws ComponentDisabledException, ConfigurationWarning, ConfigurationError {
		log.debug("initialize");
		try {			
			this.rootFileObject = getRootFileObject();
		} catch (Exception e) {
		}		
	}

	/**
	 * @return
	 */
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
    	
    	// 1. 서블릿 컨텍스트에 설정된 프로퍼티 값을 검사 : ARCHITECTURE_INSTALL_ROOT
    	String value = servletContext.getInitParameter( ApplicationConstants.ARCHITECTURE_RUNTIME_ROOT_ENV_KEY );    	
    	if(!StringUtils.isEmpty(value)){
    		try {        		
    			ServletContextResource resource = new ServletContextResource(servletContext, value);          		
        		File file = resource.getFile();
        		log.debug( "Setting install root with " + ApplicationConstants.ARCHITECTURE_RUNTIME_ROOT_ENV_KEY + ":" + file.getAbsolutePath());
        		FileObject obj = VFSUtils.resolveFile(file.toURI().toString());
				this.rootFileObject = obj;
				setState(State.INITIALIZED);
    		} catch (Throwable e) {
    			this.rootFileObject = null;
			}
    	}    	    	    	
    	
    	if(rootFileObject == null){
    		FileObject obj;
			try {
				obj = VFSUtils.resolveFile(value);
				log.debug( "Setting install root with " + ApplicationConstants.ARCHITECTURE_RUNTIME_ROOT_ENV_KEY + ":" +  obj.getName().getURI()); 
				this.rootFileObject = obj;				
				setState(State.INITIALIZED);
			} catch (Throwable e) {
				log.error(e);
			}    		
    	}
    	
    	if(rootFileObject == null){
    		try {   
	    		ServletContextResource resource = new ServletContextResource(servletContext, "/WEB-INF");          
	    		File file = resource.getFile();
	    		log.debug( "Setting application runtime root with " + file.getAbsolutePath());
	    		FileObject obj = VFSUtils.resolveFile(file.toURI().toString());
				this.rootFileObject = obj;
				setState(State.INITIALIZED);
    		} catch (Throwable e) {
    			this.rootFileObject = null;
			}
    	}
    }    
    
    
    public String getEnvironmentRootPath()
    {    	
    	// 1. Java System Property 에서 검색: architecture.install.root
        String envRootPath = System.getProperty(ApplicationConstants.ARCHITECTURE_RUNTIME_ROOT_KEY);  
        
        // 2. Jndi 에서 검색 : architecture_install_root
        if(envRootPath == null || "".equals(envRootPath))
            try
            {
            	envRootPath = jndiTemplate.lookup("java:comp/env/" + ApplicationConstants.ARCHITECTURE_RUNTIME_ROOT_ENV_KEY.toLowerCase(), String.class);                
            }
            catch(Exception e) { }
        // 3.     
        if(envRootPath == null || "".equals(envRootPath))
        {
            envRootPath = System.getenv(ApplicationConstants.ARCHITECTURE_RUNTIME_ROOT_KEY);
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
            log.warn((new StringBuilder()).append("No explicit configuration of application root path. Using system default of: '").append(envRootPath).append("'.").toString());
        }        
        return envRootPath;
    }
    
    
	public ApplicationProperties getSetupApplicationProperties() {
		if( setupProperties == null ){	
			try {			
				File file = new File( getEffectiveRootPath(), ApplicationConstants.DEFAULT_STARTUP_FILENAME );
				if(!file.exists()){					
					boolean error = false;
				    // create default file...
					log.debug("No startup file now create !!!");					
					Writer writer = null;
					try {			
						writer = new OutputStreamWriter(new FileOutputStream(file), ApplicationConstants.DEFAULT_CHAR_ENCODING);
						XMLWriter xmlWriter = new XMLWriter(writer, OutputFormat.createPrettyPrint());
						StringBuilder sb = new StringBuilder();					
						org.dom4j.Document document = org.dom4j.DocumentHelper.createDocument();    
						org.dom4j.Element root = document.addElement( "startup-config" );						
						// setup start ------------------------------------------------------------
						org.dom4j.Element setupNode = root.addElement("setup");
						setupNode.addElement("complete").setText("false");						
						// setup end --------------------------------------------------------------
						xmlWriter.write( document );					
					}catch(Exception e)
			        {
			            log.error((new StringBuilder()).append("Unable to write to file ").append(file.getName()).append(".tmp").append(": ").append(e.getMessage()).toString());
			            error = true;
			        }
			        finally
			        {
			            try
			            {
			                writer.flush();
			                writer.close();
			            }
			            catch(Exception e)
			            {
			                log.error(e);
			                error = true;
			            }
			            }
				}				
				this.setupProperties = new XmlApplicationProperties(file);				
			} catch (Exception e) {
				log.warn("I warning you!");
				log.debug(e.getMessage(), e);
				return EmptyApplicationProperties.getInstance();
			}
		}
		return setupProperties;
	}
    
	public String getURI(String name) {
		try {
			FileObject obj = getRootFileObject().resolveFile(name);		   
			return  obj.getName().getURI();
		} catch (FileSystemException e) {
		}
		return null;
	}

	public File getFile(String name) {
		try {
			FileObject obj = getRootFileObject().resolveFile(name);		 
			
			//log.debug( obj.getURL().getFile() );
			
			return  new File(obj.getURL().getFile());
		} catch (FileSystemException e) {
		}
		return null;
	}

	public File getLicenseFile() {
		return getConfigRoot().getFile("framework.license");
	}
	
}
