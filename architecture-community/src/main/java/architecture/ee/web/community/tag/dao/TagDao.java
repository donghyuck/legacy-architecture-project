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
package architecture.ee.web.community.tag.dao;

import java.util.List;

import architecture.ee.web.community.tag.ContentTag;

public interface TagDao {

    public void addTag(long tagId, int objectType, long objectId);

    public void removeTag(long tagId, int objectType, long objectId);

    public int countTags(long tagId);

    public List<Long> getTagIds(int objectType, long objectId);

    public ContentTag getContentTagById(long tagId);

    public ContentTag getContentTagByName(String name);

    public void createContentTag(ContentTag tag);

    public void deleteContentTag(long tagId);

}
