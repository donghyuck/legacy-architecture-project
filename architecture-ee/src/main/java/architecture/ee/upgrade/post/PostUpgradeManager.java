package architecture.ee.upgrade.post;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

import architecture.common.lifecycle.ApplicationProperties;
import architecture.common.lifecycle.State;
import architecture.common.util.ClassUtils;
import architecture.ee.util.ApplicationHelper;

/**
 * @author  donghyuck
 */
public class PostUpgradeManager {

    private static final String UPGRADE_CONFIG_FILENAME = "architecture/ee/upgrade/post/post-upgrade.xml";
    protected static final Log log = LogFactory.getLog(PostUpgradeManager.class);
    /**
	 * @uml.property  name="props"
	 * @uml.associationEnd  
	 */
    private final ApplicationProperties props;
    
    public PostUpgradeManager(ApplicationProperties properties)
    {
        props = properties;
    }
 
    public List<String> getUpgradeTasks()
    {
        return getUpgradeActions(loadUpgradeConfig());
    }

    public boolean isPostUpgradeStarted()
    {
        return ApplicationHelper.getState() == State.POST_UPGRADE_STARTED;
    }
    
    public void initialize()
    {
        log.info("Post Upgrade manager initialize invoked.");
        State state = ApplicationHelper.getState();
        if(state == State.RUNNING){
        	//ApplicationHelper.getAdminService().  //JiveApplication.setApplicationState(ApplicationState.RUNNING, ApplicationState.POST_UPGRADE_NEEDED);
        }else{
            log.warn((new StringBuilder()).append("Unable to initialize post upgrade, invalid application state '").append(state).append("'.").toString());
        }
    }

    public void upgradeInProgress()
    {
        // JiveApplication.setApplicationState(ApplicationState.POST_UPGRADE_NEEDED, ApplicationState.POST_UPGRADE_STARTED);
    }

    public void upgradeComplete()
    {
        if(getUpgradeTasks().size() <= 0)
        {
            //JiveApplication.setApplicationState(ApplicationState.POST_UPGRADE_STARTED, ApplicationState.POST_UPGRADE_COMPLETE);
           // JiveApplication.setApplicationState(ApplicationState.POST_UPGRADE_COMPLETE, ApplicationState.RUNNING);
            if(ApplicationHelper.getState() != State.RUNNING)
                throw new IllegalStateException("Post upgrade manager: Failed to transition application to running state");
        }
    }
    
    private PostUpgradeConfig loadUpgradeConfig(){
    	
    	XStream xstream = createXStream();
        InputStream upgradeConfig = null;
        PostUpgradeConfig config = null;
        PostUpgradeConfig postupgradeconfig = null;
        try
        {
            upgradeConfig = ClassUtils.getResourceAsStream(UPGRADE_CONFIG_FILENAME, getClass());
            if(upgradeConfig == null){
	            log.error("Could not find upgrade config file: '"+ UPGRADE_CONFIG_FILENAME +"'.");
            }else{
            	config = (PostUpgradeConfig)xstream.fromXML(new InputStreamReader(upgradeConfig));
            	if(config == null){
            		log.error("Error reading upgrade config file: '" +UPGRADE_CONFIG_FILENAME+ "'.");
            	}else{
            		if(!config.validate())
            			log.error("com/jivesoftware/community/upgrade/post/post-upgrade.xml' is not configured correctly.");
            		else
            			postupgradeconfig = config;
            	}            		
            }
        }
        finally
        {
            if(upgradeConfig != null)
                try
                {
                    upgradeConfig.close();
                }
                catch(Exception e)
                {
                    log.debug("Could not close input stream in finally block while parsing upgrade.xml", e);
                }
        }
        return postupgradeconfig;
    }
    
    private XStream createXStream()
    {
        XStream xstream = new XStream(new DomDriver());
        xstream.registerConverter(new PostUpgradeTaskConverter());
        xstream.alias("post-upgrade-config", PostUpgradeConfig.class);
        xstream.alias("upgrade", PostUpgradeConfig.Upgrade.class);
        xstream.alias("task",PostUpgradeConfig.Upgrade.Task.class);
        return xstream;
    }
    
    private List<String> getUpgradeActions(PostUpgradeConfig conf){
    	List<String> actions = new ArrayList<String>(7);
    	
    	for(PostUpgradeConfig.Upgrade upgrade : conf.getUpgrades()){
    		 if(upgrade.getTasks() != null){
    			 for(PostUpgradeConfig.Upgrade.Task task : upgrade.getTasks()){
                     if(!props.containsKey(task.getProperty()))
                         actions.add(task.getAction());
    			 }
    		 }
    	}
        return actions;
    }
}
