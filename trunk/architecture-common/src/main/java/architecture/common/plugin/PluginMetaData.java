/*
 * Copyright 2010, 2011 INKIUM, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package architecture.common.plugin;

import java.io.File;
import java.util.Locale;
import java.util.ResourceBundle;

import org.dom4j.Document;

import architecture.common.lifecycle.ApplicationProperties;

public interface PluginMetaData {

	enum LicenseType {
		COMMERCIAL,
		GPL,
		APACHE,
		INTERNAL,
		OTHER
	}
	
    public abstract File getPluginDirectory();

    public abstract Plugin getPlugin();

    public abstract PluginClassLoader getClassLoader();

    public abstract Document getConfig();

    public abstract ResourceBundle getPluginResourceBundle(Locale locale);

    public abstract ApplicationProperties getPluginProperties();

    public abstract String getName();

    public abstract String getDescription();

    public abstract String getAuthor();

    public abstract String getVersion();

    public abstract String getMinServerVersion();

    public abstract String getDatabaseKey();

    public abstract int getDatabaseVersion();

    public abstract LicenseType getLicense();

    public abstract boolean isReadmeExists();

    public abstract boolean isSmallLogoExists();

    public abstract boolean isLargeLogoExists();

    public abstract boolean isChangelogExists();

   // public abstract PluginDbBean getPluginDbBean();

    public abstract boolean isInstalled();

    public abstract boolean isUninstalled();
    
}