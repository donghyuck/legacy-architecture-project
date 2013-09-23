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

import java.util.List;

import architecture.ee.web.social.twitter.MediaEntity;
import architecture.ee.web.social.twitter.Tweet;
import architecture.ee.web.social.twitter.TwitterProfile;
import architecture.ee.web.social.twitter.TwitterServiceProvider;

public class TwitterTest {

	public TwitterTest() {
		// TODO 자동 생성된 생성자 스텁
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
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
	}

}
