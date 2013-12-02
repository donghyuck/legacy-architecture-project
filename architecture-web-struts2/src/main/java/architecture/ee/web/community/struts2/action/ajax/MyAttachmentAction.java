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
package architecture.ee.web.community.struts2.action.ajax;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.FileUtils;
import org.apache.struts2.dispatcher.multipart.MultiPartRequestWrapper;

import architecture.common.user.User;
import architecture.common.user.UserManager;
import architecture.ee.exception.NotFoundException;
import architecture.ee.web.attachment.Attachment;
import architecture.ee.web.attachment.AttachmentManager;
import architecture.ee.web.attachment.FileInfo;
import architecture.ee.web.struts2.action.support.FrameworkActionSupport;
import architecture.ee.web.util.ParamUtils;

import com.opensymphony.xwork2.Preparable;

public class MyAttachmentAction extends FrameworkActionSupport implements Preparable {

	private int DEFAULT_OBJEDT_TYPE = 2 ;

    private int pageSize = 0 ;
    
    private int startIndex = 0 ;  
    
	private User targetUser;

	private List<FileInfo> attachments = new ArrayList<FileInfo>(); 
	
	private Long attachmentId = -1L;
	
	private Attachment targetAttachement ;
	
    private UserManager userManager ;
    
    private AttachmentManager attachmentManager;

	public AttachmentManager getAttachmentManager() {
		return attachmentManager;
	}

	public void setAttachmentManager(AttachmentManager attachmentManager) {
		this.attachmentManager = attachmentManager;
	}
    

	public UserManager getUserManager() {
		return userManager;
	}

	public void setUserManager(UserManager userManager) {
		this.userManager = userManager;
	}

	
	public Long getAttachmentId() {
		return attachmentId;
	}

	public void setAttachmentId(Long attachmentId) {
		this.attachmentId = attachmentId;
	}

	/**
	 * @param targetUser 설정할 targetUser
	 */
	public void setTargetUser(User targetUser) {
		this.targetUser = targetUser;
	}

	public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getStartIndex() {
        return startIndex;
    }

    public void setStartIndex(int startIndex) {
        this.startIndex = startIndex;
    }

	protected boolean isMultiPart() {
		HttpServletRequest request = getRequest();
		if ( request instanceof MultiPartRequestWrapper ) 
			return true;
		else
			return false;		
	}
	
	protected MultiPartRequestWrapper getMultiPartRequestWrapper(){
		HttpServletRequest request = getRequest();
		MultiPartRequestWrapper multiWrapper = (MultiPartRequestWrapper) request;			
		return multiWrapper;		
	}

	private static boolean isNonEmpty(Object[] objArray) {
		boolean result = false;
		for (int index = 0; index < objArray.length && !result; index++) {
			if (objArray[index] != null) {
				result = true;
			}
		}
		return result;
	}
	public void prepare() throws Exception {		
		if ( isMultiPart() ) {			
			MultiPartRequestWrapper multiWrapper = getMultiPartRequestWrapper();			
			Enumeration<String> fileParameterNames = multiWrapper.getFileParameterNames();			
			while (fileParameterNames != null && fileParameterNames.hasMoreElements()) {				
				String inputName = (String) fileParameterNames.nextElement();				
				String[] contentTypes = multiWrapper.getContentTypes(inputName);				
				if (isNonEmpty(contentTypes)) {					
					String[] fileNames = multiWrapper.getFileNames(inputName);
					if (isNonEmpty(fileNames)) {						
						File[] files = multiWrapper.getFiles(inputName);						
						if (files != null && files.length > 0) {
							for (int index = 0; index < files.length; index++) {												
								File uploadFile = files[index];
								String uploadFilename =  fileNames[index];
								String uploadContentType = contentTypes[index];											
								FileInfo f = new FileInfo( uploadFilename,  uploadContentType, uploadFile );
								f.setFileParameterName(inputName);
								attachments.add(f);
							}
						}
					}
				}				
			}			
			//this.attachmentId = ParamUtils.getLongParameter(multiWrapper, "attachmentId", -1L);
		}		
	}
	
    public int getTotalTargetAttachmentCount() {    	
    	return attachmentManager.getTotalAttachmentCount(DEFAULT_OBJEDT_TYPE, getUser().getUserId());
    }
    
    public Attachment getTargetAttachment() throws NotFoundException{
    	if( targetAttachement == null )
    	{
    		targetAttachement = attachmentManager.getAttachment(attachmentId);
    	}
    	return targetAttachement;
    } 
    
    public List<Attachment> getTargetAttachments(){    	
    	if( getUser().getUserId() < 1 )
    		return Collections.EMPTY_LIST;
    	
    	return attachmentManager.getAttachments(DEFAULT_OBJEDT_TYPE, getUser().getUserId());
    }

	public String deleteAttachment() throws Exception {  
		
		Attachment attachment = getTargetAttachment();
		if( attachment.getAttachmentId() > 0)
			attachmentManager.removeAttachment(attachment);		
		
        return success();
    }  
	
    public String execute() throws Exception {  
        return success();
    }  

	public String updateAttachment() throws Exception {				
		if( attachmentId > 0 ){
			Attachment attachment = this.getTargetAttachment();			
			for( FileInfo f : attachments ){	
				attachment.setName(f.getName());
				attachment.setContentType(f.getContentType());		
				attachment.setSize( (int) FileUtils.sizeOf(f.getFile()));
				try {
					attachment.setInputStream(FileUtils.openInputStream(f.getFile()));
				} catch (IOException e) {
					log.debug(e);
				}	
				attachmentManager.saveAttachment(attachment);
			}
		}
		return success();
	}
	
	public String saveAttachments() throws Exception {		
		for( FileInfo f : attachments ){	
			Attachment attach = attachmentManager.createAttachment(DEFAULT_OBJEDT_TYPE, getUser().getUserId() , f.getName(), f.getContentType(), f.getFile());
			this.targetAttachement = attachmentManager.saveAttachment(attach);
		}
		return success();
	}

}
