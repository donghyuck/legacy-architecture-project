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
package architecture.ee.web.community.spring.controller;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.FileUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.NativeWebRequest;

import architecture.common.user.CompanyManager;
import architecture.common.user.SecurityHelper;
import architecture.common.user.User;
import architecture.common.util.StringUtils;
import architecture.ee.exception.NotFoundException;

import architecture.ee.web.attachment.AttachmentManager;
import architecture.ee.web.attachment.ImageManager;
import architecture.ee.web.community.announce.AnnounceManager;
import architecture.ee.web.community.page.PageManager;
import architecture.ee.web.community.streams.PhotoStreamsManager;
import architecture.ee.web.logo.LogoManager;
import architecture.ee.web.navigator.DefaultMenu;
import architecture.ee.web.navigator.Menu;
import architecture.ee.web.navigator.MenuRepository;
import architecture.ee.web.site.WebSite;
import architecture.ee.web.site.WebSiteManager;
import architecture.ee.web.struts2.action.support.FinderActionSupport.FileInfo;
import architecture.ee.web.util.WebApplicatioinConstants;
import architecture.ee.web.util.WebSiteUtils;
import architecture.ee.util.ApplicationHelper;

@Controller ("secure-community-data-controller")
@RequestMapping("/secure/data")
public class SecureCommunityDataController {

	private static final Log log = LogFactory.getLog(CommunityDataController.class);

	@Inject
	@Qualifier("photoStreamsManager")
	private PhotoStreamsManager photoStreamsManager ;
	
	@Inject
	@Qualifier("imageManager")
	private ImageManager imageManager ;
	
	@Inject
	@Qualifier("attachmentManager")
	private AttachmentManager attachmentManager;

	@Inject
	@Qualifier("announceManager")
	private AnnounceManager announceManager ;
	
	@Inject
	@Qualifier("pageManager")
	private PageManager pageManager;

	@Inject
	@Qualifier("menuRepository")
	private MenuRepository menuRepository ;

	@Inject
	@Qualifier("webSiteManager")
	private WebSiteManager webSiteManager;
	
	@Inject
	@Qualifier("companyManager")
	private CompanyManager companyManager;
	
	@Inject
	@Qualifier("logoManager")
	private LogoManager logoManager ;
	
	@Autowired
	private ResourceLoader resourceLoader;
	
	public SecureCommunityDataController() {
	}

	@RequestMapping(value="/template/list.json",method={RequestMethod.POST, RequestMethod.GET} )
	@ResponseBody
	public List<FileInfo>  getTemplateList(
			@RequestParam(value="siteId", defaultValue="0", required=false ) Long siteId,
			@RequestParam(value="path", defaultValue="", required=false ) String path,
			@RequestParam(value="customized", defaultValue="false", required=false ) boolean customized,
			NativeWebRequest request) throws NotFoundException {			
		
		
		WebSite webSite;		
		if( siteId > 0 )
			webSite = webSiteManager.getWebSiteById(siteId);
		else 
			webSite = WebSiteUtils.getWebSite(request.getNativeRequest(HttpServletRequest.class));
		
		boolean customizedToUse = customized && isCustomizedEnabled();
		
		Resource root = resourceLoader.getResource(getTemplateSrouceLocation(customizedToUse));
		List<FileInfo> list = new ArrayList<FileInfo>();
		try {
			File file = root.getFile();			
			if( StringUtils.isEmpty(path) ){
				for( File f : file.listFiles()){
					list.add(new FileInfo( file , f, customizedToUse));
				}			
			}else{
				File targetFile = resourceLoader.getResource( getTemplateSrouceLocation(customized) + path ).getFile();
				for( File f : targetFile.listFiles()){
					list.add(new FileInfo( file , f, customizedToUse));
				}				
			}
		} catch (IOException e) {
			log.error(e);
		}
		return list;
	}

    protected String getTemplateSrouceLocation(boolean customized){
    	if(customized)
    		return ApplicationHelper.getApplicationProperty("view.html.customize.source.location",  null );	
    	else
    		return ApplicationHelper.getApplicationProperty(WebApplicatioinConstants.VIEW_FREEMARKER_SOURCE_LOCATION, null);
    }
    
    protected boolean isCustomizedEnabled(){		
		return ApplicationHelper.getApplicationBooleanProperty("view.html.customize.enabled", false);
	}

	public static class FileInfo {
		private boolean customized;
		private boolean directory;
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
			if (this.directory) {
				this.size = FileUtils.sizeOfDirectory(file);
			} else {
				this.size = FileUtils.sizeOf(file);
			}
		}

		public FileInfo(File root, File file) {
			this.lastModifiedDate = new Date(file.lastModified());
			this.name = file.getName();
			this.path = StringUtils.removeStart(file.getAbsolutePath(),
					root.getAbsolutePath());
			this.absolutePath = file.getAbsolutePath();
			this.directory = file.isDirectory();
			if (this.directory) {
				this.size = FileUtils.sizeOfDirectory(file);
			} else {
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
		 * @param customized
		 *            설정할 customized
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
		 * @param directory
		 *            설정할 directory
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
		 * @param path
		 *            설정할 path
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
		 * @param absolutePath
		 *            설정할 absolutePath
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
		 * @param name
		 *            설정할 name
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
		 * @param size
		 *            설정할 size
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
		 * @param lastModifiedDate
		 *            설정할 lastModifiedDate
		 */
		public void setLastModifiedDate(Date lastModifiedDate) {
			this.lastModifiedDate = lastModifiedDate;
		}

	}
	
	
	@RequestMapping(value="/website/get.json",method={RequestMethod.POST, RequestMethod.GET} )
	@ResponseBody
	public WebSite  getPageList(
			@RequestParam(value="siteId", defaultValue="0", required=false ) Long siteId,
			NativeWebRequest request) throws NotFoundException {				
		WebSite webSite;
		
		if( siteId > 0 )
			webSite = webSiteManager.getWebSiteById(siteId);
		else 
			webSite = WebSiteUtils.getWebSite(request.getNativeRequest(HttpServletRequest.class));
		
		return webSite;
	}

	@RequestMapping(value="/menu/update.json",method={RequestMethod.POST} )
	@ResponseBody
	public Menu  updateMenu(
			@RequestBody DefaultMenu menu, 
			NativeWebRequest request) throws NotFoundException {	
		
		User user = SecurityHelper.getUser();		

		Menu menuToUse = menuRepository.getMenu(menu.getMenuId());
		menuToUse.setMenuData(menu.getMenuData());
		menuToUse.setName(menu.getName());
		menuToUse.setEnabled(menu.isEnabled());
		menuToUse.setTitle(menu.getTitle());
		menuToUse.setLoaction(menu.getLocation());		
		menuRepository.updateMenu(menuToUse);		
		return menuToUse;
	}
}
