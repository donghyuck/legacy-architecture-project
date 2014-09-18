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
package architecture.ee.web.community.social;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import architecture.ee.util.ApplicationHelper;
import architecture.ee.web.community.social.SocialNetwork.Media;
import architecture.ee.web.community.social.facebook.FacebookServiceProvider;
import architecture.ee.web.community.social.tumblr.TumblrServiceProvider;
import architecture.ee.web.community.social.twitter.TwitterServiceProvider;

public class ServiceProviderFactory {
	
	private Log log = LogFactory.getLog(getClass());
	
	public static SocialServiceProvider getServiceProvider(Media media){
		
		SocialServiceProvider provider = null;				
		String callbackUrl = getCallbackUrl(media);
		if( media == Media.FACEBOOK ){
			String scope = ApplicationHelper.getApplicationProperty("components.social.providers.facebook.scope", FacebookServiceProvider.DEFAULT_SCOPE);
			provider = new FacebookServiceProvider(
				"251365428350280",
				"704f08c943c6dfdba328e08a10550d38",							
				callbackUrl,
				scope
			);
			provider.setAccessSecret("");
		}else if ( media == Media.TWITTER ){
			provider = new TwitterServiceProvider(
				"4XebpD1MW3CQ8Koh7naQpg",
				"aFlMLXe7fsyE3EnZtTp1LdAHRqEMROqOFW8ldQNYc",
				callbackUrl
			);	
		}else if ( media == Media.TUMBLR ){
			provider = new TumblrServiceProvider(
				"VdE7COcqx3c8qiXg45AowMPausYVvYDpBSIrx1jI6gPj1X5V8T",
				"SILFKJbnYTuFLWVhCnKiD1wpd4T6ahVC0HXTemu1AqT3X7iL0r",							
				callbackUrl					
			);
		}
		return provider;
	}
	
	
	private static String getCallbackUrl(Media media){
		return ApplicationHelper.getApplicationProperty("components.social.providers." + media.name().toLowerCase() +  ".callbackUrl", null);
	}
	
	
	
}
