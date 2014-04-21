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
package architecture.user.web.struts2.action.ajax;

import java.util.List;

import architecture.common.model.factory.ModelTypeFactory;
import architecture.common.user.User;
import architecture.ee.exception.NotFoundException;
import architecture.ee.web.attachment.Attachment;
import architecture.ee.web.attachment.AttachmentManager;
import architecture.ee.web.struts2.action.support.FrameworkActionSupport;

public class UserAttachementAction extends  FrameworkActionSupport {

	private Long attachmentId = -1L;
	
    public Long getAttachmentId() {
		return attachmentId;
	}

	public void setAttachmentId(Long attachmentId) {
		this.attachmentId = attachmentId;
	}

	private AttachmentManager attachmentManager;


	public AttachmentManager getAttachmentManager() {
		return attachmentManager;
	}

	public void setAttachmentManager(AttachmentManager attachmentManager) {
		this.attachmentManager = attachmentManager;
	}
	
    public List<Attachment> getCurrentUserAttachments(){    	
    	return attachmentManager.getAttachments(ModelTypeFactory.getTypeIdFromCode("USER"), getCurrentUser().getUserId());
    }
	
	
	public User getCurrentUser(){
		User targetUser = super.getUser();
		return targetUser;
	}
	
	
    public String execute() throws Exception {  
        return success();
    }  
    
    public Attachment getTargetAttachment() throws NotFoundException{
    	return attachmentManager.getAttachment(attachmentId);
    } 
    
    public List<Attachment> getUserAttachments(){    	
    	return attachmentManager.getAttachments(ModelTypeFactory.getTypeIdFromCode("USER"), getCurrentUser().getUserId());
    }
    
	public String deleteUserAttachment() throws Exception {  		
		User user = getCurrentUser();
		Attachment attachment = getTargetAttachment();		
		if( attachment.getAttachmentId() > 0 && attachment.getObjectId() == user.getUserId() )
			attachmentManager.removeAttachment(attachment);			
        return success();
    } 
	
	public String deleteUserAttachments() throws Exception {  		
		User user = getCurrentUser();
		List<Attachment> attachments = getUserAttachments();		
		for( Attachment attachment : attachments ){
			if( attachment.getAttachmentId() > 0 && attachment.getObjectId() == user.getUserId() )
				attachmentManager.removeAttachment(attachment);			
		}
        return success();
    } 
	
}
