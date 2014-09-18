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

import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import org.apache.commons.lang.StringUtils;

import architecture.ee.exception.NotFoundException;
import architecture.ee.exception.SystemException;
import architecture.ee.web.attachment.Attachment;
import architecture.ee.web.attachment.AttachmentManager;
import architecture.ee.web.struts2.action.support.FrameworkActionSupport;

import com.opensymphony.xwork2.Preparable;

public class DownloadAttachmentActioin extends FrameworkActionSupport  implements Preparable {

	private Long attachmentId;
	
	private Attachment targetAttachment ;

	private int width = 0;
	
	private int height = 0;
	
	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	private AttachmentManager attachmentManager ;
	
	
	public void prepare() throws Exception {
		
	}

	public String getEncodedTargetAttachmentFileName() {
		try {
			return URLEncoder.encode(getTargetAttachmentFileName(), "UTF-8");
		} catch (UnsupportedEncodingException e) {
			return getTargetAttachmentFileName();
		}
	}

	public Long getAttachmentId() {
		return attachmentId;
	}

	public void setAttachmentId(Long attachmentId) {
		this.attachmentId = attachmentId;
	}

	public AttachmentManager getAttachmentManager() {
		return attachmentManager;
	}

	public void setAttachmentManager(AttachmentManager attachmentManager) {
		this.attachmentManager = attachmentManager;
	}

	protected boolean isThumbnailSupport( Attachment attachment ){
		boolean thumbnailSupport = false ;
		if( attachment != null && StringUtils.isNotEmpty( attachment.getContentType()) ){
			thumbnailSupport = StringUtils.startsWith(attachment.getContentType().toLowerCase(), "image");
		}
		return thumbnailSupport;
	}
	
	public Attachment getTargetAttachment() {
		try {
			
			if( targetAttachment == null)
				targetAttachment = attachmentManager.getAttachment(attachmentId);
				
			if( width > 0 && height > 0 && isThumbnailSupport(targetAttachment) ){
				attachmentManager.getAttachmentImageThumbnailInputStream(targetAttachment, width, height);	
			}
			log.debug( "ThumbnailSize:" + targetAttachment.getThumbnailSize());	
			
			return targetAttachment;
		} catch (NotFoundException e) {
			throw new SystemException(e);
		}		
	}
	
	public InputStream getTargetAttachmentInputStream() {
		Attachment attachmentToUse = getTargetAttachment();
		if( width > 0 && height > 0 && attachmentToUse.getThumbnailSize() > 0)
			return attachmentManager.getAttachmentImageThumbnailInputStream(attachmentToUse, width, height);
		else
			return attachmentManager.getAttachmentInputStream(attachmentToUse);
	}
	
	public String getTargetAttachmentContentType(){
		if( width > 0 && height > 0 && getTargetAttachment().getThumbnailSize() > 0 )
		{
			return getTargetAttachment().getThumbnailContentType();
		}else{
			return getTargetAttachment().getContentType();
		}
	}

	public String getTargetAttachmentFileName(){
		return getTargetAttachment().getName();
	}	
	
	public int getTargetAttachmentContentLength(){
		if( width > 0 && height > 0 && getTargetAttachment().getThumbnailSize() > 0 )
		{
			return getTargetAttachment().getThumbnailSize();
		}else{
			return getTargetAttachment().getSize();
		}	
	}
	
	
	public String execute() throws Exception {
        return SUCCESS;
    }
	
	
}