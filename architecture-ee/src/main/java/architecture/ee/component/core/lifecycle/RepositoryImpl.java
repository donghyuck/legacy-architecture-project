/*
 * Copyright 2012 Donghyuck, Son
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
package architecture.ee.component.core.lifecycle;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;

import javax.servlet.ServletContext;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.FileSystemResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.jndi.JndiTemplate;
import org.springframework.web.context.support.ServletContextResource;
import org.springframework.web.context.support.ServletContextResourceLoader;

import architecture.common.exception.ComponentDisabledException;
import architecture.common.exception.ConfigurationError;
import architecture.common.exception.ConfigurationWarning;
import architecture.common.lifecycle.ApplicationProperties;
import architecture.common.lifecycle.ComponentImpl;
import architecture.common.lifecycle.ConfigRoot;
import architecture.common.lifecycle.Repository;
import architecture.common.lifecycle.State;
import architecture.common.lifecycle.internal.EmptyApplicationProperties;
import architecture.common.lifecycle.internal.XmlApplicationProperties;
import architecture.common.util.L10NUtils;
import architecture.common.util.StringUtils;
import architecture.common.xml.XmlProperties;

import architecture.ee.util.ApplicationConstants;

/**
 * @author donghyuck
 */

public class RepositoryImpl extends ComponentImpl implements Repository {

    private Log log = LogFactory.getLog(getClass());

    private JndiTemplate jndiTemplate = new JndiTemplate();

    private ResourceLoader resoruceLoader = new FileSystemResourceLoader();

    private String effectiveRootPath;

    private boolean initailized = false;

    public RepositoryImpl() {
	super();
    }

    private Resource rootResource = getRootResource();

    private ApplicationProperties setupProperties = null;

    public String getRootURI() {

	if (rootResource == null) {
	    String uri = getEnvironmentRootPath();
	    try {
		Resource obj = resoruceLoader.getResource(uri);
		rootResource = obj;
		return obj.getURI().toString();
	    } catch (Exception e) {
		return null;
	    }
	}

	try {
	    return rootResource.getURI().toString();
	} catch (IOException e) {
	}

	return null;
    }

    public ConfigRoot getConfigRoot() {
	try {

	    File file = new File(getRootResource().getFile(), "config");
	    Resource child = new FileSystemResource(file);

	    log.debug("config root:" + child.getURI());

	    if (!child.exists()) {
		child.getFile().mkdirs();
	    }
	    return new ConfigRootImpl(child);
	} catch (Exception e) {
	}
	return null;
    }

    private Resource getRootResource() {

	if (initailized)
	    return rootResource;

	if (getState() != State.INITIALIZED || getState() == State.INITIALIZING) {

	    ClassLoader classloader = Thread.currentThread().getContextClassLoader();
	    try {
		InputStream input = classloader.getResourceAsStream("application-init.xml");
		XmlProperties prop = new XmlProperties(input);
		String path = prop.getProperty("home");
		if (!StringUtils.isEmpty(path)) {
		    Resource resource = resoruceLoader.getResource(path);
		    log.debug(L10NUtils.format("003001", resource.getURI()));
		    this.rootResource = resource;
		    setState(State.INITIALIZED);
		    initailized = true;
		}
	    } catch (Throwable e) {
	    }
	}

	return rootResource;
    }

    public void initialize() throws ComponentDisabledException, ConfigurationWarning, ConfigurationError {
	log.debug("initialize");
	try {
	    this.rootResource = getRootResource();
	} catch (Exception e) {
	}
    }

    public String getEffectiveRootPath() {
	if (!StringUtils.isEmpty(effectiveRootPath)) {
	    return effectiveRootPath;
	} else {
	    String uri = getRootURI();
	    effectiveRootPath = uri;
	    return effectiveRootPath;
	}
    }

    public void setServletContext(ServletContext servletContext) {

	// 1. 서블릿 컨텍스트에 설정된 프로퍼티 값을 검사 : ARCHITECTURE_INSTALL_ROOT
	String value = servletContext.getInitParameter(ApplicationConstants.ARCHITECTURE_PROFILE_ROOT_ENV_KEY);
	if (!StringUtils.isEmpty(value)) {
	    try {
		ServletContextResourceLoader servletResoruceLoader = new ServletContextResourceLoader(servletContext);
		Resource resource = servletResoruceLoader.getResource(value);
		if (resource.exists()) {
		    log.debug(L10NUtils.format("003003", ApplicationConstants.ARCHITECTURE_PROFILE_ROOT_ENV_KEY, resource.getURI()));
		    this.rootResource = resource;
		    setState(State.INITIALIZED);
		    initailized = true;
		}
	    } catch (Throwable e) {
		this.rootResource = null;
	    }
	}

	if (!initailized && !StringUtils.isEmpty(value)) {
	    Resource obj;
	    try {
		obj = resoruceLoader.getResource(value);
		if (obj.exists()) {
		    log.debug(L10NUtils.format("003003", ApplicationConstants.ARCHITECTURE_PROFILE_ROOT_ENV_KEY,
			    obj.getURI()));
		    this.rootResource = obj;
		    setState(State.INITIALIZED);
		    initailized = true;

		}
	    } catch (Throwable e) {
		log.error(e);
	    }
	}

	if (!initailized) {
	    try {
		ServletContextResource resource = new ServletContextResource(servletContext, "/WEB-INF");
		File file = resource.getFile();
		if (file.exists()) {
		    this.rootResource = new FileSystemResource(file);
		    setState(State.INITIALIZED);
		    initailized = true;
		}
	    } catch (Throwable e) {
		log.error(e);
	    }
	}
    }

