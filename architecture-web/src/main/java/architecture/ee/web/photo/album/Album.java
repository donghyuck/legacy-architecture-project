/*
 * Copyright 2016 donghyuck
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

package architecture.ee.web.photo.album;

import architecture.common.model.DateAware;
import architecture.common.model.PropertyAware;
import architecture.common.user.User;

public interface Album extends PropertyAware, DateAware {

    public long getAlbumId() ;

    public void setAlbumId(long albumId) ;

    public String getName();

    public void setName(String name);

    public String getDescription() ;

    public void setDescription(String description);

    public boolean isShared() ;

    public void setShared(boolean shared) ;

    public boolean isCollaborate() ;

    public void setCollaborate(boolean collaborate);

    public User getUser();

    public void setUser(User user) ;
    
}
