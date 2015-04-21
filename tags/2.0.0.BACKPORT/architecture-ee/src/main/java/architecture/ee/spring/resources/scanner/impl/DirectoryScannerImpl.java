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
package architecture.ee.spring.resources.scanner.impl;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.Collections;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.vfs2.FileObject;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;

import architecture.common.scanner.DirectoryListener;
import architecture.common.scanner.URLDirectoryScanner;
import architecture.common.util.StringUtils;
import architecture.common.util.vfs.VFSUtils;
import architecture.ee.component.admin.AdminHelper;
import architecture.ee.jdbc.sqlquery.factory.SqlQueryFactory;
import architecture.ee.spring.resources.scanner.DirectoryScanner;
import architecture.ee.util.ApplicationConstants;

/**
 * 
 * 
 * 
 * @author  <a href="mailto:donghyuck.son@gmail.com">Donghyuck Son </a>
 *
 */
public class DirectoryScannerImpl implements InitializingBean, DisposableBean, DirectoryScanner {


	private URLDirectoryScanner scanner;
	
	private List<String> resourceLocations;
	
	private boolean fastDeploy = false;
	
	private Log log = LogFactory.getLog(getClass());
	
	public DirectoryScannerImpl() {				
		this.resourceLocations = Collections.emptyList();		
		try {
			this.scanner = createURLDirectoryScanner(true);
		} catch (Exception e){}
	}
	
	public void setDirectoryListenerList(List<DirectoryListener> directoryListeners) {
		for(DirectoryListener listener : (List<DirectoryListener>)directoryListeners){
			scanner.removeDirectoryListener(listener);			
			scanner.addDirectoryListener(listener);
		}
	}

	private URLDirectoryScanner createURLDirectoryScanner(boolean recursive) throws Exception {
		URLDirectoryScanner scanner = new URLDirectoryScanner();
		scanner.setRecursiveSearch(true);
		scanner.setScanEnabled(true);
		scanner.create();
		return scanner;
	}
		
	public void setFastDeploy(boolean fastDeploy) {
		this.fastDeploy = fastDeploy;
	}

	public List<String> getResourceLocations() {
		return resourceLocations;
	}

	public void setResourceLocations(List<String> resourceLocations) {
		this.resourceLocations = resourceLocations;
	}
			
	public void setRecursiveSearch(boolean recurse){
		scanner.setRecursiveSearch(recurse);
	}
	
	public void setPollIntervalMillis(int pollIntervalMillis){
		scanner.setPollIntervalMillis(pollIntervalMillis);
	}

	public void addDirectoryListener(DirectoryListener fileListener) {
		scanner.addDirectoryListener(fileListener);
	}

	public void addScanDir(String path) {	
		scanner.addScanDir(path);
	}

	public DirectoryListener[] getDirectoryListeners() {
		return scanner.getDirectoryListeners();
	}

	public void removeDirectoryListener(DirectoryListener fileListener) {
		scanner.removeDirectoryListener(fileListener);
	}

	public void removeScanURL(URL url) {
		scanner.removeScanURL(url);
	}

	public void addScanURI(URI uri) {
		try {
			scanner.addScanURL(uri.toURL());
		} catch (MalformedURLException e) {
		}
	}

	public void addScanURL(URL url) {
		scanner.addScanURL(url);
	}

	public void removeScanURI(URI uri) {
		try {
			scanner.removeScanURL(uri.toURL());
		} catch (MalformedURLException e) {
		}
	}
	
	public void destroy() throws Exception {
		if(scanner.isStarted())
			scanner.doStop();
		scanner.destroy();		
	}


	public void afterPropertiesSet() throws Exception {		
		loadResourceLocations();		
		if(!scanner.isStarted())
		    scanner.start();
	}
	
	protected void loadResourceLocations() {
		try {
			for (String path : resourceLocations) {	
				if( path.startsWith("${") && path.endsWith("}")){
					
					int start = path.indexOf('{') + 1 ;
					int end = path.indexOf('}');
					
					String key = path.substring( start, end ).trim();	
					
					if( key.equals(ApplicationConstants.RESOURCE_SQL_LOCATION_PROP_NAME)){
						
						path = AdminHelper.getRepository().getSetupApplicationProperties().get(ApplicationConstants.RESOURCE_SQL_LOCATION_PROP_NAME);						
						if( StringUtils.isEmpty(path))
							path = AdminHelper.getRepository().getURI("sql");
						
					}else {
						path = AdminHelper.getRepository().getSetupApplicationProperties().get(key);
					}
					log.debug( key + "=" + path );
				}				
				
				FileObject fo = VFSUtils.resolveFile(path);
				if(fo.exists()){							
					URL url = fo.getURL();
					url.openConnection();
					if(fastDeploy){
						if(log.isDebugEnabled()){
							log.debug("Fast deploy : " + url );							
							SqlQueryFactory builder = null;
							for(DirectoryListener listener : scanner.getDirectoryListeners()){
								if(listener instanceof SqlQueryFactory ){									
									builder = (SqlQueryFactory)listener;	
									break;
								}
							}
							
							File file = new File(url.getFile());
							fastDeploy(file, builder);							
						}
					}
					scanner.addScanURL(url);
				}
			}
			
		} catch (Exception e) { }
	}		
	
	public void fastDeploy(File file, SqlQueryFactory builder){		
		if( file.isDirectory()){		
			for ( File f : FileUtils.listFiles(file, new String[]{"xml"} , true) ){
				
				boolean valid = builder.validateFile(f);
				StringBuilder sb = new StringBuilder();
				sb.append("deploy : ");
				sb.append( f.getAbsolutePath() ) ;
				sb.append( " , valid = " );
				sb.append( valid );
				log.debug(
					sb.toString()
				);
				
				if(valid){
					try {					
						builder.fileCreated(file);					
					} catch (Throwable e) {
						log.error(e);
					}
				}	
			}
		}
	}	
	
}