    public String getEnvironmentRootPath() {
	// 1. Java System Property 에서 검색: architecture.install.root
	String envRootPath = System.getProperty(ApplicationConstants.ARCHITECTURE_PROFILE_ROOT_KEY);

	// 2. Jndi 에서 검색 : architecture_install_root
	if (envRootPath == null || "".equals(envRootPath))
	    try {
		envRootPath = jndiTemplate.lookup(
			"java:comp/env/" + ApplicationConstants.ARCHITECTURE_PROFILE_ROOT_ENV_KEY.toLowerCase(),
			String.class);
	    } catch (Exception e) {
	    }
	// 3.
	if (envRootPath == null || "".equals(envRootPath)) {
	    envRootPath = System.getenv(ApplicationConstants.ARCHITECTURE_PROFILE_ROOT_KEY);
	    if (envRootPath != null && !"".equals(envRootPath))
		log.info(L10NUtils.format("003005", ApplicationConstants.ARCHITECTURE_PROFILE_ROOT_KEY, envRootPath));
	}
	if (envRootPath == null || "".equals(envRootPath)) {
	    StringBuilder buffer = new StringBuilder();
	    if (System.getProperty("os.name", "Linux").indexOf("Windows") == -1)
		buffer.append(File.separator).append("apps").append(File.separator).append("framework");
	    else
		buffer.append("c:").append(File.separator).append("apps").append(File.separator).append("framework");
	    envRootPath = buffer.toString();
	    log.warn(L10NUtils.format("003006", envRootPath));
	}
	return envRootPath;
    }

    public ApplicationProperties getSetupApplicationProperties() {
	if (setupProperties == null) {
	    try {
		File file = getFile(ApplicationConstants.DEFAULT_STARTUP_FILENAME);
		if (!file.exists()) {
		    boolean error = false;
		    // create default file...
		    log.debug("No startup file now create !!!");
		    Writer writer = null;
		    try {

			writer = new OutputStreamWriter(new FileOutputStream(file),
				ApplicationConstants.DEFAULT_CHAR_ENCODING);
			XMLWriter xmlWriter = new XMLWriter(writer, OutputFormat.createPrettyPrint());
			StringBuilder sb = new StringBuilder();
			org.dom4j.Document document = org.dom4j.DocumentHelper.createDocument();
			org.dom4j.Element root = document.addElement("startup-config");
			// setup start
			// ------------------------------------------------------------
			org.dom4j.Element setupNode = root.addElement("setup");
			setupNode.addElement("complete").setText("false");
			// setup end
			// --------------------------------------------------------------
			// license start
			org.dom4j.Element licenseNode = root.addElement("license");
			// license end
			// view start
			org.dom4j.Element viewNode = root.addElement("view");
			org.dom4j.Element renderNode = viewNode.addElement("render");
			org.dom4j.Element freemarkerNode = renderNode.addElement("freemarker");
			freemarkerNode.addElement("enabled").setText("true");
			freemarkerNode.addElement("source").addElement("location");
			org.dom4j.Element velocityNode = renderNode.addElement("velocity");
			velocityNode.addElement("enabled").setText("false");
			// view end
			// security start
			org.dom4j.Element securityNode = root.addElement("security");
			securityNode.addElement("authentication").addElement("encoding").addElement("algorithm")
				.setText("SHA-256");
			// security end

			// scripting start
			org.dom4j.Element scriptingNode = root.addElement("scripting");
			org.dom4j.Element groovyNode = scriptingNode.addElement("groovy");
			groovyNode.addElement("debug").setText("false");
			org.dom4j.Element sourceGroovyNode = groovyNode.addElement("source");
			sourceGroovyNode.addElement("location");
			sourceGroovyNode.addElement("encoding").setText(ApplicationConstants.DEFAULT_CHAR_ENCODING);
			sourceGroovyNode.addElement("recompile").setText("true");
			// scripting end
			// database start
			org.dom4j.Element databaseNode = root.addElement("database");
			// database end
			xmlWriter.write(document);
		    } catch (Exception e) {
			log.error(L10NUtils.format("003007", file.getName(), e.getMessage()));
			error = true;
		    } finally {
			try {
			    writer.flush();
			    writer.close();
			} catch (Exception e) {
			    log.error(e);
			    error = true;
			}
		    }
		}
		this.setupProperties = new XmlApplicationProperties(file);
	    } catch (Exception e) {
		log.warn("I warning you!");
		log.debug(e.getMessage(), e);
		return EmptyApplicationProperties.getInstance();
	    }
	}
	return setupProperties;
    }

    public String getURI(String name) {
	try {
	    return getFile(name).toURI().toString();
	} catch (Exception e) {
	}
	return null;
    }

    public File getFile(String name) {
	try {
	    File file = new File(getRootResource().getFile(), name);
	    return file;
	} catch (IOException e) {
	}
	return null;
    }

    public File getLicenseFile() {
	String filePath = getSetupApplicationProperties()
		.getStringProperty(ApplicationConstants.RESOURCE_LICENSE_LOCATION_PROP_NAME, null);
	if (!StringUtils.isEmpty(filePath)) {
	    return new File(filePath);
	} else {
	    return getConfigRoot().getFile("license");
	}
    }

}
