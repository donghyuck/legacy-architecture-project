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
package architecture.ee.web.spring.controller;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

import net.sf.ehcache.Cache;

import org.apache.commons.io.FileUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import architecture.common.license.License;
import architecture.common.lifecycle.ApplicationProperties;
import architecture.common.lifecycle.ConfigService;
import architecture.common.lifecycle.SystemInformationService;
import architecture.common.model.json.CustomJsonDateDeserializer;
import architecture.common.model.json.CustomJsonDateSerializer;
import architecture.common.user.CompanyManager;
import architecture.common.user.SecurityHelper;
import architecture.common.user.User;
import architecture.common.util.StringUtils;
import architecture.ee.component.admin.AdminHelper;
import architecture.ee.exception.NotFoundException;
import architecture.ee.util.ApplicationConstants;
import architecture.ee.util.ApplicationHelper;
import architecture.ee.web.attachment.AttachmentManager;
import architecture.ee.web.attachment.ImageManager;
import architecture.ee.web.logo.LogoImage;
import architecture.ee.web.logo.LogoImageNotFoundException;
import architecture.ee.web.logo.LogoManager;
import architecture.ee.web.navigator.DefaultMenu;
import architecture.ee.web.navigator.Menu;
import architecture.ee.web.navigator.MenuRepository;
import architecture.ee.web.site.WebSite;
import architecture.ee.web.site.WebSiteManager;
import architecture.ee.web.spring.controller.SecureWebStageDataController.CacheStats;
import architecture.ee.web.spring.controller.WebDataController.ItemList;
import architecture.ee.web.util.WebApplicatioinConstants;
import architecture.ee.web.util.WebSiteUtils;
import architecture.ee.web.ws.Property;
import architecture.ee.web.ws.Result;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@Controller ("secure-web-mgmt-data-controller")
@RequestMapping("/secure/data")
public class SecureWebMgmtDataController {

	private static final Log log = LogFactory.getLog(SecureWebMgmtDataController.class);
	
	@Inject
	@Qualifier("systemInformationService")	
	private SystemInformationService systemInformationService ;	
	
	@Inject
	@Qualifier("imageManager")
	private ImageManager imageManager ;
	
	@Inject
	@Qualifier("attachmentManager")
	private AttachmentManager attachmentManager;

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

	@Inject
	@Qualifier("taskExecutor")
	private TaskExecutor taskExecutor;
	
	public SecureWebMgmtDataController() {
	}

	/**
	 * Logo
	 */
	@RequestMapping(value="/mgmt/logo/list.json",method={RequestMethod.POST, RequestMethod.GET} )
	@ResponseBody
	public ItemList  getLogoImageList(
			@RequestParam(value="objectType", defaultValue="0", required=true ) Integer objectType,
			@RequestParam(value="objectId", defaultValue="0", required=true ) Long objectId,
			NativeWebRequest request) throws NotFoundException {					
		
		int totalCount = logoManager.getLogoImageCount(objectType, objectId);
		return new ItemList(logoManager.getLogoImages(objectType, objectId), totalCount);
	}

	@RequestMapping(value="/mgmt/logo/set_primary.json",method={RequestMethod.POST, RequestMethod.GET} )
	@ResponseBody
	public Result setPrimaryLogoImage(
			@RequestParam(value="logoId", defaultValue="0", required=true ) Long logoId,
			NativeWebRequest request) throws LogoImageNotFoundException {					
		User user = SecurityHelper.getUser();			
		LogoImage oldLogoImage = logoManager.getLogoImageById(logoId);
		oldLogoImage.setPrimary(true);
		logoManager.updateLogoImage(oldLogoImage, null);
		return Result.newResult();
	}	

	@RequestMapping(value="/mgmt/logo/upload.json", method=RequestMethod.POST)
	@ResponseBody
	public Result  uploadLogoImage(
			@RequestParam(value="objectType", defaultValue="1", required=false ) Integer objectType,
			@RequestParam(value="objectId", defaultValue="0", required=false ) Long objectId, 
			MultipartHttpServletRequest request) throws NotFoundException, IOException {				
		
		User user = SecurityHelper.getUser();
		if( objectId == 0 ){
			if( objectType == 1 ){
				objectId = user.getCompanyId();			
			}else if ( objectType == 30){
				objectId = WebSiteUtils.getWebSite(request).getWebSiteId();
			}					
		}
		Iterator<String> names = request.getFileNames(); 
		while(names.hasNext()){
			String fileName = names.next();	
			log.debug(fileName);
			MultipartFile mpf = request.getFile(fileName);
			InputStream is = mpf.getInputStream();
			log.debug("file name: " + mpf.getOriginalFilename());
			log.debug("file size: " + mpf.getSize());
			log.debug("file type: " + mpf.getContentType());
			log.debug("file class: " + is.getClass().getName());
			
			LogoImage logo = logoManager.createLogoImage();
			logo.setFilename(mpf.getName());
			logo.setImageContentType(mpf.getContentType());
			logo.setImageSize((int)mpf.getSize());
			logo.setObjectType(objectType);
			logo.setObjectId(objectId);				
			logo.setPrimary(true);			
			
			logoManager.addLogoImage(logo, mpf.getInputStream());			
		}		
		return Result.newResult();
	}
	
