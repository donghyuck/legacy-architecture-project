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
package architecture.ee.jdbc.sqlquery.factory.impl;

import architecture.ee.jdbc.sqlquery.factory.Configuration;
import architecture.ee.jdbc.sqlquery.factory.ConfigurationFactory;
import architecture.ee.jdbc.sqlquery.factory.SqlQueryFactory;
import architecture.ee.jdbc.sqlquery.factory.SqlQueryFactoryBuilder.Implementation;

/**
 * SqlQueryFactory 객체를 생성하는 클래스.
 * @author   DongHyuck, Son
 */

public class SqlQueryFactoryBuilderImpl implements Implementation {

	private SqlQueryFactory factory = null;
			
	public SqlQueryFactory getSqlQueryFactory() {		
		if(factory == null){
			this.factory = createSqlQueryFactory();
		}
		return factory;
	}
	
	public SqlQueryFactory getSqlQueryFactory(Configuration configuration) {		
		if(factory == null){
			this.factory = createSqlQueryFactory(configuration);
		}
		return factory;
	}
	
	private SqlQueryFactory createSqlQueryFactory() { 		
		Configuration configuration = ConfigurationFactory.getConfiguration();
		SqlQueryFactoryImpl factory = new SqlQueryFactoryImpl(configuration);		
		return factory;    	
	}
	
	private SqlQueryFactory createSqlQueryFactory(Configuration configuration) { 
		SqlQueryFactoryImpl factory = new SqlQueryFactoryImpl(configuration);		
		return factory;    	
	}
	
}



