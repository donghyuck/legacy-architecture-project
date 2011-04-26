package architecture.ee.spring.context.internal;

import java.io.File;
import java.io.FilenameFilter;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import javax.servlet.ServletContext;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.ConfigurableWebApplicationContext;
import org.springframework.web.context.ContextLoader;

import architecture.common.util.StringUtils;
import architecture.ee.bootstrap.Bootstrap;

public class WebApplicationContextLoader extends ContextLoader {


	public static final String CONTEXT_LOCATION_PARAM = "contextConfigLocation";		
	public static final String SETUP_LOCATION_PARAM = "setupContextLocation";
    public static final String UPGRADE_LOCATION_PARAM = "upgradeContextLocation";
    public static final String RUNTIME_LOCATION_PARAM = "runtimeContextLocation";
    public static final String EXTENSION_LOCATION_PARAM = "extensionContextLocation";    
    public static final String WS_LOCATION_PARAM = "wsContextLocation";
    
    private static final Log log = LogFactory.getLog(WebApplicationContextLoader.class);

	protected void customizeContext(ServletContext servletContext, ConfigurableWebApplicationContext applicationContext) {		
		
//		/log.info(MessageFormatter.format("016101"));		
		List<String> contextFiles = new LinkedList<String>();
		
		try{
			//String edition = StartupHelper.getBuildProperty("edition");
		    if( !StringUtils.isWhitespace(servletContext.getInitParameter(CONTEXT_LOCATION_PARAM)))
		        contextFiles.add(servletContext.getInitParameter(CONTEXT_LOCATION_PARAM));
		    /*
			if(!StartupHelper.isSetup()){			
				// setup processing
				log.info(LocalizedMessageFormatter.formatLogMessage("006328"));				
				String configFile = StringUtils.noNull(servletContext.getInitParameter(SETUP_LOCATION_PARAM), "classpath:/services/default-setup-context.xml");				
				if(configFile == null){
					log.warn(LocalizedMessageFormatter.formatLogMessage("006321"));					
				}else{					
					log.info(LocalizedMessageFormatter.formatLogMessage("006322"));
                    contextFiles.add(configFile.trim());                    
				}				
				
			}else{				
				// runtime
				String configFile = StringUtils.noNull(servletContext.getInitParameter(RUNTIME_LOCATION_PARAM), "file:" +StartupHelper.getWokrspace() + File.separator + "context-config" + File.separator + "*.xml");				
                if(null == configFile)
                {
                    log.warn(LocalizedMessageFormatter.formatLogMessage("006323"));                                        
                } else
                {
                    log.info(LocalizedMessageFormatter.formatLogMessage("006324"));
                    contextFiles.add(configFile.trim());                    
                }                
                // extention
                String extensionPath = servletContext.getInitParameter(EXTENSION_LOCATION_PARAM);
                if(null == extensionPath)
                {
                    log.info(LocalizedMessageFormatter.formatLogMessage("006325", StartupHelper.getWokrspace() + File.separator + "context-config" + File.separator + "ext"));
                    addXmlFilesToContextList((new StringBuilder()).append("file:" + StartupHelper.getWokrspace()).append(File.separator + "context-config" + File.separator + "ext" + File.separator + "*.xml" ).toString(), contextFiles);
                } else
                {
                    log.info(LocalizedMessageFormatter.formatLogMessage("006326", extensionPath, StartupHelper.getWokrspace()+ File.separator + "context-config" + File.separator + "ext" + File.separator + "*.xml"));                    		
                    addXmlFilesToContextList(extensionPath.trim(), contextFiles);
                }
			}	
			*/
		    
		    
			
		} catch(Exception ex){
			//String msg = MessageFormatter.format("016102");
            throw new IllegalStateException(ex); //msg, ex);
        }
				
		String files[] = new String[contextFiles.size()];
		int counter = 0;
        for(Iterator<String> iter = contextFiles.iterator(); iter.hasNext();)
        {
            String file = iter.next();
            files[counter++] = file;
        }
        
        applicationContext.setConfigLocations(files);
        
	}
		
    protected void addXmlFilesToContextList(String pathStr, List<String> files)
    {
        File path = new File(pathStr);        
        if(!path.exists() && !path.isDirectory())
            return;
        
        int counter = 0;        
        FilenameFilter filter = new FilenameFilter() {
            public boolean accept(File dir, String name)
            {
                return name.endsWith(".xml");
            }        
        };        
        File paths[] = path.listFiles(filter);
        int size = paths.length;
        for(int i = 0; i < size; i++)
        {
            File file = paths[i];
            files.add((new StringBuilder()).append("file:").append(file.getAbsolutePath()).toString());
            log.info((new StringBuilder()).append("Adding '").append(file.getAbsolutePath()).append("' to spring context.").toString());
            counter++;
        }
        log.info((new StringBuilder()).append("Added ").append(counter).append(" xml files to spring context.").toString());
    }

	@Override
	protected ApplicationContext loadParentContext(ServletContext servletContext) throws BeansException {		
		try {			
			return Bootstrap.getBootstrapApplicationContext();
			
		} catch (Exception e) {
		    log.error(e);
		}		
		return super.loadParentContext(servletContext);
	}
	
}
