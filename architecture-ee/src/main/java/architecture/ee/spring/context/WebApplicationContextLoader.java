package architecture.ee.spring.context;

import java.io.File;
import java.io.FilenameFilter;
import java.util.LinkedList;
import java.util.List;

import javax.servlet.ServletContext;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.ConfigurableWebApplicationContext;
import org.springframework.web.context.ContextLoader;

import architecture.common.lifecycle.bootstrap.Bootstrap;
import architecture.common.util.StringUtils;

public class WebApplicationContextLoader extends ContextLoader {

	private static final Log log = LogFactory.getLog(WebApplicationContextLoader.class);
	
	public static final String CONTEXT_LOCATION_PARAM = CONFIG_LOCATION_PARAM ;		
	public static final String SETUP_LOCATION_PARAM = "setupContextLocation";
    public static final String UPGRADE_LOCATION_PARAM = "upgradeContextLocation";
    public static final String RUNTIME_LOCATION_PARAM = "runtimeContextLocation";
    public static final String EXTENSION_LOCATION_PARAM = "extensionContextLocation";    
        

	protected void customizeContext(ServletContext servletContext, ConfigurableWebApplicationContext applicationContext) {		
			
		List<String> contextFiles = new LinkedList<String>();		
		try{
			
		    if( !StringUtils.isWhitespace(servletContext.getInitParameter(CONTEXT_LOCATION_PARAM))){
		    	String [] locations = StringUtils.split(servletContext.getInitParameter(CONTEXT_LOCATION_PARAM), ',');
		    	for( String location : locations){
		    		 contextFiles.add(location);
		    	}
		    }		
		} catch(Exception ex){
            throw new IllegalStateException(ex); //msg, ex);
        }
				
		String files[] = new String[contextFiles.size()];
		contextFiles.toArray(files);     
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
		return Bootstrap.getBootstrapApplicationContext();
	}
	
}
