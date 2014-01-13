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
package architecture.user.web.struts2.action.admin.ajax;

import java.util.List;

import architecture.common.model.factory.ModelTypeFactory;
import architecture.common.user.User;
import architecture.common.user.UserManager;
import architecture.common.user.UserNotFoundException;
import architecture.common.user.UserTemplate;
import architecture.ee.exception.NotFoundException;
import architecture.ee.web.attachment.Attachment;
import architecture.ee.web.attachment.AttachmentManager;
import architecture.ee.web.struts2.action.support.FrameworkActionSupport;

public class AttachmentManagementAction extends FrameworkActionSupport {

    private int pageSize = 0 ;
    
    private int startIndex = 0 ;  
    
	private User targetUser;

	private Long userId;
	
	private Long attachmentId = -1L;
    
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

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
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
    
    
    public Attachment getTargetAttachment() throws NotFoundException{
    	return attachmentManager.getAttachment(attachmentId);
    } 
    
    public List<Attachment> getTargetUserAttachments(){    	
    	return attachmentManager.getAttachments(ModelTypeFactory.getTypeIdFromCode("USER"), getTargetUser().getUserId());
    }

	public User getTargetUser() {
		if (userId == null)
			log.warn("Edit profile for unspecified user.");
		if(targetUser == null){
			try {
				targetUser = userManager.getUser(userId.longValue());
			} catch (UserNotFoundException e) {
				log.warn((new StringBuilder()).append("Could not load user object for id: ").append(userId).toString());
				return null;
			}
		}
		return new UserTemplate(targetUser);
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
    
}