	/**
	 * Template 
	 */
	
	@RequestMapping(value="/mgmt/template/list.json",method={RequestMethod.POST, RequestMethod.GET} )
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
		
		boolean customizedToUse = customized && isTemplateCustomizedEnabled();
		
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
	
	@RequestMapping(value="/mgmt/template/get.json",method={RequestMethod.POST, RequestMethod.GET} )
	@ResponseBody
	public FileInfo  getTemplateContent(
			@RequestParam(value="siteId", defaultValue="0", required=false ) Long siteId,
			@RequestParam(value="path", defaultValue="", required=false ) String path,
			@RequestParam(value="customized", defaultValue="false", required=false ) boolean customized,
			NativeWebRequest request) throws NotFoundException, IOException {					
		WebSite webSite;		
		if( siteId > 0 )
			webSite = webSiteManager.getWebSiteById(siteId);
		else 
			webSite = WebSiteUtils.getWebSite(request.getNativeRequest(HttpServletRequest.class));
		
		boolean customizedToUse = customized && isTemplateCustomizedEnabled();
		File file = resourceLoader.getResource(getTemplateSrouceLocation(customizedToUse) + path ).getFile();
		FileInfo fileInfo = new FileInfo( file );
		fileInfo.setCustomized(customizedToUse);
		fileInfo.setFileContent( file.isDirectory() ? "": FileUtils.readFileToString(file));
		
		return fileInfo;
	}	

	protected String getTemplateFileContent(String path, boolean customized){		
		try {
			File targetFile = resourceLoader.getResource(getTemplateSrouceLocation(customized) + path).getFile();
			if( !targetFile.isDirectory() ){
				return FileUtils.readFileToString(targetFile);				
			}
		} catch (IOException e) {			
		}
		return "";
	}

	protected String getTemplateSrouceLocation(boolean customized) {
		if (customized)
			return ApplicationHelper.getApplicationProperty("view.html.customize.source.location", null);
		else
			return ApplicationHelper.getApplicationProperty(WebApplicatioinConstants.VIEW_FREEMARKER_SOURCE_LOCATION, null);
	}

	protected boolean isTemplateCustomizedEnabled() {
		return ApplicationHelper.getApplicationBooleanProperty("view.html.customize.enabled", false);
	}
	
	/**
	 * Config 
	 */

	@RequestMapping(value="/config/license/get.json",method={RequestMethod.POST, RequestMethod.GET} )
	@ResponseBody
	public License getLicenseInfo(NativeWebRequest request) throws NotFoundException {				
		return systemInformationService.getLicenseInfo();
	}
	
	
	@RequestMapping(value="/config/setup/list.json",method={RequestMethod.POST, RequestMethod.GET} )
	@ResponseBody
	public List<Property>  getSetupProperties(NativeWebRequest request) throws NotFoundException {				
		ApplicationProperties props = ApplicationHelper.getRepository().getSetupApplicationProperties();
		Collection<String> names = props.getPropertyNames();		
    	List<Property> list = new ArrayList<Property>(names.size());
    	for( String name : names ){
    		Object value = props.get(name);
    		if( name.contains("password"))
    			value = "**********";
    		list.add( new Property(name, value) ) ;
    	}
    	return list;
	}

	
	@RequestMapping(value="/config/application/list.json",method={RequestMethod.POST, RequestMethod.GET} )
	@ResponseBody
	public List<Property>  getApplicationProperties(NativeWebRequest request) throws NotFoundException {				
    	ConfigService configService = ApplicationHelper.getConfigService();
    	List<String> names =  configService.getApplicationPropertyNames();
    	List<Property> list = new ArrayList<Property>(names.size());
    	for( String name : names){
    		String value = configService.getApplicationProperty(name);
    		if( name.contains("password"))
    			value = "**********";
    		list.add( new Property(name, value) ) ;
    	}
    	return list;
	}

