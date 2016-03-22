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

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;

import architecture.common.model.support.PropertyAndDateAwareSupport;
import architecture.common.user.User;

public class DefaultAlbum extends PropertyAndDateAwareSupport {

    private boolean shared;

    private boolean collaborate;

    private User user;

    private long albumId;

    private String name;

    private String description;

    public DefaultAlbum() {

	this.albumId = -1L;
	this.name = null;
	this.description = null;

	this.shared = false;
	this.collaborate = false;

	Date now = new Date();
	setCreationDate(now);
	setModifiedDate(now);
    }

    public long getAlbumId() {
	return albumId;
    }

    public void setAlbumId(long albumId) {
	this.albumId = albumId;
    }

    public String getName() {
	return name;
    }

    public void setName(String name) {
	this.name = name;
    }

    public String getDescription() {
	return description;
    }

    public void setDescription(String description) {
	this.description = description;
    }

    public boolean isShared() {
	return shared;
    }

    public void setShared(boolean shared) {
	this.shared = shared;
    }

    public boolean isCollaborate() {
	return collaborate;
    }

    public void setCollaborate(boolean collaborate) {
	this.collaborate = collaborate;
    }

    public User getUser() {
	return user;
    }

    public void setUser(User user) {
	this.user = user;
    }

    @JsonIgnore
    public Serializable getPrimaryKeyObject() {
	return this.albumId;
    }

    @JsonIgnore
    public int getModelObjectType() {
	return 29;
    }

    @JsonIgnore
    public int getCachedSize() {

	return 0;
    }

}
