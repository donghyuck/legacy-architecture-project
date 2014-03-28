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
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import architecture.common.model.factory.ModelTypeFactory;
import architecture.common.user.User;
import architecture.common.user.UserAlreadyExistsException;
import architecture.common.user.UserManager;
import architecture.common.user.UserNotFoundException;
import architecture.common.user.UserTemplate;
import architecture.common.util.StringUtils;
import architecture.ee.exception.NotFoundException;
import architecture.ee.web.attachment.Image;
import architecture.ee.web.attachment.impl.ImageImpl;
import architecture.ee.web.util.ParamUtils;
import architecture.ee.web.ws.Property;
import architecture.user.CompanyManager;
import architecture.user.GroupManager;
import architecture.user.Role;
import architecture.user.RoleManager;

public class MyProfileAction extends MyUploadImageAction {

	private UserManager userManager;
	
	private GroupManager groupManager;
	
	private RoleManager roleManager;

	private CompanyManager companyManager;

	private String password ;
		
	private Long photoId = -1L;
	
	/**
	 * @return photoId
	 */
	public Long getPhotoId() {
		return photoId;
	}

	/**
	 * @param photoId 설정할 photoId
	 */
	public void setPhotoId(Long photoId) {
		this.photoId = photoId;
	}

	/**
	 * @return userManager
	 */
	public UserManager getUserManager() {
		return userManager;
	}

	/**
	 * @param userManager 설정할 userManager
	 */
	public void setUserManager(UserManager userManager) {
		this.userManager = userManager;
	}

	/**
	 * @return groupManager
	 */
	public GroupManager getGroupManager() {
		return groupManager;
	}

	/**
	 * @param groupManager 설정할 groupManager
	 */
	public void setGroupManager(GroupManager groupManager) {
		this.groupManager = groupManager;
	}

	/**
	 * @return roleManager
	 */
	public RoleManager getRoleManager() {
		return roleManager;
	}

	/**
	 * @param roleManager 설정할 roleManager
	 */
	public void setRoleManager(RoleManager roleManager) {
		this.roleManager = roleManager;
	}

	/**
	 * @return companyManager
	 */
	public CompanyManager getCompanyManager() {
		return companyManager;
	}

	/**
	 * @param companyManager 설정할 companyManager
	 */
	public void setCompanyManager(CompanyManager companyManager) {
		this.companyManager = companyManager;
	}

