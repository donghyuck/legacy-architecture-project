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

import groovy.util.GroovyScriptEngine;

import java.net.URL;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.vfs2.FileObject;
import org.codehaus.groovy.control.CompilerConfiguration;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.core.io.ResourceLoader;

import architecture.common.lifecycle.ApplicationProperties;
import architecture.common.util.L10NUtils;
import architecture.common.util.StringUtils;
import architecture.common.util.vfs.VFSUtils;
import architecture.ee.component.admin.AdminHelper;
import architecture.ee.util.ApplicationConstants;

public class GroovyScriptEngineFactory implements FactoryBean<GroovyScriptEngine>, InitializingBean,  ResourceLoaderAware {
	
	private Log log = LogFactory.getLog(getClass());
	
	private GroovyScriptEngine groovyScriptEngine;
	
	private ResourceLoader resourceLoader ;

	public void setResourceLoader(ResourceLoader resourceLoader) {
		this.resourceLoader = resourceLoader;
	}

	public void afterPropertiesSet() throws Exception {
		if(this.groovyScriptEngine == null)
		{			
			
			ApplicationProperties setupProperties = AdminHelper.getRepository().getSetupApplicationProperties();
			
			String path = setupProperties.get(ApplicationConstants.SCRIPTING_GROOVY_SOURCE_LOCATION_PROP_NAME);	
			
			String sourceEncoding = setupProperties.get(ApplicationConstants.SCRIPTING_GROOVY_SOURCE_ENCODING_PROP_NAME);	
			
			boolean recompileGroovySource = setupProperties.getBooleanProperty(ApplicationConstants.SCRIPTING_GROOVY_SOURCE_RECOMPILE_PROP_NAME, false);
			
			
			if( StringUtils.isEmpty(path) ){
				path = AdminHelper.getRepository().getURI("groovy");
			}			
			
			if( StringUtils.isEmpty( sourceEncoding ))
				sourceEncoding = ApplicationConstants.DEFAULT_CHAR_ENCODING;
						
			if(log.isErrorEnabled())
				log.debug( L10NUtils.format("003031", path ) );
			
			FileObject fo = VFSUtils.resolveFile(path);			
			CompilerConfiguration config = CompilerConfiguration.DEFAULT ;
			config.setRecompileGroovySource(recompileGroovySource);
			config.setSourceEncoding(sourceEncoding);
			
			this.groovyScriptEngine = new GroovyScriptEngine( new URL[]{ fo.getURL()  } );		
			
		}
	}

	public GroovyScriptEngine getObject() throws Exception {
		return groovyScriptEngine;
	}

	public Class<GroovyScriptEngine> getObjectType() {
		return GroovyScriptEngine.class;
	}

	public boolean isSingleton() {
		return true;
	}
	
}
