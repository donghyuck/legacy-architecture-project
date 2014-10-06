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
package architecture.ee.web.community.social.provider.connect;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.social.connect.ConnectionFactory;
import org.springframework.social.connect.support.ConnectionFactoryRegistry;
import org.springframework.social.facebook.connect.FacebookConnectionFactory;
import org.springframework.social.twitter.connect.TwitterConnectionFactory;

import architecture.ee.web.community.social.provider.ServiceProviderHelper;
import architecture.ee.web.community.social.provider.connect.ConnectionFactoryLocator.Implementation;
import architecture.ee.web.community.social.provider.connect.SocialConnect.Media;

public class DefaultConnectionFactoryLocator implements Implementation {
	
	private Log log = LogFactory.getLog(getClass());
	
	private ConnectionFactoryRegistry registry;

	public DefaultConnectionFactoryLocator() {
		
		this.registry = new ConnectionFactoryRegistry();			 
		for(MediaInfo m : ServiceProviderHelper.getAllMediaInfo()){
			Media media = Media.valueOf(m.getProvider().toUpperCase());
			if( log.isDebugEnabled())
			{
				log.debug("clientId:" +m.getProvider() );
				log.debug("clientSecret:" + m.getScope());
				log.debug("callbackUrl"+m.getCallbackUrl());				
			}
			
			if( media == Media.FACEBOOK ){
				FacebookConnectionFactory factory = new FacebookConnectionFactory(m.getClientId(), m.getClientSecret());				
				factory.setScope(m.getScope());				
				registry.addConnectionFactory(factory);				
			}else if (media == Media.TWITTER){
				TwitterConnectionFactory factory = new TwitterConnectionFactory(m.getClientId(), m.getClientSecret());
				registry.addConnectionFactory(factory);
			}	
		}
	}
	
	public void refresh(){
		
	}
	
	public ConnectionFactoryRegistry getConnectionFactoryLocator(){
		return registry;
	}

	public <A> ConnectionFactory<A> getConnectionFactory(Class<A> apiType) {
		return registry.getConnectionFactory(apiType);
	}
	
	public ConnectionFactory<?>  getConnectionFactory(String providerId) {
		return registry.getConnectionFactory(providerId);
	}

}