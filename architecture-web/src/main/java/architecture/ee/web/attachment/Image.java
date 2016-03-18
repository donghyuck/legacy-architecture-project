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
package architecture.ee.web.attachment;

import java.io.IOException;
import java.io.InputStream;

import architecture.common.model.DateAware;
import architecture.common.model.PropertyAware;
import architecture.common.user.User;

public interface Image extends PropertyAware, DateAware {

    public abstract long getImageId();

    public abstract int getObjectType();

    public abstract long getObjectId();

    public String getName();

    public void setName(String name);
    /**
     * 이미지 ContentType 값
     * 
     * @return
     */
    public abstract String getContentType();

    /**
     * 이미지의 바이트 크기 값
     * 
     * @return
     */
    public abstract int getSize();

    /**
     * 이미지 데이터를 InputStream 형태로 리턴한다.
     * 
     * @return
     * @throws IOException
     */
    public abstract InputStream getInputStream() throws IOException;

    public abstract Integer getThumbnailSize();

    public abstract void setThumbnailSize(Integer thumbnailSize);

    public abstract void setThumbnailContentType(String contentType);

    public abstract String getThumbnailContentType();

    public abstract User getUser();

    public abstract void setUser(User user);

}
