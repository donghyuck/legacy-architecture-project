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

import java.util.Date;

import org.junit.Test;

import architecture.ee.web.community.social.SocialNetwork;
import architecture.ee.web.community.social.SocialNetwork.Media;
import architecture.ee.web.community.social.SocialServiceProvider;
import architecture.ee.web.community.social.facebook.FacebookServiceProvider;
import architecture.ee.web.community.social.impl.SocailNetworkImpl;
import architecture.ee.web.community.social.twitter.TwitterServiceProvider;

public class TwitterTest {

	public TwitterTest() {
		// TODO 자동 생성된 생성자 스텁
	}

	private  SocialServiceProvider createSocialServiceProvider(Media media){		
		SocialServiceProvider provider = null;				
		if(media == SocialNetwork.Media.TWITTER){					
			String callbackUrl = "http://222.122.63.147/community/twitter-callback.do";
			if( callbackUrl!=null ){
				provider = new TwitterServiceProvider(
					"4XebpD1MW3CQ8Koh7naQpg",
					"aFlMLXe7fsyE3EnZtTp1LdAHRqEMROqOFW8ldQNYc",
					callbackUrl
				);
			}else{
				provider = new TwitterServiceProvider(
					"4XebpD1MW3CQ8Koh7naQpg",
					"aFlMLXe7fsyE3EnZtTp1LdAHRqEMROqOFW8ldQNYc"
				);
			}					
		}else if ( media == SocialNetwork.Media.FACEBOOK){		
			String callbackUrl = "http://222.122.63.147/community/facebook-callback.do";
			String scope = "export_stream, read_stream, user_about_me, user_activities, user_education_history, user_friends, user_photos, user_work_history";
			provider = new FacebookServiceProvider(
					"251365428350280",
					"704f08c943c6dfdba328e08a10550d38",							
					callbackUrl,
					scope
			);			
		}	
		return provider;
	} 
	
	public SocialNetwork createSocialNetwork(Media media) {
		SocailNetworkImpl impl = new SocailNetworkImpl(); 
		Date now = new Date();		
		impl.setCreationDate(now);
		impl.setModifiedDate(now);
		impl.setObjectType(2);
		impl.setObjectId(2);
		impl.setServiceProviderName(media.name().toLowerCase());
		impl.setSocialAccountId(-1L);		
		impl.setSocialServiceProvider(createSocialServiceProvider(media));	
		return impl;
	}
	
	/**
	 * @param args
	 */
	@Test
	public void twitter() {
		TwitterTest tt = new TwitterTest();
		SocialNetwork sn = tt.createSocialNetwork(Media.TWITTER);
		sn.getSocialServiceProvider().setAccessSecret(null);
		sn.getSocialServiceProvider().setAccessToken(null);
		System.out.println( sn.getAuthorizationUrl() ) ;
		/**
		TwitterServiceProvider provider = new TwitterServiceProvider(
			"4XebpD1MW3CQ8Koh7naQpg",
			"aFlMLXe7fsyE3EnZtTp1LdAHRqEMROqOFW8ldQNYc"
		);
		provider.setAccessToken("50153444-RgtZ4gNQtUtCHSnPgKuqxvqwO2piFQzET76iGjUtc");
		provider.setAccessSecret("DfL22L3yhsVDLfW9uTYgZe7e7BoodDC49Nbzu54");
		//provider.getAuthorizationUrl();
		
		TwitterProfile profile = provider.authenticate();
		System.out.println( profile.getId());	
		System.out.println( profile.getScreenName());			
		System.out.println( profile.getName() );	
		System.out.println( profile.getDescription() );	
		System.out.println( profile.getProfileUrl() );	
		System.out.println( "==========================");
		List<Tweet> list = provider.getUserTimeline();
		for( Tweet row : list ){
			System.out.println(row.getText());
			System.out.println(row.hasMedia());
			System.out.println(row.getCreatedAt());
			if( row.hasMedia() )
			{
				List<MediaEntity> mlist = row.getEntities().getMedia();
				for( MediaEntity m : mlist ){
					System.out.println(m.getType() + "," + m.getMediaUrl());
				}
				
			}			
			System.out.println("------------------------------");
		}
		**/
	}

}
