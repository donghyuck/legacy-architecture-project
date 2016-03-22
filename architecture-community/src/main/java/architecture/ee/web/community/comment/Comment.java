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
package architecture.ee.web.community.comment;

import java.util.Date;
import java.util.Map;

import architecture.common.cache.Cacheable;
import architecture.common.user.User;
import architecture.common.user.authentication.UnAuthorizedException;
import architecture.ee.web.community.model.ContentObject;

public interface Comment extends Cacheable {

    public abstract ContentObject.Status getStatus();

    public abstract void setStatus(ContentObject.Status status);

    public abstract Long getCommentId();

    public abstract void setCommentId(Long commentId);

    public abstract Integer getObjectType();

    public abstract void setObjectType(Integer objectType);

    public abstract Long getObjectId();

    public abstract void setObjectId(Long objectId);

    public abstract Long getParentCommentId();

    public abstract Integer getParentObjectType();

    public abstract Long getParentObjectId();

    public abstract User getUser();

    public abstract void setUser(User user);

    public abstract String getName();

    public abstract void setName(String name) throws UnAuthorizedException;

    public abstract String getEmail();

    public abstract void setEmail(String email) throws UnAuthorizedException;

    public abstract String getURL();

    public abstract void setURL(String url);

    public abstract String getIPAddress();

    public abstract void setIPAddress(String address);

    public abstract boolean isAnonymous();

    public abstract String getBody();

    public abstract void setBody(String body);

    public Date getCreationDate();

    public void setCreationDate(Date creationDate);

    public Date getModifiedDate();

    public void setModifiedDate(Date modifiedDate);

    public abstract Map<String, String> getProperties();

    public abstract void setProperties(Map<String, String> properties);

}
