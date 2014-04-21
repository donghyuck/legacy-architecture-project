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

import architecture.common.model.factory.ModelTypeFactory;
import architecture.common.user.User;
import architecture.ee.web.attachment.Attachment;
import architecture.ee.web.attachment.FileInfo;
import architecture.ee.web.struts2.action.UploadAttachmentAction;

/**
 * 현재 로그인된 사용자에 파일을 업로드 한다.
 * 
 * @author donghyuck
 *
 */
public class UploadUserAttachmentAction extends UploadAttachmentAction  {

	private Attachment attachement ;
	
	public User getCurrentUser(){
		User targetUser = super.getUser();
		return targetUser;
	}
	
	public Attachment getTargetAttachment(){
		return attachement;
	}
		
	public String execute() throws Exception {		
		User user = getCurrentUser();
		if( ! user.isAnonymous() )
		for( FileInfo f : getAttachmentFileInfos()){	
			Attachment attach = attachmentManager.createAttachment(
				ModelTypeFactory.getTypeIdFromCode("USER"), 
				user.getUserId(), f.getName(), f.getContentType(), f.getFile());
			this.attachement = attachmentManager.saveAttachment(attach);
		}
		return success();
	}
}
