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

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import architecture.common.user.User;
import architecture.ee.web.photo.album.dao.AlbumDao;
import net.sf.ehcache.Cache;
import net.sf.ehcache.Element;

public class DefaultAlbumManager implements AlbumManager {

    private Log log = LogFactory.getLog(getClass());
    
    private AlbumDao albumDao;
    
    private Cache albumCache;
    
    public DefaultAlbumManager() {
    }

    public AlbumDao getAlbumDao() {
        return albumDao;
    }

    public void setAlbumDao(AlbumDao albumDao) {
        this.albumDao = albumDao;
    }

    public Cache getAlbumCache() {
        return albumCache;
    }

    public void setAlbumCache(Cache albumCache) {
        this.albumCache = albumCache;
    }

    
    public int getTotalAlbumCount(User user) {
	return albumDao.getAlbumCount(user);
    }

    public Album getAlbum(long albumId) throws AlbumNotFoundException {

	Album album = getAlbumInCache(albumId);
	log.debug("album in cache : " + album);

	if (album == null) {
	    album = albumDao.getAlbumById(albumId);
	    if (album == null) {
		throw new AlbumNotFoundException();
	    }
	    updateCache(album);
	}
	return album;
    }

    
    private Album getAlbumInCache(Long albumId) {
	if (albumCache.get(albumId) == null)
	    return null;
	return (Album) albumCache.get(albumId).getValue();
    }
    
    private void updateCache(Album album) {
	if (albumCache.get(album.getAlbumId()) != null) {
	    albumCache.remove(album.getAlbumId());
	}
	albumCache.put(new Element(album.getAlbumId(), album));
    }   

    private void clearCache(Album album) {
	if (albumCache.get(album.getAlbumId()) != null) {
	    albumCache.remove(album.getAlbumId());
	}
    }
    
    public List<Album> getAlbums(User user) {
	List<Long> ids = albumDao.getAlbumIds(user);
	ArrayList<Album> list = new ArrayList<Album>(ids.size());
	for (Long id : ids) {
	    try {
		list.add(getAlbum(id));
	    } catch (AlbumNotFoundException e) {

	    }
	}
	return list;
    }

    public List<Album> getAlbums(User user, int startIndex, int pageSize) {
	List<Long> ids = albumDao.getAlbumIds(user, startIndex, pageSize);
	ArrayList<Album> list = new ArrayList<Album>(ids.size());
	for (Long id : ids) {
	    try {
		list.add(getAlbum(id));
	    } catch (AlbumNotFoundException e) {

	    }
	}
	return list;
    }

    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    public void saveOrUpdate(Album album) throws AlbumNotFoundException {
	boolean isNew = true;
	Date now = new Date();
	if (album.getAlbumId() > 0) {
	    album.setModifiedDate(now);
	    isNew = false;
	} else {
	    album.setCreationDate(now);
	    album.setModifiedDate(now);
	}
	albumDao.saveOrUpdateAlbum(album);
	clearCache(album);
    }

}
