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
package architecture.ee.web.attachment.impl;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import architecture.common.cache.CacheSizes;
import architecture.common.model.factory.ModelTypeFactory;
import architecture.common.model.json.UserDeserializer;
import architecture.common.model.support.PropertyAndDateAwareSupport;
import architecture.common.user.User;
import architecture.common.user.UserTemplate;
import architecture.ee.web.attachment.Image;

public class ImageImpl extends PropertyAndDateAwareSupport implements Image {

    private String name;
    private Long imageId;
    private Integer size;
    private String contentType;
    private Integer objectType;
    private Long objectId;
    private InputStream inputStream;
    private Integer thumbnailSize = 0;
    private String thumbnailContentType = "png";
    private User user ;

    
    public ImageImpl() {
	this.user = new UserTemplate(-1L);
	this.imageId =-1L;
	this.objectType = -1;
	this.objectId = -1L;
	this.name = null;
    }
    
    /**
     * @return user
     */
    public User getUser() {
	return user;
    }

    /**
     * @param user
     *            설정할 user
     */
    @JsonDeserialize(using = UserDeserializer.class)
    public void setUser(User user) {
	this.user = user;
    }
    
    @JsonIgnore
    public Serializable getPrimaryKeyObject() {
	return imageId;
    }

    @JsonIgnore
    public int getModelObjectType() {
	return ModelTypeFactory.getTypeIdFromCode("IMAGE");
    }

    @JsonIgnore
    public int getCachedSize() {
	return CacheSizes.sizeOfLong() + CacheSizes.sizeOfString(getName()) + CacheSizes.sizeOfString(contentType)
		+ CacheSizes.sizeOfInt() + CacheSizes.sizeOfLong() + CacheSizes.sizeOfDate() + CacheSizes.sizeOfDate();
    }

    public long getImageId() {
	return imageId;
    }

    public void setImageId(Long imageId) {
	this.imageId = imageId;
    }

    public void setSize(int size) {
	this.size = size;
    }

    public void setContentType(String contentType) {
	this.contentType = contentType;
    }

    public String getContentType() {
	return contentType;
    }

    public int getSize() {
	return size;
    }

    @JsonIgnore
    public InputStream getInputStream() throws IOException {
	return inputStream;
    }

    public int getObjectType() {
	return objectType;
    }

    public void setObjectType(Integer objectType) {
	this.objectType = objectType;
    }

    public long getObjectId() {
	return objectId;
    }

    public void setObjectId(Long objectId) {
	this.objectId = objectId;
    }

    public void setInputStream(InputStream inputStream) {
	this.inputStream = inputStream;
    }

    public Integer getThumbnailSize() {
	return thumbnailSize;
    }

    public void setThumbnailSize(Integer thumbnailSize) {
	this.thumbnailSize = thumbnailSize;
    }

    public String getThumbnailContentType() {
	return thumbnailContentType;
    }

    public void setThumbnailContentType(String thumbnailContentType) {
	this.thumbnailContentType = thumbnailContentType;
    }

    /**
     * @return name
     */
    public String getName() {
	return name;
    }

    /**
     * @param name
     *            설정할 name
     */
    public void setName(String name) {
	this.name = name;
    }

    @Override
    public String toString() {
	StringBuilder sb = new StringBuilder();
	sb.append("Image{");
	sb.append("imageId=").append(imageId);
	sb.append(",name=").append(getName());
	sb.append(",contentType=").append(contentType);
	sb.append("size=").append(size);
	sb.append("}");
	return sb.toString();
    }

}
