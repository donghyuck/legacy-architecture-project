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

package architecture.ee.spring.scripting.groovy;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.vfs2.FileObject;
import org.codehaus.groovy.control.CompilerConfiguration;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.core.io.ResourceLoader;
import org.springframework.util.ClassUtils;

import architecture.common.lifecycle.ApplicationProperties;
import architecture.common.util.L10NUtils;
import architecture.common.util.StringUtils;
import architecture.common.util.vfs.VFSUtils;
import architecture.ee.component.admin.AdminHelper;
import architecture.ee.util.ApplicationConstants;
import groovy.lang.GroovyClassLoader;

public class GroovyClassLoaderFactory implements FactoryBean<GroovyClassLoader>, InitializingBean,  ResourceLoaderAware {

	private Log log = LogFactory.getLog(getClass());
	
	private GroovyClassLoader groovyClassLoader;
	
	private ResourceLoader resourceLoader ;
	
	public ResourceLoader getResourceLoader() {
		return resourceLoader;
	}

	public void setResourceLoader(ResourceLoader resourceLoader) {
		this.resourceLoader = resourceLoader;
	}

	public Class<?> getObjectType() {
		return GroovyClassLoader.class;
	}
	
	public GroovyClassLoader getObject() throws Exception {
		return groovyClassLoader;
	}

	public boolean isSingleton() {
		return true;
	}

	public void afterPropertiesSet() throws Exception {
			if(groovyClassLoader == null)
			{
				ApplicationProperties setupProperties = AdminHelper.getRepository().getSetupApplicationProperties();
				
				String sourcePath = setupProperties.get(ApplicationConstants.SCRIPTING_GROOVY_SOURCE_LOCATION_PROP_NAME);				
				String sourceEncoding = setupProperties.get(ApplicationConstants.SCRIPTING_GROOVY_SOURCE_ENCODING_PROP_NAME);			
				boolean recompileSource = setupProperties.getBooleanProperty(ApplicationConstants.SCRIPTING_GROOVY_SOURCE_RECOMPILE_PROP_NAME, false);				
				boolean debug = setupProperties.getBooleanProperty(ApplicationConstants.SCRIPTING_GROOVY_DEBUG_PROP_NAME, false);
				
				if( StringUtils.isEmpty(sourcePath) ){
					sourcePath = AdminHelper.getRepository().getURI("groovy");
				}							
				if( StringUtils.isEmpty( sourceEncoding ))
					sourceEncoding = ApplicationConstants.DEFAULT_CHAR_ENCODING;
				
				CompilerConfiguration config = CompilerConfiguration.DEFAULT;
				config.setSourceEncoding(sourceEncoding);
				config.setRecompileGroovySource(recompileSource);
				config.setDebug(debug);
				
				GroovyClassLoader groovyClassLoaderToUse = new GroovyClassLoader( ClassUtils.getDefaultClassLoader(), config );				
				
				
				try {
					FileObject fo = VFSUtils.resolveFile(sourcePath);	
					groovyClassLoaderToUse.addClasspath( fo.getURL().getFile() );
					
					if( log.isDebugEnabled() )
					{
						log.debug( fo.getURL() );
					}
					
				} catch (Exception e) {
					if(log.isErrorEnabled())
						log.debug( L10NUtils.format("003031", sourcePath ) );
				}
				
				this.groovyClassLoader = groovyClassLoaderToUse;
			}				
	}
}
