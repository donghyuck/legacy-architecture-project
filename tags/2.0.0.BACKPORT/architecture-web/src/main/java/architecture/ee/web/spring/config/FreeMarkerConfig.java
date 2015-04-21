/*
 * Copyright 2012, 2013 Donghyuck, Son
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
package architecture.ee.web.spring.config;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import architecture.common.exception.ConfigurationError;
import architecture.ee.web.view.freemarker.FreeMarkerConfigurer;

@Configuration
public class FreeMarkerConfig {

	public FreeMarkerConfig() {
	}

	
	
	@Bean
	public FreeMarkerConfigurer freemarkerConfig(){
		FreeMarkerConfigurer freeMarkerConfigurer = new FreeMarkerConfigurer();
		initializingBean(freeMarkerConfigurer);
		return freeMarkerConfigurer;
	}
	
	private void initializingBean (InitializingBean bean){
		try {
			bean.afterPropertiesSet();
		} catch (Exception e) {
			throw new ConfigurationError(e);
		}
	}
}
