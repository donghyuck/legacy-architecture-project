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
package architecture.ee.web.view.freemarker;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletContext;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.core.io.Resource;
import org.springframework.ui.freemarker.FreeMarkerConfigurationFactory;
import org.springframework.ui.freemarker.SpringTemplateLoader;
import org.springframework.web.context.ServletContextAware;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfig;

import architecture.common.util.StringUtils;
import architecture.ee.util.ApplicationHelper;
import architecture.ee.web.util.WebApplicatioinConstants;
import freemarker.cache.ClassTemplateLoader;
import freemarker.cache.FileTemplateLoader;
import freemarker.cache.TemplateLoader;
import freemarker.ext.jsp.TaglibFactory;
import freemarker.template.Configuration;
import freemarker.template.TemplateException;

/**
 * 
 * @author <a href="mailto:donghyuck.son@gmail.com">Donghyuck Son </a>
 * 
 */
public class FreeMarkerConfigurer extends FreeMarkerConfigurationFactory
		implements FreeMarkerConfig, InitializingBean, ResourceLoaderAware,
		ServletContextAware {

	private Configuration configuration;

	private TaglibFactory taglibFactory;

	/**
	 * Set a preconfigured Configuration to use for the FreeMarker web config,
	 * e.g. a shared one for web and email usage, set up via
	 * FreeMarkerConfigurationFactoryBean. If this is not set,
	 * FreeMarkerConfigurationFactory's properties (inherited by this class)
	 * have to be specified.
	 * 
	 * @see org.springframework.ui.freemarker.FreeMarkerConfigurationFactoryBean
	 */
	public void setConfiguration(Configuration configuration) {
		this.configuration = configuration;
	}

	/**
	 * Initialize the {@link TaglibFactory} for the given ServletContext.
	 */
	public void setServletContext(ServletContext servletContext) {
		this.taglibFactory = new TaglibFactory(servletContext);
	}

	/**
	 * Initialize FreeMarkerConfigurationFactory's Configuration if not
	 * overridden by a preconfigured FreeMarker Configuation.
	 * <p>
	 * Sets up a ClassTemplateLoader to use for loading Spring macros.
	 * 
	 * @see #createConfiguration
	 * @see #setConfiguration
	 */
	public void afterPropertiesSet() throws IOException, TemplateException {
		if (this.configuration == null) {			
			
			logger.debug( "template customized enabled : " + isCustomizedEnabled() );			
			if( isCustomizedEnabled() ){
				File home = getCustomizedTemplateHome();
				logger.debug( "customized template source path : " + home );
				if( home != null ){
					DatabaseTemplateLoader templateLoader = new DatabaseTemplateLoader(home);
					templateLoader.initialize();
					this.setPreTemplateLoaders(new TemplateLoader[]{ templateLoader });
				}
			}			
			this.configuration = createConfiguration();
		}
	}

	protected final File getCustomizedTemplateHome(){			
		String location = ApplicationHelper.getApplicationProperty("view.html.customize.source.location",  null );				
		if ( StringUtils.isEmpty(location))
			return null;					
		Resource resource = getResourceLoader().getResource(location);
		File file = null;
		try {
			file = resource.getFile();
			if( !file.exists()){
				file.mkdirs();
			}
		} catch (IOException e) {
			logger.error(e);
		}	
		return file;		
	}
	
	protected final boolean isCustomizedEnabled(){		
		return ApplicationHelper.getApplicationBooleanProperty("view.html.customize.enabled", false);
	}
	
	/**
	 * This implementation registers an additional ClassTemplateLoader for the
	 * Spring-provided macros, added to the end of the list.
	 */
	@Override
	protected void postProcessTemplateLoaders( List<TemplateLoader> templateLoaders) {
		templateLoaders.add(new ClassTemplateLoader(FreeMarkerConfigurer.class, "/"));
		logger.info("ClassTemplateLoader for Spring macros added to FreeMarker configuration");
	}
	
	protected String[] getFreemarkerSourceLoactions(){
		String pathString = ApplicationHelper.getRepository().getSetupApplicationProperties().getStringProperty(WebApplicatioinConstants.VIEW_FREEMARKER_SOURCE_LOCATION, null);
		if( StringUtils.isNotEmpty(pathString)){
			String[] paths  = org.springframework.util.StringUtils.commaDelimitedListToStringArray(pathString);
			return paths;
		}
		return new String[0];
	}
	
	@Override
	public void setTemplateLoaderPaths(String[] templateLoaderPaths) {
		String[] paths  = getFreemarkerSourceLoactions();
		
		if( paths.length > 0 ){
			logger.debug("setting template source paths from startup-config.xml");
			List<String> list = new ArrayList<String>();
			if( templateLoaderPaths != null ){
				for(String path:templateLoaderPaths){
					list.add(path);
				}
			}			
			//String[] paths  = getFreemarkerSourceLoactions();			
			for( String path : paths ){
				if( ! list.contains( path) )
					list.add(path);
			}
			logger.debug("template source paths : " + list );
			
			String[] pathsToUse = new String[list.size()] ; 
			list.toArray(pathsToUse);
			super.setTemplateLoaderPaths( pathsToUse );
		}else{
			super.setTemplateLoaderPaths(templateLoaderPaths);
		}
		
	}

	/**
	 * Return the Configuration object wrapped by this bean.
	 */
	public Configuration getConfiguration() {
		return this.configuration;
	}

	/**
	 * Return the TaglibFactory object wrapped by this bean.
	 */
	public TaglibFactory getTaglibFactory() {
		return this.taglibFactory;
	}
	
    @Override
    protected TemplateLoader getTemplateLoaderForPath(String templateLoaderPath) {
    	    	
        if (isPreferFileSystemAccess()) {
            // Try to load via the file system, fall back to SpringTemplateLoader
            // (for hot detection of template changes, if possible).
            try {
                Resource path =getResourceLoader().getResource(templateLoaderPath);
                File file = path.getFile();  // will fail if not resolvable in the file system
                if (logger.isDebugEnabled()) {
                    logger.debug(  "Template loader path [" + path + "] resolved to file path [" + file.getAbsolutePath() + "]");
                }
                return new FileTemplateLoader(file);
            }
            catch (IOException ex) {
                if (logger.isDebugEnabled()) {
                    logger.debug("Cannot resolve template loader path [" + templateLoaderPath +  "] to [java.io.File]: using SpringTemplateLoader as fallback", ex);
                }
                return new SpringTemplateLoader(getResourceLoader(), templateLoaderPath);
            }
        }
        else {
            // Always load via SpringTemplateLoader (without hot detection of template changes).
            logger.debug("File system access not preferred: using SpringTemplateLoader");
            return new SpringTemplateLoader(getResourceLoader(), templateLoaderPath);
        }
    }
}