/*
public class SqlQueryFactoryBuilderImpl implements SqlQueryFactoryBuilder {

	private Log log = LogFactory.getLog(getClass());

	private String prefix = "";

	private String suffix = "sqlset.xml";
	
	private Configuration configuration;

	private DataSource dataSource;

	private List<String> resourceLocations;

	public Configuration getConfiguration() {
		return configuration;
	}

	public SqlQueryFactoryBuilderImpl() {
		this.dataSource = null;
		this.configuration = ConfigurationFactory.getConfiguration();
		this.resourceLocations = Collections.emptyList();
	}
	
	public SqlQueryFactoryBuilderImpl(Configuration configuration) {
		this.dataSource = null;
		this.configuration = configuration;
		this.resourceLocations = Collections.emptyList();
	}
	
	public boolean isSetDataSource() {
		if( dataSource == null)
			return false;
		else
			return true;		
	}
	
	public void initialize(){
		if(resourceLocations.size() > 0)
			loadResourceLocations();
	}
	
		
	public SqlQueryFactory createSqlQueryFactory() { 				
		SqlQueryFactoryImpl factory = new SqlQueryFactoryImpl(configuration);		
		if(isSetDataSource())
			factory.setDataSource(dataSource);		
		return factory;    	
	}

	public SqlQueryFactory createSqlQueryFactory(DataSource dataSource) {		
		SqlQueryFactoryImpl factory = new SqlQueryFactoryImpl(configuration);
		factory.setDataSource(dataSource);
		return factory;
	}
	
	protected void buildSqlFromInputStream(InputStream inputStream, Configuration configuration) {
		XmlSqlBuilder builder = new XmlSqlBuilder(inputStream, configuration);
		builder.build();
	}

	*//**
	 * @return
	 *//*
	public List<String> getResourceLocations() {
		return this.resourceLocations;
	}

	protected void loadResourceLocations() {

		for (String path : resourceLocations) {
			try {
				FileObject fo = VFSUtils.resolveFile(path);
				if (fo.exists()) {
					if (!configuration.isResourceLoaded(fo.getName().getURI())) {
						buildSqlFromInputStream(fo.getContent().getInputStream(), configuration);
						configuration.addLoadedResource(fo.getName().getURI());
					}
				}
			} catch (Throwable e) {
				log.warn(path + " not found.", e);
			}
		}

	}

	*//**
	 * @param dataSource
	 *//*
	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	*//**
	 * @param resourceLocations
	 *//*
	public void setResourceLocations(List<String> resourceLocations) {
		this.resourceLocations = resourceLocations;
	}

	*//**
	 * @return
	 *//*
	public String getPrefix() {
		return prefix;
	}
	
	
	 * Set the prefix that gets prepended to view names when building a URL.
	 
	*//**
	 * @param prefix
	 *//*
	public void setPrefix(String prefix) {
		this.prefix = (prefix != null ? prefix : "");
	}

    *//**
	 * @return
	 *//*
    public String getSuffix() {
		return suffix;
	}
    
	*//**
	 * Set the suffix that gets appended to view names when building a URL.
	 *//*
	public void setSuffix(String suffix) {
		this.suffix = (suffix != null ? suffix : "");
	}
			
	private Map<URI, DeployedInfo> deployments = Collections.synchronizedMap(new HashMap<URI, DeployedInfo>());
		
	
	static class DeployedInfo {

		protected long deployedTime;
		protected String name;
		protected URI uri;
		protected long timestamp = -1L;
		
		public DeployedInfo(URI uri, String name, long deployedTime) {
			this.deployedTime = deployedTime;
			this.name = name;
			this.uri = uri;
		}
		public DeployedInfo(URI uri, String name, long deployedTime, long timestamp) {
			this.deployedTime = deployedTime;
			this.name = name;
			this.uri = uri;
			this.timestamp = timestamp;
		}		

		public DeployedInfo(URI uri, long deployedTime) {
			this.deployedTime = deployedTime;
			this.uri = uri;
		}
	}

	public String fileCreated(File file) {		
		try {	
			URI uri = file.toURI();
			if( deployments.containsKey(uri) ){
				DeployedInfo di = deployments.get(uri);
				if( di.timestamp == file.lastModified()){
					return "";
				}
			}else{
				buildSqlFromInputStream(new FileInputStream(file), configuration);
				DeployedInfo di = new DeployedInfo(file.toURI(), System.currentTimeMillis());
				di.timestamp = file.lastModified();
				deployments.put(di.uri, di);
			}
		} catch (FileNotFoundException e) {}		
		return "success";
	}

	public boolean fileDeleted(File file) {
		
		if (configuration.isResourceLoaded(file.toURI().toString())) {
			configuration.removeUriNamespace(file.toURI().toString(), true);
			configuration.removeLoadedResource(file.toURI().toString());
		}
		
		//DeployedInfo di = 
		deployments.remove(file.toURI());
		
		return true;
		
	}

	public void fileChanged(File file) {
		try {
			buildSqlFromInputStream(new FileInputStream(file), ConfigurationFactory.getConfiguration());
			DeployedInfo di = deployments.get(file.toURI());
			di.deployedTime = System.currentTimeMillis();
			deployments.put(di.uri, di);
		} catch (FileNotFoundException e) {
		}
	}

	public long getDeploymentTime(File file) {
		if (isFileDeployed(file))
			return deployments.get(file.toURI()).deployedTime;
		return 0;
	}

	public boolean isFileDeployed(File file) {
		return deployments.containsKey(file.toURI());
	}

	*//**
	 * 
	 * 파일이 유요한 형태의 파일인가를 확인하여 true/false 값을 리턴한다.
	 *//*
	public boolean validateFile(File file) {

		boolean readable = file.canRead();
		boolean flag1 = StringUtils.isEmpty(getSuffix()) ? true : file.getName().endsWith(getSuffix());
		boolean flag2 = StringUtils.isEmpty(getPrefix()) ? true : file.getName().startsWith(getPrefix());
		boolean valid = readable && flag1 && flag2;
		if (!valid) {
			// if (log.isDebugEnabled())
			// log.info(MessageFormatter.format("011023",
			// file.getAbsolutePath()));
		}

		// log.debug(file.getPath() + "validate:" + valid);
		return valid;
	}

}*/