package architecture.ee.upgrade.impl;

import java.util.List;

import architecture.ee.upgrade.ScheduledUpgrade;
import architecture.ee.upgrade.UpgradeManager;
import architecture.ee.upgrade.dao.UpgradeDao;

public class UpgradeManagerImpl implements UpgradeManager {

	private UpgradeDao uggradeDao ;

	public UpgradeDao getUggradeDao() {
		return uggradeDao;
	}

	public void setUggradeDao(UpgradeDao uggradeDao) {
		this.uggradeDao = uggradeDao;
	}

	public boolean isUpgraded() {
		// TODO Auto-generated method stub
		return false;
	}

	public int getVersionNumber(String name) {
		// TODO Auto-generated method stub
		return 0;
	}

	public void setVersionNumber(String name, int version) {
		// TODO Auto-generated method stub
		
	}

	public void upgradeComplete(ScheduledUpgrade upgrade) {
		// TODO Auto-generated method stub
		
	}

	public boolean isUpgradeStarted() {
		// TODO Auto-generated method stub
		return false;
	}

	public void initialize() {
		// TODO Auto-generated method stub
		
	}

	public List getUpgradeTasks() {
		// TODO Auto-generated method stub
		return null;
	}
	
}
