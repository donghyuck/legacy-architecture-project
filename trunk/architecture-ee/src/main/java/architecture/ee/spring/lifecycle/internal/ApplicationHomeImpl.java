package architecture.ee.spring.lifecycle.internal;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import javax.naming.InitialContext;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.jndi.JndiTemplate;

import architecture.common.lifecycle.ApplicationHome;


public class ApplicationHomeImpl implements ApplicationHome {
	
    public static final String SERVER_ROOT_KEY = "runtime.server.root";

    public static final String SERVER_LOGS_KEY = "runtime.server.logs";

    public static final String SERVER_HOME_KEY = "runtime.server.home";

    public static final String SERVER_ROOT_ENV_KEY = "RUNTIME_SERVER_HOME";
    
	private Log log = LogFactory.getLog(getClass());
	
	private JndiTemplate jndiTemplate = new JndiTemplate();
	
	private boolean initialization = true;
	
	private String effectiveRootPath;
	
	private String effectiveHomePath;
	
	private String effectiveLogsPath;
	
    private String startupHomePath = getHomePath();
    
    private String startupInstallRootPath = getInstallRootPath();

    private String startupLogPath = getLogsPath();
    
    private void createStartupXmlFile(File startupFile) throws IOException    {
        FileWriter jhWriter = new FileWriter(startupFile);
        jhWriter.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
        jhWriter.write("<server-startup>\n");
        jhWriter.write("    <setup>false</setup>\n");
        jhWriter.write("</server-startup>\n");
        jhWriter.flush();
        jhWriter.close();
    }
    
	
	public String getLogsPath() {
        if(effectiveLogsPath != null)
        {
            return effectiveLogsPath;
        } else
        {
            effectiveLogsPath = getEnvironmentLogsPath();
            return effectiveLogsPath;
        }
	}

	public File getLogs() {
		return new File(getLogsPath());
	}

    public File getInstallRoot()
    {
        return new File(getInstallRootPath());
    }

    public String getInstallRootPath()
    {
        if(!StringUtils.isEmpty(effectiveRootPath))
        {
            return effectiveRootPath;
        } else
        {
        	effectiveRootPath = getEnvironmentRootPath();
            return effectiveRootPath;
        }
    }
	
	
    public File getHome()
    {
        return new File(getHomePath());
    }

    public String getHomePath()
    {
        if(!StringUtils.isEmpty(effectiveHomePath))
        {
            return effectiveHomePath;
        } else
        {
            effectiveHomePath = getEnvironmentHomePath();
            return effectiveHomePath;
        }
    }
    
    
    public String getEnvironmentLogsPath()
    {
        String envPath = System.getProperty(SERVER_LOGS_KEY);
        if(envPath == null || "".equals(envPath))
            try
            {
                envPath = jndiTemplate.lookup("java:comp/env/" + SERVER_LOGS_KEY , String.class);
            }
            catch(Exception e) { }
        if(envPath == null || "".equals(envPath))
        {
            File root = getInstallRoot();
            if(!root.exists())
            {
                if(initialization)
                    log.warn("Jive root directory does not exist for log configuration. Reverting to JIVE_HOME/logs.");
                StringBuilder buffer = new StringBuilder(getHomePath());
                buffer.append(File.separator).append("logs");
                envPath = buffer.toString();
            } else
            {
                if(initialization)
                    log.warn((new StringBuilder()).append("No explicit configuration of Jive log path. Using system default of: '").append(envPath).append("'.").toString());
                StringBuilder buffer = new StringBuilder(getInstallRootPath());
                buffer.append(File.separator).append("var").append(File.separator).append("logs");
                envPath = buffer.toString();
            }
        }
        return envPath;
    }
    
    
    /**
     * 환경 변수에서 정보를 가져온다.
     * @return
     */
    public String getEnvironmentHomePath()
    {    	
    	
        String path = null;
        try
        {
        	path = jndiTemplate.lookup("java:comp/env/" + SERVER_ROOT_KEY , String.class);
            if(path != null)
                log.info((new StringBuilder()).append("Community home set from JNDI to '").append(path).append("'.").toString());
        }
        catch(Exception e) { }
        if(path == null || "".equals(path))
        {
            path = System.getProperty(SERVER_ROOT_KEY);
            if(path != null && !"".equals(path))
                log.info((new StringBuilder()).append("Community home set from system property to '").append(path).append("'.").toString());
        }
        
        if(path == null || "".equals(path))
            try
            {
                path = jndiTemplate.lookup("java:comp/env/" + SERVER_HOME_KEY, String.class);
                if( !StringUtils.isEmpty (path) && initialization)
                    log.warn((new StringBuilder()).append("Community home set from legacy JNDI jiveHome setting to '").append(path).append("'. Please upate configuration to use ").append("jive.instance.home").toString());
            }
            catch(Exception e) { }
        if(path == null || "".equals(path))
        {
            path = System.getProperty(SERVER_HOME_KEY);
            if(path != null && !"".equals(path) && initialization)
                log.warn((new StringBuilder()).append("Communuity home set from legacy jiveHome system property to '").append(path).append("'. Please update configuration to use ").append("jive.instance.home").toString());
        }
        
        
        if(path == null || "".equals(path))
        {
            File rootFilePath;
            if(effectiveRootPath != null)
                rootFilePath = new File(effectiveRootPath);
            else
                rootFilePath = new File(getEnvironmentRootPath());
            
            String name = System.getProperty("runtime.server.name", "default");
            StringBuilder buffer = new StringBuilder(rootFilePath.getAbsolutePath());
            buffer.append(File.separator).append("nodes").append(File.separator).append(name).append(File.separator).append("home");
            path = buffer.toString();
            if(initialization)
                log.warn((new StringBuilder()).append("Attempting to use default community home value in absence of explicit configuration: '").append(path).append("'.").toString());
        }
        return path;
    }
    
    
    public String getEnvironmentRootPath()
    {
        String envRootPath = System.getProperty(SERVER_ROOT_ENV_KEY);
        if(envRootPath == null || "".equals(envRootPath))
            try
            {
                InitialContext context = new InitialContext();
                envRootPath = (String)context.lookup("java:comp/env/" + SERVER_ROOT_ENV_KEY.toLowerCase());
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