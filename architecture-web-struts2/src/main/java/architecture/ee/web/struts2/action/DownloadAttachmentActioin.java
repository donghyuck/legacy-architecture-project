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

import architecture.ee.exception.NotFoundException;
import architecture.ee.exception.SystemException;
import architecture.ee.web.attachment.Attachment;
import architecture.ee.web.attachment.AttachmentManager;
import architecture.ee.web.struts2.action.support.FrameworkActionSupport;

import com.opensymphony.xwork2.Preparable;

public class DownloadAttachmentActioin extends FrameworkActionSupport  implements Preparable {

	private Long attachmentId;

	private AttachmentManager attachmentManager ;
	
	public void prepare() throws Exception {
		
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



	public Attachment getTargetAttachment() {
		try {
			return attachmentManager.getAttachment(attachmentId);
		} catch (NotFoundException e) {
			throw new SystemException(e);
		}		
	}
	
	public InputStream getTargetAttachmentInputStream() {
		return attachmentManager.getAttachmentInputStream(getTargetAttachment());
	}
	
	public String getTargetAttachmentContentType(){
		return getTargetAttachment().getContentType();
	}

	public String getTargetAttachmentFileName(){
		return getTargetAttachment().getName();
	}	
	
	public int getTargetAttachmentContentLength(){
		return getTargetAttachment().getSize();
	}
	
}
