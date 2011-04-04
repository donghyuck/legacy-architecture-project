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
package architecture.ee.spring.jdbc.support;

import java.io.InputStream;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.vfs.FileChangeEvent;
import org.apache.commons.vfs.FileListener;
import org.apache.commons.vfs.FileObject;

import architecture.ee.jdbc.query.builder.xml.XmlSqlBuilder;
import architecture.ee.jdbc.query.factory.Configuration;
import architecture.ee.jdbc.query.factory.ConfigurationFactory;

public class SqlFileListener implements FileListener {

	private Configuration configuration;
	private Log log = LogFactory.getLog(getClass());
		
	public SqlFileListener() {
		this.configuration = ConfigurationFactory.getConfiguration();
	}
	
	public SqlFileListener(Configuration configuration) {
		this.configuration = configuration;
	}

	public void fileChanged(FileChangeEvent event) throws Exception {
		// update
		FileObject fo = event.getFile();
		String uri = fo.getName().getURI();				
		log.debug("file changed:" + uri );
		
		configuration.removeUriNamespace(uri, true);
		buildSqlFromInputStream(fo.getContent().getInputStream(), configuration, uri);
		
	}

	public void fileCreated(FileChangeEvent event) throws Exception {
	    // added
		FileObject fo = event.getFile();
		String uri = fo.getName().getURI();				
		log.debug("file added:" + uri );		
		configuration.removeUriNamespace(uri, true);		
		buildSqlFromInputStream(fo.getContent().getInputStream(), configuration, uri);
		configuration.addLoadedResource(fo.getName().getPath());
	}

	public void fileDeleted(FileChangeEvent event) throws Exception {
	    // removed
		FileObject fo = event.getFile();
		String uri = fo.getName().getURI();		
		
		log.debug("file removed:" + uri );		
		if(configuration.isResourceLoaded(uri)){		
			configuration.removeUriNamespace(uri, true);
			configuration.removeLoadedResource(uri);
		}
	}
	
	protected void buildSqlFromInputStream(InputStream inputStream, Configuration configuration, String uri){
		XmlSqlBuilder builder = new XmlSqlBuilder(inputStream, configuration, uri);
		builder.build();
		
		//String domain = configuration.getUriNamespace(uri);
		
	}

}