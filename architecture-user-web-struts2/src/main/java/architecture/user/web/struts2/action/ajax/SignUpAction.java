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

import org.apache.commons.lang.StringUtils;

import architecture.common.user.User;
import architecture.common.user.UserManager;
import architecture.common.user.UserNotFoundException;
import architecture.common.user.UserTemplate;
import architecture.common.user.authentication.UnAuthorizedException;
import architecture.ee.web.struts2.action.support.FrameworkActionSupport;
import architecture.ee.web.util.ParamUtils;
import architecture.user.util.CompanyUtils;

public class SignUpAction extends  FrameworkActionSupport  {
	
	private UserManager userManager ;
	
	private String username ;
	
	private User targetUser ;
	
	/**
	 * @return targetUser
	 */
	public User getTargetUser() {
		return targetUser;
	}

	/**
	 * @param targetUser 설정할 targetUser
	 */
	public void setTargetUser(User targetUser) {
		this.targetUser = targetUser;
	}

	/**
	 * @return username
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * @param username 설정할 username
	 */
	public void setUsername(String username) {
		this.username = username;
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

	public String execute() throws Exception {      	
	
		return success();
	
	}
	
	public boolean isUsernameAvailable() throws Exception {      	
		if( StringUtils.isNotEmpty( username )){
			try {
				userManager.getUser(username);
			} catch (UserNotFoundException e) {
				return true;
			}
		}
		return false;
	}
	
    public String createUser() throws Exception {    	 
    	
    	SignupForm form = ParamUtils.getJsonParameter(request, "item", SignupForm.class);
    	
		/*Map<String, Object> map = ParamUtils.getJsonParameter(request, "item", Map.class);		
		String username = (String)map.get("username");
		String name = (String)map.get("name");
		String email = (String)map.get("email");
		String password = (String)map.get("password");
		
		boolean nameVisible = (Boolean)map.get("nameVisible");
		boolean emailVisible = (Boolean)map.get("emailVisible");		*/
    	
    	log.debug( form );
    	
		if( !CompanyUtils.isAllowedSignup(getCompany()) ){
			throw new UnAuthorizedException("user create not allowed.");
		}
		
		if( form != null ){ 
			if( !StringUtils.equals(form.getMedia(), "internal") ){
				throw new UnAuthorizedException("user create operation not supported.");
			}
			
			if( form.agree ){
				UserTemplate t = new UserTemplate(form.getUsername(), form.getPassword1(), form.getEmail(), form.getName() );
				t.setEmailVisible(form.getEmailVisible());
				t.setNameVisible(form.getNameVisible());
				if(getUser().getCompany() != null)
					t.setCompanyId(this.getUser().getCompany().getCompanyId());
				else
					t.setCompanyId(1);
				
				this.targetUser = userManager.createApplicationUser(t);
				
				
			}
		}		
		return success();
    }

    
    public static class SignupForm {
    	
    	String media = "UNKNOWN" ;
    	String id ;
    	String username ;
    	String firstName ;
    	String lastName ;
    	String name ;
    	String email ;
		String locale ;
    	String location ;
    	String language ;
    	String timezone ;
    	String gender ;
    	String password1 ;
    	String password2 ;
    	String onetime ;
    	Boolean agree ;
    	Boolean nameVisible ;
    	Boolean emailVisible;
    	String customClass ;
    	
    	
    	
    	
    	/**
		 * @return nameVisible
		 */
		public Boolean getNameVisible() {
			return nameVisible;
		}

		/**
		 * @param nameVisible 설정할 nameVisible
		 */
		public void setNameVisible(Boolean nameVisible) {
			this.nameVisible = nameVisible;
		}

		/**
		 * @return emailVisible
		 */
		public Boolean getEmailVisible() {
			return emailVisible;
		}

		/**
		 * @param emailVisible 설정할 emailVisible
		 */
		public void setEmailVisible(Boolean emailVisible) {
			this.emailVisible = emailVisible;
		}

		/**
		 * @return username
		 */
		public String getUsername() {
			return username;
		}

		/**
		 * @param username 설정할 username
		 */
		public void setUsername(String username) {
			this.username = username;
		}

    	
		/**
		 * @return media
		 */
		public String getMedia() {
			return media;
		}

		/**
		 * @param media 설정할 media
		 */
		public void setMedia(String media) {
			this.media = media;
		}

		/**
		 * @return id
		 */
		public String getId() {
			return id;
		}

		/**
		 * @param id 설정할 id
		 */
		public void setId(String id) {
			this.id = id;
		}

		/**
		 * @return firstName
		 */
		public String getFirstName() {
			return firstName;
		}

		/**
		 * @param firstName 설정할 firstName
		 */
		public void setFirstName(String firstName) {
			this.firstName = firstName;
		}

		/**
		 * @return lastName
		 */
		public String getLastName() {
			return lastName;
		}

		/**
		 * @param lastName 설정할 lastName
		 */
		public void setLastName(String lastName) {
			this.lastName = lastName;
		}

		/**
		 * @return name
		 */
		public String getName() {
			return name;
		}

		/**
		 * @param name 설정할 name
		 */
		public void setName(String name) {
			this.name = name;
		}

		/**
		 * @return email
		 */
		public String getEmail() {
			return email;
		}

		/**
		 * @param email 설정할 email
		 */
		public void setEmail(String email) {
			this.email = email;
		}

		/**
		 * @return locale
		 */
		public String getLocale() {
			return locale;
		}

		/**
		 * @param locale 설정할 locale
		 */
		public void setLocale(String locale) {
			this.locale = locale;
		}

		/**
		 * @return location
		 */
		public String getLocation() {
			return location;
		}

		/**
		 * @param location 설정할 location
		 */
		public void setLocation(String location) {
			this.location = location;
		}

		/**
		 * @return language
		 */
		public String getLanguage() {
			return language;
		}

		/**
		 * @param language 설정할 language
		 */
		public void setLanguage(String language) {
			this.language = language;
		}

		/**
		 * @return timezone
		 */
		public String getTimezone() {
			return timezone;
		}

		/**
		 * @param timezone 설정할 timezone
		 */
		public void setTimezone(String timezone) {
			this.timezone = timezone;
		}

		/**
		 * @return gender
		 */
		public String getGender() {
			return gender;
		}

		/**
		 * @param gender 설정할 gender
		 */
		public void setGender(String gender) {
			this.gender = gender;
		}

		/**
		 * @return password1
		 */
		public String getPassword1() {
			return password1;
		}

		/**
		 * @param password1 설정할 password1
		 */
		public void setPassword1(String password1) {
			this.password1 = password1;
		}

		/**
		 * @return password2
		 */
		public String getPassword2() {
			return password2;
		}

		/**
		 * @param password2 설정할 password2
		 */
		public void setPassword2(String password2) {
			this.password2 = password2;
		}

		/**
		 * @return onetime
		 */
		public String getOnetime() {
			return onetime;
		}

		/**
		 * @param onetime 설정할 onetime
		 */
		public void setOnetime(String onetime) {
			this.onetime = onetime;
		}

		/**
		 * @return agree
		 */
		public Boolean getAgree() {
			return agree;
		}

		/**
		 * @param agree 설정할 agree
		 */
		public void setAgree(Boolean agree) {
			this.agree = agree;
		}

		/**
		 * @return customClass
		 */
		public String getCustomClass() {
			return customClass;
		}

		/**
		 * @param customClass 설정할 customClass
		 */
		public void setCustomClass(String customClass) {
			this.customClass = customClass;
		}

/*		public Media getMediaType(){
			return Media.valueOf(media.toUpperCase());
		}*/


		@Override
		public String toString() {
			StringBuilder builder = new StringBuilder();
			builder.append("SignupForm [");
			if (media != null)
				builder.append("media=").append(media).append(", ");
			if (id != null)
				builder.append("id=").append(id).append(", ");
			if (firstName != null)
				builder.append("firstName=").append(firstName).append(", ");
			if (lastName != null)
				builder.append("lastName=").append(lastName).append(", ");
			if (name != null)
				builder.append("name=").append(name).append(", ");
			if (email != null)
				builder.append("email=").append(email).append(", ");
			if (locale != null)
				builder.append("locale=").append(locale).append(", ");
			if (location != null)
				builder.append("location=").append(location).append(", ");
			if (language != null)
				builder.append("language=").append(language).append(", ");
			if (timezone != null)
				builder.append("timezone=").append(timezone).append(", ");
			if (gender != null)
				builder.append("gender=").append(gender).append(", ");
			if (password1 != null)
				builder.append("password1=").append(password1).append(", ");
			if (password2 != null)
				builder.append("password2=").append(password2).append(", ");
			if (onetime != null)
				builder.append("onetime=").append(onetime).append(", ");
			if (agree != null)
				builder.append("agree=").append(agree).append(", ");
			if (customClass != null)
				builder.append("customClass=").append(customClass);
			builder.append("]");
			return builder.toString();
		}
		
		
    }    

}
