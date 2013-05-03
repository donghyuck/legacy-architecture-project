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

import org.apache.commons.lang.StringUtils;

import architecture.common.model.ModelObjectType;
import architecture.common.user.User;
import architecture.common.user.UserManager;
import architecture.common.user.UserNotFoundException;
import architecture.common.user.UserTemplate;
import architecture.ee.exception.NotFoundException;
import architecture.ee.web.attachment.Image;
import architecture.ee.web.attachment.impl.ImageImpl;
import architecture.ee.web.struts2.action.UploadImageAction;

public class UserImageUploadAction extends UploadImageAction  {

	private Long userId;
	
	private User targetUser;
	
	 private Long imageId = -1L; 
		
	private UserManager userManager ;
	
	public Long getImageId() {
		return imageId;
	}

	public void setImageId(Long imageId) {
		this.imageId = imageId;
	}

	@Override
	public void prepare() throws Exception {
		super.prepare();
		if( isMultiPart() ){
			String userIdStr;
			try {
				userIdStr = getMultiPartRequestWrapper().getParameter("userId");
				if( StringUtils.isNotEmpty( userIdStr )){
					userId = Long.parseLong(userIdStr);
				}
			} catch (Exception e) {
				userId = -1L;
			}
			try {
				String imageIdStr = getMultiPartRequestWrapper().getParameter("imageId");
				if(StringUtils.isNotEmpty( imageIdStr)){
					imageId = Long.parseLong(imageIdStr);
				}
			} catch (Exception e) {
				imageId = -1L;
			}
		}
		
	}

	public UserManager getUserManager() {
		return userManager;
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
			return getImageManager().getImage(imageId);
		} catch (NotFoundException e) {
			return null;
		}
	}
	
	public String execute() throws Exception {		
		User user = getTargetUser();
		Image imageToUse;
		if( imageId < 0  ){	
			File fileToUse = getUploadImage();			
			imageToUse = getImageManager().createImage(
				ModelObjectType.USER.getTypeId(), 
				user.getUserId(), 
				getUploadImageFileName(), 
				getUploadImageContentType(), 
				fileToUse);	
			
			imageId = getImageManager().saveImage(imageToUse).getImageId();			
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