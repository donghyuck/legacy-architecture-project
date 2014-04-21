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
package architecture.ee.web.community;

import architecture.common.model.EntityModelObject;
import architecture.common.user.User;

public interface Comment extends EntityModelObject {

	public void setCommentId(Long commentId);
	
	public Long getCommentId();
	
	public void setParentCommentId(Long commentId);
	
	public Long getParentCommentId();	
	
	/**
	 * @return objectType
	 */
	public int getObjectType() ;

	/**
	 * @param objectType 설정할 objectType
	 */
	public void setObjectType(int objectType);
	/**
	 * @return objectId
	 */
	public Long getObjectId();

	/**
	 * @param objectId 설정할 objectId
	 */
	public void setObjectId(Long objectId) ;

	
	
	/**
	 * @return userId
	 */
	public Long getUserId() ;

	/**
	 * @param userId 설정할 userId
	 */
	public void setUserId(Long userId);
	
	public abstract User getUser();
	public void setUser(User user);
	public abstract String getBodyText();
	
	public abstract void setBodyText(String body);
	
    public abstract String getEmail();

    public abstract void setEmail(String email);
    
    public abstract String getURL();

    public abstract void setURL(String url);
    
    public abstract String getIPAddress();

    public abstract void setIPAddress(String address);
    
    public abstract boolean isAnonymous();
    
}
