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

import java.io.File;
import java.io.FileInputStream;
import java.util.Map;

import architecture.common.model.factory.ModelTypeFactory;
import architecture.common.user.User;
import architecture.common.user.UserManager;
import architecture.common.user.UserNotFoundException;
import architecture.common.user.UserTemplate;
import architecture.ee.exception.NotFoundException;
import architecture.ee.web.attachment.Image;
import architecture.ee.web.attachment.impl.ImageImpl;
import architecture.ee.web.struts2.action.UploadImageAction;

/**
 * 사용자 이미지를 업로드하기 위한 액션...
 * 
 * @author donghyuck
 *
 */
public class UserImageUploadAction extends UploadImageAction  {

	private Long userId = -1L;
	
	private User targetUser;
	
	 private Long imageId = -1L; 
		
	private UserManager userManager ;
	
	private Image targetUserImage;
	
	public Long getImageId() {
		return imageId;
	}

	public void setImageId(Long imageId) {
		this.imageId = imageId;
	}

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
	
	public Image getTargetUserImage( ){
		try {	
			if( targetUserImage == null){
				targetUserImage = getImageManager().getImage(imageId);
			}
			return targetUserImage;
		} catch (NotFoundException e) {
			log.error(e);
			return null;
		}
	}
	
	public String execute() throws Exception {		
		
		User user = getTargetUser();
		Image imageToUse;
		if( this.imageId < 0  ){	
			File fileToUse = getUploadImage();			
			imageToUse = getImageManager().createImage(
				ModelTypeFactory.getTypeIdFromCode("USER"),
				user.getUserId(), 
				getUploadImageFileName(), 
				getUploadImageContentType(), 
				fileToUse);	
			
			this.imageId = getImageManager().saveImage(imageToUse).getImageId();			
			Map<String, String> properties = user.getProperties();			
			properties.put("imageId", imageId.toString());	
			((UserTemplate) user).setProperties(properties);
			userManager.updateUser(user);
			
		}else{
			imageToUse = getTargetUserImage();
			File fileToUse = getUploadImage();			
			((ImageImpl)imageToUse).setSize( (int)fileToUse.length());
			((ImageImpl)imageToUse).setInputStream( new FileInputStream(fileToUse));
			log.debug("image size:" + imageToUse.getSize());
			log.debug("image stream:" + imageToUse.getInputStream());
			getImageManager().saveImage(imageToUse);
		}
		
		return success();
	}
}