	/**
	 * @return password
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * @param password 설정할 password
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	public List<Property> getTargetUserProperty() {
		Map<String, String> properties = getUser().getProperties();
		List<Property> list = new ArrayList<Property>();
		for (String key : properties.keySet()) {
			String value = properties.get(key);
			list.add(new Property(key, value));
		}
		return list;
	}

	public String deleteUserProperties() throws Exception {
		User user = getUser();
		Map<String, String> properties = user.getProperties();
		List<Map> list = ParamUtils.getJsonParameter(request, "items", List.class);
		for (Map row : list) {
			String n = (String) row.get("name");
			String v = (String) row.get("value");
			properties.remove(n);
		}
		updateTargetUserProperties( user, properties );
		return success();
	}
	
	public String updateUserPassword() throws Exception {
		User user = getUser(); 
		if( user instanceof UserTemplate ){
			if( !StringUtils.isEmpty(password))
			{ 
				((UserTemplate) user).setPassword(password);
				userManager.updateUser(user);
			}
		}
		return success();	
	}
	
	
	public String updateUserProperties() throws Exception {
		User user = getUser();
		Map<String, String> properties = user.getProperties();
		List<Map> list = ParamUtils.getJsonParameter(request, "items", List.class);		
		for (Map row : list) {
			String n = (String) row.get("name");
			String v = (String) row.get("value");
			properties.put(n, v);
		}		
		updateTargetUserProperties(user, user.getProperties());
		return success();	
	}
	
	public String updateUser() throws Exception {		
		try {
			User user = getUser();
			Map map = ParamUtils.getJsonParameter(request, "item", Map.class);
			String name = (String)map.get("name");
			String email = (String)map.get("email");
			boolean enabled = (Boolean)map.get("enabled");
			boolean emailVisible = (Boolean)map.get("emailVisible");
			boolean nameVisible = (Boolean)map.get("nameVisible");
			String password = (String)map.get("password");
			
			if( user instanceof UserTemplate ){
				((UserTemplate) user).setName(name);
				((UserTemplate) user).setEmail(email);
				((UserTemplate) user).setEnabled(enabled);
				((UserTemplate) user).setEmailVisible(emailVisible);				
				((UserTemplate) user).setNameVisible(nameVisible);
				
				if( StringUtils.isNotEmpty(password)){
					((UserTemplate) user).setPassword(password);
				}
			}
			userManager.updateUser(user);			
			return success();
		} catch (Throwable e) {
			e.printStackTrace();
			throw new Exception(e);
		}	
	}
	
    public List<Role> getFinalUserRoles(){    	
    	List<Role> roles =  roleManager.getFinalUserRoles(getUser().getUserId());
    	return roles;
    }

    public List<Role> getUserGroupRoles(){    	
    	List<Role> roles =  roleManager.getUserRolesFromGroup(getUser().getUserId());
    	return roles;
    }
    
    public List<Role> getUserRoles(){
    	User user = getUser();
    	List<Role> roleForUser = roleManager.getFinalUserRoles(user.getUserId());
    	
    	for( Role role : getUserGroupRoles() ){
    		roleForUser.remove(role);
    	}    	
    	return roleForUser;
    }
    
	public Image getPhoto( ){
		try {	
			return getImageManager().getImage(photoId);
		} catch (NotFoundException e) {
			log.error(e);
			return null;
		}
	}
    
    public String updatePhoto () throws Exception {			
    	User user = getUser();
    	this.photoId = user.getLongProperty("imageId", -1L);	
    	Image imageToUse;
    	if(isMultiPart() ){	    	
	    	File fileToUse = getUploadImage();	    	
	    	if( this.photoId < 0  ){	
				imageToUse = getImageManager().createImage(
					ModelTypeFactory.getTypeIdFromCode("USER"),
					user.getUserId(), 
					getUploadImageFileName(), 
					getUploadImageContentType(), 
					fileToUse);					
				this.photoId = getImageManager().saveImage(imageToUse).getImageId();
				
				Map<String, String> properties = user.getProperties();			
				properties.put("imageId", photoId.toString());	
				((UserTemplate) user).setProperties(properties);
				userManager.updateUser(user);				
			}else{
				imageToUse = getPhoto();
				((ImageImpl)imageToUse).setSize( (int)fileToUse.length());
				((ImageImpl)imageToUse).setInputStream( new FileInputStream(fileToUse));
				log.debug("image size:" + imageToUse.getSize());
				log.debug("image stream:" + imageToUse.getInputStream());
				getImageManager().saveImage(imageToUse);
			}
    	}
    	return success();		
    }
    
	public String updateUserRoles() throws Exception {			
		List<Role> oleRoles = getUserRoles();
		List<Role> groupRoles = getUserGroupRoles();		
		List<Map> list = ParamUtils.getJsonParameter(request, "items", List.class);			
		List<Role> newRoles = new ArrayList<Role>(list.size()); 		
		for( Map map : list ){			
			long roleId = Long.parseLong(map.get("roleId").toString() );
			Role role = roleManager.getRole(roleId);
			if( !groupRoles.contains(role) )
				newRoles.add(role);
		}		
		
		User user = getUser();		
		if(oleRoles.size() > 0 ){
			roleManager.removeUserRole(user, oleRoles);
		}
		
		if(newRoles.size() > 0 ){
			roleManager.addUserRole(user, newRoles);
		}		
		
		log.debug( user.getName() + " : " + oleRoles + ">" + newRoles );		
		return success();			
	}
	 
	protected void updateTargetUserProperties(User user, Map<String, String> properties) throws UserNotFoundException, UserAlreadyExistsException {
		if (user instanceof UserTemplate) {
			((UserTemplate) user).setProperties(properties);
			userManager.updateUser(user);
		}
	} 
}
