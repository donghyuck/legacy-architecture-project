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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.vfs2.FileObject;
import org.apache.commons.vfs2.FileSelectInfo;
import org.apache.commons.vfs2.FileSelector;
import org.apache.commons.vfs2.FileSystemException;

import architecture.common.scanner.DirectoryListener;
import architecture.common.util.StringUtils;
import architecture.common.util.vfs.VFSUtils;
import architecture.ee.jdbc.sqlquery.builder.xml.XmlSqlBuilder;
import architecture.ee.jdbc.sqlquery.factory.Configuration;
import architecture.ee.jdbc.sqlquery.factory.SqlQueryFactory;

/**
 * @author  donghyuck
 */
public abstract class AbstractSqlQueryFactory implements SqlQueryFactory, DirectoryListener {
	
	protected Log log = LogFactory.getLog(getClass());
	
	private Map<URI, DeployedInfo> deployments = Collections.synchronizedMap(new HashMap<URI, DeployedInfo>());
	
	public static final String DEFAULT_FILE_SUFFIX = "sqlset.xml";
	
	private String prefix = "";
	
	private String suffix = DEFAULT_FILE_SUFFIX;
	
	private final Configuration configuration;
	
	protected List<String> resourceLocations;
			
	public AbstractSqlQueryFactory(Configuration configuration) {
		this.configuration = configuration;
	}


	public void setPrefix(String prefix) {
		this.prefix = (prefix != null ? prefix : "");
	}
	


	public List<String> getResourceLocations() {
		return resourceLocations;
	}



	public void setResourceLocations(List<String> resourceLocations) {
		this.resourceLocations = resourceLocations;
	}
	


	public void setSuffix(String suffix) {
		this.suffix = (suffix != null ? suffix : "");
	}	
	


	public Configuration getConfiguration(){
		return configuration;
	}
	
	protected void buildSqlFromInputStream(InputStream inputStream, Configuration configuration) {
		XmlSqlBuilder builder = new XmlSqlBuilder(inputStream, configuration);
		builder.build();
	}
	
	private FileObject[] findSqlFiles ( FileObject fo ) throws FileSystemException {
		return fo.findFiles(new FileSelector(){
			public boolean includeFile(FileSelectInfo fileInfo) throws Exception {
				FileObject f = fileInfo.getFile();
				log.debug("varifing : " + f.getName() );
				return StringUtils.endsWith(f.getName().getBaseName(), DEFAULT_FILE_SUFFIX);	
			}
			public boolean traverseDescendents(FileSelectInfo fileInfo) throws Exception {	
				return VFSUtils.isFolder(fileInfo.getFile());
		}});			
	}
	
	protected void loadResourceLocations() {
		
		List<FileObject> list = new ArrayList<FileObject>();		
		for (String path : resourceLocations) {
			try {
				FileObject f = VFSUtils.resolveFile(path);
				if (f.exists()) {
					list.add(f);
				}
			} catch (Throwable e) {
				log.warn(path + " not found.", e);
			}
		}	
		
		try {
			log.debug("searching sql ...");
			ClassLoader cl = Thread.currentThread().getContextClassLoader();
			Enumeration<URL> paths = cl.getResources("sql/");
			do {
				if (!paths.hasMoreElements())
					break;			
				URL url = paths.nextElement();
				String pathToUse = "jar:" + url.getPath();				
				log.debug( "target:" + pathToUse);				
				FileObject fo = VFSUtils.resolveFile(pathToUse);				
				FileObject[] selected = findSqlFiles(fo);				
				for( FileObject f : selected ){
					if( ! list.contains(f) ){
						list.add(f);
					}						
				}			
			} while ( true );	
		} catch (Throwable e) {
			log.warn(e);
		}					
		for( FileObject fo : list){
			try {
				log.debug("sql : " + fo.getName() );
				if (!configuration.isResourceLoaded(fo.getName().getURI())) {
					buildSqlFromInputStream(fo.getContent().getInputStream(), configuration);
					configuration.addLoadedResource(fo.getName().getURI());
				}
			} catch (FileSystemException e) {
				log.warn(e);
			}			
		}
	}
	
	
	
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
		} catch (FileNotFoundException e) {
			
		}		
		return "success";
	}

	public boolean fileDeleted(File file) {		
		if (configuration.isResourceLoaded(file.toURI().toString())) {
			configuration.removeUriNamespace(file.toURI().toString(), true);
			configuration.removeLoadedResource(file.toURI().toString());
		}		
		//DeployedInfo di = 
		deployments.remove(file.toURI());
		// ToDo more ...
		return true;		
	}

	public void fileChanged(File file) {
		try {
			buildSqlFromInputStream(new FileInputStream(file), configuration);
			DeployedInfo di = deployments.get(file.toURI());
			di.deployedTime = System.currentTimeMillis();
			deployments.put(di.uri, di);			
			configuration.buildAllStatements();			
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
	

	// 파일이 유요한 형태의 파일인가를 확인하여 true/false 값을 리턴한다.	
	public boolean validateFile(File file) {
		boolean readable = file.canRead();
		boolean flag1 = StringUtils.isEmpty(suffix) ? true : file.getName().endsWith(suffix);
		boolean flag2 = StringUtils.isEmpty(prefix) ? true : file.getName().startsWith(prefix);
		boolean valid = readable && flag1 && flag2;
		
		if (!valid) {
			// if (log.isDebugEnabled())
			// log.info(MessageFormatter.format("011023",
			// file.getAbsolutePath()));
		}

		//if( log.isDebugEnabled() )
		//	log.debug(file.getPath() + "validate:" + valid);
		
		return valid;
	}

	/**
	 * Setter of the property <tt>configuration</tt>
	 * @param configuration  The configuration to set.
	 * @uml.property  name="configuration"
	 */
/*	public void setConfiguration(Configuration configuration) {
		this.configuration = configuration;
	}*/
	
}
