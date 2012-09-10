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

import groovy.lang.GroovyClassLoader;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.vfs2.FileObject;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.core.io.ResourceLoader;

import architecture.common.lifecycle.ApplicationProperties;
import architecture.common.lifecycle.bootstrap.Bootstrap;
import architecture.common.util.vfs.VFSUtils;

import architecture.ee.component.admin.AdminHelper;
import architecture.ee.util.ApplicationConstants;

public class GroovyClassLoaderFactory implements FactoryBean<GroovyClassLoader>, InitializingBean,  ResourceLoaderAware {

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
				groovyClassLoader = new GroovyClassLoader( Bootstrap.getBootstrapClassLoader() );				
			
				ApplicationProperties setupProperties = AdminHelper.getRepository().getSetupApplicationProperties();
				String path = setupProperties.get(ApplicationConstants.RESOURCE_GROOVY_LOCATION_PROP_NAME);				
				if( StringUtils.isEmpty(path) ){
					path = AdminHelper.getRepository().getURI("groovy");
				}
				
				FileObject fo = VFSUtils.resolveFile(path);
				
				groovyClassLoader.addURL(fo.getURL());
			}
	}


}
