package architecture.ee.upgrade;

import java.util.List;

public interface UpgradeManager {

    public abstract boolean isUpgraded();

    public abstract int getVersionNumber(String name);

    public abstract void setVersionNumber(String name, int version);

    public abstract void upgradeComplete(ScheduledUpgrade upgrade);

    public abstract boolean isUpgradeStarted();

    public abstract void initialize();

    public abstract List getUpgradeTasks();

}