	@RequestMapping(value="/config/application/update.json",method={RequestMethod.POST} )
	@ResponseBody
	public Result  updateApplicationProperties(@RequestBody Property[] items, NativeWebRequest request) throws NotFoundException {				
    	ConfigService configService = ApplicationHelper.getConfigService();    	
    	for( Property p : items){
    		configService.setApplicationProperty(p.getName(), (String)p.getValue());
    	}
    	return Result.newResult();
	}

	@RequestMapping(value="/config/application/delete.json",method={RequestMethod.POST} )
	@ResponseBody
	public Result  deleteApplicationProperties(@RequestBody Property[] items, NativeWebRequest request) throws NotFoundException {				
		ConfigService configService = ApplicationHelper.getConfigService();    	
		for( Property p : items){
			configService.deleteApplicationProperty(p.getName());
		}
		return Result.newResult();
	}

	/**
	 * Cache 
	 */
	
	@RequestMapping(value="/mgmt/cache/refresh.json",method={RequestMethod.POST, RequestMethod.GET} )
	@ResponseBody
	public CacheStats  refreshCache(@RequestParam(value="name", required=true ) String name, NativeWebRequest request) throws NotFoundException {		
		Cache cache = AdminHelper.getCacheManager().getCache(name);
		cache.removeAll();
		cache.clearStatistics();
		return new CacheStats(cache);
	}
	
	
	/**
	 * Navigator 
	 */
	
	/**
	 * navigator
	 * @param request
	 * @return
	 * @throws NotFoundException
	 */
	@RequestMapping(value="/mgmt/navigator/list.json",method={RequestMethod.POST, RequestMethod.GET} )
	@ResponseBody
	public ItemList  getMenuList(NativeWebRequest request) throws NotFoundException {				
		int totalCount = menuRepository.getTotalMenuCount();
		return new ItemList(menuRepository.getMenus(), totalCount);		
	}
	
	@RequestMapping(value="/mgmt/navigator/update.json",method={RequestMethod.POST} )
	@ResponseBody
	public Menu  updateMenu(@RequestBody DefaultMenu menu, NativeWebRequest request) throws NotFoundException {			
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
	
	/**
	 * Sql 
	 */		
	@RequestMapping(value="/mgmt/sql/list.json",method={RequestMethod.POST, RequestMethod.GET} )
	@ResponseBody
	public List<FileInfo>  getSqlList(
			@RequestParam(value="path", defaultValue="", required=false ) String path,
			NativeWebRequest request) throws NotFoundException {					
		
		List<FileInfo> list = new ArrayList<FileInfo>();		
		try {
			File file = getRootSqlFile();			
			if( StringUtils.isEmpty(path) ){
				for( File child : file.listFiles()){
					list.add(new FileInfo( file, child));
				}			
			}else{
				File file2 = new File( file.getAbsolutePath() + path );
				for( File child : file2.listFiles()){
					list.add(new FileInfo( file , child));
				}				
			}
		} catch (Exception e) {
			log.error(e);
		}		
		return list;
	}	

	@RequestMapping(value="/mgmt/sql/get.json",method={RequestMethod.POST, RequestMethod.GET} )
	@ResponseBody
	public FileInfo  getSql(
			@RequestParam(value="path", required=true ) String path,
			NativeWebRequest request) throws NotFoundException, IOException {	
		File file = new File( getRootSqlFile().getAbsolutePath() + path );
		FileInfo fileInfo = new FileInfo( file );
		fileInfo.setFileContent( file.isDirectory() ? "": FileUtils.readFileToString(file));		
		return fileInfo;
	}	
	
	
	protected Resource fileToResource( File file ){
		return new FileSystemResource(file);
	}
	
	protected File getRootSqlFile(){
    	return AdminHelper.getRepository().getFile("sql");
    }
    
    protected boolean isSqlCustomizedEnabled(){		
    	if( StringUtils.isNotEmpty( ApplicationHelper.getApplicationProperty(ApplicationConstants.RESOURCE_SQL_LOCATION_PROP_NAME, null ))){
    		return true;
    	}
    	return false;
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
		private String fileContent;
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
		 * @return fileContent
		 */
		public String getFileContent() {
			return fileContent;
		}

		/**
		 * @param fileContent 설정할 fileContent
		 */
		public void setFileContent(String fileContent) {
			this.fileContent = fileContent;
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
		@JsonSerialize(using = CustomJsonDateSerializer.class)	
		public Date getLastModifiedDate() {
			return lastModifiedDate;
		}

		/**
		 * @param lastModifiedDate
		 *            설정할 lastModifiedDate
		 */
		@JsonDeserialize(using = CustomJsonDateDeserializer.class)
		public void setLastModifiedDate(Date lastModifiedDate) {
			this.lastModifiedDate = lastModifiedDate;
		}

	}	
}
