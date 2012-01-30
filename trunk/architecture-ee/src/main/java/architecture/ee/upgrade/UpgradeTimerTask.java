package architecture.ee.upgrade;

import java.util.List;
import java.util.TimerTask;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import architecture.ee.upgrade.post.PostUpgradeManager;


public class UpgradeTimerTask extends TimerTask
{

    private static final Log log = LogFactory.getLog(UpgradeTimerTask.class);
    private UpgradeManager upgradeManager;
    private PostUpgradeManager postUpgradeManager;
    
    public UpgradeTimerTask()
    {
    }

    public void run()
    {
        log.info("Upgrade timer task running.");
        try
        {
            Thread.sleep(10000L);
        }
        catch(InterruptedException e)
        {
            e.printStackTrace();
        }
        List tasks = upgradeManager.getUpgradeTasks();
        if(tasks.size() > 0 && !upgradeManager.isUpgradeStarted())
        {
            log.info("Upgrade manager has pending tasks, initializing upgrade.");
            upgradeManager.initialize();
            return;
        }
        if(upgradeManager.isUpgradeStarted())
            return;
        try
        {
            List actions = postUpgradeManager.getUpgradeTasks();
            if(actions.size() > 0 && !postUpgradeManager.isPostUpgradeStarted())
                postUpgradeManager.initialize();
        }
        catch(Exception ex)
        {
            log.fatal("Exception running upgrade task.", ex);
        }
        return;
    }

    public void setUpgradeManager(UpgradeManager upgradeManager)
    {
        this.upgradeManager = upgradeManager;
    }

    public void setPostUpgradeManager(PostUpgradeManager postUpgradeManager)
    {
        this.postUpgradeManager = postUpgradeManager;
    }


}
