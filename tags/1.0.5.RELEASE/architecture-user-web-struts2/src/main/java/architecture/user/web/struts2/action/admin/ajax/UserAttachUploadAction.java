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

import architecture.common.model.factory.ModelTypeFactory;
import architecture.common.user.User;
import architecture.common.user.UserManager;
import architecture.common.user.UserNotFoundException;
import architecture.common.user.UserTemplate;
import architecture.ee.web.attachment.Attachment;
import architecture.ee.web.attachment.FileInfo;
import architecture.ee.web.struts2.action.UploadAttachmentAction;

public class UserAttachUploadAction extends UploadAttachmentAction {

	private Long userId = -1L;
	
	private User targetUser;
	
	private UserManager userManager ;

	public UserManager getUserManager() {
		return userManager;
	}
	
	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public void setUserManager(UserManager userManager) {
		this.userManager = userManager;
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
	
	public String execute() throws Exception {
		
		User user = getTargetUser();
		if( ! user.isAnonymous() )
		for( FileInfo f : getAttachmentFileInfos()){			
			Attachment attach = attachmentManager.createAttachment(ModelTypeFactory.getTypeIdFromCode("USER"), user.getUserId(), f.getName(), f.getContentType(), f.getFile());
			attachmentManager.saveAttachment(attach);
		}
		
		return success();
	}
}
