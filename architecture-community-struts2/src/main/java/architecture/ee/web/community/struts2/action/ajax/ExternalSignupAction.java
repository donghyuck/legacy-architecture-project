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

import java.math.BigInteger;
import java.security.SecureRandom;

import org.apache.commons.lang.StringUtils;

import architecture.common.user.EmailAlreadyExistsException;
import architecture.common.user.User;
import architecture.common.user.UserAlreadyExistsException;
import architecture.common.user.UserNotFoundException;
import architecture.common.user.UserTemplate;
import architecture.ee.web.community.social.SocialNetwork;
import architecture.ee.web.community.social.SocialNetwork.Media;
import architecture.ee.web.community.social.facebook.FacebookProfile;
import architecture.ee.web.community.social.impl.SocailNetworkImpl;
import architecture.ee.web.community.social.twitter.TwitterProfile;
import architecture.ee.web.community.struts2.action.support.SocialCallbackSupport;
import architecture.ee.web.util.ParamUtils;

public class ExternalSignupAction extends SocialCallbackSupport {
	
	private User newUser ;
	
	@Override
	public User findUser() {		
		return null;
	}

	public User getAccount(){
		return newUser ;
	}
	
	public String execute() throws Exception {				
		
		SignupForm form = ParamUtils.getJsonParameter(request, "item", SignupForm.class);
		setOnetime(form.getOnetime());		
		Object profileToUse = getUserProfile();
		if( profileToUse != null ){
			if( form.getMediaType() == Media.FACEBOOK ){
				FacebookProfile f = (FacebookProfile)profileToUse;
				form.username = f.getUsername();
				form.email = f.getEmail();
				form.name = f.getName();
				form.id = f.getId();				
			}else if (form.getMediaType() == Media.TWITTER ){
				TwitterProfile t = (TwitterProfile)profileToUse;				
				form.username = t.getName();
				form.name = t.getScreenName();
				form.id = String.valueOf( t.getId() )						;
			}		
			if( form.agree ){
				if( StringUtils.isEmpty( form.getPassword1() ))
					form.setPassword1( getRandomPassword() );
			}			
			if( StringUtils.isEmpty(form.username) && StringUtils.isNotEmpty(form.email)){
				form.username = form.email;
			}
			signup(form);			
		}
		log.debug(">>" + form.toString());		
		return success();
	}
		
	private void signup( SignupForm form ){			
		UserTemplate t = new UserTemplate(form.username, form.password1, form.email, form.name );
		t.setExternal(true);	
		try {
			this.newUser = getUserManager().createUser(t, getCompany());
			SocialNetwork networkToUse = getSocialNetwork();
			((SocailNetworkImpl)networkToUse).setObjectType(newUser.getModelObjectType());
			((SocailNetworkImpl)networkToUse).setObjectId(newUser.getUserId());				
			networkToUse.setUsername(form.id);
			getSocialNetworkManager().saveSocialNetwork(networkToUse);		
		} catch (UserAlreadyExistsException e) {			
		} catch (EmailAlreadyExistsException e) {}	
	}
		
	private SecureRandom random = new SecureRandom();
	
	private String getRandomPassword() {
		return new BigInteger(130, random).toString(32);
	}
	
	public boolean isUsernameAvailable(String username) throws Exception {      	
		if( StringUtils.isNotEmpty( username )){
			try {
				getUserManager().getUser(username);
			} catch (UserNotFoundException e) {
				return true;
			}
		}
		return false;
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
    	String customClass ;
    	
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

		public Media getMediaType(){
			return Media.valueOf(media.toUpperCase());
		}


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
