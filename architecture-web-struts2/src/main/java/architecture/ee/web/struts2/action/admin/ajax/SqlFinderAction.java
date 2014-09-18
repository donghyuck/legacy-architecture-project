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
package architecture.ee.web.struts2.action.admin.ajax;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;

import architecture.ee.component.admin.AdminHelper;
import architecture.ee.util.ApplicationConstants;
import architecture.ee.web.struts2.action.support.FinderActionSupport;

public class SqlFinderAction extends FinderActionSupport{
	
	private String path = null ;	
	
	public SqlFinderAction()  {
		
	}
	
	/**
	 * @return path
	 */
	public String getPath() {
		return path;
	}


	/**
	 * @param path 설정할 path
	 */
	public void setPath(String path) {
		this.path = path;
	}

	public String getTargetFileContent(){		
		try {
			File targetFile = getTargetResource().getFile();
			if( !targetFile.isDirectory() ){
				return FileUtils.readFileToString(targetFile);				
			}
		} catch (IOException e) {		
			log.error(e);
		}
		return "";
	}

	public List<FileInfo> getTargetFiles(){
		Resource root = getTargetRootResource();	
		List<FileInfo> list = new ArrayList<FileInfo>();
		try {
			File file = root.getFile();			
			if( StringUtils.isEmpty(path) ){
				for( File f : file.listFiles()){
					list.add(new FileInfo( file , f));
				}			
			}else{
				File targetFile = getTargetResource().getFile();
				for( File f : targetFile.listFiles()){
					list.add(new FileInfo( file , f));
				}				
			}
		} catch (IOException e) {
			log.error(e);
		}
		return list;
	}
	
	public Resource getTargetResource(){
		log.debug(path);
		log.debug( getSqlFileLocation().getAbsolutePath()  );
		
		return new FileSystemResource( getSqlFileLocation().getAbsolutePath() +  this.path);
	}
	
	public Resource getTargetRootResource(){
		return new FileSystemResource(getSqlFileLocation());
	}
	
    public File getSqlFileLocation(){
    	return AdminHelper.getRepository().getFile("sql");
    }
    
    public boolean isCustomizedEnabled(){		
    	if( StringUtils.isNotEmpty( getApplicationProperty(ApplicationConstants.RESOURCE_SQL_LOCATION_PROP_NAME, null ))){
    		return true;
    	}
    	return false;
	}
         
}
