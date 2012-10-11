/*
 * Copyright 2012 Donghyuck, Son
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
package architecture.common.model;

import java.util.Date;
import java.util.Map;

import architecture.common.user.User;
import architecture.common.user.authentication.UnAuthorizedException;

public interface UserModel extends ModelObject<User> {
	
    public static final String EMAIL_VISIBILITY_DEFAULT = "user.default.displayEmailAddress";
    
    public static final String EMAIL_VISIBILITY_ALLOW_TOGGLE = "user.allowEmailVisibilityToggle";
    
    public static final String NAME_VISIBILITY_ALLOW_TOGGLE = "user.allowNameVisibilityToggle";
    
    public abstract long getUserId();

    public abstract String getUsername();
    
    public abstract String getEmail();

    public abstract String getFirstName();
    
    public abstract String getLastName();
    
    public abstract String getName();

    public abstract String getPassword() throws UnAuthorizedException;
    
    public abstract String getPasswordHash() throws UnAuthorizedException;

    public abstract Date getLastLoggedIn();
    
    public abstract Date getLastProfileUpdate();
    
    public abstract boolean isEnabled();
    
    public abstract boolean isFederated();
    
    public abstract boolean isExternal();
    
    public abstract boolean isEmailVisible();
    
    public abstract boolean isNameVisible();
    
    public abstract boolean isSetPasswordSupported();
        
    public abstract boolean isGetPasswordHashSupported();
    
    public abstract boolean isSetPasswordHashSupported();
    
    public abstract boolean isSetUsernameSupported();
    
    public abstract boolean isSetNameSupported();
    
    public abstract boolean isSetNameVisibleSupported();
    
    public abstract boolean isSetEmailSupported();
    
    public abstract boolean isSetEmailVisibleSupported();
    
    public abstract boolean isPropertyEditSupported();
    
    public abstract User.Status getStatus();
    
    public abstract boolean isAnonymous();
    
    public abstract Map<String, String> getProperties();
    
    
}