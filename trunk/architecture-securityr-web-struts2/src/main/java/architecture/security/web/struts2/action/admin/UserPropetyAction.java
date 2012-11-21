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
package architecture.security.web.struts2.action.admin;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import architecture.common.user.User;
import architecture.common.user.UserManager;
import architecture.common.user.UserNotFoundException;
import architecture.common.user.UserTemplate;
import architecture.ee.web.struts2.action.support.FrameworkActionSupport;

public class UserPropetyAction extends FrameworkActionSupport   {

    private User targetUser;
    
    private Long userId  ;
    
    private UserManager userManager ;

    public UserManager getUserManager() {
        return userManager;
    }

    public void setUserManager(UserManager userManager) {
        this.userManager = userManager;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
    
    public User getTargetUser(){
        log.debug("" + userId);        
        if(userId == null)
            log.warn("Edit profile for unspecified user.");
        //if(targetUser == null){
            try {
                targetUser = userManager.getUser(userId.longValue());
            } catch (UserNotFoundException e) {
                log.warn((new StringBuilder()).append("Could not load user object for id: ").append(userId).toString());
                return null;
            }
       // }        
        return new UserTemplate(targetUser);
    }
    
  public List<Property> getTargetUserProperty(){
        Map<String, String> properties = getTargetUser().getProperties();        
        List<Property> list = new ArrayList<Property>();
        for( String key : properties.keySet() ){
            String value = properties.get(key);
            list.add( new Property(key, value) );
        }        
        return list;
    }
    
    @Override
    public String execute() throws Exception {  
        return success();
    }
    
}
