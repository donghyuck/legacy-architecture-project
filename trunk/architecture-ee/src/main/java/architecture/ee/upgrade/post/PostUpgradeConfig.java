package architecture.ee.upgrade.post;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


public class PostUpgradeConfig
{
    public static class Upgrade
    {
        public static class Task
        {
             
            private String action;

            private String property;

            public Task()
            {
            }

            public Task(String action, String property)
            {
                this.action = action;
                this.property = property;
            }

            public String getAction()
            {
                return action;
            }

            public String getProperty()
            {
                return property;
            }
            public void setAction(String action)
            {
                this.action = action;
            }

            public void setProperty(String property)
            {
                this.property = property;
            }

            public boolean validate()
            {
                boolean validated = true;
                if(action == null || "".equals(action.trim()))
                {
                	PostUpgradeConfig.log.error("Upgrade.xml validation failed for task: Action not specified.");
                    validated = false;
                } else
                if(property == null || "".equals(action.trim()))
                	PostUpgradeConfig.log.error((new StringBuilder()).append("Upgrade.xml validation failed for task '").append(action).append("': Property not specified.").toString());
                return validated;
            }
        }

        private String name;

        private List<Task> tasks;

        public Upgrade()
        {
        }

        public String getName()
        {
            return name;
        }

        public List<Task> getTasks()
        {
            return tasks;
        }
        
        public void setName(String name)
        {
            this.name = name;
        }
        public void setTasks(List<Task> tasks)
        {
            this.tasks = tasks;
        }

        public boolean validate()
        {
            boolean validated = true;
            if(name == null || "".equals(name.trim()))
            {
                log.error("Upgrade.xml validation failed: Upgrade name not specified.");
                validated = false;
            }
            if(tasks != null){
                for(Task task : tasks){
                	if( ! task.validate())
                		return false;
                }
            }
            return validated;            
        }
    }
    
    private static final Log log = LogFactory.getLog(PostUpgradeConfig.class);

    private List<Upgrade> upgrades;
    
    public PostUpgradeConfig()
    {
    }

    public List<Upgrade> getUpgrades()
    {
        return upgrades;
    }

    public void setUpgrades(List<Upgrade> upgrades)
    {
        this.upgrades = upgrades;
    }

    public boolean validate()
    {
        if(upgrades == null)
            return true;
        for(Upgrade upgrade : upgrades ){
            if(!upgrade.validate())
                return false;
        }
        return true;
    }

}
