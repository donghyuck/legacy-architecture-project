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
package architecture.ee;

import org.junit.Test;

import architecture.ee.web.community.social.tumblr.BlogInfo;
import architecture.ee.web.community.social.tumblr.Photo;
import architecture.ee.web.community.social.tumblr.PhotoSize;
import architecture.ee.web.community.social.tumblr.Post;
import architecture.ee.web.community.social.tumblr.PostType;
import architecture.ee.web.community.social.tumblr.TumblrServiceProvider;
import architecture.ee.web.community.social.tumblr.UserInfo;

public class TumblrTest {

	@Test
	public void getUserInfo() {
		TumblrServiceProvider p = new TumblrServiceProvider("VdE7COcqx3c8qiXg45AowMPausYVvYDpBSIrx1jI6gPj1X5V8T", "SILFKJbnYTuFLWVhCnKiD1wpd4T6ahVC0HXTemu1AqT3X7iL0r");
		p.setAccessSecret("54VxMoFQJvlcrUNXsGn6N5muX8wzX001Gc7Wfmd7OOxY7wwZ5t");
		p.setAccessToken("rJiDUkj4GEz1ZaBsWWXBo411XYBuAcutUwwoeRmkDzHF9wjf1s");
		
		UserInfo u = p.getUserProfile();
		System.out.println(u);
		
		for( BlogInfo blog : u.getBlogs())
		{	
			String blogName = blog.getName();
			System.out.println( p.getBlogAvatar(blogName));
			System.out.println( p.getBlogInfo(blogName));
		}
		
		for( Post post : p.getDashboardPosts() ){
			System.out.println("--------------------" + post.getType());
			
			if( post.getType() == PostType.PHOTO ){
				for( Photo photo : post.getPhotos()){
					for( PhotoSize ps : photo.getSizes() ){
						
						System.out.println(ps.getUrl());
						break;
					}
				}
			}
		}
	}
}
