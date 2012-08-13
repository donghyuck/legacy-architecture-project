package architecture.ee.plugin.impl;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.Document;
import org.dom4j.Element;

import architecture.ee.plugin.Plugin;
import architecture.ee.plugin.PluginClassLoader;
import architecture.ee.plugin.PluginMetaData;
import architecture.ee.plugin.dao.PluginEntityObject;
import architecture.ee.util.LocaleUtils;

public class PluginMetaDataImpl implements PluginMetaData {
	
	private static final Log log = LogFactory.getLog(PluginMetaDataImpl.class);
	
	protected String name;
	protected String author;
	protected Plugin plugin;
	protected PluginClassLoader loader;
	protected PluginManagerImpl pluginManager;
	protected Document config;
	protected Collection webServiceClasses;
	protected Collection widgetClasses;
	protected Collection cssUrls;
	protected Collection javaScriptUrls;
	protected final File pluginDirectory;
	protected String version;
	protected String minServerVersion;
	protected String databaseKey;
	protected String description;
	protected int databaseVersion;
	protected PluginMetaData.LicenseType licenseType;
	protected boolean readmeExists;
	protected boolean changelogExists;
	protected boolean largeLogoExists;
	protected boolean smallLogoExists;
	protected PluginEntityObject pluginDbBean;
	protected boolean installed;
	protected boolean uninstalled;

	public PluginMetaDataImpl(PluginManagerImpl pluginManager, Document config, File pluginDirectory) {
		this(null, null, pluginManager, config, pluginDirectory);
	}

	public PluginMetaDataImpl(Plugin plugin, PluginClassLoader loader,
			PluginManagerImpl pluginManager, Document config,
			File pluginDirectory) {
		
		webServiceClasses = new ArrayList();
		widgetClasses = new ArrayList();
		cssUrls = new ArrayList();
		javaScriptUrls = new ArrayList();
		databaseVersion = -1;
		licenseType = PluginMetaData.LicenseType.other;
		
		this.plugin = plugin;
		this.loader = loader;
		this.pluginManager = pluginManager;
		this.config = config;
		this.pluginDirectory = pluginDirectory;
		
		name = getElementValue("/plugin/name");
		author = getElementValue("/plugin/author");
		version = getElementValue("/plugin/version");
		minServerVersion = getElementValue("/plugin/minServerVersion");
		databaseKey = getElementValue("/plugin/databaseKey");
		
		description = getText(getElementValue("/plugin/description"), name);
		
		String versionString = getElementValue("/plugin/databaseVersion");
		if (versionString != null)
			try {
				databaseVersion = Integer.parseInt(versionString.trim());
			} catch (NumberFormatException nfe) {
				log.error(nfe);
			}
		String licenseString = getElementValue("/plugin/licenseType");
		if (licenseString != null)
			try {
				licenseType = PluginMetaData.LicenseType.valueOf(licenseString.toLowerCase().trim());
			} catch (IllegalArgumentException iae) {
				log.error(iae);
			}
		readmeExists = (new File(pluginDirectory, "readme.html")).exists();
		changelogExists = (new File(pluginDirectory, "changelog.html")).exists();
		largeLogoExists = (new File(pluginDirectory, "logo_large.png")).exists();
		smallLogoExists = (new File(pluginDirectory, "logo_small.png")).exists();
	}

	public File getPluginDirectory() {
		return pluginDirectory;
	}

	public Plugin getPlugin() {
		return plugin;
	}

	public PluginClassLoader getClassLoader() {
		return loader;
	}

	public Document getConfig() {
		return config;
	}

	public ResourceBundle getPluginResourceBundle(Locale locale) {
		return pluginManager.getPluginResourceBundle(plugin, locale);
	}

	public Map getPluginProperties() {
		return null ;// pluginManager.getPluginProperties(name);
	}

	public String getName() {
		return name;
	}

	public String getDescription() {
		return description;
	}

	public String getAuthor() {
		return author;
	}

	public String getVersion() {
		return version;
	}

	public String getMinServerVersion() {
		return minServerVersion;
	}

	public String getDatabaseKey() {
		return databaseKey;
	}

	public int getDatabaseVersion() {
		return databaseVersion;
	}

	public PluginMetaData.LicenseType getLicense() {
		return licenseType;
	}

	public boolean isReadmeExists() {
		return readmeExists;
	}

	public boolean isSmallLogoExists() {
		return smallLogoExists;
	}

	public boolean isLargeLogoExists() {
		return largeLogoExists;
	}

	public boolean isChangelogExists() {
		return changelogExists;
	}

	protected String getElementValue(String xpath) {
		try {
		
			Element element = (Element) config.selectSingleNode(xpath);
			if (element != null)
				return element.getTextTrim();
		} catch (Exception e) {
			log.error(e);
		}
		return null;
	}

	public PluginEntityObject getPluginDbBean() {
		return pluginDbBean;
	}

	public void setPluginDbBean(PluginEntityObject pluginDbBean) {
		this.pluginDbBean = pluginDbBean;
	}

	public boolean isInstalled() {
		return installed;
	}

	public void setInstalled(boolean installed) {
		this.installed = installed;
	}

	public boolean isUninstalled() {
		return uninstalled;
	}

	public void setUninstalled(boolean uninstalled) {
		this.uninstalled = uninstalled;
	}

	
    public static String getText(String str, String pluginName)
    {
        if(str == null)
            return null;
        if(str.indexOf("${") == 0 && str.indexOf("}") == str.length() - 1)
            return LocaleUtils.getLocalizedString(str.substring(2, str.length() - 1));//(str.substring(2, str.length() - 1), pluginName);
        else
            return str;
    }
}
