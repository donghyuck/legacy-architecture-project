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

import architecture.common.user.authentication.UnAuthorizedException;

public interface TagDelegator {
    public ContentTag createTag(String tagname);

    public ContentTag getTag(String tagname) throws TagNotFoundException;

    public ContentTag getTag(long tagId) throws TagNotFoundException;

    public void setTags(String tags);

    public String getTagsAsString();

    public void addTag(ContentTag tag) throws UnAuthorizedException;

    public List<ContentTag> getTags();

    public void removeTag(ContentTag tag) throws UnAuthorizedException;

    public void removeAllTags() throws UnAuthorizedException;

}
