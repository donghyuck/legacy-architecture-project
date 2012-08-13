package architecture.ee.plugin;

import java.io.File;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

import org.dom4j.Document;

import architecture.ee.plugin.dao.PluginEntityObject;

public interface PluginMetaData {

	public enum LicenseType {
		apache, 
		commercial, 
		gpl, 
		internal, 
		other;		
	};
	

    public abstract String getAuthor();

    public abstract PluginClassLoader getClassLoader();

    public abstract Document getConfig();

    public abstract String getDatabaseKey();

    public abstract int getDatabaseVersion();

    public abstract String getDescription();

    public abstract LicenseType getLicense();

    public abstract String getMinServerVersion();

    public abstract String getName();

    public abstract Plugin getPlugin();

    public abstract PluginEntityObject getPluginDbBean();

    public abstract File getPluginDirectory();

    public abstract Map getPluginProperties();

    public abstract ResourceBundle getPluginResourceBundle(Locale locale);

    public abstract String getVersion();

    public abstract boolean isChangelogExists();

    public abstract boolean isInstalled();

    public abstract boolean isLargeLogoExists();

    public abstract boolean isReadmeExists();

    public abstract boolean isSmallLogoExists();

    public abstract boolean isUninstalled();
}
