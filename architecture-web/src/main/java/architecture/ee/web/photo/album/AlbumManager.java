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

import java.util.List;

import architecture.common.user.User;

public interface AlbumManager {

    public abstract int getTotalAlbumCount(User user);
    
    public abstract Album getAlbum(long albumId) throws AlbumNotFoundException ;
    
    public abstract List<Album> getAlbums(User user);
    
    public abstract List<Album> getAlbums(User user, int startIndex, int pageSize );
    
    public abstract void saveOrUpdate(Album album) throws AlbumNotFoundException;
        
}
