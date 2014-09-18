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
package architecture.ee.web.struts2.action.support;

import java.io.File;
import java.util.Date;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.ResourceLoader;

public class FinderActionSupport extends FrameworkActionSupport  implements ResourceLoaderAware {

	private ResourceLoader resourceLoader = new DefaultResourceLoader();

	/**
	 * @return resourceLoader
	 */
	public ResourceLoader getResourceLoader() {
		return resourceLoader;
	}

	/**
	 * @param resourceLoader 설정할 resourceLoader
	 */
	public void setResourceLoader(ResourceLoader resourceLoader) {
		this.resourceLoader = resourceLoader;
	}

	
	public String execute() throws Exception {
		return success();
	}    
    

    public static class FileInfo {
    	private boolean customized ;
    	private boolean directory ;    	
    	private String path;
    	private String relativePath;
    	private String absolutePath;    	
    	private String name;
    	private long size;
    	private Date lastModifiedDate;
		/**
		 * 
		 */
		public FileInfo(File file) {		
			this.customized = false;
			this.lastModifiedDate = new Date(file.lastModified());
			this.name = file.getName();
			this.path = file.getPath();
			this.absolutePath = file.getAbsolutePath();
			this.directory = file.isDirectory();
			if( this.directory ){
				this.size = FileUtils.sizeOfDirectory(file);
			}else{
				this.size = FileUtils.sizeOf(file);
			}			
		}

		public FileInfo(File root, File file) {		
			this.lastModifiedDate = new Date(file.lastModified());
			this.name = file.getName();
			this.path = StringUtils.removeStart(file.getAbsolutePath(), root.getAbsolutePath());			
			this.absolutePath = file.getAbsolutePath();
			this.directory = file.isDirectory();
			if( this.directory ){
				this.size = FileUtils.sizeOfDirectory(file);
			}else{
				this.size = FileUtils.sizeOf(file);
			}

			this.customized = false;
		}

		public FileInfo(File root, File file, boolean customized) {		
			this(root, file);
			this.customized = customized;
		}
		
		/**
		 * @return customized
		 */
		public boolean isCustomized() {
			return customized;
		}

		/**
		 * @param customized 설정할 customized
		 */
		public void setCustomized(boolean customized) {
			this.customized = customized;
		}

		/**
		 * @return directory
		 */
		public boolean isDirectory() {
			return directory;
		}
		/**
		 * @param directory 설정할 directory
		 */
		public void setDirectory(boolean directory) {
			this.directory = directory;
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
		/**
		 * @return absolutePath
		 */
		public String getAbsolutePath() {
			return absolutePath;
		}
		/**
		 * @param absolutePath 설정할 absolutePath
		 */
		public void setAbsolutePath(String absolutePath) {
			this.absolutePath = absolutePath;
		}
		/**
		 * @return name
		 */
		public String getName() {
			return name;
		}
		/**
		 * @param name 설정할 name
		 */
		public void setName(String name) {
			this.name = name;
		}
		/**
		 * @return size
		 */
		public long getSize() {
			return size;
		}
		/**
		 * @param size 설정할 size
		 */
		public void setSize(long size) {
			this.size = size;
		}
		/**
		 * @return lastModifiedDate
		 */
		public Date getLastModifiedDate() {
			return lastModifiedDate;
		}
		/**
		 * @param lastModifiedDate 설정할 lastModifiedDate
		 */
		public void setLastModifiedDate(Date lastModifiedDate) {
			this.lastModifiedDate = lastModifiedDate;
		}    	
    	
    }
    
}
