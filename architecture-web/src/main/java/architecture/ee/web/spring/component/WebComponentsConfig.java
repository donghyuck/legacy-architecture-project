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
package architecture.ee.web.spring.component;

import net.sf.ehcache.Ehcache;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import architecture.ee.util.ApplicationHelper;

@Configuration
public class WebComponentsConfig {

	public WebComponentsConfig() {
	}


	/**
	 * config by xml "components.multipart.maxUploadSize" node value in setup-config.xml file.
	 * default value by 10mb 
	 * @return
	 */
	@Bean
	public MultipartResolver multipartResolver() {
		CommonsMultipartResolver resolver = new CommonsMultipartResolver();
		long maxUploadSize = ApplicationHelper.getApplicationLongProperty("components.multipart.maxUploadSize",  10485760 );
		resolver.setMaxUploadSize(maxUploadSize);
		return resolver;
	}
	
	
	
	@Bean(name = "monitoringCache")
	public Ehcache monitoringCache(){		
		org.springframework.cache.ehcache.EhCacheFactoryBean factory = new org.springframework.cache.ehcache.EhCacheFactoryBean();
		factory.setCacheName("monitoringCache");
		factory.setMaxEntriesLocalHeap(100);
		factory.setDiskPersistent(false);
		factory.setOverflowToDisk(false);
		factory.setTimeToIdle(30);
		factory.setTimeToLive(3600);
		factory.setDiskExpiryThreadIntervalSeconds(0);
		factory.setEternal(false);
		factory.setStatisticsEnabled(true);		
		factory.afterPropertiesSet();
		return factory.getObject();
	}

	@Bean(name = "imageCache")
	public Ehcache imageCache(){		
		org.springframework.cache.ehcache.EhCacheFactoryBean factory = new org.springframework.cache.ehcache.EhCacheFactoryBean();
		factory.setCacheName("imageCache");
		factory.setMaxEntriesLocalHeap(5000);
		factory.setDiskPersistent(false);
		factory.setOverflowToDisk(false);
		factory.setTimeToIdle(30);
		factory.setTimeToLive(60);
		factory.setDiskExpiryThreadIntervalSeconds(0);
		factory.setEternal(false);
		factory.setStatisticsEnabled(true);		
		factory.afterPropertiesSet();
		return factory.getObject();
	}
	
	@Bean(name = "imageLinkCache")
	public Ehcache imageLinkCache(){		
		org.springframework.cache.ehcache.EhCacheFactoryBean factory = new org.springframework.cache.ehcache.EhCacheFactoryBean();
		factory.setCacheName("imageLinkCache");
		factory.setMaxEntriesLocalHeap(5000);
		factory.setDiskPersistent(false);
		factory.setOverflowToDisk(false);
		factory.setTimeToIdle(30);
		factory.setTimeToLive(3600);
		factory.setDiskExpiryThreadIntervalSeconds(0);
		factory.setEternal(false);
		factory.setStatisticsEnabled(true);		
		factory.afterPropertiesSet();
		return factory.getObject();
	}
	
	@Bean(name = "imageLinkIdCache")
	public Ehcache imageLinkIdCache(){		
		org.springframework.cache.ehcache.EhCacheFactoryBean factory = new org.springframework.cache.ehcache.EhCacheFactoryBean();
		factory.setCacheName("imageLinkIdCache");
		factory.setMaxEntriesLocalHeap(5000);
		factory.setDiskPersistent(false);
		factory.setOverflowToDisk(false);
		factory.setTimeToIdle(30);
		factory.setTimeToLive(3600);
		factory.setDiskExpiryThreadIntervalSeconds(0);
		factory.setEternal(false);
		factory.setStatisticsEnabled(true);		
		factory.afterPropertiesSet();
		return factory.getObject();
	}
	
	@Bean(name = "attachmentCache")
	public Ehcache attachmentCache(){		
		org.springframework.cache.ehcache.EhCacheFactoryBean factory = new org.springframework.cache.ehcache.EhCacheFactoryBean();
		factory.setCacheName("attachmentCache");
		factory.setMaxEntriesLocalHeap(5000);
		factory.setDiskPersistent(false);
		factory.setOverflowToDisk(false);
		factory.setTimeToIdle(30);
		factory.setTimeToLive(60);
		factory.setDiskExpiryThreadIntervalSeconds(0);
		factory.setEternal(false);
		factory.setStatisticsEnabled(true);		
		factory.afterPropertiesSet();
		return factory.getObject();
	}
	
	@Bean(name = "menuCache")
	public Ehcache menuCache(){		
		org.springframework.cache.ehcache.EhCacheFactoryBean factory = new org.springframework.cache.ehcache.EhCacheFactoryBean();
		factory.setCacheName("menuCache");
		factory.setMaxEntriesLocalHeap(5000);
		factory.setDiskPersistent(false);
		factory.setOverflowToDisk(false);
		factory.setTimeToIdle(30);
		factory.setTimeToLive(60);
		factory.setDiskExpiryThreadIntervalSeconds(0);
		factory.setEternal(false);
		factory.setStatisticsEnabled(true);		
		factory.afterPropertiesSet();
		return factory.getObject();
	}
	
	@Bean(name = "menuIdCache")
	public Ehcache menuIdCache(){		
		org.springframework.cache.ehcache.EhCacheFactoryBean factory = new org.springframework.cache.ehcache.EhCacheFactoryBean();
		factory.setCacheName("menuIdCache");
		factory.setMaxEntriesLocalHeap(5000);
		factory.setDiskPersistent(false);
		factory.setOverflowToDisk(false);
		factory.setTimeToIdle(30);
		factory.setTimeToLive(60);
		factory.setDiskExpiryThreadIntervalSeconds(0);
		factory.setEternal(false);
		factory.setStatisticsEnabled(true);		
		factory.afterPropertiesSet();
		return factory.getObject();
	}
	
	
}
