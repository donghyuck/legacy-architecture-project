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

import java.util.List;

import architecture.common.user.User;
import architecture.common.user.UserManager;
import architecture.ee.web.struts2.action.support.FrameworkActionSupport;

public class UserManagementAction  extends FrameworkActionSupport  {

    private int pageSize = 0 ;
    
    private int startIndex = 0 ;    
    
    private UserManager userManager ;

    public UserManager getUserManager() {
        return userManager;
    }

    public void setUserManager(UserManager userManager) {
        this.userManager = userManager;
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
    
    public int getTotalUserCount(){
        return userManager.getTotalUserCount();
    }

    public List<User> getUsers(){        
        if( pageSize > 0 ){
            return userManager.getUsers(startIndex, pageSize);            
        }else{            
            return userManager.getUsers();
        }
    }
    
    @Override
    public String execute() throws Exception {  
        return success();
    }    
}
