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
package architecture.ee.web.community.struts2.action.admin.ajax;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.List;

import architecture.common.user.Company;
import architecture.common.user.CompanyManager;
import architecture.common.user.CompanyNotFoundException;
import architecture.ee.exception.NotFoundException;
import architecture.ee.exception.SystemException;
import architecture.ee.web.attachment.FileInfo;
import architecture.ee.web.attachment.Image;
import architecture.ee.web.community.logo.DefaultLogoImage;
import architecture.ee.web.logo.LogoImage;
import architecture.ee.web.logo.LogoImageNotFoundException;
import architecture.ee.web.logo.LogoManager;
import architecture.ee.web.site.WebSite;
import architecture.ee.web.site.WebSiteManager;
import architecture.ee.web.site.WebSiteNotFoundException;
import architecture.ee.web.struts2.action.support.UploadFileAction;

public class LogoManagementAction extends UploadFileAction {

	private Long targetCompanyId;
	
	private Long targetWebSiteId;
	
	private int objectType;
	
	private Long objectId;
	
	private Long targetLogoImageId;

	private WebSiteManager webSiteManager;
	
	private CompanyManager companyManager;
	
	private LogoManager logoManager ;
	
	private WebSite targetWebSite;
	
	private Company targetCompany;
	
	private LogoImage targetLogoImage;
	
	private InputStream imputStream = null;
	
	
	public LogoManagementAction() {
		this.objectType = 0 ;
		this.objectId = -1L;
		this.targetCompanyId = -1L;
		this.targetWebSiteId = -1L;
		this.targetLogoImageId = -1L;
	}
	
	
	
	/**
	 * @return targetLogoImageId
	 */
	public Long getLogoId() {
		return targetLogoImageId;
	}

	/**
	 * @param targetLogoImageId 설정할 targetLogoImageId
	 */
	public void setLogoId(Long logoId) {
		this.targetLogoImageId = logoId;
	}
	/**
	 * @return targetCompanyId
	 */
	public Long getTargetCompanyId() {
		return targetCompanyId;
	}

	/**
	 * @param targetCompanyId 설정할 targetCompanyId
	 */
	public void setTargetCompanyId(Long targetCompanyId) {
		this.targetCompanyId = targetCompanyId;
	}

	/**
	 * @return targetWebSiteId
	 */
	public Long getTargetWebSiteId() {
		return targetWebSiteId;
	}

	/**
	 * @param targetWebSiteId 설정할 targetWebSiteId
	 */
	public void setTargetWebSiteId(Long targetWebSiteId) {
		this.targetWebSiteId = targetWebSiteId;
	}

	/**
	 * @return objectType
	 */
	public int getObjectType() {
		return objectType;
	}

	/**
	 * @param objectType 설정할 objectType
	 */
	public void setObjectType(int objectType) {
		this.objectType = objectType;
	}

	/**
	 * @return objectId
	 */
	public Long getObjectId() {
		return objectId;
	}

	/**
	 * @param objectId 설정할 objectId
	 */
	public void setObjectId(Long objectId) {
		this.objectId = objectId;
	}

	/**
	 * @return logoManager
	 */
	public LogoManager getLogoManager() {
		return logoManager;
	}

	/**
	 * @param logoManager 설정할 logoManager
	 */
	public void setLogoManager(LogoManager logoManager) {
		this.logoManager = logoManager;
	}

	/**
	 * @return webSiteManager
	 */
	public WebSiteManager getWebSiteManager() {
		return webSiteManager;
	}

	/**
	 * @param webSiteManager 설정할 webSiteManager
	 */
	public void setWebSiteManager(WebSiteManager webSiteManager) {
		this.webSiteManager = webSiteManager;
	}

	/**
	 * @return companyManager
	 */
	public CompanyManager getCompanyManager() {
		return companyManager;
	}

	/**
	 * @param companyManager 설정할 companyManager
	 */
	public void setCompanyManager(CompanyManager companyManager) {
		this.companyManager = companyManager;
	}

	public Company getTargetCompany() throws CompanyNotFoundException{
		if( targetCompany== null )	{
			if( targetCompanyId < 1 )
				targetCompany = getCompany();
			else
				this.targetCompany = companyManager.getCompany(targetCompanyId);
		}
		return targetCompany;
	}
	
	public WebSite getTargetWebSite() throws WebSiteNotFoundException{
		if( targetWebSite == null )	{
			if( targetWebSiteId < 1 )
				targetWebSite = getWebSite();			
			else
				this.targetWebSite = webSiteManager.getWebSiteById(targetWebSiteId);
		}
		return targetWebSite;
	}
	
	public int getTargetLogoImageCount(){
		if( objectType > 0 && objectId > 0 ){
			return logoManager.getLogoImageCount(objectType, objectId);
		}
		return 0;
	}
	
	public LogoImage getTargetPrimaryLogoImage() throws LogoImageNotFoundException{
		return logoManager.getPrimaryLogoImage(objectType, objectId);
	}
	
	public List<LogoImage> getTargetLogoImages(){
		if( objectType > 0 && objectId > 0 ){
			return logoManager.getLogoImages(objectType, objectId);
		}		
		return Collections.EMPTY_LIST;
	}

	public LogoImage getTargetLogoImage() {
		try {
			if( targetLogoImage == null && targetLogoImageId > 0){
				targetLogoImage = logoManager.getLogoImageById(targetLogoImageId);	
			}
			return targetLogoImage;
		} catch (Exception e) {
			throw new SystemException(e);
		}		
	}
	
	
	public InputStream getTargetImageInputStream() throws IOException {
		LogoImage imageToUse = getTargetLogoImage();	
		if( imputStream == null )
			imputStream = logoManager.getImageInputStream(imageToUse);			
		return imputStream;
	}
	
	public String getTargetImageContentType(){
		return getTargetLogoImage().getImageContentType();
	}

	public String getTargetImageFileName(){
		return getTargetLogoImage().getFilename();
	}	
	
	public int getTargetImageContentLength(){
		return getTargetLogoImage().getImageSize();	
	}
	
	public String addLogoImage () throws Exception {			
		List<FileInfo> uploadFilesToUse = getUploadedFiles();
		if( uploadFilesToUse.size() > 0 && (objectType > 0 && objectId > 0 )){
			for( FileInfo fi : uploadFilesToUse ){				
				
				DefaultLogoImage logo = new DefaultLogoImage();
				logo.setFilename(fi.getName());
				logo.setImageContentType(fi.getContentType());
				logo.setObjectType(objectType);
				logo.setObjectId(objectId);				
				logo.setPrimary(true);
				
				logoManager.addLogoImage(logo, fi.getFile());			
			}
		}
		return success();		
	}
	public String execute() throws Exception {		
		return success();
	}
}
