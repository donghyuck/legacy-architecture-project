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
package architecture.ee.web.community.tag;

import java.util.List;
import java.util.Map;

import architecture.common.user.authentication.UnAuthorizedException;

public interface TagManager {

    public abstract ContentTag createTag(String name);

    public abstract ContentTag getTag(String name) throws TagNotFoundException;

    public abstract ContentTag getTag(long tagId) throws TagNotFoundException;

    public abstract void addTag(ContentTag contenttag, int objectType, long objectId) throws UnAuthorizedException;

    public abstract void setTags(String name, int objectType, long objectId);

    public abstract List<ContentTag> getTags(int objectType, long objectId);

    public abstract String getTagsAsString(int objectType, long objectId);

    public abstract int getTagCount(int objectType, long objectId);

    public abstract Map getTagMap(int objectType);

    public abstract void removeTag(ContentTag contenttag, int objectType, long objectId) throws UnAuthorizedException;

    public abstract void removeAllTags(int objectType, long objectId) throws UnAuthorizedException;

}
