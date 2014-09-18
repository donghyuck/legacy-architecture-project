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
package architecture.ee.web.community.dao;

import java.util.List;

import architecture.ee.web.community.ContentNotFoundException;
import architecture.ee.web.community.page.Content;

public interface ContentDao {

	public List<Long> getContentIdsForUser(long userId);
	
	public int getContentCountForUser(long userId);
	
	public Content getContent(long contentId) throws ContentNotFoundException;
	
	public void updateContent(Content content);
	
	public void insertContent(Content content);
	
}
