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
package architecture.ee.spring.jdbc;

import java.io.InputStream;
import java.util.Collections;
import java.util.List;

import javax.sql.DataSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.vfs.FileObject;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.UrlResource;

import architecture.common.lifecycle.State;
import architecture.ee.jdbc.query.builder.xml.XmlSqlBuilder;
import architecture.ee.jdbc.query.factory.Configuration;
import architecture.ee.jdbc.query.factory.ConfigurationFactory;
import architecture.ee.jdbc.query.factory.SqlQueryFactory;
import architecture.ee.jdbc.query.factory.impl.SqlQueryFactoryImpl;

/**
 * SqlQueryFactory 객체를 생성하는 클래스.
 * 
 * @author DongHyuck, Son 
 *
 */
public class SqlQueryFactoryBuilder {
		
	private Log log = LogFactory.getLog(getClass());
		
	private ResourceLoader resourceLoader ;
		
	private DataSource defaultDataSource;
	
	private List<String> resourceLocations;	
	
	private SqlResourceDeployer sqlResourceDeployer ;
	
	private boolean isSetSqlResourceDeployer ;
	
	public SqlQueryFactoryBuilder() {
		this.resourceLoader = new DefaultResourceLoader();
		this.defaultDataSource = null;
		this.resourceLocations = Collections.EMPTY_LIST;
		this.isSetSqlResourceDeployer = false;
	}

	public List<String> getResourceLocations() {
		return resourceLocations;
	}
	
	public void setDefaultDataSource(DataSource dataSource) {		
		this.defaultDataSource = dataSource;
	}

	public void setResourceLocations(List<String> resourceLocations) {
		this.resourceLocations = resourceLocations;
	}

	public void setResourceLoader(ResourceLoader loader) {
		resourceLoader = loader;
	}


	public SqlQueryFactory createSqlQueryFactory() {
		loadResourceLocations();
		return createSqlQueryFactory(ConfigurationFactory.getConfiguration(), defaultDataSource);
	}	
	
	public ResourceLoader getResourceLoader() {
		return resourceLoader;
	}

	
	protected void loadResourceLocations(){		
		
		try {				
			Configuration configuration = ConfigurationFactory.getConfiguration();

			for(String path : resourceLocations ){			
				
				if( !configuration.isResourceLoaded(path) ){					
					Resource resource = getResourceLoader().getResource(path);	
					
					log.debug("#" + path);
					log.debug("#" + resource.getClass().getName());
					log.debug("#" + resource.exists());					
					if(resource.exists()){						
						if( resource instanceof UrlResource || resource instanceof FileSystemResource ){							
							if(isSetSqlResourceDeployer){								
								sqlResourceDeployer.addUri(path);								
								List<FileObject> list = sqlResourceDeployer.getMonitoredFileObjectList();
								for( FileObject fo : list ){	
									String uri = fo.getName().getURI();	
									if(fo.getType().hasContent()){
										log.debug("##deploy:" + uri );
										sqlResourceDeployer.buildSqlFromInputStream(fo.getContent().getInputStream(), configuration, uri);
										configuration.addLoadedResource(uri);
									}
								}								
							}
						}else{
							buildSqlFromInputStream(resource.getInputStream(), configuration);
							configuration.addLoadedResource(path);
						}						
					}
				}
			}		

			if(isSetSqlResourceDeployer){				
				if( sqlResourceDeployer.getState() == State.INITIALIZED )
					sqlResourceDeployer.start();
				log.debug("SqlResourceDeployer Status:" + sqlResourceDeployer.getState());
			}
			
		} catch (Exception e) {}
		
		
	}	
	
	public SqlQueryFactory createSqlQueryFactory(Configuration configuration, DataSource dataSource) {
		SqlQueryFactory factory = new SqlQueryFactoryImpl(configuration);
		factory.setDefaultDataSource(dataSource);
		return factory;
	}	
	

	
	public void setSqlResourceDeployer(SqlResourceDeployer sqlResourceDeployer) {
		this.sqlResourceDeployer = sqlResourceDeployer;
		this.isSetSqlResourceDeployer = true;
	}

	protected void buildSqlFromInputStream(InputStream inputStream, Configuration configuration){
		XmlSqlBuilder builder = new XmlSqlBuilder(inputStream, configuration);
		builder.build();
	}	
		
}