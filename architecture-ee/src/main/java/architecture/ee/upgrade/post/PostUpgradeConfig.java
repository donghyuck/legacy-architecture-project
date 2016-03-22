package architecture.ee.upgrade.post;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author donghyuck
 */
public class PostUpgradeConfig {
    /**
     * @author donghyuck
     */
    public static class Upgrade {
	/**
	 * @author donghyuck
	 */
	public static class Task {

	    /**
	     * @uml.property name="action"
	     */
	    private String action;

	    /**
	     * @uml.property name="property"
	     */
	    private String property;

	    public Task() {
	    }

	    public Task(String action, String property) {
		this.action = action;
		this.property = property;
	    }

	    /**
	     * @return
	     * @uml.property name="action"
	     */
	    public String getAction() {
		return action;
	    }

	    /**
	     * @return
	     * @uml.property name="property"
	     */
	    public String getProperty() {
		return property;
	    }

	    /**
	     * @param action
	     * @uml.property name="action"
	     */
	    public void setAction(String action) {
		this.action = action;
	    }

	    /**
	     * @param property
	     * @uml.property name="property"
	     */
	    public void setProperty(String property) {
		this.property = property;
	    }

	    public boolean validate() {
		boolean validated = true;
		if (action == null || "".equals(action.trim())) {
		    PostUpgradeConfig.log.error("Upgrade.xml validation failed for task: Action not specified.");
		    validated = false;
		} else if (property == null || "".equals(action.trim()))
		    PostUpgradeConfig.log.error((new StringBuilder()).append("Upgrade.xml validation failed for task '")
			    .append(action).append("': Property not specified.").toString());
		return validated;
	    }
	}

	/**
	 * @uml.property name="name"
	 */
	private String name;

	/**
	 * @uml.property name="tasks"
	 */
	private List<Task> tasks;

	public Upgrade() {
	}

	/**
	 * @return
	 * @uml.property name="name"
	 */
	public String getName() {
	    return name;
	}

	/**
	 * @return
	 * @uml.property name="tasks"
	 */
	public List<Task> getTasks() {
	    return tasks;
	}

	/**
	 * @param name
	 * @uml.property name="name"
	 */
	public void setName(String name) {
	    this.name = name;
	}

	/**
	 * @param tasks
	 * @uml.property name="tasks"
	 */
	public void setTasks(List<Task> tasks) {
	    this.tasks = tasks;
	}

	public boolean validate() {
	    boolean validated = true;
	    if (name == null || "".equals(name.trim())) {
		log.error("Upgrade.xml validation failed: Upgrade name not specified.");
		validated = false;
	    }
	    if (tasks != null) {
		for (Task task : tasks) {
		    if (!task.validate())
			return false;
		}
	    }
	    return validated;
	}
    }

    private static final Log log = LogFactory.getLog(PostUpgradeConfig.class);

    /**
     * @uml.property name="upgrades"
     */
    private List<Upgrade> upgrades;

    public PostUpgradeConfig() {
    }

    /**
     * @return
     * @uml.property name="upgrades"
     */
    public List<Upgrade> getUpgrades() {
	return upgrades;
    }

    /**
     * @param upgrades
     * @uml.property name="upgrades"
     */
    public void setUpgrades(List<Upgrade> upgrades) {
	this.upgrades = upgrades;
    }

    public boolean validate() {
	if (upgrades == null)
	    return true;
	for (Upgrade upgrade : upgrades) {
	    if (!upgrade.validate())
		return false;
	}
	return true;
    }

}
