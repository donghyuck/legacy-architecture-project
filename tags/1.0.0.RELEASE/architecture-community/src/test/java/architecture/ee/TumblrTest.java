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
import org.scribe.model.Token;
import org.scribe.model.Verifier;

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
		
		TumblrServiceProvider p = 
				new TumblrServiceProvider(
						"VdE7COcqx3c8qiXg45AowMPausYVvYDpBSIrx1jI6gPj1X5V8T", 
						"SILFKJbnYTuFLWVhCnKiD1wpd4T6ahVC0HXTemu1AqT3X7iL0r",
						"http://222.122.63.146/community/tumblr-callback.do");
		
	//Token t = p.getTokenWithCallbackReturns("2yLGSX6dPq0VannTTUGHxdiFaMeAerDQqDFiLWwz9sABlJFYwL", "vEgN8WZdinCJ0szJNHcXMP0gGosZro4H84uR3r5WT8u1hmbfj0");
	
	//Token accessToken = p.getOAuthService()..getAccessToken(getRequestToken(), new Verifier(accessKey));

	/*Token accessToken = p.getOAuthService().getAccessToken(
			new Token("vCL5Xw5GtYGF8eBZZeSgPKpKbkEZ9atq16wsru1hYorK8WMBV3", "4HXUP4fOIP9o5XjZJUAsh5M1a4etSaOUBRJBpTlRJ0LTyKTDVM" ), 
			new Verifier("vEgN8WZdinCJ0szJNHcXMP0gGosZro4H84uR3r5WT8u1hmbfj0"));
	*/
	
	//	System.out.println("==============================");
	//	System.out.println("getSecret:" + t.getSecret());
	//	System.out.println("getToken:" +t.getToken());
	//	System.out.println("getRawResponse:" + t.getRawResponse());
	//.out.println("==============================");
		
		//Token t2 = p.get//p.getTokenWithCallbackReturns("FS0hwAHSILvfTPzMJwIULXAthInoUmzYJfd860X4LReGsYlgav", "kplZgRDbtRuPEwXSKZR6XsE5nPoi3Ho3iiwJjIzpG0QWwVD7jt#_=_");
		
		/*Token t2 = new Token("FS0hwAHSILvfTPzMJwIULXAthInoUmzYJfd860X4LReGsYlgav", "ZHLbT2fZtmpVTt3HSVt9CNYx8snayiGkJZP3dK2S9wacdYW1Us");
		Token accessToken = p.getOAuthService().getAccessToken(t, new Verifier("kplZgRDbtRuPEwXSKZR6XsE5nPoi3Ho3iiwJjIzpG0QWwVD7jt#_=_"));
		
		System.out.println("==============================");
		System.out.println("getSecret:" + accessToken.getSecret());
		System.out.println("getToken:" +accessToken.getToken());
		System.out.println("getRawResponse:" + accessToken.getRawResponse());
		System.out.println("==============================");*/
		
	//	p.setAccessSecret(t.getSecret());
	//	p.setAccessToken(t.getToken());
		
		p.setAccessSecret("4HXUP4fOIP9o5XjZJUAsh5M1a4etSaOUBRJBpTlRJ0LTyKTDVM");
		p.setAccessToken("vCL5Xw5GtYGF8eBZZeSgPKpKbkEZ9atq16wsru1hYorK8WMBV3");
		
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
