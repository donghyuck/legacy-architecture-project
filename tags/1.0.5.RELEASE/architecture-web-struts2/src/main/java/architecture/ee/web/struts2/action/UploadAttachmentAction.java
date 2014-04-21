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
package architecture.ee.web.struts2.action;

import java.io.File;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.dispatcher.multipart.MultiPartRequestWrapper;

import architecture.ee.web.attachment.Attachment;
import architecture.ee.web.attachment.AttachmentManager;
import architecture.ee.web.attachment.FileInfo;
import architecture.ee.web.struts2.action.support.FrameworkActionSupport;

import com.opensymphony.xwork2.Preparable;

public class UploadAttachmentAction extends FrameworkActionSupport  implements Preparable {
	 
	private Integer objectType = 0;
	 
	 private Long objectId = -1L;
	 
	private List<FileInfo> attachments = new ArrayList<FileInfo>(); 
	
	protected AttachmentManager attachmentManager;
	
	private Attachment attachement ;
	
	public AttachmentManager getAttachmentManager() {
		return attachmentManager;
	}

	public void setAttachmentManager(AttachmentManager attachmentManager) {
		this.attachmentManager = attachmentManager;
	}
	
	protected List<FileInfo> getAttachmentFileInfos() {
		return attachments;
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
		}		
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
	

	public Attachment getTargetAttachment(){
		return attachement;
	}
	
	
		
	/**
	 * @return objectType
	 */
	public Integer getObjectType() {
		return objectType;
	}

	/**
	 * @param objectType 설정할 objectType
	 */
	public void setObjectType(Integer objectType) {
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
	 * @return attachments
	 */
	public List<FileInfo> getAttachments() {
		return attachments;
	}

	/**
	 * @param attachments 설정할 attachments
	 */
	public void setAttachments(List<FileInfo> attachments) {
		this.attachments = attachments;
	}

	/**
	 * @return attachement
	 */
	public Attachment getAttachement() {
		return attachement;
	}

	/**
	 * @param attachement 설정할 attachement
	 */
	public void setAttachement(Attachment attachement) {
		this.attachement = attachement;
	}

	public String execute() throws Exception {		
		for( FileInfo f : getAttachmentFileInfos()){	
			Attachment attach = attachmentManager.createAttachment(objectType, getObjectId() , f.getName(), f.getContentType(), f.getFile());
			this.attachement = attachmentManager.saveAttachment(attach);
		}
		return success();
	}
}
